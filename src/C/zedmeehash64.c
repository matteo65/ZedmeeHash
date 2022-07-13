/**
 * ZedmeeHash64 
 * Strong, fast, simple, non-cryptographic 32-bit hash function
 * 
 * Author: Matteo Zapparoli
 * Date: 2021
 * Licence: Public Domain
 * 
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 * 
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * For more information, please refer to <https://unlicense.org>
 * 
 */
 
#include <stdlib.h> // size_t
#include <stdint.h> // uint64_t

#include "zedmeehash64.h"
 
uint64_t default_table64[256];
 
/**
 * Generate the lookup table of 256 uint64_t using the lfsr258 algorithm
 * param table: uint64_t[256]
 **/
void zmh64create_table(uint64_t table[], uint64_t seed1, uint64_t seed2, 
                       uint64_t seed3, uint64_t seed4, uint64_t seed5)
{
	// lfsr258 pseudo random number generator
	// Author: Pierre L'Ecuyer
	// The initial seeds y1, y2, y3, y4, y5  MUST be larger than
	// 1, 511, 4095, 131071 and 8388607 respectively.
	
	// if seed1 is less than 2 
	if((seed1 & 0xFFFFFFFFFFFFFFFEL) == 0) seed1 |= 0x02L;
	
	// if seed2 is less than 512 
	if((seed2 & 0xFFFFFFFFFFFFFE00L) == 0) seed2 |= 0x0200L;
	
	// if seed3 is less than 4096 
	if((seed3 & 0xFFFFFFFFFFFFF000L) == 0) seed3 |= 0x01000L;
	
	// if seed4 is less than 131072
	if((seed4 & 0xFFFFFFFFFFFE0000L) == 0) seed4 |= 0x20000L;
	
	// if seed5 is less than 8388608
	if((seed5 & 0xFFFFFFFFFF800000L) == 0) seed5 |= 0x800000L;
	
	for(int i = 0; i < 256; i++) 
	{
		uint64_t b = ((seed1 << 1) ^ seed1) >> 53;
		seed1 = ((seed1 & 0xFFFFFFFFFFFFFFFEL) << 10) ^ b;
		b = ((seed2 << 24) ^ seed2) >> 50;
		seed2 = ((seed2 & 0xFFFFFFFFFFFFFE00L) << 5) ^ b;
		b = ((seed3 << 3) ^ seed3) >> 23;
		seed3 = ((seed3 & 0xFFFFFFFFFFFFF000L) << 29) ^ b;
		b = ((seed4 << 5) ^ seed4) >> 24;
		seed4 = ((seed4 & 0xFFFFFFFFFFFE0000L) << 23) ^ b;
		b = ((seed5 << 3) ^ seed5) >> 33;
		seed5 = ((seed5 & 0xFFFFFFFFFF800000L) << 8) ^ b;
		
		table[i] = (seed1 ^ seed2 ^ seed3 ^ seed4 ^ seed5);
	}
}							
 
/**
 * Initialize the defalt table
 */
void zmh64init_table(void)
{
	zmh64create_table(default_table64, DEFAULT_TABLE_SEED64_1, DEFAULT_TABLE_SEED64_2,
	                 DEFAULT_TABLE_SEED64_3, DEFAULT_TABLE_SEED64_4, DEFAULT_TABLE_SEED64_5);
}

