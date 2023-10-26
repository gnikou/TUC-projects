close all;
clear all;
clc;

%1.
prompt = 'Give me an image';
x = input(prompt,'s');  %user input
im = imread(x);
gray = rgb2gray(im);
imwrite(gray,'gray_image.jpg',"jpg"); %store grayscale image
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%2.
clear all;
H = zeros(256);   
RGB = zeros(256,3); %rgb table
k=7;
I=0.5;
S=(1-k/10);
M=I*(1+S);
m=2*I-M;

for i=1:256
    H(i) = i*(360/256);   %create 256 values of H
end

for i=1:256
    %R
    if (H(i)<=60)
        R = m+(M-m)*(H(i)/60);
    
    elseif (H(i)>60 && H(i)<=180)
        R = M;
    
    elseif (H(i)>180 && H(i)<=240)
        R = m+(M-m)*((240-H(i))/60);
        
    else
        R = m;
    end
    
    %G
    if (H(i)<=120)
        G = m;
        
    elseif (H(i)>120 && H(i)<=180)
        G = m+(M-m)*((H(i)-120)/60);
        
    elseif (H(i)>180 && H(i)<=300)
        G = M;
        
    else
        G = m+(M-m)*((360-H(i))/60);
    end

    %B
    if (H(i)<=60)
        B = M;
        
    elseif (H(i)>60 && H(i)<=120)
        B = m+(M-m)*((120-H(i))/60);
        
    elseif (H(i)>120 && H(i)<=240)
        B = m;
        
    elseif (H(i)>240 && H(i)<=300)
        B = m+(M-m)*((H(i)-240)/60);
        
    else
        B = M;
    end
    
    RGB(i,1)=R; %store rgb values
    RGB(i,2)=G;
    RGB(i,3)=B;
end

new_image = zeros(256,256);

for i = 1:256
    for j = 1:256
        new_image(i,j) = i;
    end
end

imwrite(new_image,RGB,'hsi_2_rgb_image.bmp');   %store image
figure(1);
imshow('hsi_2_rgb_image.bmp');
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%3.
clear all;
im = imread('3.jpg');
figure(2);
hold on;
subplot(2,3,1);
imshow(im);
hsv = rgb2hsv(im);
s = hsv(:, :, 2);
%%%%%%%%%%%%%%%%%%%%%%%%
s = s - 0.2;    %decrease by 2
s(s<0)=0;   %set negative values to 0
hsv(:,:,2)=s;
im=hsv2rgb(hsv);
hold on;
subplot(2,3,2);
imshow(im);
%%%%%%%%%%%%%%%%%%%%%%%%
s = s - 0.2;
s(s<0)=0;
hsv(:,:,2)=s;
im=hsv2rgb(hsv);
hold on;
subplot(2,3,3);
imshow(im);
%%%%%%%%%%%%%%%%%%%%%%%%
s = s - 0.2;
s(s<0)=0;
hsv(:,:,2)=s;
im=hsv2rgb(hsv);
hold on;
subplot(2,3,4);
imshow(im);
%%%%%%%%%%%%%%%%%%%%%%%%
s = s - 0.2;
s(s<0)=0;
hsv(:,:,2)=s;
im=hsv2rgb(hsv);
hold on;
subplot(2,3,5);
imshow(im);


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%4.
clear all;
image = imread('4.jpg');
image_res = imresize(image, [900 900], 'bicubic');  %resize image to nXn
n=900;
figure(3);
hold on;
subplot(1,2,1);
imshow(image_res);
R = image_res(:,:,1);   %take rgb tables
G = image_res(:,:,2);
B = image_res(:,:,3);

R_avg=0;
for x=1:n
    for y=1:n
        R_avg = R_avg + (1/n^2)*double(R(x,y));     %calculate r_avg
    end
end

G_avg=0;
for x=1:n
    for y=1:n
        G_avg = G_avg + (1/n^2)*double(G(x,y));     %calculate g_avg
    end
end

B_avg=0;
for x=1:n
    for y=1:n
        B_avg = B_avg + (1/n^2)*double(B(x,y));     %calculate b_avg
    end
end

if(R_avg == G_avg && R_avg == B_avg)
    disp('no more actions neeeded');
else
    R_new = (G_avg/R_avg)*R;        %set new values
    B_new = (G_avg/B_avg)*B;
    image_res(:,:,1) = R_new;
    image_res(:,:,3) = B_new;
end


subplot(1,2,2);
imshow(image_res);
imwrite(image_res,'white_balanced_image.jpg',"jpg");