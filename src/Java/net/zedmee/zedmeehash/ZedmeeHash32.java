package net.zedmee.zedmeehash;

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
public class ZedmeeHash32 {
		
	public static final int DEFAULT_SEED = 0;
	
	public static final int DEFAULT_TABLE_SEED_1 = 0xB8F09159;
	public static final int DEFAULT_TABLE_SEED_2 = 0x69C2A8E9;
	public static final int DEFAULT_TABLE_SEED_3 = 0x40B732C7;
	public static final int DEFAULT_TABLE_SEED_4 = 0xAE597B8B;
	
	private static final int[] DEFAULT_TABLE = genTable(new int[256], DEFAULT_TABLE_SEED_1, DEFAULT_TABLE_SEED_2, 
			                                                  DEFAULT_TABLE_SEED_3, DEFAULT_TABLE_SEED_4);

	/**
	 * Genarate the random table using lfsr113 RNG 
	 * @param table int[256]
	 * @param seed1
	 * @param seed2
	 * @param seed3
	 * @param seed4
	 * @return the table
	 */
	public static int[] genTable(int[] table, int seed1, int seed2, int seed3, int seed4) {
		// Note: seed1,2,3,4 are consider unsigned
		// Used by lfsr113 pseudo random number generator
		// Author: Pierre L'Ecuyer
		// The initial seeds seed1, seed2, seed3, seed4  MUST be larger than
		// 1, 7, 15, and 127 respectively!!!
		
		// if seed1 is less than 2 
		if((seed1 & 0xFFFFFFFE) == 0) seed1 |= 0x02;
		
		// if seed2 is less than 8
		if((seed2 & 0xFFFFFFF8) == 0) seed2 |= 0x08;
		
		// if seed3 is less than 16
		if((seed3 & 0xFFFFFFF0) == 0) seed3 |= 0x10;
		
		// if seed4 is less than 128;
		if((seed4 & 0xFFFFFF80) == 0) seed4 |= 0x80;

		for (int i = 0; i < 256; i++) {
			int b = (((seed1 << 6) ^ seed1) >>> 13);
			seed1 = (((seed1 & 0xFFFFFFFE) << 18) ^ b);
			b = (((seed2 << 2) ^ seed2) >>> 27);
			seed2 = (((seed2 & 0xFFFFFFF8) << 2) ^ b);
			b = (((seed3 << 13) ^ seed3) >>> 21);
			seed3 = (((seed3 & 0xFFFFFFF0) << 7) ^ b);
			b = (((seed4 << 3) ^ seed4) >>> 12);
			seed4 = (((seed4 & 0xFFFFFF80) << 13) ^ b);
			table[i] = (seed1 ^ seed2 ^ seed3 ^ seed4);
	    	}
		
		return table;
	}

	/**
	 * Return the expected collisions of 32-bit hash functions for n distinct values
	 * @return
	 */
	public static double expectedCollisions(int n) {
		// n-m*(1-((m-1)/m)^n) where m = 2^32
		double r = 4294967295d / 4294967296d;
		return n - 4294967296d * (1 - Math.pow(r, n));
	}
	
	private ZedmeeHash32() {}
	
	/**
	 * Return zedmee32 hash value
	 * @param data not null
	 * @param pos >= 0 and < data.length
	 * @param length >= 0 and <= data.length
	 * @param seed
	 * @return hash value
	 */
	public static int hash(final byte[] data, final int pos, int length, final int seed, final int[] table) {
		int h = seed;
		while(length > 0)
			h = table[(--length + data[pos + length]) & 0xFF] ^ (h * 5);
		return h;
	}
	
	public static int hash(final byte[] data, final int pos, final int length, int seed) {
		return hash(data, pos, length, seed, DEFAULT_TABLE);
	}
	
	public static int hash(final byte[] data, final int pos, final int length) {
		return hash(data, pos, length, DEFAULT_SEED, DEFAULT_TABLE);
	}
	
	public static int hash(final byte[] data, final int length) {
		return hash(data, 0, length, DEFAULT_SEED, DEFAULT_TABLE);
	}
	
	public static int hash(final byte[] data) {
		return hash(data, 0, data.length, DEFAULT_SEED, DEFAULT_TABLE);
	}

}
