package net.zedmee.zedmeehash;

/**
 * ZedmeeHash64 
 * Strong, fast, simple, non-cryptographic 64-bit hash function
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
public class ZedmeeHash64 {

	public static final long DEFAULT_SEED = 0;
	
	public static final long DEFAULT_TABLE_SEED_1 = 0x3964D44B4DE22DC3L;
	public static final long DEFAULT_TABLE_SEED_2 = 0xF509942DD52B6A13L;
	public static final long DEFAULT_TABLE_SEED_3 = 0x1E5499BE8734977FL;
	public static final long DEFAULT_TABLE_SEED_4 = 0x759712F4EAA664EEL;
	public static final long DEFAULT_TABLE_SEED_5 = 0xCA2E28643E732272L;
	
	private static final long[] DEFAULT_TABLE = genTable(new long[256], DEFAULT_TABLE_SEED_1, DEFAULT_TABLE_SEED_2,
			                                             DEFAULT_TABLE_SEED_3, DEFAULT_TABLE_SEED_4, DEFAULT_TABLE_SEED_5);
	
	/**
	 * Return the expected collisions of 64-bit hash functions for n distinct values
	 * @return
	 */
	public static java.math.BigDecimal expectedCollisions(int n) {
		java.math.MathContext mc = new java.math.MathContext(50);
		java.math.BigDecimal two_at_64 = new java.math.BigDecimal("18446744073709551616");
		java.math.BigDecimal r = new java.math.BigDecimal("18446744073709551615").divide(two_at_64, mc);
		java.math.BigDecimal s = java.math.BigDecimal.ONE.add(r.pow(n, mc).negate(mc));
		return (new java.math.BigDecimal(n)).add(s.multiply(two_at_64, mc).negate());
	}
	
	public static long[] genTable(long[] table, long seed1, long seed2, long seed3, long seed4, long seed5) {
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
		
		for(int i = 0; i < 256; i++) {
			long b = ((seed1 << 1) ^ seed1) >>> 53;
			seed1 = ((seed1 & 0xFFFFFFFFFFFFFFFEL) << 10) ^ b;
			b = ((seed2 << 24) ^ seed2) >>> 50;
			seed2 = ((seed2 & 0xFFFFFFFFFFFFFE00L) << 5) ^ b;
			b = ((seed3 << 3) ^ seed3) >>> 23;
			seed3 = ((seed3 & 0xFFFFFFFFFFFFF000L) << 29) ^ b;
			b = ((seed4 << 5) ^ seed4) >>> 24;
			seed4 = ((seed4 & 0xFFFFFFFFFFFE0000L) << 23) ^ b;
			b = ((seed5 << 3) ^ seed5) >>> 33;
			seed5 = ((seed5 & 0xFFFFFFFFFF800000L) << 8) ^ b;
			
			table[i] = (seed1 ^ seed2 ^ seed3 ^ seed4 ^ seed5);
		}
		return table;
	}
	
	private ZedmeeHash64() {}

	public static long hash(final byte[] data, int pos, int length, long seed, long[] table) {
		long h = seed;
		while(length > 0)
			h = table[(--length + data[pos + length]) & 0xFF] ^ (h * 5);
		return h;
	}
	
	public static long hash(final byte[] data, int pos, int length, long seed) {
		return hash(data, pos, length, seed, DEFAULT_TABLE);
	}

	public static long hash(final byte[] data, final int pos, final int length) {
		return hash(data, pos, length, DEFAULT_SEED, DEFAULT_TABLE);
	}
	
	public static long hash(final byte[] data, final int length) {
		return hash(data, 0, length, DEFAULT_SEED, DEFAULT_TABLE);
	}
	
	public static long hash(final byte[] data) {
		return hash(data, 0, data.length, DEFAULT_SEED, DEFAULT_TABLE);
	}

}
