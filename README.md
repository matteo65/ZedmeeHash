# ZedmeeHash 32/64
Strong, fast, simple, non-cryptographic 32/64 universal hash function  

Zedmee is based on the use of a table of random numbers (lookup table). The mathematical properties of distribution and number of collisions are typical of **Universal Hashing**. The algorithm is very simple and is uses only bitwise, shift and adding operations.  

The lookup table contains 256 ramdom values (one for each byte); by default zedmee uses a table generated with the lfsr113 algorithm for the 32-bit version or lfsr258 for the 64 bit version, but any other random table can be used and it is also possible to specify a seed.  

The result may seem like a trivial algorithm, as a multiplicative hash function, but the characteristics and quality of the result are equal to if not superior to the best and complex hash functions.  

```java
public static int hash(final byte[] data, int pos, int length, final int seed, final int[] table) {
	int h = seed;
	while(length-- > 0) {
		h += h << 2; 
		h ^= table[(length + data[pos++]) & 0xFF];
	}
	return h;
}
``` 

## Uniform distribution of hash values (diffusion)
Zedmee has an absolutely uniform, chaotic distribution of hash values independent of the number, length and type of input values.  
It also has a good Avalanche Effect property: even a minimal differences (1 bit) of input values produces very different hash values.  

#### java.util.Arrays.hashCode()
![Alt Text](https://raw.githubusercontent.com/matteo65/ZedmeeHash/main/Resource/java_hash.png)
  
#### ZedmeeHash32.hash()
![Alt Text](https://raw.githubusercontent.com/matteo65/ZedmeeHash/main/Resource/zmh_distributions.png)

## Minimum number of collisions
Zedmee belongs to the family of universal hash functions which always guarantees a very low number of collisions for each reasonably large number of values.  
The number of expected collisions is given by the formula _n-m*(1-((m-1)/m)^n)_ where _n_ is the number of values, _m_ is 2^32 or 2^64.  

#### 32-bit hash functions: number of collisions for small data arrays (1-4 bytes)

Data input                                             |  #Hashes  | Zedmee  | Murmur3|  XX | Rabin
------------------------------------------------------ |-----------|---------|--------|-----|-------
4-bytes values 00000000-05F5E0FF                       |100,000,000|1,151,677|       0|    0|  0
4-bytes values FA0A1F00-FFFFFFFF                       |100,000,000|1,152,546|       0|    0|  0
1 to 3 bytes values 00-FF, 0100-FFFF, 010000-FFFFFF    | 16,777,216|   32,990|       0|    0|  0
1 to 3 bytes values 00-FF, 0000-FFFF, 000000-FFFFFF    |4,311,810,304|16,843,008|16,843,008|16,843,008|16,843,008

#### 32-bit hash functions: number of collisions for strings (ASCII 1 byte per char)

Data input                                                  |   #Hashes   | Zedmee | Murmur3 |   XX   |  Rabin
------------------------------------------------------------|-------------|--------|---------|--------|---------
Numbers as strings from "0" to "999999999"                  |1,000,000,000|107,946,574|107,822,463|110,287,893|365,950,432
File Resource words_en.txt                                  |    65,503   |       0|        0|       0|      14
File Resource words_es.txt                                  |    74,571   |       0|        2|       0|      38
File Resource words_it.txt                                  |   117,558   |       0|        0|       2|      28
File Resource words_latin.txt                               |    80,007   |       0|        1|       1|      34
File Resource words_en_es_it_latin.txt                      |   315,198   |       0|        9|       9|     271
File Resource words_and_numbers.txt                         |   429,187   |       4|       20|      19|     251
File Resource first_million_primes.txt                      |   1,000,000 |     104|      118|      85|       0
File Resource random_64bit_signed_numbers.txt               |   1,000,000 |     109|      110|     143|     122

#### 32-bit hash functions: number of collisions for data input from [19-48] bytes

Data input                                                                             | #Hashes   |  Zedmee   | Murmur3  |     XX   | Rabin
---------------------------------------------------------------------------------------|-----------|-----------|----------|----------|----------
Number as strings from "1234567890123456789" to "1234567890223456789"                  |100,000,000| 1,152,470 | 1,155,789|   808,693|         0      
Strings from "abcdefg1234567890123456789hijklmn" to "abcdefg1234567890223456789hijklmn"|100,000,000| 1,153,146 | 1,152,600| 1,037,151|         0  
Binary 24 bytes [b,b,b,b,b,b], b from 00000000 to 05F5E0FF                             |100,000,000| 1,156,639 | 1,154,653| 1,411,483|         0
Binary 24 bytes [b,b\*3,b\*5,b\*7,b\*11,b\*13], b from 00000000 to 05F5E0FF            |100,000,000| 1,155,495 | 1,154,542| 1,160,003| 1,150,862
Strings 48 length "ssssss", s from "00000000" to "05F5E0FF"                            |100,000,000| 1,156,397 | 1,156,254| 1,155,854|22,595,936
Random 32 bytes [rrrrrrrr], r from 00000000 to FFFFFFFF random                         |100,000,000| 1,153,728 | 1,156,450| 1,154,307| 1,156,219


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
