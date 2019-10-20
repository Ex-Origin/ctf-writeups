#!/bin/sh

EXP_FILE=$1
INTERPRETER="python2"
STDERR_FILE="/tmp/exp.sh.err"

times=0
trap   " rm -f $STDERR_FILE ; exit "  INT

if [ ! $1 ]
then
    echo "Usage: ./exp.sh ./your_file.py"
    exit
fi

ulimit -c 0

while ((!(test -e $STDERR_FILE) || (test -s $STDERR_FILE)))
do
    times=$((times+1))
    printf "times %d\n\n" $times
    $INTERPRETER $EXP_FILE 2>$STDERR_FILE
done

rm -f $STDERR_FILE