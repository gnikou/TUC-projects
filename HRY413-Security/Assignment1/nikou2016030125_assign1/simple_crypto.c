#include "simple_crypto.h"

char *otp(char *inp)
{
    char key[strlen(inp) + 1]; // allocate space for key
    FILE *fp;
    fp = fopen("/dev/urandom", "r"); // open dev/urandom
    fread(&key, 1, strlen(inp), fp); // get and store random key
    fclose(fp);

    char *encr = malloc((strlen(inp) + 1) * sizeof(char)); // allocate space for encrypted string
    char decrypt[strlen(inp) + 1];
    for (int i = 0; i < strlen(inp); i++)
    {
        encr[i] = inp[i] ^ key[i];     // input XOR random key to get encrypted value
        decrypt[i] = encr[i] ^ key[i]; // encrypted value XOR random key to get input value
    }
    encr[strlen(inp)] = '\0'; // null character at the end of the string
    decrypt[strlen(inp)] = '\0';

    printf("[OTP]Encrypted:");
    char *ptr = encr;
    while (*ptr)
    {
        printf("%02x", (unsigned int)*ptr++); // print encrypted text in hex
    }
    printf("\n[OTP]Decrypted:%s\n", decrypt); // print decrypted text

    return encr;
}

char *caesars(char *inp, int key)
{
    int valid_chars[62]; // array of int to store 0-9,A-Z,a-z

    for (int i = 0; i < 10; i++)
    {
        valid_chars[i] = 48 + i; // store 0-9 in positions 0-9
    }

    for (int i = 10; i < 36; i++)
    {
        valid_chars[i] = i + 55; // store A-Z in positions 10-35
    }

    for (int i = 36; i < 62; i++)
    {
        valid_chars[i] = i + 61; // store a-z in positions 36-61
    }

    char *encr = (char *)malloc((strlen(inp) + 1) * sizeof(char)); // allocte encryption array
    key = key % 62;                                                // they can occur 61 shifts before we return to original value
    for (int i = 0; i < strlen(inp); i++)
    { // for loop about input
        for (int j = 0; j < 62; j++)
        {
            if (valid_chars[j] == inp[i])
            {                                          // compare input value to each value of valid_chars
                encr[i] = valid_chars[(j + key) % 62]; // shift key positions in valid_chars and get new positions value, mod to start from 0 if j+key>61
            }
        }
    }

    encr[strlen(inp)] = '\0';
    printf("[Caesars]Encrypted:%s\n", encr);

    char decr[strlen(encr) + 1];
    for (int i = 0; i < strlen(encr); i++)
    {
        for (int j = 0; j < 62; j++)
        {
            if (valid_chars[j] == encr[i])
            { // compare encypted value to each value of valid_chars
                if (j - key < 0)
                {
                    decr[i] = valid_chars[62 + (j - key)]; // if left shift is less than zero start from the top (z)
                }
                else
                {
                    decr[i] = valid_chars[j - key]; // shift left key positions to get original value
                }
            }
        }
    }
    decr[strlen(inp)] = '\0';
    printf("[Caesars]Decrypted:%s\n", decr);

    return encr;
}

char *vigeners(char *inp, char *vkey)
{
    char *encr = (char *)malloc((strlen(inp) + 1) * sizeof(char));

    for (int i = 0; i < strlen(inp); i++)
    {
        int shift = inp[i] - 65;                           // get input letter "distance" from A
        encr[i] = (vkey[i % (strlen(vkey))] + shift) % 91; // add shift and mod any values greater than Z, mod strlen(key) for key size < input size
        if (encr[i] < 65)
        {
            encr[i] += 65; // start from A for shift greater than Z
        }
    }
    encr[strlen(inp)] = '\0';
    printf("[Vigeners]Encrypted:%s\n", encr);

    char decr[strlen(inp) + 1];
    for (int i = 0; i < strlen(encr); i++)
    {
        int shift = vkey[i % (strlen(vkey))] - 65; // get key letter "distance" from A
        decr[i] = encr[i] - shift;                 //  subtract shift from encrypted letter
        if (decr[i] < 65)
        {
            decr[i] += 26; // start from z for left shift less than A
        }
    }
    decr[strlen(inp)] = '\0';
    printf("[Vigeners]Decrypted:%s\n", decr);

    return encr;
}

char *inputString(FILE *fp)
{
    // The size is extended by the input with the value of the provisional
    char *str;
    int ch;
    size_t len = 0;
    size_t size = 10;
    str = realloc(NULL, sizeof(char) * size); // size is start size
    if (!str)
        return str;
    while (EOF != (ch = fgetc(fp)) && ch != '\n')
    {
        if ((ch > 47 && ch < 58) || (ch > 64 && ch < 91) || (ch > 96 && ch < 123))
        {
            str[len++] = ch;
            if (len == size)
            {
                str = realloc(str, sizeof(char) * (size += 16));
                if (!str)
                    return str;
            }
        }
    }
    str[len++] = '\0';

    return realloc(str, sizeof(char) * len);
}

char *inputCapitalsOnly(FILE *fp)
{
    // The size is extended by the input with the value of the provisional
    char *str;
    int ch;
    size_t len = 0;
    size_t size = 10;
    str = realloc(NULL, sizeof(char) * size); // size is start size
    if (!str)
        return str;
    while (EOF != (ch = fgetc(fp)) && ch != '\n')
    {
        if (ch > 64 && ch < 91)
        {
            str[len++] = ch;
            if (len == size)
            {
                str = realloc(str, sizeof(char) * (size += 16));
                if (!str)
                    return str;
            }
        }
    }
    str[len++] = '\0';

    return realloc(str, sizeof(char) * len);
}
