import numpy as np
import numpy.linalg as la
from rbf import Basis_Function
from policy import Policy


class AgentLSPI:
    def __init__(self, env, gamma, sfn):
        self.env = env
        self.num_actions = self.env.action_space.n
        self.state_dim = self.env.observation_space.n
        self.basis_function_dim = self.num_actions + 1
        self.actions = list(range(self.num_actions))
        self.gamma = gamma
        self.basis_function = Basis_Function(self.state_dim, self.basis_function_dim, self.num_actions, gamma)
        self.num_basis = self.basis_function._num_basis()
        self.policy = Policy(self.basis_function, self.num_basis, self.actions)
        self.sample_file = open(sfn, 'a+')

    def initialize_policy(self):
        self.policy = Policy(self.basis_function, self.num_basis, self.actions)

    def choose_action(self, state):
        index = self.policy.get_actions(state)
        action = self.policy.actions[index[0]]
        return action

    def train(self):
        error = float('inf')
        i = 0
        lspi_iteration = 20
        epsilon = 0.00001

        policy_list = []

        while epsilon < error and i < lspi_iteration:
            policy_list.append(self.policy.weights)
            new_weights = self.lstdq(self.policy)
            error = np.linalg.norm((new_weights - self.policy.weights))
            self.policy.update_weights(new_weights)
            print("Train error at iteration no {} : {}".format(i, error))
            i += 1

    def collect_samples(self, state, action, reward, next_state):
        sample = str(state) + ',' + str(action) + ',' + str(reward) + ',' + str(next_state) + '\n'
        self.sample_file.write(sample)

    def read_file(self):  # read observation from samples file
        sample = self.sample_file.readline()
        sample = sample.strip()
        if sample == "":
            return None
        sample = sample.split(',')
        state = int(sample[0])
        action = int(sample[1])
        reward = int(sample[2])
        next_state = int(sample[3])
        return [state, action, reward, next_state]

    def lstdq(self, greedy_policy):
        p = self.num_basis

        A = np.zeros([p, p])
        b = np.zeros([p, 1])
        np.fill_diagonal(A, .1)  # Singular matrix error

        self.sample_file.seek(0)
        sample = self.read_file()

        while sample is not None:
            states = sample[0]
            actions = sample[1]
            rewards = sample[2]
            next_states = sample[3]
            phi = self.basis_function.evaluate(states, actions)
            greedy_action = greedy_policy.get_best_action(next_states)
            phi_next = self.basis_function.evaluate(next_states, greedy_action)

            loss = (phi - self.gamma * phi_next)
            phi = np.reshape(phi, [p, 1])
            loss = np.reshape(loss, [1, p])

            A = A + np.dot(phi, loss)
            b = b + (phi * rewards)
            sample = self.read_file()

        inv_A = np.linalg.inv(A)
        w = np.dot(inv_A, b)
        print(w)
        return w
