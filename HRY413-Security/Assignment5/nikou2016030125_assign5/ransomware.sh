#!/bin/bash

export LD_PRELOAD=/home/coregoord/Documents/asfaleia/Assignment5/logger.so

if [ -n $4 ]
then
    pass=1234
else
    pass=$4
fi

if [ "$3" == "-c" ]
then
    ./test_aclog $1 $2

elif [ "$3" == "-d" ]
then
    cd $1
    for ((i=1; i <=$2; i++))
    do
        openssl aes-256-ecb -pbkdf2 -iter 1 -in file$i.encrypt -out file$i -d -k $pass
    done    
elif [ "$3" == "-e" ]
then
    cd $1
    for ((i=1; i <=$2; i++))
    do
        openssl enc -aes-256-ecb -pbkdf2 -iter 1 -in file$i -out file$i.encrypt -k $pass
        rm -f file$i
    done    
fi

