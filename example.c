#include <stdio.h> // printf
#include <string.h> // strlen

#include "zedmeehash32.h"
#include "zedmeehash64.h"

/*
	Output: 
	
	zedmeehash32("blablabla") = 65218EDA
	zedmeehash64("blablabla") = B047CF62AFF2384D
	zedmeehash32("123456789") = 7ECE0FC4
	zedmeehash64("123456789") = B29B51359389E4CD
	zedmeehash32("AAAAAAAAAAAAAAAAAAAA") = 5D7881E5
	zedmeehash64("AAAAAAAAAAAAAAAAAAAA") = 523F45DE501D5B9B
*/

void zedmee(const char *str)
{
	printf("zedmeehash32(\"%s\") = %08X\n", str, zedmeehash32def(str, strlen(str)));
	printf("zedmeehash64(\"%s\") = %016llX\n", str, zedmeehash64def(str, strlen(str)));
}

int main(char *argv[], int argc)
{
	zmh32init_table();
	zmh64init_table();
	
	zedmee("blablabla");
	zedmee("123456789");
	zedmee("AAAAAAAAAAAAAAAAAAAA");
}