#include "utils.h"
/*
 * Prints the hex value of the input
 *
 * arg0: data
 * arg1: data len
 */
void print_hex(unsigned char *data, size_t len)
{
	size_t i;

	if (!data)
		printf("NULL data\n");
	else
	{
		for (i = 0; i < len; i++)
		{
			if (!(i % 16) && (i != 0))
				printf("%02X ", data[i]);
			printf("%02X ", data[i]);
		}
		printf("\n");
	}
}

/*
 * Prints the input as string
 *
 * arg0: data
 * arg1: data len
 */
void print_string(unsigned char *data, size_t len)
{
	size_t i;

	if (!data)
		printf("NULL data\n");
	else
	{
		for (i = 0; i < len; i++)
			printf("%c", data[i]);
		printf("\n");
	}
}

/*
 * Prints the usage message
 */
void usage(void)
{
	printf(
		"\n"
		"Usage:\n"
		"    assign_3 -g \n"
		"    assign_3 -i in_file -o out_file -k key_file [-d | -e]\n"
		"    assign_3 -h\n");
	printf(
		"\n"
		"Options:\n"
		" -i    path    Path to input file\n"
		" -o    path    Path to output file\n"
		" -k    path    Path to key file\n"
		" -d            Decrypt input and store results to output\n"
		" -e            Encrypt input and store results to output\n"
		" -g            Generates a keypair and saves to 2 files\n"
		" -h            This help message\n");
	exit(EXIT_FAILURE);
}

/*
 * Checks the validity of the arguments
 *
 * arg0: path to input file
 * arg1: path to output file
 * arg2: path to key file
 * arg3: operation mode
 */
void check_args(char *input_file, char *output_file, char *key_file, int op_mode)
{
	if ((!input_file) && (op_mode != 2))
	{
		printf("Error: No input file!\n");
		usage();
	}

	if ((!output_file) && (op_mode != 2))
	{
		printf("Error: No output file!\n");
		usage();
	}

	if ((!key_file) && (op_mode != 2))
	{
		printf("Error: No user key!\n");
		usage();
	}

	if (op_mode == -1)
	{
		printf("Error: No mode\n");
		usage();
	}
}

void store_keys(size_t n, size_t d, size_t e)
{
	FILE *fp = fopen("public.key", "wb");

	fwrite(&n, sizeof(size_t), 1, fp);
	fwrite(&d, sizeof(size_t), 1, fp);
	fclose(fp);

	FILE *fp2 = fopen("private.key", "wb");

	fwrite(&n, sizeof(size_t), 1, fp2);
	fwrite(&e, sizeof(size_t), 1, fp2);

	fclose(fp2);
}

/* read the contents of the file and return */
unsigned char *readFile(char *filename, int *size)
{
	char *buffer = 0;
	long length;
	FILE *f = fopen(filename, "rb");

	if (f)
	{
		fseek(f, 0, SEEK_END);
		length = ftell(f);
		fseek(f, 0, SEEK_SET);
		buffer = malloc(length);
		size_t sizeRead = fread(buffer, 1, length, f);

		if (sizeRead != length)
		{
			printf("Unable to read data from file\n");
		}

		fclose(f);
	}

	*size = length;

	return (unsigned char *)buffer;
}

int textFileLength(char *filename)
{
	FILE *f = fopen(filename, "r");

	fseek(f, 0L, SEEK_END);
	return ftell(f);
}