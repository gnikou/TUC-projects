#include "simple_crypto.h"

int main()
{
    int key = 0;
    char *input;
    char *cinput;
    char *vinput;
    char *vk;

    printf("[OTP]Input: ");
    input = inputString(stdin); // input for otp
    otp(input);

    printf("[Caesars]Input: ");
    cinput = inputString(stdin); // input for caesars
    printf("[Caesars]Key: ");
    scanf("%d", &key); // key for caesars
    getchar();
    caesars(cinput, key);
    fflush(stdin);

    printf("[Vigeners]Input: ");
    vinput = inputCapitalsOnly(stdin);
    fflush(stdin);
    printf("[Vigeners]Key: ");
    vk = inputCapitalsOnly(stdin);

    vigeners(vinput, vk);

    return 0;
}