close all;
clear all;
clc;
%1)
Fs = 10^4;
Ts = 1/Fs;
Rp = 3; %ripple
Rs = 30; %attenuation
Wp = 2*pi*3*10^3;
Ws = 2*pi*4*10^3;

[Num, Wn] = buttord(Wp, Ws, Rp, Rs, 's');

[Z,P,K] = buttap(Num);

[NUM,DEN] = zp2tf(Z,P,K);

[NUMT,DENT] = lp2lp(NUM,DEN,Wn);

N = 2048;
W = linspace(0,Fs/2, N);
H1 = freqs(NUMT,DENT,2*pi*W);

[NUMD1,DEND1] = bilinear(NUMT,DENT,Fs);

H2 = freqz(NUMD1,DEND1,N,Fs);

figure;
plot(W , (10*log10(abs(H1))), 'r:');
hold on;
plot(W , (10*log10(abs(H2))), 'b-');
legend('Analog filter', 'Digital filter');
xlabel('Frequency (Hz)');
ylabel(' |H(j?)| (dB)');
title('Butterworth Low-pass Filter with attenuation=30dB');
hold off;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Rs = 50; %attenuation

[Num, Wn] = buttord(Wp, Ws, Rp, Rs, 's');

[Z,P,K] = buttap(Num);

[NUM,DEN] = zp2tf(Z,P,K);

[NUMT,DENT] = lp2lp(NUM,DEN,Wn);

H3 = freqs(NUMT,DENT,2*pi*W);

[NUMD2,DEND2] = bilinear(NUMT,DENT,Fs);

H4 = freqz(NUMD2,DEND2,N,Fs);

figure;
plot(W , (10*log10(abs(H3))), 'r:');
hold on;
plot(W , (10*log10(abs(H4))), 'b-');
legend('Analog filter', 'Digital filter');
xlabel('Frequency (Hz)');
ylabel(' |H(j?)| (dB)');
title('Butterworth Low-pass Filter with attenuation=50dB');
hold off;
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% --- 2 ---
% highpass Chebyshev filter
n=[2 16];

Ts=0.2;
fs=1/Ts;            % sampling frequency 
Wc=2;               % omega
fc= Wc/(2*pi);
Wp=fc/(fs/2);
Pbrpl=3;%db         % passband ripple
N=256;              % samples
W=0:1/(N-1):1;  % frequency axis radians/sample

 
[b_low,a_low] = cheby1(n(1),Pbrpl,Wp,'high');      % designs a highpass filter
filter1 = freqz(b_low,a_low,N);
[b_high,a_high] = cheby1(n(2),Pbrpl,Wp,'high');      % designs a highpass filter
filter2=freqz(b_high,a_high,N);

figure;
plot(W,20*log(abs(filter1)),'-.',W,20*log(abs(filter2)));
legend('n=2', 'n=16');
xlabel('Frequency (radians/sample)');
ylabel('Magnitude (dB)');
title('Chebyshev Highpass Filter');

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%3a)
Fs = 10^4;
Ts = 1/Fs;
N =500;
n = 0:N-1;

X = 1+cos(1000*n*Ts) + cos(16000*n*Ts) + cos(30000*n*Ts);

figure;
stem(n,X);
title('Sampled signal of x(t) wth Fs=10^4 Hz');
xlabel('t(sec)');
ylabel('x(n*Ts)');

X_filter_1 = filter(NUMD1,DEND1,X);
X_filter_2 = filter(NUMD2,DEND2,X);


X_F = fftshift(fft(X)*Ts);
X_F2 = fftshift(fft(X_filter_1)*Ts);
X_F3 = fftshift(fft(X_filter_2)*Ts);


f_axis= -Fs/2:Fs/N:Fs/2-Fs/N; 

figure;
subplot(3,1,1);
plot(f_axis, abs(X_F));
title('Fourier transform of x(t)=1+cos(1000t)+cos(16000t)+cos(30000t)');
xlabel('Frequency (Hz)');
ylabel('|X(F)|');

subplot(3,1,2);
plot(f_axis, abs(X_F2));
title('Filtered low-pass Fourier transform of x(t) with attenuation=30dB');
xlabel('Frequency (Hz)');
ylabel('|X(F)|');

subplot(3,1,3);
plot(f_axis, abs(X_F3));
title('Filtered low-pass Fourier transform of x(t) with attenuation=50dB');
xlabel('Frequency (Hz)');
ylabel('|X(F)|');


%3b)
Ts = 0.2;
Fs = 1/Ts;
N =500;
n = 0:N-1;

x = 1+cos(1.5*n*Ts) + cos(5*n*Ts);

figure;
stem(n,x);
title('Fourier transform of x(t)=1+cos(1.5t)+cos(5t)');
xlabel('t(sec)');


X_filter_1 = filter(b_low,a_low,x);
X_filter_2 = filter(b_high,a_high,x);


X_f = fftshift(fft(x)*Ts);
X_f2 = fftshift(fft(X_filter_1)*Ts);
X_f3 = fftshift(fft(X_filter_2)*Ts);

f_axis= -Fs/2:Fs/N:Fs/2-Fs/N; 

figure;
subplot(3,1,1);
plot(f_axis, abs(X_f));
title('Fourier transform of x(t)=1+cos(1.5t)+cos(5t)');
xlabel('Frequency (Hz)');
ylabel('|X(F)|');

subplot(3,1,2);
plot(f_axis, abs(X_f2));
title('Filtered high-pass Fourier transform of x(t) with n=2');
xlabel('Frequency (Hz)');
ylabel('|X(F)|');

subplot(3,1,3);
plot(f_axis, abs(X_f3));
title('Filtered high-pass Fourier transform of x(t) with n=16');
xlabel('Frequency (Hz)');
ylabel('|X(F)|');


