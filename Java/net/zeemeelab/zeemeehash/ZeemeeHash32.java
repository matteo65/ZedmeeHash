package net.zeemeelab.zeemeehash;

/**
 * ZeemeeHash32 
 * Very strong, fast, non-cryptographic 32-bit hash function
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
public class ZeemeeHash32 extends ZeemeeHash {
	
	public static final int DEFAULT_SEED = 0x92DDCBFB;

	public ZeemeeHash32(int seed1, int seed2, int seed3, int seed4) {
		super(seed1, seed2, seed3, seed4);
	}
	
	public ZeemeeHash32(int seed) {
		super(seed);
	}
	
	public ZeemeeHash32() {
		super(DEFAULT_SEED);
	}

	/**
	 * Evaluate and return the hash value of [data[pos], data[pos+1] ... data[pos + length - 1]]
	 * @param data
	 * @param pos >= 0 && pos < data.length
	 * @param length >= 1 && pos + length <= data.length
	 * @return hash value
	 */
	public int hash(final byte[] data, int pos, int length) {
		int b4, b3, b2, b1;
		
		switch(length) {
		case 1:
			b1 = byteTable[data[pos] & 0xff];
			b2 = byteTable[b1];
			b3 = byteTable[b2];
			return (byteTable[b3] << 24) | (b3 << 16) | (b2 << 8) | b1;
			
		case 2:
			b1 = byteTable[data[pos] & 0xff];
			b2 = byteTable[data[pos + 1] & 0xff];
			b3 = byteTable[(b1 ^ b2) & 0xff];
			return (byteTable[b3] << 24) | (b3 << 16) | (b2 << 8) | (b1 ^ b2);
			
		case 3:
			b1 = byteTable[data[pos] & 0xff];
			b2 = byteTable[data[pos + 1] & 0xff];
			b3 = byteTable[data[pos + 2] & 0xff];
			return (byteTable[b3 ^ b2 ^ b1] << 24) | (b3 << 16) | ((b1 ^ b3) << 8) | (b2 ^ b3);
		
    		default:
    			b4 = byteTable[data[pos] & 0xff];
    			b3 = byteTable[data[pos + 1] & 0xff];
    			b2 = byteTable[data[pos + 2] & 0xff];
    			b1 = byteTable[data[pos + 3] & 0xff];
		}
		
		int h = (length < 6) ? 2 : 0;
			
		do {
			b4 = byteTable[b4 ^ b3];			
			b3 = byteTable[b3 ^ b2];
			b2 = byteTable[b2 ^ b1];
			b1 = byteTable[b1 ^ b4];		
		}
		while(h-- != 0);
		
		h = (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
		
		if(length > 4) {
			length = pos + length;
			pos += 4;
			do {
				h = 134775813 * h + data[pos++];
			}
			while(pos < length);
		}
		return h; 
	}
	
	public int hash(final byte[] data, int length) {
		return hash(data, 0, length);
	}
	
	public int hash(final byte[] data) {
		return hash(data, 0, data.length);
	}
	
}
