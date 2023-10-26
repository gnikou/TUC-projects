close all;
clear all;
clc;

%3.
A = imread('gray_image.jpg');
figure;
imshow(A);
title('Original Image');

g1 = fspecial('gaussian',[3 3]);
g2 = fspecial('gaussian',[5 5]);
avg1 = fspecial('average',[3 3]);
avg2 = fspecial('average',[5 5]);

gaussian1 = imfilter(A,g1);
gaussian2 = imfilter(A,g2);
average1 = imfilter(A,avg1);
average2 = imfilter(A,avg2);

figure;
subplot(2,2,1);
imshow(gaussian1);
title("Gaussian 3x3");
subplot(2,2,2);
imshow(gaussian2);
title("Gaussian 5x5");
subplot(2,2,3);
imshow(average1);
title("Average 3x3");
subplot(2,2,4);
imshow(average2);
title("Average 5x5");
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%4.

gaus_noise = imnoise(A,'gaussian',0);
figure;
imshow(gaus_noise);
title('Image with gaussian noise');

g1 = fspecial('gaussian',[3 3]);
g2 = fspecial('gaussian',[5 5]);

gaussian1 = imfilter(gaus_noise,g1);
gaussian2 = imfilter(gaus_noise,g2);
median1 = medfilt2(gaus_noise, [3 3]);
median2 = medfilt2(gaus_noise, [5 5]);

figure;
subplot(2,2,1);
imshow(gaussian1);
title("Gaussian lowpass 3x3(Gaussian noise)");
subplot(2,2,2);
imshow(gaussian2);
title("Gaussian lowpass5x5(Gaussian noise)");
subplot(2,2,3);
imshow(median1);
title("Median lowpass 3x3(Median noise)");
subplot(2,2,4);
imshow(median2);
title("Median lowpass 5x5(Median noise)");




%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%5.
saltnpepper_image = imnoise(A,'salt & pepper');
figure;
imshow(saltnpepper_image);
title('Image with salt and pepper noise');

g1 = fspecial('gaussian',[3 3]);
g2 = fspecial('gaussian',[5 5]);

gaussian1 = imfilter(saltnpepper_image,g1);
gaussian2 = imfilter(saltnpepper_image,g2);
median1 = medfilt2(saltnpepper_image, [3 3]);
median2 = medfilt2(saltnpepper_image, [5 5]);

figure;
subplot(2,2,1);
imshow(gaussian1);
title("Gaussian lowpass 3x3(Salt and pepper)");
subplot(2,2,2);
imshow(gaussian2);
title("Gaussian lowpass5x5(Salt and pepper)");
subplot(2,2,3);
imshow(median1);
title("Median lowpass 3x3(Salt and pepper)");
subplot(2,2,4);
imshow(median2);
title("Median lowpass 5x5(Salt and pepper)");