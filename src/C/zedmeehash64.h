#ifndef _ZEDMEEHASH64_H
#define _ZEDMEEHASH64_H

#include <stdlib.h> // size_t
#include <stdint.h> // uint64_t

#ifdef __cplusplus
extern "C" {
#endif

#ifdef __GNUC__
#define FORCE_INLINE __attribute__((always_inline)) inline
#else
#define FORCE_INLINE inline
#endif

#define DEFAULT_SEED64           0

#define DEFAULT_TABLE_SEED64_1 0x3964D44B4DE22DC3L
#define DEFAULT_TABLE_SEED64_2 0xF509942DD52B6A13L
#define DEFAULT_TABLE_SEED64_3 0x1E5499BE8734977FL
#define DEFAULT_TABLE_SEED64_4 0x759712F4EAA664EEL
#define DEFAULT_TABLE_SEED64_5 0xCA2E28643E732272L

extern uint64_t default_table64[];

/**
 * Generate the random table of 256 uint64_t using the lfsr258 algorithm
 * param table: uint64_t[256]
 **/
void zmh64create_table(uint64_t table[], uint64_t seed1, uint64_t seed2, 
                       uint64_t seed3, uint64_t seed4, uint64_t seed5);

/**
 * Initialize the defalt table
 */
void zmh64init_table(void);

/**
 * Return zedmee64 hash value
 * data not NULL
 * pos >= 0 && pos < data.length
 * length <= data.length
 * table a 256 uint64_t array
 **/
FORCE_INLINE uint64_t zedmeehash64(const uint8_t *data, size_t length, uint64_t seed, const uint64_t table[]) 
{
	while(length)
		seed = table[(--length + data[length]) & 0xFF] ^ ((seed << 2) + seed);
	return seed;
}

/**
 * Use the default table, default seed
 **/
FORCE_INLINE uint64_t zedmeehash64def(const uint8_t *data, size_t length)
{
	uint64_t seed = DEFAULT_SEED64;
	while(length)
		seed = default_table64[(--length + data[length]) & 0xFF] ^ ((seed << 2) + seed);
	return seed;
}

#ifdef __cplusplus
}
#endif

#endif