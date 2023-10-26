import numpy as np
import random
import time


class AgentQLearning:
    def __init__(self, env):
        self.sample_file = None
        self.env = env
        self.q_table = np.zeros([self.env.observation_space.n, self.env.action_space.n])
        self.alpha_decay = 0.0001

    def train(self, q_table):
        alpha = 0.9
        gamma = 0.6
        epsilon = 0.1
        if q_table is None:
            q_table = np.zeros([self.env.observation_space.n, self.env.action_space.n])

        for i in range(1, 100001):
            state = self.env.reset()

            done = False

            while not done:
                if random.uniform(0, 1) < epsilon:
                    action = self.env.action_space.sample()  # Explore action space
                else:
                    action = np.argmax(q_table[state])  # Exploit learned values

                next_state, reward, done, info = self.env.step(action)

                old_value = q_table[state, action]
                next_max = np.max(q_table[next_state])

                if done:
                    new_value = old_value + alpha * (reward - old_value)
                    q_table[state, action] = new_value
                else:
                    new_value = old_value + alpha * (reward + (gamma * next_max) - old_value)
                    q_table[state, action] = new_value

                state = next_state

            if i % 100 == 0:
                print(f"Episode: {i}")
            if alpha - self.alpha_decay > 0:
                alpha -= self.alpha_decay

        print("Training finished.\n")
        return q_table

    def simulate(self, episodes, q_table, wRender):
        total_epochs, total_penalties, total_score = 0, 0, 0
        rewards_table = np.array([])
        if q_table is None:
            q_table = np.zeros([self.env.observation_space.n, self.env.action_space.n])

        for i in range(episodes):
            state = self.env.reset()
            epochs, num_penalties, reward, episode_score = 0, 0, 0, 0
            done = False

            while not done:
                action = np.argmax(q_table[state])
                new_state, reward, done, _ = self.env.step(action)
                episode_score += reward
                if wRender & (i % 100 == 0):
                    print("Episode {}".format(i))
                    self.env.render()
                    print("Score: {}\n".format(episode_score))
                state = new_state

                if reward == -10:
                    num_penalties += 1
                epochs += 1

            print("Episode {} total score: {}".format(i, episode_score))
            rewards_table = np.append(rewards_table, episode_score)
            total_penalties += num_penalties
            total_epochs += epochs
            total_score += episode_score

        avg_timesteps = total_epochs / episodes
        avg_penalties = total_penalties / episodes
        avg_rewards = total_score / episodes
        return avg_timesteps, avg_penalties, avg_rewards, rewards_table

    def learning_agent(self, episodes, q_table, wRender, sampleFile=None):
        if sampleFile is not None:
            self.sample_file = open(sampleFile, 'a+')

        alpha = 0.9
        gamma = 0.6
        epsilon = 0.1
        total_epochs, total_penalties, total_score = 0, 0, 0
        rewards_table = np.array([])
        if q_table is None:
            q_table = np.zeros([self.env.observation_space.n, self.env.action_space.n])

        for i in range(episodes):
            state = self.env.reset()
            epochs, num_penalties, reward, episode_score = 0, 0, 0, 0
            done = False

            while not done:
                if random.uniform(0, 1) < epsilon:
                    action = self.env.action_space.sample()
                else:
                    action = np.argmax(q_table[state])  # Exploit learned values

                next_state, reward, done, _ = self.env.step(action)

                old_value = q_table[state, action]
                next_max = np.max(q_table[next_state])

                if done:
                    new_value = old_value + alpha * (reward - old_value)
                    q_table[state, action] = new_value
                else:
                    new_value = old_value + alpha * (reward + (gamma * next_max) - old_value)
                    q_table[state, action] = new_value

                episode_score += reward

                if wRender & (i % 500 == 0):
                    print("Episode {}".format(i))
                    self.env.render()
                    print("Score: {}\n".format(episode_score))
                    time.sleep(.4)

                if sampleFile is not None:
                    self.collect_samples(state, action, reward, next_state)

                state = next_state

                if reward == -10:
                    num_penalties += 1
                epochs += 1

            print("Episode {} total score: {}".format(i, episode_score))
            print("--------------------------------------------\n")
            rewards_table = np.append(rewards_table, episode_score)
            total_penalties += num_penalties
            total_epochs += epochs
            total_score += episode_score
            if alpha - self.alpha_decay > 0:
                alpha -= self.alpha_decay

        avg_timesteps = total_epochs / episodes
        avg_penalties = total_penalties / episodes
        avg_rewards = total_score / episodes
        return avg_timesteps, avg_penalties, avg_rewards, rewards_table

    def collect_samples(self, state, action, reward, next_state):
        sample = str(state) + ',' + str(action) + ',' + str(reward) + ',' + str(next_state) + '\n'
        self.sample_file.write(sample)
