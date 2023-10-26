CC = gcc
DBUG = -g
CCFLAGS = -O2 -Wall -fsanitize=address -pedantic 
OBJFILES = main.o rsa.o utils.o

TARGET = assign_3

all: $(TARGET)

a:
	make clean && make all && make g && make e && make d && make e1 && make d1

$(TARGET): $(OBJFILES)
	$(CC) $(CFLAGS) $(DBG) -o $(TARGET) $(OBJFILES) -lm

clean:
	rm -f $(TARGET) *.o test2.txt public.key private.key

g:	
	./assign_3 -g

e:
	./assign_3 -i hpy414_encryptme_pub.txt -o TUC2016030125_encrypted_pub.txt -k  hpy414_public.key -e 	

d:
	./assign_3 -i hpy414_decryptme_pub.txt -o TUC2016030125_decrypted_pub.txt -k hpy414_public.key -d 	

e1:
	./assign_3 -i hpy414_encryptme_priv.txt -o TUC2016030125_encrypted_priv.txt -k hpy414_private.key -e 	

d1:
	./assign_3 -i hpy414_decryptme_priv.txt -o TUC2016030125_decrypted_priv.txt -k  hpy414_private.key -d 	