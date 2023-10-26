%1.
clear ; close all; clc
X1 = [2 2 -2 -2; 2 -2 -2 2]; 
X2 = [1 1 -1 -1; 1 -1 -1 1];

figure;
plot(X1(1,:), X1(2,:), 'bo','LineWidth', 2, 'MarkerSize', 7);
hold on;
plot(X2(1,:), X2(2,:), 'r+','LineWidth', 2, 'MarkerSize', 7);
legend('ω_1', 'ω_2');
legend('Location', 'east');
axis([-2.5 2.5 -2.5 2.5]);
xlabel('x_1');
ylabel('x_2');
grid on
title("Δείγματα των κλάσεων ω_1 και ω_2");
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%2.
X1 = [-10 -10 -14 -14; -10 -14 -14 -10]; 
X2 = [-5 -5 -7 -7; -5 -7 -7 -5];
x=[-20:20];
f = -x-17;
figure;
plot(X1(1,:), X1(2,:), 'bo','LineWidth', 2, 'MarkerSize', 7);
hold on;
plot(X2(1,:), X2(2,:), 'r+','LineWidth', 2, 'MarkerSize', 7);
plot(x, f, 'k-','LineWidth', 2, 'MarkerSize', 7);
legend('ω_1', 'ω_2', 'f(x)=-x-17');
legend('Location', 'southeast');
axis([-14.5 -4.5 -14.5 -4.5]);
xlabel('x_1');
ylabel('x_2');
grid on
title("Μετασχηματισμένα Δείγματα των κλάσεων ω_1 και ω_2");