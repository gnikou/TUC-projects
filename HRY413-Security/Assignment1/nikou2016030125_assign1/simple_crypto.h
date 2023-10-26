#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <linux/random.h>

char *otp(char *inp);
char *caesars(char *inp, int key);
char *vigeners(char *inp, char *vkey);
char *inputString(FILE *fp);
char *inputCapitalsOnly(FILE *fp);
