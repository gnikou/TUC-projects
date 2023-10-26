#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#define BUF_LEN 256

struct entry *create_list(struct entry *, int, char *, char *, char *, int, int, char *);
void count_malicious(struct entry *);
struct entry *parse_log(FILE *);
void print_list(struct entry *);
int check_suffix(char *);
char *filename_finder(char *);
int check_time(char *);

struct malicious
{
	int uid;
	int count;
	struct malicious *next;
};

struct entry
{

	int uid;		   /* user id (positive integer) */
	int access_type;   /* access type values [0-2] */
	int action_denied; /* is action denied values [0-1] */

	char *date; /* file access date */
	char *time; /* file access time */

	char *file;		   /* filename (string) */
	char *fingerprint; /* file fingerprint */

	struct entry *next;
};

void usage(void)
{
	printf(
		"\n"
		"usage:\n"
		"\t./monitor \n"
		"Options:\n"
		"-m, Prints malicious users\n"
		"-i <filename>, Prints table of users that modified "
		"the file <filename> and the number of modifications\n"
		"-v <number of files>, Prints the total number of files "
		"created in the last 20 minutes\n"
		"-e, Prints all the files that were encrypted by the ransomware\n"
		"-h, Help message\n\n");

	exit(1);
}

void list_unauthorized_accesses(FILE *log)
{
	struct entry *head = parse_log(log);

	count_malicious(head);

	return;
}

void list_file_modifications(FILE *log, char *file_to_scan)
{

	/* add your code here */
	/* ... */
	/* ... */
	/* ... */
	/* ... */

	return;
}

void num_of_files_created(FILE *log, char *threshold)
{
	struct entry *head = parse_log(log);
	int count = 0;
	while ((head->next) != NULL)
	{
		char *file_time = head->time;
		int isInTimeFrame = check_time(file_time);

		if ((head->access_type == 0) && (head->action_denied == 0) && isInTimeFrame)
			count++;
		head = head->next;
	}
	int thresh = atoi(threshold);
	printf("Number of created files (last 20 mins) is : %d\n", count);

	if (count >= thresh)
		printf("A suspiciouly high amount of files was created\n");
	else
		printf("It's all good\n");
}

void encrypted_files(FILE *log)
{

	struct entry *head = parse_log(log);
	printf("Files encrypted: \n");

	while ((head->next) != NULL)
	{
		int isEncryptedFile = check_suffix(head->file);
		if ((head->access_type == 0) && (head->action_denied == 0) && !isEncryptedFile)
		{
			char *file = filename_finder(head->file);
			printf("%s,\n", file);
		}

		head = head->next;
	}
}

int main(int argc, char *argv[])
{

	int ch;
	FILE *log;

	if (argc < 2)
		usage();

	log = fopen("./file_logging.log", "r");
	if (log == NULL)
	{
		printf("Error opening log file \"%s\"\n", "./log");
		return 1;
	}

	while ((ch = getopt(argc, argv, "hi:mve")) != -1)
	{
		switch (ch)
		{
		case 'i':
			list_file_modifications(log, optarg);
			break;
		case 'm':
			list_unauthorized_accesses(log);
			break;
		case 'v':
			num_of_files_created(log, argv[2]);
			break;
		case 'e':
			encrypted_files(log);
			break;
		default:
			usage();
		}
	}

	/* add your code here */
	/* ... */
	/* ... */
	/* ... */
	/* ... */

	fclose(log);
	argc -= optind;
	argv += optind;

	return 0;
}

struct entry *create_list(struct entry *head, int uid, char *filepath,
						  char *time, char *date, int access_type,
						  int action_denied, char *fingerprint)
{
	struct entry *cur = head;
	struct entry *node = (struct entry *)malloc(sizeof(struct entry));

	node->uid = uid;
	node->file = malloc(strlen(filepath));
	strcpy(node->file, filepath);

	node->date = malloc(strlen(date));
	strcpy(node->date, date);

	node->time = malloc(strlen(time));
	strcpy(node->time, time);

	node->access_type = access_type;
	node->action_denied = action_denied;

	node->fingerprint = malloc(strlen(fingerprint));
	strcpy(node->fingerprint, fingerprint);

	node->next = NULL;

	if (head == NULL)
		return node;

	while (cur->next != NULL)
		cur = cur->next;

	cur->next = node;

	return head;
}

