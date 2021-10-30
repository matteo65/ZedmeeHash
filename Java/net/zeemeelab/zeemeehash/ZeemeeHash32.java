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
	
	public static final int DEFAULT_SEED = 1773772666;
		
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
	 * Return the hash value of {data[pos], data[pos+1] ... data[pos + length - 1]}
	 * @param data
	 * @param pos >= 0 && pos < data.length
	 * @param length >= 0 && pos + length <= data.length
	 * @return hash value
	 */
	public int hash(final byte[] data, int pos, int length) {
		int hash = 1;

		while(length > 0) {
    			int b1, b2, b3, b4;
    		
    			switch(length) {
    			case 1:
    				b1 = b2 = b3 = byteTable0;
    				b4 = byteTable[data[pos] & 0xff];
    				length = 0;
    				break;
    			
    			case 2:
    				b1 = b2 = byteTable0;
    				b3 = byteTable[data[pos] & 0xff];
    				b4 = byteTable[data[pos + 1] & 0xff];
    				length = 0;
    				break;
    			
    			case 3:
    				b1 = byteTable0;
    				b2 = byteTable[data[pos] & 0xff];
    				b3 = byteTable[data[pos + 1] & 0xff];
    				b4 = byteTable[data[pos + 2] & 0xff];
    				length = 0;
    				break;
    			
    			default:
    				b1 = byteTable[(pos + data[pos++]) & 0xff];
    				b2 = byteTable[(pos + data[pos++]) & 0xff];
    				b3 = byteTable[(pos + data[pos++]) & 0xff];
    				b4 = byteTable[(pos + data[pos++]) & 0xff];
    				length -= 4;
    			}

    			int x = b2 ^ b1;
    			int y = b4 ^ b3;
    			hash = (0x08088405 * hash) ^
    			       (((b3 ^ x) << 24) | ((b4 ^ x) << 16) | ((y ^ b1) << 8) | (y ^ b2));
		}
				
		return hash;
	}
	
	public int hash(final byte[] data, int length) {
		return hash(data, 0, length);
	}
	
	public int hash(final byte[] data) {
		return hash(data, 0, data.length);
	}
	
}
