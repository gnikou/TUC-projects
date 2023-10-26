function g = sigmoid(z)
%SIGMOID Compute sigmoid functoon
%   J = SIGMOID(z) computes the sigmoid of z.

% You need to return the following variables correctly 
g = zeros(size(z));

if ismatrix(z)
    g = 1./(1+exp(-z));
else  
    g = 1/(1+exp(-z));


% =============================================================

end
