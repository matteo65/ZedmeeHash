#ifndef _ZEDMEEHASH32_H
#define _ZEDMEEHASH32_H

#include <stdlib.h> // size_t
#include <stdint.h> // uint32_t

#ifdef __cplusplus
extern "C" {
#endif

#define DEFAULT_SEED           0

#define DEFAULT_TABLE_SEED_1   0xB8F09159u
#define DEFAULT_TABLE_SEED_2   0x69C2A8E9u
#define DEFAULT_TABLE_SEED_3   0x40B732C7u
#define DEFAULT_TABLE_SEED_4   0xAE597B8Bu

/**
 * Generate the lookup table of 256 uint32_t using the lfsr113 algorithm
 * param table: uint32_t[256]
 **/
void zmh32create_table(uint32_t table[], uint32_t seed1, uint32_t seed2, uint32_t seed3, uint32_t seed4);

/**
 * Initialize the defalt table
 */
void zmh32init_table(void);

/**
 * Return zedmee32 hash value
 * data not NULL
 * pos >= 0 && pos < data.length
 * length <= data.length
 * table a 256 uint32_t array
 **/
uint32_t zedmeehash32(const char *data, int pos, size_t length, uint32_t seed, uint32_t table[]);

/**
 * Uses the default table
 **/
uint32_t zedmeehash32def(const char *data, int pos, size_t length, uint32_t seed);

#ifdef __cplusplus
}
#endif

#endif