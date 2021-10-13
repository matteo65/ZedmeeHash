package net.zeemeelab.zeemeehash;

/**
 * ZeemeeHash 
 * Very strong, fast, non-cryptographic 32-bit hash function
 * Abstract class
 * 
 * Copyright(C) 2021 - Matteo Zapparoli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
abstract class ZeemeeHash {
	
	protected final int[] byteTable = new int[256];
	
	// Used by lfsr113 pseudo random number generator
	// Author: Pierre L'Ecuyer
	// The initial seeds z1, z2, z3, z4  MUST be larger than
	// 1, 7, 15, and 127 respectively!!!
	private int z1, z2, z3, z4;
	
	public ZeemeeHash(int seed1, int seed2, int seed3, int seed4) {
		genByteTable(seed1, seed2, seed3, seed4);
	}
	
	public ZeemeeHash(int seed) {
		genByteTable(seed, seed, seed, seed);
	}
	
	private void genByteTable(int seed1, int seed2, int seed3, int seed4) {
		// Note: seed1,2,3,4 are consider unsigned
		
		if((seed1 & 0xFFFFFFFE) == 0) {
			// seed1 is less than 2 
			seed1 |= 0x02;
		}
		z1 = seed1;
		
		if((seed2 & 0xFFFFFFF8) == 0) {
			// seed2 is less than 8
			seed2 |= 0x08;
		}
		z2 = seed2;
		
		if((seed3 & 0xFFFFFFF0) == 0) {
			// seed3 is less than 16
			seed3 |= 0x10;
		}
		z3 = seed3;
		
		if((seed4 & 0xFFFFFF80) == 0) {
			// seed4 is less than 128;
			seed4 |= 0x80; // Force bit 128
		}
		z4 = seed4;
		
		// Sorded table
		for(int i = 0; i < 256; i++) {
			byteTable[i] = i;
		}
		
		// Random permutations
		for (int i = byteTable.length - 1; i > 0; i--) {
			int x = (int)(nextUnsigned32() % i);
	        int temp = byteTable[x];
	        byteTable[x] = byteTable[i];
	        byteTable[i] = temp;
	    }
	}

	/**
	 * lfsr113 algorithm
	 * @return
	 */
	private long nextUnsigned32() { 
		int b = (((z1 << 6) ^ z1) >>> 13);
		z1 = (((z1 & 0xFFFFFFFE) << 18) ^ b);
		b = (((z2 << 2) ^ z2) >>> 27);
		z2 = (((z2 & 0xFFFFFFF8) << 2) ^ b);
		b = (((z3 << 13) ^ z3) >>> 21);
		z3 = (((z3 & 0xFFFFFFF0) << 7) ^ b);
		b = (((z4 << 3) ^ z4) >>> 12);
		z4 = (((z4 & 0xFFFFFF80) << 13) ^ b);
		return (z1 ^ z2 ^ z3 ^ z4) & 0xffffffffL;
	}

}
