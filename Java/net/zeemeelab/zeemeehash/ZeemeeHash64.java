package net.zeemeelab.zeemeehash;

/**
 * ZeemeeHash64 
 * Very strong, fast, non-cryptographic 64-bit hash function
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
public class ZeemeeHash64 extends ZeemeeHash {

	public static final int DEFAULT_SEED = 0x5F1F344C;
	
	public ZeemeeHash64(int seed1, int seed2, int seed3, int seed4) {
		super(seed1, seed2, seed3, seed4);
	}
	
	public ZeemeeHash64(int seed) {
		super(seed);
	}
	
	public ZeemeeHash64() {
		super(DEFAULT_SEED);
	}
	
	/**
	 * Evaluate and return the hash value of [data[pos], data[pos+1] ... data[pos + length - 1]]
	 * @param data
	 * @param pos >= 0 && pos < data.length
	 * @param length >= 1 && pos + length <= data.length
	 * @return hash value
	 */
	public long hash(final byte[] data, int pos, int length) {
		long b8, b7, b6, b5, b4, b3, b2, b1;
		
		switch(length) {
		case 1:
			b1 = byteTable[data[pos] & 0xff];
			b2 = byteTable[(int)b1];
			b3 = byteTable[(int)b2];
			b4 = byteTable[(int)b3];
			b5 = byteTable[(int)b4];
			b6 = byteTable[(int)b5];
			b7 = byteTable[(int)b6];
			
			return (byteTable[(int)b7] << 56) | (b7 << 48) | (b6 << 40) | (b5 << 32) |
				   (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
		case 2:
			b1 = byteTable[data[pos] & 0xFF];
			b2 = byteTable[data[pos + 1] & 0xFF];
			b3 = byteTable[(int)(b1 ^ b2)];
			b4 = byteTable[(int)b3];
			b5 = byteTable[(int)b3];
			b6 = byteTable[(int)b4];
			b7 = byteTable[(int)b6];
			
			return (byteTable[(int)b7] << 56) | (b7 << 48) | (b6 << 40) | (b5 << 32) |
				   (b4 << 24) | (b3 << 16) | (b2 << 8) | (b1 ^ b2);
		
		case 3:
			b1 = byteTable[data[pos] & 0xFF];
			b2 = byteTable[data[pos + 1] & 0xFF];
			b3 = byteTable[data[pos + 2] & 0xFF];
			b4 = byteTable[(int)(b1 ^ b2 ^ b3) & 0xFF];
			b5 = byteTable[(int)b4];
			b6 = byteTable[(int)b5];
			b7 = byteTable[(int)b6];
			
			return (byteTable[(int)b7] << 56) | (b7 << 48) | (b6 << 40) | (b5 << 32) |
					(b4 << 24) | (b3 << 16) | ((b1 ^ b3) << 8) | (b2 ^ b3);
		
		case 4:
			b1 = byteTable[data[pos] & 0xFF];
			b2 = byteTable[data[pos + 1] & 0xFF];
			b3 = byteTable[data[pos + 2] & 0xFF];
			b4 = byteTable[data[pos + 3] & 0xFF];
			b5 = byteTable[(int)(b1 ^ b2 ^ b3 ^ b4)];
			b6 = byteTable[(int)b4];
			b7 = byteTable[(int)b6];
			
			return (byteTable[(int)b7] << 56) | (b7 << 48) | (b6 << 40) | (b5 << 32) |
				    (b4 << 24) | ((b1 ^ b4) << 16) | ((b3 ^ b4) << 8) | (b2 ^ b4);
		
		case 5:
			b1 = byteTable[data[pos] & 0xFF];
			b2 = byteTable[data[pos + 1] & 0xFF];
			b3 = byteTable[data[pos + 2] & 0xFF];
			b4 = byteTable[data[pos + 3] & 0xFF];
			b5 = byteTable[data[pos + 4] & 0xFF];
			b6 = byteTable[(int)(b1 ^ b2 ^ b3 ^ b4 ^ b5)];
			b7 = byteTable[(int)b6];
			
			return (byteTable[(int)b7] << 56) | (b7 << 48) | (b6 << 40) | (b5 << 32) |
				   ((b2 ^ b5) << 24) | ((b4 ^ b5) << 16) | ((b3 ^ b5) << 8) | (b3 ^ b5);
		
		case 6:
			b1 = byteTable[data[pos] & 0xFF];
			b2 = byteTable[data[pos + 1] & 0xFF];
			b3 = byteTable[data[pos + 2] & 0xFF];
			b4 = byteTable[data[pos + 3] & 0xFF];
			b5 = byteTable[data[pos + 4] & 0xFF];
			b6 = byteTable[data[pos + 5] & 0xFF];
			b7 = byteTable[(int)(b1 ^ b2 ^ b3 ^ b4 ^ b5 ^ b6)];
			
			return (byteTable[(int)b7] << 56) | (b7 << 48) | (b6 << 40) | ((b3 ^ b6) << 32) |
				   ((b5 ^ b6) << 24) | ((b4 ^ b6) << 16) | ((b1 ^ b5) << 8) | (b2 ^ b6);

		case 7:
			b1 = byteTable[data[pos] & 0xFF];
			b2 = byteTable[data[pos + 1] & 0xFF];
			b3 = byteTable[data[pos + 2] & 0xFF];
			b4 = byteTable[data[pos + 3] & 0xFF];
			b5 = byteTable[data[pos + 4] & 0xFF];
			b6 = byteTable[data[pos + 5] & 0xFF];
			b7 = byteTable[data[pos + 6] & 0xFF];
			b8 = byteTable[(int)(b1 ^ b2 ^ b3 ^ b4 ^ b5 ^ b6 ^ b7)];
			
			return (b8 << 56) | (b7 << 48) | ((b5 ^ b7) << 40) | ((b6 ^ b8) << 32) |
				   ((b3 ^ b8) << 24) | ((b2 ^ b7) << 16) | ((b1 ^ b7) << 8) | (b3 ^ b7);
		
		default:
			b8 = byteTable[data[pos] & 0xFF];
			b7 = byteTable[data[pos + 1] & 0xFF];
			b6 = byteTable[data[pos + 2] & 0xFF];
			b5 = byteTable[data[pos + 3] & 0xFF];
			b4 = byteTable[data[pos + 4] & 0xFF];
			b3 = byteTable[data[pos + 5] & 0xFF];
			b2 = byteTable[data[pos + 6] & 0xFF];
			b1 = byteTable[data[pos + 7] & 0xFF];
		}
		
		int i = (length < 10) ? 2 : 0;
		
		do {
			b8 = byteTable[(int)(b8 ^ b7)];			
			b7 = byteTable[(int)(b7 ^ b6)];
			b6 = byteTable[(int)(b6 ^ b5)];
			b5 = byteTable[(int)(b5 ^ b4)];		
			b4 = byteTable[(int)(b4 ^ b3)];			
			b3 = byteTable[(int)(b3 ^ b2)];
			b2 = byteTable[(int)(b2 ^ b1)];
			b1 = byteTable[(int)(b1 ^ b8)];		
		}
		while(i-- != 0);
		
		long h = (b8 << 56) | (b7 << 48) | (b6 << 40) | (b5 << 32) |
		         (b4 << 24) | (b3 << 16) | (b2 << 8) | b1;
		
		if(length > 8) {
			length = pos + length;
			pos += 8;
			do {
				h = 0xF1A4A647C324826DL * h + data[pos++];
			}
			while(pos < length);
		}	
		return h;
	}
	
	public long hash(final byte[] data, int length) {
		return hash(data, 0, length);
	}
	
	public long hash(final byte[] data) {
		return hash(data, 0, data.length);
	}

}
