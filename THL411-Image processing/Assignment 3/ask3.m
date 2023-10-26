clear all;
close all;
clc;
%1.
c=20;
[im,map]=imread('tools.bmp');

F = abs(fft2(im));
figure;
colormap(map);
image(F);
title("Fourier Transform of Image");

F2 = abs(fftshift(fft2(im,255,255)));   %shifted FT with values 0-255
D = c*log(1+F2);

figure;
colormap(map);
image(D);
title("Shifted grayscale Fourier Transform of image");

figure;
colormap(jet(256));
image(D);
title("Shifted rgb Fourier Transform of image");

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%2.
[A,map_ver] = imread('flatiron.bmp');
im_ver = rgb2gray(A);
im_ver_F = abs(fftshift(fft2(im_ver)));
D1 = c*log(1+im_ver_F);

figure;
subplot(2,1,1);
imshow(A);
title("Vertical Image");
subplot(2,1,2);
colormap(jet(256));
image(D1);
title("Fourier Transform of Image");

[B,map_hor] = imread('horizon.bmp');
im_hor = rgb2gray(B);
im_hor_F = abs(fftshift(fft2(im_hor)));
D2 = c*log(1+im_hor_F);

figure;
subplot(2,1,1);
imshow(B);
title("Horizontal Image");
subplot(2,1,2);
colormap(jet(256));
image(D2);
title("Fourier Transform of Image");
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%3.
Im1 = zeros(256,256);
Im2 = zeros(256,256);

Im1((256/2-5):(256/2+5),(256/2-5):(256/2+5)) = 255;
Im2((256/2-15):(256/2+15),(256/2-15):(256/2+15)) = 255;
figure;
subplot(1,2,1);
imshow(Im1);
subplot(1,2,2);
imshow(Im2);

F1=abs(fftshift(fft2(Im1)));
F2=abs(fftshift(fft2(Im2)));

figure;
subplot(2,2,1);
colormap(jet(256));
image(F1);
title("MF 10x10 χωρις μετασχηματισμό εντάσεων ");
subplot(2,2,2);
colormap(jet(256));
image(F2);
title("MF 30x30 χωρις μετασχηματισμό εντάσεων ");

D1 = c*log(1+F1);
D2 = c*log(1+F2);

subplot(2,2,3);
colormap(jet(256));
image(D1);
title("MF 10x10 με μετασχηματισμό εντάσεων ");
subplot(2,2,4);
colormap(jet(256));
image(D2);
title("MF 30x30 με μετασχηματισμό εντάσεων ");
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%4.
Im_1r = imrotate(Im1,45);
Im_2r = imrotate(Im2,45);

F_1r=abs(fftshift(fft2(Im_1r)));
F_2r=abs(fftshift(fft2(Im_2r)));

figure;
subplot(2,2,1);
colormap(jet(256));
image(F_1r);
title("MF rotated 10x10 χωρις μετασχηματισμό εντάσεων ");
subplot(2,2,2);
colormap(jet(256));
image(F_2r);
title("MF rotated 30x30 χωρις μετασχηματισμό εντάσεων ");

D_1r = c*log(1+F_1r);
D_2r = c*log(1+F_2r);

subplot(2,2,3);
colormap(jet(256));
image(D_1r);
title("MF rotated 10x10 με μετασχηματισμό εντάσεων ");
subplot(2,2,4);
colormap(jet(256));
image(D_2r);
title("MF rotated 30x30 με μετασχηματισμό εντάσεων ");
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%5.
Im_3 = zeros(256,256);
Im_4 = zeros(256,256);
Im_3((256/4-5):(256/4+5),(256/4-5):(256/4+5)) = 255;
Im_4((256/4-15):(256/4+15),(256/4-15):(256/4+15)) = 255;
figure;
subplot(1,2,1);
imshow(Im_3);
subplot(1,2,2);
imshow(Im_4);

F_3=abs(fftshift(fft2(Im_3)));
F_4=abs(fftshift(fft2(Im_4)));

figure;
subplot(2,2,1);
colormap(jet(256));
image(F_3);
title("MF not centered 10x10 χωρις μετασχηματισμό εντάσεων ");
subplot(2,2,2);
colormap(jet(256));
image(F_4);
title("MF not centered 30x30 χωρις μετασχηματισμό εντάσεων ");

D_3 = c*log(1+F_3);
D_4 = c*log(1+F_4);

subplot(2,2,3);
colormap(jet(256));
image(D_3);
title("MF not centered 10x10 με μετασχηματισμό εντάσεων ");
subplot(2,2,4);
colormap(jet(256));
image(D_4);
title("MF not centered 30x30 με μετασχηματισμό εντάσεων ");
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%6.
histeq_im = histeq(im);

F1=abs(fftshift(fft2(im)));
F2=abs(fftshift(fft2(histeq_im)));
D1 = c*log(1+F1);
D2 = c*log(1+F2);

figure;
subplot(2,1,1);
colormap(jet(256));
image(D1);
title('Original histogram');
subplot(2,1,2);
colormap(jet(256));
image(D2);
title('Histogram after histogram equalization');
