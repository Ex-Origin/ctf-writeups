#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <unistd.h>

#define MAX_SIZE 0x8

struct people
{
    int id;
    bool is_waitting;
    char *info;
};

int waitting_line[MAX_SIZE];
int num;
struct people people_list[MAX_SIZE];



int lookup_line(int id){
	int index = -1;
	for(int i=0; i<MAX_SIZE; i++){
		if(waitting_line[i] == id)
			index = i;
	}
	return index;
}

int lookup_people(int id){
    for(int i=0; i<MAX_SIZE; i++){
        if(people_list[i].id == id)
            return i;
    }
}

void put_in_line(int id){
    waitting_line[MAX_SIZE-num-1] = id;
}


void people_quit(int id){
    for(int i=0; i<MAX_SIZE; i++){
        if(people_list[i].id == id){
            free(people_list[i].info);
            people_list[i].is_waitting = false;
        }
    }

}


void forward_line(){
    int first = waitting_line[MAX_SIZE-1];
    for(int i=0; i<num; i++){
        if(i<=6){
            waitting_line[MAX_SIZE-1-i] = waitting_line[MAX_SIZE-2-i];
        }else{
            waitting_line[MAX_SIZE-1-i] = 0;
        }   
    }
    if(num>0){
        people_quit(first);
        num--;
    }
    return;
}



void new_people(){

    int id, index, size;

    printf("ID: ");
    scanf("%d", &id);
    if(id <=0 || lookup_line(id) >= 0) return;

    if(num >= MAX_SIZE) {
        forward_line();
    }

    for(index=0; index<num; index++){
        if(!people_list[index].is_waitting) break;
    }


    printf("SIZE: ");
    scanf("%d", &size);
    if(size <= 0 || size > 0x100) return;
    people_list[index].id = id;
    people_list[index].info = malloc(size);
        
    read(0,people_list[index].info,size);
    people_list[index].is_waitting = true;
    put_in_line(id);
    num++;
    return;
}


void show(){
    int id;
    int index; 

    for(int i=0; i<num; i++){
        id = waitting_line[MAX_SIZE-1-i];
        index = lookup_people(id);
        printf("%d : %d (%s)\n", i+1, id, people_list[index].info);
    }
    return;
}

void menu(){
    printf("1. New people\n");
    printf("2. Line information\n");
    printf("choice: ");
}

void init(){
    setvbuf(stdout, NULL, _IONBF, 0);
    setvbuf(stdin, NULL, _IONBF, 0);

    for(int i=0; i<MAX_SIZE; i++){
		waitting_line[i] = 0;
        people_list[i].is_waitting = false;
	}
}

int main(){

    int choice;
    
    init();
    
	
    while(true){
        menu();
        scanf("%d", &choice);
        switch (choice)
        {
        case 1:
            new_people();
            break;

        case 2:
            show();
            break;

        default:
            exit(0);
        }
        
    }


}