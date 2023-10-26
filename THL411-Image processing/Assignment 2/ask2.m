close all;
clear all;
clc;

%1.
img = imread('1.jpg');
figure;
imshow(img);
title('Image');         %read image and show it 

figure;
imhist(img);          %show histogram
title('Histogram');

prompt = 'Enter a threshold';
threshold = input(prompt);  %user input for thresold
[a,b] = size(img);
thr_image = zeros(a,b);

for i=1:a
    for j=1:b
        if img(i,j) < threshold
            thr_image(i,j) = 0;
        else
            thr_image(i,j) = 255;
        end;
    end;
end;

figure;
imhist(thr_image);          %show histogram
title('Histogram after threshold');
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%2.
clear all;
img = imread('2.jpg');
figure;
imshow(img);
title('Bad contrast Image');         %read image and show it 

figure;
imhist(img);          %show histogram
title('Histogram');

newhist=histeq(img);
figure;
imhist(newhist);
title('Histogram after histogram equalization');
