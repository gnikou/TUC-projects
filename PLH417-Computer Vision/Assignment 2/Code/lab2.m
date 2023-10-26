close all;
clear all;
clc;

im_1 = rgb2gray(imread('fishes.jpg'));
im_2 = rgb2gray(imread('sunflowers.jpg'));
im_3 = rgb2gray(imread('seattlesonics.jpg'));
im_4 = rgb2gray(imread('pikachu.jpg'));

im_1 = im2double(im_1);
im_2 = im2double(im_2);
im_3 = im2double(im_3);
im_4 = im2double(im_4);

dim_1 = size(im_1);  %get size of images
dim_2 = size(im_2);
dim_3 = size(im_3);
dim_4 = size(im_4);

levels = 15;    %scale pyramid levels
sigma = 1.6;
k = 2;  %factor of downsampling
threshold = 0.02;

scale_space1 = createScaleSpace(im_1, levels, k, sigma);
scale_space2 = createScaleSpace(im_2, levels, k, sigma);
scale_space3 = createScaleSpace(im_3, levels, k, sigma);
scale_space4 = createScaleSpace(im_4, levels, k, sigma);


local_extrema_1 = zeros(dim_1(1), dim_1(2), levels);
local_extrema_2 = zeros(dim_2(1), dim_2(2), levels);
local_extrema_3 = zeros(dim_3(1), dim_3(2), levels);
local_extrema_4 = zeros(dim_4(1), dim_4(2), levels);

domain = ones(5,5);

for i = 1:levels
    
    local_extrema_1(:,:,i) = ordfilt2(scale_space1(:,:,i), 5^2, domain);
    local_extrema_2(:,:,i) = ordfilt2(scale_space2(:,:,i), 5^2, domain);
    local_extrema_3(:,:,i) = ordfilt2(scale_space3(:,:,i), 5^2, domain);
    local_extrema_4(:,:,i) = ordfilt2(scale_space4(:,:,i), 5^2, domain);
    
end

scale_space1 = findExtrema(local_extrema_1, scale_space1, levels);
scale_space2 = findExtrema(local_extrema_2, scale_space2, levels);
scale_space3 = findExtrema(local_extrema_3, scale_space3, levels);
scale_space4 = findExtrema(local_extrema_4, scale_space4, levels);

thresholdFlag_1 = scale_space1 > threshold;
thresholdFlag_2 = scale_space2 > threshold;
thresholdFlag_3 = scale_space3 > threshold;
thresholdFlag_4 = scale_space4 > threshold;


scale_space1 = scale_space1 .* thresholdFlag_1;
scale_space2 = scale_space2 .* thresholdFlag_2;
scale_space3 = scale_space3 .* thresholdFlag_3;
scale_space4 = scale_space4 .* thresholdFlag_4;



radScale = calcRadiusByScale(levels, k, sigma);

blobs = retrieveBlobMarkers(scale_space1, radScale, levels); 

blobs2 = retrieveBlobMarkers(scale_space2, radScale, levels); 

blobs3 = retrieveBlobMarkers(scale_space3, radScale, levels); 

blobs4 = retrieveBlobMarkers(scale_space4, radScale, levels); 



colP = blobs(:,1); %column positions
rowP = blobs(:,2); %row positions
rad = blobs(:,3); %radius

colP_2 = blobs2(:,1); 
rowP_2 = blobs2(:,2); 
rad_2 = blobs2(:,3); 

colP_3 = blobs3(:,1); 
rowP_3 = blobs3(:,2); 
rad_3 = blobs3(:,3); 

colP_4 = blobs4(:,1); 
rowP_4 = blobs4(:,2); 
rad_4 = blobs4(:,3); 

%figures
figure(1);
show_all_circles(im_1, colP, rowP, rad, 'r', .5); 
print('fig1', '-dpng')

figure(2);
show_all_circles(im_2, colP_2, rowP_2, rad_2, 'r', .5);
print('fig2', '-dpng')

figure(3);
show_all_circles(im_3, colP_3, rowP_3, rad_3, 'r', .5);
print('fig3', '-dpng')

figure(4);
show_all_circles(im_4, colP_4, rowP_4, rad_4, 'r', .5);
print('fig4', '-dpng')
