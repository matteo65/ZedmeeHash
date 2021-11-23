/**
 * ZedmeeHash32 
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
#include <stdint.h> // uint32_t

#include "zedmeehash32.h"


/**
 * Generate and return a array of 256 uint32_t using the lfsr113 algorithm
 * It return a static array rewrited every call
 **/
const uint32_t *gentable(uint32_t seed1, uint32_t seed2, uint32_t seed3, uint32_t seed4) 
{
		static uint32_t table[256];
		
		// LFSR113 pseudo random number generator
		// Author: Pierre L'Ecuyer
		// The initial seeds seed1, seed2, seed3, seed4  MUST be larger than
		// 1, 7, 15, and 127 respectively!!!
		
		if(seed1 < 2) seed1 |= 0x02;
				
		if(seed2 < 8) seed2 |= 0x08;
		
		if(seed3 < 16) seed3 |= 0x10;
		
		if(seed4 < 128)	seed4 |= 0x80;

		for (int i = 0; i < 256; i++) {
			int b = (((seed1 << 6) ^ seed1) >> 13);
			seed1 = (((seed1 & 0xFFFFFFFE) << 18) ^ b);
			b = (((seed2 << 2) ^ seed2) >> 27);
			seed2 = (((seed2 & 0xFFFFFFF8) << 2) ^ b);
			b = (((seed3 << 13) ^ seed3) >> 21);
			seed3 = (((seed3 & 0xFFFFFFF0) << 7) ^ b);
			b = (((seed4 << 3) ^ seed4) >> 12);
			seed4 = (((seed4 & 0xFFFFFF80) << 13) ^ b);
			
			table[i] = seed1 ^ seed2 ^ seed3 ^ seed4;
	    }
		
		return table;
}

/**
 * Return zedmee32 hash value
 * data not NULL
 * pos >= 0 && pos < data.length
 * length <= data.length
 * table a 256 uint32_t array
 **/
uint32_t zedmeehash32(const void *data, int pos, size_t length, uint32_t seed, const uint32_t table[]) 
{
	register const char *p = (const char*)(data + pos);
	register uint32_t h = seed;
	while (length) {
		h += h << 2;
		h ^= table[(--length + *p++) & 0xFF];
	}
	return h;
}



