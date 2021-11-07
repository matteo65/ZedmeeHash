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
	
	public static int DEFAULT_SEED = 1;
	
	/** Random table */
	private static final int[] rtable = new int[256];
	
	static {
		// lfsr113 pseudo random number generator
		// Author: Pierre L'Ecuyer
		// The initial (uint32) seeds z1, z2, z3, z4  MUST be larger than
		// 1, 7, 15, and 127 respectively!!!
		
		int z1 = 0x88663CAD;
		int z2 = 0xB1F12FCD;
		int z3 = 0xBDD2F56E;
		int z4 = 0xA89EC50B;
		
		for(int i = 0; i < 256; i++) {
			int b = (((z1 << 6) ^ z1) >>> 13);
			z1 = (((z1 & 0xFFFFFFFE) << 18) ^ b);
			b = (((z2 << 2) ^ z2) >>> 27);
			z2 = (((z2 & 0xFFFFFFF8) << 2) ^ b);
			b = (((z3 << 13) ^ z3) >>> 21);
			z3 = (((z3 & 0xFFFFFFF0) << 7) ^ b);
			b = (((z4 << 3) ^ z4) >>> 12);
			z4 = (((z4 & 0xFFFFFF80) << 13) ^ b);
			
			rtable[i] = z1 ^ z2 ^ z3 ^ z4;
		}
	}
	
	private ZedmeeHash32() {}
	
	/**
	 * Best seed = 1, -201393507
	 * Good seeds = -1702396384, -1386153758, 1521663501, -737420661, -14935042, 
	 *              -201978080, -1079041895
	 * @param data not null
	 * @param pos >= 0 and < data.length
	 * @param length >= 0 and <= data.length
	 * @return
	 */
	public static int hash(final byte[] data, final int pos, final int length, int seed) {
		int h = seed;
		final int len = pos + length;
		final int[] table = rtable[];
		for(int i = pos; i < len; i++)
			h = (h * 134775813) ^ table[(i + data[i]) & 0xFF];
		return h;
	}
	
	/**
	 * Preferable version with default seed
	 * @param data not null
	 * @param pos >= 0 and < data.length
	 * @param length >= 0 and <= data.length
	 * @return
	 */
	public static int hash(final byte[] data, final int pos, final int length) {
		return hash(data, pos, length, DEFAULT_SEED);
	}
	
	public static int hash(final byte[] data, final int length) {
		return hash(data, 0, length, DEFAULT_SEED);
	}
	
	public static int hash(final byte[] data) {
		return hash(data, 0, data.length, DEFAULT_SEED);
	}

	public static int hash(int i) {
		int h = DEFAULT_SEED;
		final int[] table = rtable;
		for(int p = 0, s = 24; p < 4; p++, s -= 8)
			h = (134775813 * h) ^ table[(p + (i >>> s)) & 0xFF];
		return h;
	}
	
	public static int hash(long l) {
		int h = DEFAULT_SEED;
		final int[] table = rtable;
		for(int p = 0, s = 56; p < 8; p++, s -= 8)
			h = (134775813 * h) ^ table[(int)((p + (l >>> s)) & 0xFF)];
		return h;
	}
	
	public static int hash(short i) {
		int h = 134775813 ^ rtable[(i >>> 8) & 0xFF];
		return (134775813 * h) ^ rtable[(1 + i) & 0xFF];
	}
	
	public static int hash(char c) {
		int h = 134775813 ^ rtable[(c >>> 8) & 0xFF];
		return (134775813 * h) ^ rtable[(1 + c) & 0xFF];
	}
	
	public static int hash(byte b) {
		return 134775813 ^ rtable[b & 0xFF];
	}
	
	public static int hash(CharSequence cs) {
		int h = DEFAULT_SEED;
		final int len = cs.length();
		final int[] table = rtable;
		for(int i = 0; i < len; i++) {
			char ch = cs.charAt(i);
			h = (134775813 * h) ^ table[((i << 1) + ch) & 0xFF];
			h = (134775813 * h) ^ table[(1 + (i << 1) + (ch >>> 8)) & 0xFF];
		}
		return h;
	}
	

}
