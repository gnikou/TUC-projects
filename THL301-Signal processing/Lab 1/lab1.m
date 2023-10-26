clear all;
close all;
clc;
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%1A)
nx = 0:10;
X = 2 * (nx>=0); %x(n) = 2*u(n)

ny = -15:15;
Y = (1/2).^(abs(ny)); %y(n) = 1/2^(|n|)

len_x = length(X);
len_y = length(Y);
len_z = len_x + len_y - 1;

x0 = [ zeros(1, len_y-1) X zeros(1, len_y-1)];  %padding X(t) with zeros
y_rev = Y(end:-1:1);    %Y(-t)

for i =1:len_z
    y_rev0 = [ zeros(1, (i-1)) y_rev zeros(1, (len_z-i))]; %padding Y(-t+i)
    z1(i) = sum(x0.*y_rev0); 
end


figure;
subplot(3,1,1);
stem(nx, X);
title('X(t) = 2*u(n)');
xlabel('Time(s)');
ylabel('X(t)');


subplot(3,1,2);
stem(ny, Y);
title('Y(t) = (1/2)^{|n|}');
xlabel('Time(s)');
ylabel('Y(t)');

nz = nx(1)+ny(1):nx(end)+ny(end);
subplot(3,1,3);
stem(nz, z1);
title('Calculated convolution of X(t) and Y(t)');
xlabel('Time(s)');
ylabel('Z(t) = X(t)*Y(t)');



figure;
z2 = conv(X, Y);

subplot(2,1,1);
stem(nz, z1);
title('Convolution without conv');
xlabel('Time(s)');
ylabel('Z(t) = X(t)*Y(t)');



subplot(2,1,2);
stem(nz, z2);
title('Convolution with conv');
xlabel('Time(s)');
ylabel('Z(t) = X(t)*Y(t)');


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%1B)
n=-20:20;
x = exp(-abs(n)); %x[n] = e^[|n|]
y = 2.^n; %y[n] = 2^n
z = conv(x,y);
nz = 2*n(1):2*n(end);

figure;
subplot(2,1,1);
stem(nz, z);
title("Convolution of z[n] = x[n]*y[n]");
xlabel('Time(s)');
ylabel('z[n] = x[n]*y[n]');

x_F = fft(x, length(nz));
y_F = fft(y, length(nz));
z_F=x_F.*y_F;

z_t=ifft(z_F);
subplot(2,1,2);
stem(nz,z_t);
title("Inverse Fourier transform of Z[F] = X[F].*Y[F]");
xlabel('Time(s)');
ylabel('IFFT(X[F].*Y[F])');

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%2.
t = 0:10^-3:500*10^-3;
x = 5*cos(24*pi*t) - 2*sin(1.5*pi*t);

T_s = [1/48 1/24 1/12 1/27];
figure;
for i=1:4
    Ts = T_s(i);
    
    n = 0:floor(500*10^-3/Ts);
    Xn = 5*cos(24*pi*n*Ts) - 2*sin(1.5*pi*n*Ts);
    
    subplot(4,1,i);
    plot(t, x);
    hold on;
    stem(n*Ts, Xn);
    title(sprintf('Sampling with sampling time Ts=%fs', Ts));
    xlabel('Time(s)');
    ylabel('x(t)-x(nTs)');
    
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%3a

fa=20;
fb=40;

%sampling frequency
fs=90;    
Ts=1/fs;

%samples
N=128;    
n=0:N;

x=10*cos(2*pi*fa*Ts*n);
y=4*sin(2*pi*fb*Ts*n+5);

z=x-y;

figure;
stem(n,z);
xlabel('Time');
title('x(t)=10cos(2*pi*20t)-4sin(2*pi*40t+5)');

%[-fs/2:fs/N:fs/2]
f_axis=-fs/2:fs/N:fs/2; 
Zf=fftshift(fft(z));
%pause;
figure;
stem(f_axis,abs(Zf));
xlabel('Frequency Hz');
title('Fourier transform of x2(t)=10*cos(2*pi*20*t)-4*sin(2*pi*40*t+5)');






%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%3b

for f0=100:125:475
     function1(f0);
end

for f0=7525:125:7900
    function1(f0);
end




function  function1(f0)
    fs=8000; %sampling frequency 
    Ts=1/fs;
    f=27; 
    N=128;  %samples
       
    n=0:N;
    t=n*Ts;
    x=sin(2*pi*f0*t+f);
    figure; 
    stem(n,x);
    xlabel('Time');
    title(sprintf('x(t)=cos(2*pi*%0.f*t+%0.f)',f0,f));
    
    %FOURIER
    f_axis=-fs/2:fs/N:fs/2; 
    Xf=fftshift(fft(x));  
    figure; 
    stem(f_axis,abs(Xf));
    xlabel('Frequency Hz');
    title(sprintf('The Fourier transform of x(t)=cos(2*pi*%0.f*t+%0.f)',f0,f));
end 



