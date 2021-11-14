# ZedmeeHash 32/64
Strong, fast, simple, non-cryptographic 32/64 hash function  

Zedmee is based on the use of a table of randomly calculated numbers. It uses two algorithms, thw first for data up to 4 bytes long for the 32-bit version (8 bytes for the 64-bit version), the second for data with a longer length.  

The first algorithm is based on bitwise operations and values substitution, the second one is a multiplicative hash function, it belongs to the family of LCG (linear congruential generator) like the hash function of java, also it uses the preloaded random table and uses some tricks to eliminate the defects of the LCG functions: their distribution is not perfectly uniform, but follows some patterns that are highlighted through the representation on a two-dimensional diagram.  

```java
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
```
  
The preloaded table contains 256 random values (one for each byte) generated with the algorithm lfsr113 (for 32 bit) or lfsr258 (for 64 bit) in the initialization phase.  

Through the selection of the particular seeds, numerical recipes and other small tricks, zedmee is able to achieve the quality of the best hash functions, maintaining the simplicity and the speed of the LCGs.  

## Uniform and chaotic distribution of hash values (dispersion)
Zedmee has an absolutely uniform, chaotic distribution of hash values independent of the number, length and type of input values.  
Even minimal differences (1 bit) in the input values produce very different hash values.  

#### java.util.Arrays.hashCode()
![Alt Text](https://raw.githubusercontent.com/matteo65/ZedmeeHash/main/Resource/java_hash.png)
  
#### ZedmeeHash32.hash()
![Alt Text](https://raw.githubusercontent.com/matteo65/ZedmeeHash/main/Resource/zmh_distributions.png)

## Minimum number of collisions
Zedmee belongs to the family of dispersing hash functions which always guarantees a very low number of collisions for each reasonably large number of values.  

#### 32-bit hash functions: number of collisions for small data arrays (1-4 bytes)

Data input                                                            |   #Hashes   |  Zedmee  | Murmur3|    XX  |  Rabin
--------------------------------------------------------------------- |-------------|----------|--------|--------|--------
All 4-bytes values 00000000-FFFFFFFF                                  |4,294,967,296|         0|       0|       0|      0
1 to 4 bytes values 00-FF, 0100-FFFF, 010000-FFFFFF, 01000000-FFFFFFFF|4,294,967,296|16,776,960|16,711,655|16,711,713| 0
1 to 4 bytes values 00-FF, 0000-FFFF, 000000-FFFFFF, 00000000-FFFFFFFF|4,311,810,304|16,843,008|16,843,008|16,843,008|16,843,008

#### 32-bit hash functions: number of collisions for strings (ASCII 1 byte per char)

Data input                                                  |   #Hashes   | Zedmee | Murmur3 |   XX   |  Rabin
------------------------------------------------------------|-------------|--------|---------|--------|---------
Numbers as strings from "0" to "999999999"                  |1,000,000,000|107,844,089|107,822,463|110,287,893|365,950,432
File Resource words_en.txt                                  |    65,503   |       0|        0|       0|      14
File Resource words_es.txt                                  |    74,571   |       0|        2|       0|      38
File Resource words_it.txt                                  |   117,558   |       0|        0|       2|      28
File Resource words_latin.txt                               |    80,007   |       0|        1|       1|      34
File Resource words_en_es_it_latin.txt                      |   315,198   |       0|        9|       9|     271
File Resource words_and_numbers.txt                         |   429,187   |       7|       20|      19|     251
File Resource first_million_primes.txt                      |   1,000,000 |     116|      118|      85|       0
File Resource random_64bit_signed_numbers.txt               |   1,000,000 |     111|      110|     143|     122

#### 32-bit hash functions: number of collisions for data input from [19-48] bytes

Data input                                                                             | #Hashes   |  Zedmee   | Murmur3  |     XX   | Rabin
---------------------------------------------------------------------------------------|-----------|-----------|----------|----------|----------
Number as strings from "1234567890123456789" to "1234567890223456789"                  |100,000,000| 1,154,366 | 1,155,789|   808,693|         0      
Strings from "abcdefg1234567890123456789hijklmn" to "abcdefg1234567890223456789hijklmn"|100,000,000| 1,153,317 | 1,152,600| 1,037,151|         0  
Binary 24 bytes [b,b,b,b,b,b], b from 00000000 to 05F5E0FF                             |100,000,000| 1,154,552 | 1,154,653| 1,411,483|         0
Binary 24 bytes [b,b\*3,b\*5,b\*7,b\*11,b\*13], b from 00000000 to 05F5E0FF            |100,000,000| 1,155,340 | 1,154,542| 1,160,003| 1,150,862
Strings 48 length "ssssss", s from "00000000" to "05F5E0FF"                            |100,000,000| 1,156,789 | 1,156,254| 1,155,854|22,595,936
Random 32 bytes [rrrrrrrr], r from 00000000 to FFFFFFFF random                         |100,000,000| 1,153,104 | 1,156,450| 1,154,307| 1,156,219


## Speed


## Vulnerability
Zedmee, like most non-cryptographic functions, is non-secure because it is not specifically designed to be difficult to reverse by an adversary, making it unsuitable for cryptographic purposes. Its use is instead recommended in all other contexts where hash functions are used.  
Like other non-cryptographic functions, its security depends on the secrecy of the possibly used seed, but unlike the other algorithms, zedmee allows you to initialize the preloaded table with four own 32-bit seeds (or five 64-bit seeds for version 64) which make it much more secure than the others functions.  

## Portability
It is simple, straightforward and can be easily written in virtually any programming language.  
Currently C and Java versions are for Big Endian architecture but mirror functions for Little Endian can be easily written.  

## About comparison functions
The family of 32/64 bit hash functions is very numerous: SuperFastHash, FNV, djb2, PJW, Murmur

## Conclusion
Zedmee demonstrates to have a quality of the dispersion of values close to an ideal Universal Hash Function, and shares this quality with Murmur3, while XX has a slightly lower quality as it has a distribution that in some cases differs (positively or negatively) from a random spread.  
In terms of speed, XX is certainly the best performing, even in the non-parallel version, but zedmee is only slightly lower and certainly faster than all other hash functions of similar quality.  
Therefore it can be said that zedmee combines maximum distribution performance, speed and simplicity which make it an ideal function for non-cryptographic hashing.  
