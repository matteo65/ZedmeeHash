# ZedmeeHash 32/64
Strong, fast, simple, non-cryptographic 32/64 universal hash function  

Zedmee is based on the use of a table of random numbers (lookup table). The mathematical properties of distribution and number of collisions are typical of **Universal Hashing**. The algorithm is minimalist and uses only bitwise, shift and adding operations.  

The lookup table contains 256 ramdom values (one for each byte); by default zedmee uses a table generated with the lfsr113 algorithm for the 32-bit version or lfsr258 for the 64 bit version, but any other random table can be used; it is also possible to specify a seed.  

The result may seem like a trivial algorithm, as a multiplicative hash function, but the characteristics and quality of the result are equal to if not superior to the best and complex hash functions.  

```java
public static int hash(final byte[] data, int pos, int length, int seed, final int[] table) {
	int h = seed;
	while(length > 0)
		h = table[(--length + data[pos + length]) & 0xFF] ^ (h * 5);
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
Zedmee belongs to the family of universal hash functions which always guarantees a very low number of collisions for each reasonably large number of distinct values.  
The number of expected collisions for a Ideal Hash Funcion is given by the formula _n-m*(1-((m-1)/m)^n)_ where _n_ is the number of input values, _m_ is the number of all possible hash values (2^32 or 2^64).  

#### 32-bit : number of collisions for small data arrays (1-4 bytes)

Data input                                             |  #Values  |#Expected Collisions| Zedmee  | Murmur3|  XX 
:---                                                   |       ---:|                ---:|     ---:|    ---:| ---:
4-bytes values 00000000-05F5E0FF                       |100,000,000|           1,155,170|1,154,926|       0|    0
4-bytes values FA0A1F00-FFFFFFFF                       |100,000,000|           1,155,170|1,155,292|       0|    0
1 to 3 bytes values 00-FF, 0100-FFFF, 010000-FFFFFF    | 16,777,216|              32,725|   32,284|       0|    0
1 to 3 bytes values 00-FF, 0000-FFFF, 000000-FFFFFF    | 16,843,008|              32,982|   32,590|       0|    0  

Note that both Murmur and XX for arrays up to 4 bytes long (8 for version 64) behave like perfect hash functions (0 collisions), but this feature makes them more vulnerable.  

#### 32-bit : number of collisions for ASCII strings

Data input                                         |   #Vaues    |#Expected Collisions|    Zedmee |   Murmur3 |     XX
:---                                               |         ---:|                ---:|       ---:|       ---:|    ---:
Numbers as strings from "0" to "999999999"         |1,000,000,000| 107,882,641        |107,882,812|107,822,463|*110,287,893*
File words_en.txt                                  |    65,503   |         0          |          0|          0|          0
File words_es.txt                                  |    74,571   |         0          |          0|        *2*|          0
File words_it.txt                                  |   117,558   |         1          |          1|          0|        *2*
File words_latin.txt                               |    80,007   |         0          |          0|        *1*|        *1*
File words_en_es_it_latin.txt                      |   315,198   |        11          |          3|          9|          9
File words_and_numbers.txt                         |   429,187   |        21          |          5|         20|         19
File first_million_primes.txt                      |   1,000,000 |       116          |        110|      *118*|         85
File random_64bit_signed_numbers.txt               |   1,000,000 |       116          |        113|        110|       *143*

#### 32-bit : number of collisions for data input from [19-48] bytes. 100,000,000 values, 1,155,170 expected collisions

Data input                                                                             |  Zedmee   | Murmur3  |     XX   
:---                                                                                   |       ---:|      ---:|      ---:
Number as strings from "1234567890123456789" to "1234567890223456788"                  | 1,155,834 | 1,155,789|   808,693      
Strings from "abcdefg1234567890123456789hijklmn" to "abcdefg1234567890223456788hijklmn"| 1,155,449 | 1,152,600| 1,037,151  
Binary 24 bytes [b,b,b,b,b,b], b from 00000000 to 05F5E0FF                             | 1,154,554 | 1,154,653| 1,411,483
Binary 24 bytes [b,b\*3,b\*5,b\*7,b\*11,b\*13], b from 00000000 to 05F5E0FF            | 1,155,723 | 1,154,542| 1,160,003
Strings 48 length "ssssss", s from "00000000" to "05F5E0FF"                            | 1,153,944 | 1,156,254| 1,155,854
Random 32 bytes [rrrrrrrr], r from 00000000 to FFFFFFFF random                         | 1,155,668 | 1,156,450| 1,154,307


## Reduction of collisions through the choice of an appropriate table
If you first know that the input values are limited to a subset of all possible bytes, e.g. alphanumeric strings, printable ascii, digit only... you can use a special table to minimize collisions with that particular input subset. A simple statistical program can be used to generate this table.  
In most other algorithms, a similar but less effective result can be obtained using a particular seed. Zedmee allows both.  

#### Number of collisions with different tables

File input                   | #Values | #Expected Collisions |Collisions with Default table| Table (genTable())| Collisions 
:---                         |     ---:|               :---   |                       :---  |    :---           |     :---
File first_million_primes.txt|1,000,000|                   116|                         110 |187298315, 1315206703, -898698574, -282484222| 75
File random_64bit_signed_numbers.txt|1,000,000|            116|                         113 |-557943317, -1545483323, -942556476, -1399776912|74


## Speed
#### 32-bit hashing run time of 100,000,000 values length from 1 to 30 bytes
![Alt Text](https://raw.githubusercontent.com/matteo65/ZedmeeHash/main/Resource/32-bit_speed.png)

Zedmee32 is the fastest with length values up to 12 bytes, then it is slightly slower.  
Comparison was made with the non-parallel XX version.  

## Vulnerability
Zedmee, like most non-cryptographic functions, is non-secure because it is not specifically designed to be difficult to reverse by an adversary, making it unsuitable for cryptographic purposes. Its use is instead recommended in all other contexts where hash functions are used.  
Like other non-cryptographic functions, its security depends on the secrecy of the possibly used seed, but unlike most other algorithms, zedmee allows you to use a 256 length table of 32-bit random values (or 64-bit for zedmee64) which make it much more secure than the others functions.  


## Portability
It is minimalist, elegant, straightforward and can be easily written in virtually any programming language; the algorithm and results are the same for the Big Endian and Little Endian architectures.  
Currently C and Java versions are available.  


## About comparison functions
The family of 32/64 bit hash functions is very numerous: SuperFastHash, FNV, djb2, PJW, Murmur


## Conclusion
Zedmee demonstrates to have a quality of the dispersion of values close to an ideal Universal Hash Function, and shares this quality with Murmur3, while XX has a slightly lower quality as it has a distribution that in some cases differs (positively or negatively) from a random spread.  
In terms of speed, XX is certainly the best performing, even in the non-parallel version, but zedmee is only slightly lower and certainly faster than all other hash functions of similar quality.  
Therefore it can be said that zedmee combines maximum distribution performance, speed and simplicity which make it an ideal function for non-cryptographic hashing.  
