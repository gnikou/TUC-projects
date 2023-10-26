close all;
clear all;
clc;
%1)
gamma = 1*10000000;

im = imread('lenna.bmp');
f = fspecial('average', [5 5]);
imf = imfilter(im,f);
mean=0;
gaussvar1 = 0.2;
gaussvar2 = 0.01;
imnoise1 = imnoise(imf,'gaussian',mean,gaussvar1);
imnoise2 = imnoise(imf,'gaussian',mean,gaussvar2);

figure;
subplot(2,2,1);
imshow(im);
title('Original image');
subplot(2,2,2);
imshow(imf);
title('Blurred image');
subplot(2,2,3);
imshow(imnoise1);
title('Blurred with gauss noise(mean=0,var=0.2)');
subplot(2,2,4);
imshow(imnoise2);
title('Blurred with gauss noise(mean=0,var=0.01)');


Sf1 = abs(fft2(im)).^2;
Sn1 = gaussvar1;

[x, y] = size(im);
H = fft2(f,x,y);    
Hconj = conj(H);
G1 = fft2(imnoise1);
F1 = (Hconj./(abs(H).^2 + gamma.*(Sn1./Sf1))).*G1;  
f1 = ifft2(F1);

Sn2 = gaussvar2;
G2 = fft2(imnoise2);
F2 = (Hconj./(abs(H).^2 + gamma.*(Sn2./Sf1))).*G2;  
f2 = ifft2(F2);

figure;
subplot(1,2,1);
imshow(f1, [min(min(f1)) max(max(f1))]);
title('Wiener filter correction (mean=0,var=0.2)')
subplot(1,2,2);
imshow(f2, [min(min(f2)) max(max(f2))]);
title('Wiener filter correction (mean=0,var=0.01)');

mse_1 = sqrt(sum(sum((double(im) - double(f1)) .^ 2))) / (x * y)
mse_2 = sqrt(sum(sum((double(im) - double(f2)) .^ 2))) / (x * y)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%2)
[A,B] = size(im);
M = A+3-1;
N = B+3-1;

P = [0 -1 0; -1 4 -1; 0 -1 0];
P_new = zeros(M,N);
P_new((M/2-1):(M/2+1),(N/2-1):(N/2+1)) = P;
F_P = fft2(P_new);

H1 = fft2(f,M,N);
H1conj = conj(H1);
G1  = fft2(imnoise1,M,N);
g1 = ifft2(G1);

%i.
gamma = 80;
n_1 = norm((M-1)*(N-1)*(mean.^2+gaussvar1),1)^2;
a1 = n_1*0.05;
i=0;
while(i<100)
    %ii.
    F1 = (H1conj./(abs(H1).^2 + gamma*(abs(F_P).^2))).*G1;
    f1 = ifft2(F1);
    %iii.
    r1 = g1 - H1.*f1;
    f_gamma = norm(r1,1)^2;
    %iv.
    if(f_gamma<(n_1-a1))
        gamma = gamma + 0.1;       
    end;
    if(f_gamma>(n_1+a1))
        gamma = gamma - 0.1;
    end;
    %v.
    if(f_gamma>(n_1-a1) & f_gamma<(n_1+a1))
        break;
    end;
    i = i+1;
end

G2 = fft2(imnoise2,M,N);
g2 = ifft2(G2);

%i.
n_2 = norm((M-1)*(N-1)*(mean.^2+gaussvar2),1)^2;
a2 = n_2*0.05;
i=0;
while(i<100)
    %ii.
    F2 = (H1conj./(abs(H1).^2 + gamma*(abs(F_P).^2))).*G2;
    f2 = ifft2(F2);
    %iii.
    r2 = g2 - H1.*f2;
    f_gamma = norm(r2,1)^2;
    %iv.
    if(f_gamma<(n_2-a2))
        gamma = gamma + 0.1;       
    end;
    if(f_gamma>(n_2+a2))
        gamma = gamma - 0.1;
    end;
    %v.
    if(f_gamma>(n_2-a2) & f_gamma<(n_2+a2))
        break;
    end;
    i = i+1;
end


figure;
subplot(1,2,1);
imshow(f1, [min(min(f1)) max(max(f1))]);
title('CLSR filter (mean=0,var=0.2)');
subplot(1,2,2);
imshow(f2, [min(min(f2)) max(max(f2))]);
title('CLSR filter (mean=0,var=0.01)');

im = ifft2(fft2(im,M,N));
[x,y] = size(im);
mse_3 = sqrt(sum(sum((double(im) - double(f1)) .^ 2))) / (x * y)
mse_4 = sqrt(sum(sum((double(im) - double(f2)) .^ 2))) / (x * y)