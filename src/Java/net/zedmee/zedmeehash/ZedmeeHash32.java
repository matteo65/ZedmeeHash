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
	
	/** Random table */
	private static final int[] rtable = new int[256];
	
	private static int rtable0, rtable1, rtable2;
	
	public static final int DEFAULT_SEED = 0;
	
	static {
		init(-714727681, -1283802615, 25595457, -498285495);
	}
	
	/**
	 * Generate random table
	 * @param seed1
	 * @param seed2
	 * @param seed3
	 * @param seed4
	 */
	public static void init(int seed1, int seed2, int seed3, int seed4) {
		// lfsr113 pseudo random number generator
		// Author: Pierre L'Ecuyer
		// The initial (uint32) seed1, seed2, seed33, seed4  MUST be larger than
		// 1, 7, 15, and 127 respectively!!!

		if((seed1 & 0xFFFFFFFE) == 0) {
			// seed1 is less than 2 
			seed1 |= 0x02;
		}
				
		if((seed2 & 0xFFFFFFF8) == 0) {
			// seed2 is less than 8
			seed2 |= 0x08;
		}
		
		if((seed3 & 0xFFFFFFF0) == 0) {
			// seed3 is less than 16
			seed3 |= 0x10;
		}
				
		if((seed4 & 0xFFFFFF80) == 0) {
			// seed4 is less than 128;
			seed4 |= 0x80; // Force bit 128
		}
		
		byte[] bt = new byte[32];
		
		int i = 0;
		while(i < 256) {
			int b = (((seed1 << 6) ^ seed1) >>> 13);
			seed1 = (((seed1 & 0xFFFFFFFE) << 18) ^ b);
			b = (((seed2 << 2) ^ seed2) >>> 27);
			seed2 = (((seed2 & 0xFFFFFFF8) << 2) ^ b);
			b = (((seed3 << 13) ^ seed3) >>> 21);
			seed3 = (((seed3 & 0xFFFFFFF0) << 7) ^ b);
			b = (((seed4 << 3) ^ seed4) >>> 12);
			seed4 = (((seed4 & 0xFFFFFF80) << 13) ^ b);
			
			int c = seed1 ^ seed2 ^ seed3 ^ seed4;
			
			int h = c & 0xFF;
			if(h == i) {
				continue;
			}
			int k = (h >>> 3);
			int m = 1 << (h & 0x07);

			if((bt[k] & m) != 0) {
				continue;
			}
			bt[k] |= m;
			
			rtable[i++] = c;
		}
		rtable0 = rtable[0];
		rtable1 = rtable[1];
		rtable2 = rtable[2];
	}
	
	private ZedmeeHash32() {}
	
	/**
	 * @param data not null
	 * @param pos >= 0 and < data.length
	 * @param length >= 0 and <= data.length
	 * @param seed
	 * @return hash value
	 */
	public static int hash(final byte[] data, final int pos, final int length, final int seed) {
		int b1, b2, b3, b4;
		final int[] table = rtable;
		switch(length) {
		case 1:
			b1 = rtable0;
			b2 = rtable1;
			b3 = rtable2;
			b4 = table[data[pos] & 0xFF];
			break;
			
		case 2:
			b1 = rtable1;
			b2 = rtable2;
			b3 = table[data[pos] & 0xFF];
			b4 = table[data[pos + 1] & 0xFF];
			break;
			
		case 3:
			b1 = rtable2;
			b2 = table[data[pos] & 0xFF];
			b3 = table[data[pos + 1] & 0xFF];
			b4 = table[data[pos + 2] & 0xFF];
			break;
			
		case 4:
			b1 = table[data[pos] & 0xFF];
			b2 = table[data[pos + 1] & 0xFF];
			b3 = table[data[pos + 2] & 0xFF];
			b4 = table[data[pos + 3] & 0xFF];
			break;
			
		default:
			final int len = pos + length;
			int h = seed;
			for(int i = pos; i < len; i++)
				h = (h * 134775813) ^ table[(i + data[i]) & 0xFF];
			
			return h;
		}
		
		int x = b3 ^ b4;
		int y = b1 ^ b2;
		
		return seed ^ (((b2 ^ x) << 24) |
		              (((b1 ^ x) << 16) & 0x00FF0000) |
		              (((y ^ b4) << 8) & 0x0000FF00) |
		              ((y ^ b3) & 0x000000FF));		
	}

	public static int hash(final byte[] data, final int pos, final int length) {
		return hash(data, pos, length, DEFAULT_SEED);
	}
	
	public static int hash(final byte[] data, final int length) {
		return hash(data, 0, length, DEFAULT_SEED);
	}
	
	public static int hash(final byte[] data) {
		return hash(data, 0, data.length, DEFAULT_SEED);
	}

}