void count_malicious(struct entry *entry)
{
	struct entry *cur = entry;

	struct malicious *node = (struct malicious *)malloc(sizeof(struct malicious));
	struct malicious *head = NULL;
	struct malicious *prev = NULL;

	while (cur->next != NULL)
	{
		if (cur->action_denied == 1)
		{
			prev = head;
			if (head == NULL)
			{
				head = node;
				head->uid = cur->uid;
				head->count = 1;
				head->next = NULL;
			}
			else if (cur->uid == head->uid)
			{
				head->count = head->count + 1;
			}
			/* else
			{

				node = head;
				prev = node;
				while (node->next != NULL)
				{
					if (node == node->next)
					{
						node->next->count = node->next->count + 1;
						break;
					}
					else
						node = node->next;
				}
				prev = node;
				prev->next = node;
				node->uid = node->uid;
				node->count = node->count + 1;
				node->next = NULL;
			} */
		}

		cur = cur->next;
	}
	prev = head;
	if (prev == NULL)
	{
		printf("No malicious users");
	}
	else
	{
		while (prev->next != NULL)
		{
			fprintf(stdout, "uid: %d count: %d\n", prev->uid, prev->count);
			prev = prev->next;
		}
		fprintf(stdout, "uid: %d count: %d\n", prev->uid, prev->count);
	}
}

struct entry *parse_log(FILE *log)
{
	char *line = NULL;
	size_t len = 0;
	ssize_t read;
	// fseek(log, 0, SEEK_SET);

	struct entry *head = NULL;

	while ((read = getline(&line, &len, log)) != -1)
	{
		char *token = strtok(line, " ");
		char *list[7];

		int i = 0;
		while (token != NULL)
		{
			list[i] = token;
			token = strtok(NULL, " ");
			i++;
		}
		head = create_list(head, atoi(list[0]), list[1], list[2], list[3],
						   atoi(list[4]), atoi(list[5]), list[6]);
	}

	return head;
}

void print_list(struct entry *head)
{

	while ((head->next) != NULL)
	{
		printf("Log: %d %s %s %s %d %d %s", head->uid, head->file,
			   head->date, head->time, head->access_type,
			   head->action_denied, head->fingerprint);
		head = head->next;
	}
	printf("Log: %d %s %s %s %d %d %s", head->uid, head->file,
		   head->date, head->time, head->access_type,
		   head->action_denied, head->fingerprint);
}

int check_suffix(char *filename)
{
	filename = strrchr(filename, '.');

	if (filename != NULL)
		return (strcmp(filename, ".encrypt"));

	return (-1);
}

char *filename_finder(char *filename)
{
	char *f = strrchr(filename, '/');
	f = (f + 1);
	return strtok(f, ".");
}

int check_time(char *file_time)
{
	struct tm *p;
	time_t t;
	t = time(NULL);
	p = localtime(&t);
	char time[BUF_LEN] = {0};
	memset(time, 0, BUF_LEN);
	strftime(time, BUF_LEN, "%T", p);

	char *list1[3];
	char *list2[3];

	int i = 0;
	char *token1 = strtok(time, ":");
	while (token1 != NULL)
	{
		list1[i] = token1;
		token1 = strtok(NULL, ":");
		i++;
	}
	i = 0;
	char *token2 = strtok(file_time, ":");
	while (token2 != NULL)
	{
		list2[i] = token2;
		token2 = strtok(NULL, ":");
		i++;
	}

	int cur_hr = atoi(list1[0]);
	int cur_min = atoi(list1[1]);
	int file_hr = atoi(list2[0]);
	int file_min = atoi(list2[1]);
	if ((cur_min < file_min + 20 && cur_hr == file_hr) || ((cur_min + file_min) % 60 < 20) && cur_hr == file_hr + 1)
	{
		return 1;
	}
	return 0;
}
