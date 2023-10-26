close all;
clear all;
clc;

%1.b)
%k(n) = 0.9k(n-1)+0.2x(n)
%G2(z) = 1/(z+0.2)
%H(z) = 0.2z/(z^2 -0.7z-0.18)

Fs = 1;
Ts = 1/Fs;

%     0.2 z
%  -----------
%  z^2 - 0.9 z
G1_Numerator   =    [0  0.2 0];
G1_Denominator =    [1 -0.9 0];
G1 = tf(G1_Numerator, G1_Denominator, Ts);

%     1
%  -------
%  z + 0.2
G2_Numerator  = [0 1];
G2_Denominator= [1 0.2];
G2=tf(G2_Numerator,G2_Denominator,Ts);

%          0.2 z
%  ----------------------
%  z^3 - 0.7 z^2 - 0.18 z
H1 = G1*G2
z = 0;
p = [0.9 ; -0.2];
figure;
zplane(z,p);
title('Z-plane zero-pole plot for H1(z) = 0.2z/z^2-0.7z-0.18');
%d)
N = -pi:pi/128:pi;
n1 = [0 0.2 0];
d1 = [1 -0.7 -0.18];

figure;
freqz(n1,d1,N);

figure;
freqz(n1,d1);

%e)
num3 = [0 1];
den3 = [1 -1];
G3 = tf(num3,den3,Ts);
H2 = H1*G3

n2 = [0 0 0.2 0];
d2 = [1 -1.7 0.52 0.18];

figure;
freqz(n2,d2,N);




% ---2---


%       4-3.5z^-1
%  -----------------
%  1 - 2.5z^-1 +z^-2
Numerator   = [4 -3.5 0];
Denominator = [1 -2.5 1];
[residues,poles,k]=residuez(Numerator,Denominator)

syms z;
H3=0;
for i=1:2
    H(i)= residues(i)/(1-poles(i)/z);
    H3=H3+H(i);
end

disp('H3=');
pretty(H3)
Hz=iztrans(H3)
