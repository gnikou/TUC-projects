function f = sigmoid(z)
%SIGMOID Compute sigmoid functoon
%   J = SIGMOID(z) computes the sigmoid of z.

% You need to return the following variables correctly 
f = zeros(size(z));

if ismatrix(z)
    f = 1./(1+exp(-z));
else  
    f = 1/(1+exp(-z));

end
