#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char **argv)
{
	int i;
	size_t bytes;
	FILE *file;
	char *path = argv[1];
	char *tmp;

	int num = atoi(argv[2]);

	for (i = 1; i <= num; i++)
	{
		tmp = malloc(10000);
		strcpy(tmp, path);
		char filename[sizeof "file10000.txt"];

		sprintf(filename, "file%d", i);
		strcat(tmp, filename);

		file = fopen(tmp, "w+");

		if (file == NULL)
			printf("fopen error\n");
		else
		{
			bytes = fwrite(filename, strlen(filename), 1, file);
			fclose(file);
		}
		free(tmp);
	}
}