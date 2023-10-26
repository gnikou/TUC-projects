clc;
close all;
clear all;

%2.A
f_axis = @(w,Fs) 0:Fs/(2*length(w)):Fs/2-Fs/(2*length(w));

Wc = 0.5*pi;   
%cutoff frequency
Fc = Wc/(2*pi);
%sampling frequency
Fs = 100;
%windows
N0 = 21;
N1 = 41;
%normalized cutoff frequency
Wn = Fc/(Fs/2);

%filters
hammFil0 = fir1(N0-1,Wn,hamming(N0));
hammFil1 = fir1(N1-1,Wn,hamming(N1));
hannFil0 = fir1(N0-1,Wn,hann(N0));
hannFil1 = fir1(N1-1,Wn,hann(N1));

[hHamm0,wHamm0] = freqz(hammFil0,N0);
[hHamm1,wHamm1] = freqz(hammFil1,N1);
[hHann0,wHann0] = freqz(hammFil0,N0);
[hHann1,wHann1] = freqz(hammFil1,N1);

hamm0_freq = f_axis(wHamm0,Fs);
hamm1_freq = f_axis(wHamm1,Fs);
hann0_freq = f_axis(wHann0,Fs);
hann1_freq = f_axis(wHann1,Fs);

figure;
subplot(1,2,1);
plot(hamm0_freq,abs(hHamm0));
xlabel('F(Hz)');
ylabel('Magnitude');
title('Hamming filter: N=21');
subplot(1,2,2);
plot(hamm1_freq,abs(hHamm1));
xlabel('F(Hz)');
ylabel('Magnitude');
title('Hamming filter: N=41');

figure;
subplot(1,2,1);
plot(hann0_freq,abs(hHann0));
xlabel('F(Hz)');
ylabel('Magnitude');
title('Hanning filter: N=21');
subplot(1,2,2);
plot(hann1_freq,abs(hHann1));
xlabel('F(Hz)');
ylabel('Magnitude');
title('Hanning filter: N=41');

%2.B
%sampling frequency
Fs = 100;
Ts = 1/Fs;
w0 = 15;
w1 = 200;
f0 = w0/(2*pi);
f1 = w1/(2*pi);
%samples
N  = 500;
n = 0:N-1;

%signal
x = sin(2*pi*f0*n*Ts) + 0.25*sin(2*pi*f1*n*Ts);
f_axis = -Fs/2:Fs/N:Fs/2-Fs/N;

Xf = fftshift(fft(x));

fil0=filter(hammFil0,1,x);                                
Xfhamm0=fftshift(fft(fil0));
fil1=filter(hammFil1,1,x);
Xfhamm1=fftshift(fft(fil1));
fil2=filter(hannFil0,1,x);                                 
Xfhann0=fftshift(fft(fil2));
fil3=filter(hannFil1,1,x);                                
Xfhann1=fftshift(fft(fil3));

figure;
subplot(3,1,1);
plot(f_axis,abs(Xf));
xlabel('F(Hz)');
ylabel('|Xf|');
title('The FT of x signal');
subplot(3,1,2);
plot(f_axis,abs(Xfhamm0));
xlabel('F(Hz)');
ylabel('|Xhamm0|');
title('The filtered FT of x');

subplot(3,1,3);
plot(f_axis,abs(Xfhamm1));
xlabel('F(Hz)');
ylabel('|Xhamm1|');
title('The filtered FT of x');


figure;
subplot(3,1,1);
plot(f_axis,abs(Xf));
xlabel('F(Hz)');
ylabel('|Xf|');
title('The FT of x signal');
subplot(3,1,2);
plot(f_axis,abs(Xfhann0));
xlabel('F(Hz)');
ylabel('|Xhann0|');
title('The filtered FT of x');

subplot(3,1,3);
plot(f_axis,abs(Xfhann1));
xlabel('F(Hz)');
ylabel('|Xhann1|');
title('The filtered FT of x');

%2.C
Fs1 = 50;
Ts1 = 1/Fs1;

x=sin(2*pi*f0*n*Ts1)+0.25*sin(2*pi*f1*n*Ts1); 
f_axis1=-Fs1/2:Fs1/N:Fs1/2-Fs1/N; 

Xf=fftshift(fft(x)); 

fil0=filter(hammFil0,1,x);                                
Xfhamm0=fftshift(fft(fil0));
fil1=filter(hammFil1,1,x);
Xfhamm1=fftshift(fft(fil1));
fil2=filter(hannFil0,1,x);                                 
Xfhann0=fftshift(fft(fil2));
fil3=filter(hannFil1,1,x);                                
Xfhann1=fftshift(fft(fil3));

figure;
subplot(3,1,1);
plot(f_axis,abs(Xf));
xlabel('F(Hz)');
ylabel('|Xf|');
title('The FT of x signal');
subplot(3,1,2);
plot(f_axis,abs(Xfhamm0));
xlabel('F(Hz)');
ylabel('|Xhamm0|');
title('The filtered FT of x');

subplot(3,1,3);
plot(f_axis,abs(Xfhamm1));
xlabel('F(Hz)');
ylabel('|Xhamm1|');
title('The filtered FT of x');


figure;
subplot(3,1,1);
plot(f_axis,abs(Xf));
xlabel('F(Hz)');
ylabel('|Xf|');
title('The FT of x signal');
subplot(3,1,2);
plot(f_axis,abs(Xfhann0));
xlabel('F(Hz)');
ylabel('|Xhann0|');
title('The filtered FT of x');

subplot(3,1,3);
plot(f_axis,abs(Xfhann1));
xlabel('F(Hz)');
ylabel('|Xhann1|');
title('The filtered FT of x');