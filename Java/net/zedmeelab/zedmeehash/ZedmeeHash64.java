package net.zedmeelab.zedmeehash;

/**
 * ZedmeeHash64
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
public class ZedmeeHash64 extends ZedmeeHash {

	public static final int DEFAULT_SEED = 1473841489;

	public ZedmeeHash64(int seed1, int seed2, int seed3, int seed4) {
		super(seed1, seed2, seed3, seed4);
	}

	public ZedmeeHash64(int seed) {
		super(seed);
	}

	public ZedmeeHash64() {
		super(DEFAULT_SEED);
	}

	/**
	 * Evaluate and return the hash value of [data[pos], data[pos+1] ... data[pos + length - 1]]
	 * @param data
	 * @param pos >= 0 && pos < data.length
	 * @param length >= 0 && pos + length <= data.length
	 * @return hash value
	 */
	public long hash(final byte[] data, int pos, int length) {
		long h = 1L;
		
		while(length > 0) {
    		long b1, b2, b3, b4, b5, b6, b7, b8;
    
    		switch(length) {
    		case 1:
    			b1 = b2 = b3 = b4 = b5 = b6 = b7 = byteTable0;
    			b8 = byteTable[data[pos] & 0xff];
    			length = 0;
    			break;
    
    		case 2:
    			b1 = b2 = b3 = b4 = b5 = b6 = byteTable0;
    			b7 = byteTable[data[pos] & 0xff];
    			b8 = byteTable[data[pos + 1] & 0xff];
    			length = 0;
    			break;
    
    		case 3:
    			b1 = b2 = b3 = b4 = b5 = byteTable0;
    			b6 = byteTable[data[pos] & 0xff];
    			b7 = byteTable[data[pos + 1] & 0xff];
    			b8 = byteTable[data[pos + 2] & 0xff];
    			length = 0;
    			break;
    
    		case 4:
    			b1 = b2 = b3 = b4 = byteTable0;
    			b5 = byteTable[data[pos] & 0xff];
    			b6 = byteTable[data[pos + 1] & 0xff];
    			b7 = byteTable[data[pos + 2] & 0xff];
    			b8 = byteTable[data[pos + 3] & 0xff];
    			length = 0;
    			break;
    
    		case 5:
    			b1 = b2 = b3 = byteTable0;
    			b4 = byteTable[data[pos] & 0xff];
    			b5 = byteTable[data[pos + 1] & 0xff];
    			b6 = byteTable[data[pos + 2] & 0xff];
    			b7 = byteTable[data[pos + 3] & 0xff];
    			b8 = byteTable[data[pos + 4] & 0xff];
    			length = 0;
    			break;
    
    		case 6:
    			b1 = b2 = byteTable0;
    			b3 = byteTable[data[pos] & 0xff];
    			b4 = byteTable[data[pos + 1] & 0xff];
    			b5 = byteTable[data[pos + 2] & 0xff];
    			b6 = byteTable[data[pos + 3] & 0xff];
    			b7 = byteTable[data[pos + 4] & 0xff];
    			b8 = byteTable[data[pos + 5] & 0xff];
    			length = 0;
    			break;
    
    		case 7:
    			b1 = byteTable0;
    			b2 = byteTable[data[pos] & 0xff];
    			b3 = byteTable[data[pos + 1] & 0xff];
    			b4 = byteTable[data[pos + 2] & 0xff];
    			b5 = byteTable[data[pos + 3] & 0xff];
    			b6 = byteTable[data[pos + 4] & 0xff];
    			b7 = byteTable[data[pos + 5] & 0xff];
    			b8 = byteTable[data[pos + 6] & 0xff];
    			length = 0;
    			break;
    
    		default:
    			b1 = byteTable[(pos + data[pos++]) & 0xff];
    			b2 = byteTable[(pos + data[pos++]) & 0xff];
    			b3 = byteTable[(pos + data[pos++]) & 0xff];
    			b4 = byteTable[(pos + data[pos++]) & 0xff];
    			b5 = byteTable[(pos + data[pos++]) & 0xff];
    			b6 = byteTable[(pos + data[pos++]) & 0xff];
    			b7 = byteTable[(pos + data[pos++]) & 0xff];
    			b8 = byteTable[(pos + data[pos++]) & 0xff];
    			length -= 8;
    		}
    
    		long x1 = b2 ^ b1;
    		long x2 = b4 ^ b3;
    		long x3 = b6 ^ b5;
    		long x4 = b8 ^ b7;
    		long y1 = x2 ^ x1;
    		long y2 = x4 ^ x3;
    
    		h = (6364136223846793005L * h) ^
    		    (((b7 ^ x3 ^ y1) << 56) |
    			 ((b8 ^ x3 ^ y1) << 48) |
    			 ((x4 ^ b5 ^ y1) << 40) |
    			 ((x4 ^ b6 ^ y1) << 32) |
    			 ((y2 ^ b3 ^ x1) << 24) |
    			 ((y2 ^ b4 ^ x1) << 16) |
    			 ((y2 ^ x2 ^ b1) << 8) |
    			 ((y2 ^ x2 ^ b2)));
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
