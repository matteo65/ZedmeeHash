#ifndef _ZEDMEEHASH32_H
#define _ZEDMEEHASH32_H

#include <stdlib.h> // size_t
#include <stdint.h> // uint32_t

#ifdef __cplusplus
extern "C" {
#endif

#define DEFAULT_SEED           0

#define DEFAULT_TABLE_SEED_1   0x4BA5DDB7
#define DEFAULT_TABLE_SEED_2   0xFF608999
#define DEFAULT_TABLE_SEED_3   0x761D33E0
#define DEFAULT_TABLE_SEED_4   0xB709D577

/**
 * Generate and return a static array of 256 uint32_t using
 * the lfsr113 algorithm
 **/
const uint32_t *gentable(uint32_t seed1, uint32_t seed2, uint32_t seed3, uint32_t seed4);

/**
 * Return zedmee32 hash value
 * data not NULL
 * pos >= 0 && pos < data.length
 * length <= data.length
 * table a 256 uint32_t array
 **/
uint32_t zedmeehash32(const void *data, int pos, size_t length, uint32_t seed, const uint32_t table[]);

#ifdef __cplusplus
}
#endif

#endif