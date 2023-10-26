close all;
clear all;
clc;

%6.
img = imread("cameraman.bmp");
figure;
subplot(2,2,1);
imshow(img);
title("Original photo");

roberts = edge(img, 'Roberts');
subplot(2,2,2);
imshow(roberts);
title("Photo with roberts edge detection");

sobel = edge(img, 'sobel');
subplot(2,2,3);
imshow(sobel);
title("Photo with sobel edge detection");

laplacian = edge(img, 'log');
subplot(2,2,4);
imshow(laplacian);
title("Photo with laplacian edge detection");


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%7.
gaus_img = imnoise(img,'gaussian',0);
figure;
imshow(gaus_img);
title('Image with gaussian noise');

sobel = edge(gaus_img,'sobel');
figure;
subplot(1,2,1);
imshow(sobel);
title("Photo with gauss noise and sobel edge detection");

laplacian = edge(gaus_img, 'log');
subplot(1,2,2);
imshow(laplacian);
title("Photo with gauss noise and laplacian edge detection");
