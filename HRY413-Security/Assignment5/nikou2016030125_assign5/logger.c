#define _GNU_SOURCE
#define BUF_LEN 256

#include <time.h>
#include <stdio.h>
#include <dlfcn.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/stat.h>
#include <openssl/md5.h>
#include <errno.h>
#include <fcntl.h>
#include <openssl/evp.h>

#define MAX_SIZE 1024
#define MAXPATH 0xFFF
#define LOG_FILE_PATH "/home/coregoord/Documents/asfaleia/Assignment5/file_logging.log";

void print_hex(unsigned char *, size_t);
void print_string(unsigned char *, size_t);
char *get_full_path(FILE *);
void log_info(int, char *, char *, char *, int, int, unsigned char *);
unsigned char *get_fingerprint(char *, char *);

FILE *
fopen(const char *path, const char *mode)
{

	FILE *original_fopen_ret;
	FILE *(*original_fopen)(const char *, const char *);

	int access_type = (access(path, F_OK)) ? 0 : 1;

	/* call the original fopen function */
	original_fopen = dlsym(RTLD_NEXT, "fopen");
	original_fopen_ret = (*original_fopen)(path, mode);

	/* add your code here */

	int action_denied;

	(!original_fopen_ret) ? (action_denied = 1) : (action_denied = 0);

	uid_t uid;
	struct tm *p;
	time_t t;
	char *filepath = malloc(MAXPATH);

	uid = getuid();

	t = time(NULL);
	p = localtime(&t);

	char date[BUF_LEN] = {0};
	char time[BUF_LEN] = {0};

	memset(date, 0, BUF_LEN);
	strftime(date, BUF_LEN, "%D", p);

	memset(time, 0, BUF_LEN);
	strftime(time, BUF_LEN, "%T", p);

	(action_denied) ? (filepath = (char *)path) : (filepath = get_full_path(original_fopen_ret));

	unsigned char *fingerprint = (unsigned char *)malloc(MD5_DIGEST_LENGTH);

	(action_denied) ? (memset(fingerprint, 0, strlen((char *)fingerprint))) : (fingerprint = get_fingerprint((char *)path, (char *)NULL));

	log_info(uid, filepath, time, date, access_type, action_denied,
			 fingerprint);

	if (!action_denied)
		free(filepath);

	free(fingerprint);

	return original_fopen_ret;
}

size_t
fwrite(const void *ptr, size_t size, size_t nmemb, FILE *stream)
{

	size_t original_fwrite_ret;
	size_t (*original_fwrite)(const void *, size_t, size_t, FILE *);

	/* call the original fwrite function */
	original_fwrite = dlsym(RTLD_NEXT, "fwrite");
	original_fwrite_ret = (*original_fwrite)(ptr, size, nmemb, stream);

	/* add your code here */

	int access_type = 2;

	int action_denied;

	(original_fwrite_ret != nmemb) ? (action_denied = 1) : (action_denied = 0);

	uid_t uid;
	struct tm *p;
	time_t t;

	uid = getuid();

	t = time(NULL);
	p = localtime(&t);

	char date[BUF_LEN] = {0};
	char time[BUF_LEN] = {0};

	memset(date, 0, BUF_LEN);
	strftime(date, BUF_LEN, "%D", p);

	memset(time, 0, BUF_LEN);
	strftime(time, BUF_LEN, "%T", p);

	char *filepath = malloc(MAXPATH);
	(action_denied) ? (filepath = (char *)stream) : (filepath = get_full_path(stream)); // NADW GIA NULL

	unsigned char *fingerprint = (unsigned char *)malloc(MD5_DIGEST_LENGTH);
	(action_denied) ? (memset(fingerprint, 0, strlen((char *)fingerprint))) : (fingerprint = get_fingerprint((char *)filepath, (char *)ptr));

	log_info(uid, filepath, time, date, access_type, action_denied,
			 fingerprint);

	if (!action_denied)
		free(filepath);

	free(fingerprint);

	return original_fwrite_ret;
}

char *get_full_path(FILE *fp)
{
	int fd = fileno(fp);

	char *linkname;

	linkname = malloc(MAXPATH);

	char *num;
	char buffer[100];
	ssize_t r;

	const char *str1 = "/proc/self/fd/";

	asprintf(&num, "%d", fd);
	strcat(strcpy(buffer, str1), num);

	r = readlink(buffer, linkname, MAXPATH);

	*(linkname + r) = '\0';

	return linkname;
}

unsigned char *get_fingerprint(char *ptr, char *mod)
{

	FILE *original_fopen_ret;
	FILE *(*original_fopen)(const char *, const char *);

	original_fopen = dlsym(RTLD_NEXT, "fopen");
	original_fopen_ret = (*original_fopen)(ptr, "rb");
	// fseek(original_fopen_ret, 0, SEEK_SET);

	size_t len;
	MD5_CTX ctx;
	MD5_Init(&ctx);
	unsigned char *hash = (unsigned char *)malloc(MD5_DIGEST_LENGTH);

	char *data = (char *)malloc(MAX_SIZE * sizeof(char));

	while ((len = fread(data, 1, MAX_SIZE, original_fopen_ret)) != 0)
		MD5_Update(&ctx, data, len);

	if (mod != NULL)
		MD5_Update(&ctx, mod, strlen(mod));

	MD5_Final(hash, &ctx);

	free(data);
	fclose(original_fopen_ret);

	return hash;
}

void log_info(int uid, char *filepath, char *time, char *date,
			  int access_type, int action_denied, unsigned char *fingerprint)
{
	FILE *fp_fopen_ret;
	FILE *(*fp_fopen)(const char *, const char *);

	const char *fname = LOG_FILE_PATH;
	fp_fopen = dlsym(RTLD_NEXT, "fopen");
	fp_fopen_ret = (*fp_fopen)(fname, "a");

	char m[] = "0777";
	int i = strtol(m, 0, 8);
	chmod(fname, i);

	fprintf(fp_fopen_ret, "%d %s %s %s %d %d ", uid, filepath,
			(char *)time, (char *)date, access_type, action_denied);

	for (i = 0; i < strlen((char *)fingerprint); i++)
	{
		fprintf(fp_fopen_ret, "%02X", fingerprint[i]);
	}
	fprintf(fp_fopen_ret, "\n");

	fclose(fp_fopen_ret);
}

/*
 * Prints the hex value of the input
 * 16 values per line
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
			printf("%02X ", data[i]);
		}
		printf("\n");
	}
}

/*
 * Prints the input as string
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
		printf(" ");
	}
}

FILE *fopen64(const char *path, const char *mode)
{
	return fopen(path, mode);
}