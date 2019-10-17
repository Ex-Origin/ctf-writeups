all: a.out

a.out: main.o userfaultfd_tool.o
	gcc main.o userfaultfd_tool.o -pthread

main.o: main.c
	gcc -O3 -march=native -c main.c

userfaultfd_tool.o: userfaultfd_tool.c
	gcc -c userfaultfd_tool.c

clean: 
	rm -f main.o userfaultfd_tool.o a.out