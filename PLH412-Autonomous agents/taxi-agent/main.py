import gym
import matplotlib.pyplot as plt
import numpy as np
from lspi import AgentLSPI
from q_learning import AgentQLearning


class Agent:
    def __init__(self):
        self.env = gym.make("Taxi-v3")
        self.q_agent = AgentQLearning(self.env)
        self.episodes = 10000
        self.q_table = None
        self.num_basis = 3

    def main(self):
        while True:
            choice = int(input("Select:\n1)train agent (Q-Learning)\n2)evaluating agent\n3)learning agent without "
                               "initial Q-table\n4)collect samples"
                               "\n5)train agent (LSPI)\n9)Exit: "))

            if choice == 1:
                self.q_table = self.q_agent.train(self.q_table)
                print(self.q_table)

            elif choice == 2:
                inp = input("Do you want to see the taxi(y/n)?")
                if inp == 'y':
                    (avg_timesteps, avg_penalties,
                     avg_rewards, rewards_table) = self.q_agent.simulate(self.episodes, self.q_table, True)
                else:
                    (avg_timesteps, avg_penalties,
                     avg_rewards, rewards_table) = self.q_agent.simulate(self.episodes, self.q_table, False)

                print("Evaluation results after {} trials".format(self.episodes))
                print("Average time steps taken: {}".format(avg_timesteps))
                print("Average number of penalties incurred: {}".format(avg_penalties))
                print("Total average reward: {}".format(avg_rewards))
                rol_avg, i = rolling_average(rewards_table)
                plot_creation(rol_avg, i)

            elif choice == 3:
                inp = input("Do you want to see the taxi(y/n)? ")
                if inp == 'y':
                    (avg_timesteps, avg_penalties,
                     avg_rewards, rewards_table) = self.q_agent.learning_agent(self.episodes, self.q_table, True)
                else:
                    (avg_timesteps, avg_penalties,
                     avg_rewards, rewards_table) = self.q_agent.learning_agent(self.episodes, self.q_table, False)

                print("Evaluation results after {} trials".format(self.episodes))
                print("Average time steps taken: {}".format(avg_timesteps))
                print("Average number of penalties incurred: {}".format(avg_penalties))
                print("Total average reward: {}".format(avg_rewards))
                rol_avg, i = rolling_average(rewards_table)
                plot_creation(rol_avg, i)

            elif choice == 4:  # collect samples
                inp = int(input("Collect samples with:\n(1)random policy\n(2)Q-Learning Agent: "))
                episodes = int(input("Enter number of episodes:"))
                sfn = input("Enter samples filename to be written:")
                if inp == 1:
                    lspi = AgentLSPI(self.env, 0.99, sfn)
                    for i in range(episodes):
                        state = self.env.reset()
                        done = False
                        episode_score = 0
                        while not done:
                            action = self.env.action_space.sample()  # Explore action space
                            new_state, reward, done, _ = self.env.step(action)
                            episode_score += reward
                            lspi.collect_samples(state, action, reward, new_state)
                            state = new_state
                        print("Episode {} samples collected. Reward: {}".format(i, episode_score))
                else:
                    self.q_agent.learning_agent(episodes, self.q_table, False, sfn)

            elif choice == 5:
                if 'lspi' not in locals():
                    sfn = input("Enter samples filename:")
                    lspi = AgentLSPI(self.env, 0.99, sfn)
                # lspi.initialize_policy()
                policy = lspi.policy
                lspi.train()
                state = self.env.reset()
                total_epochs, total_penalties, total_score = 0, 0, 0
                rewards_table = np.array([])
                for i in range(self.episodes):
                    state = self.env.reset()
                    epochs, num_penalties, reward, episode_score = 0, 0, 0, 0
                    done = False

                    while not done:
                        next_action = lspi.choose_action(state)
                        new_state, reward, done, _ = self.env.step(next_action)
                        episode_score += reward
                        state = new_state

                        if reward == -10:
                            num_penalties += 1
                        epochs += 1

                    print("Episode {} total score: {}".format(i, episode_score))
                    rewards_table = np.append(rewards_table, episode_score)
                    total_penalties += num_penalties
                    total_epochs += epochs
                    total_score += episode_score

                avg_timesteps = total_epochs / self.episodes
                avg_penalties = total_penalties / self.episodes
                avg_rewards = total_score / self.episodes
                print("Evaluation results after {} trials".format(self.episodes))
                print("Average time steps taken: {}".format(avg_timesteps))
                print("Average number of penalties incurred: {}".format(avg_penalties))
                print("Total average reward: {}".format(avg_rewards))
                rol_avg, i = rolling_average(rewards_table)
                plot_creation(rol_avg, i)
                break

            elif choice == 9:
                break

            else:
                print('Wrong command!')

        print('Exiting...')


def rolling_average(rewards_table):
    moving_averages = []
    window_size = 500
    i = 0
    while i < len(rewards_table) - window_size + 1:
        # Calculate the average of current window
        window_average = round(np.sum(rewards_table[
                                      i:i + window_size]) / window_size, 2)

        # Store the average of current
        # window in moving average list
        moving_averages.append(window_average)

        # Shift window to right by one position
        i += 1
    return moving_averages, i


def plot_creation(moving_averages, i):
    # print(moving_averages)
    x = np.arange(i)
    plt.plot(x, moving_averages)
    plt.xlabel('Episode number')
    plt.ylabel('Rolling average reward (500 episodes)')
    plt.title('Average Reward of Q-Learning Algorithm')
    plt.show()


agent = Agent()
agent.main()
