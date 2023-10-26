Compiled with gcc (Ubuntu 9.3.0-17ubuntu1~20.04)

ransomware.sh: The ransomware script takes 3(+1 optional) arguments. Its called like that:
./ransomware.sh <filepath> <num_of_files_handled> <-c/-d/-e> [password]
$1 is the path where the files will be created/encypted/decrypted (path with '/' at the end)
$2 is the number of files to be handled
$3 is a flag -c or -d or -e for file creation, encryption, and decryption respectively
$4 is an optional argument for giving password for encryption/decryption. if blank, it is set to 1234

In order to use the logging tool in bash script I load it with export. For -c flag I call test_aclog.c with the path to create the files and also the number of 
files to be created. For -c flag the script encrypts the number of files that was indicated with aes-256-ecb encryptio with 1 iteration and given password. After
it removes the text_file. For -d flag the script decrypts the number of files that was indicated with the given password.

test_aclog: The functionality is kind of similar to previous assignment. The main difference is that it takes as arguments the path to create the files and the
number of files to be created rather than creating a fixed number of files in the same directory. Also as previously it writes inside the file the name of the
file with fwrite for all files.

logger.c: One change at this file is that it implements fopen64() function used for openssl to create the encrypted file. Because fopen64() is similar to the
plain fopen() and also with some arguments, return values etc. I call fopen via fopen64 and do the logging from there. Also I did a minor change in logging file
more specifically in new line write to better handle its parse in acmonitor.

acmonitor.c: Initially I added the -v and -e flags in main function switch to handle the new functions. From previous assignment the change I did is making a
new function called parse_log to put correctly the log files in the linked list struct and not do it only in list_unauthorized_accesses() function.

num_of_files_created(FILE *log, char *threshold): it takes as arguments the pointer to log file and a threshold. With parse_log it takes the linked list of logs.
Afterwards it sets a counter passes through the list. With file time as argument it calls check_time to see if this file was created the last 20 minutes. And if 
the function returns 1 and also the access_type and access_denied are equal to 0 (so we know that this file was opened) then the counter increases. At the end
it checks if the counter is bigger than the threshold of files set, and if it does it prints that a large number of files was created, else that all is normal.

encrypted_files(FILE *log): creating a list of logs again this function passes through the list and calls the check_suffix() function with filename as argument.
The check_suffix function returns 0 if the file has .encrypt as a suffix (so it is an encrypted file). Then it checks if access_type and access_denied are 
equal to 0 and if it does it means that an encrypted file was created. With filename_finder it strips its text filename from the path and prints the filename
of the text that was encrypted.

parse_log(FILE *log): it takes the logs and converts it into the linked list of logs. this was done in the previous assignment at the 
list_unauthorized_accesses function. it returns the head of the list.

check_suffix(char *filename): with function strrchr it compares the suffix of the function with .encrypt to check if it is an encrypted file.

char *filename_finder(char *filename): it returns the filename of the text that was encrypted

int check_time(char *file_time): it compares the current time and time of the log that is searched. If it is in the same hour it compares their minutes difference
or if the current time if one hour ahead it adds the current and file minutes and takes their modulo to see if they are 20 minutes or less apart. Also it returns 1
if the two times are 20 minutes apart else it returns 0.

print_list(struct entry *head): for debug purposes only

I did all the steps and from my tests it runs correctly. The only issue I think it is that the acmonitor doesn't work for logs of large number of files. Also I
did not change or develop further the functionality of the previous assignment in acmonitor.