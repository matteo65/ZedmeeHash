# ZedmeeHash 32/64
Very strong, fast, non-cryptographic 32/64 hash function  

The algorithm is very simple: it processes blocks of 4 bytes (any remaining at the end) (blocks of 8 for the 64-bit), calculates a hash that mixes with the previous value.  
It uses a preloaded table containing a random permutation (4 seeds) of all 256 bytes generated at inizialization or instantiation.  
The result is a very strong, fast and simple function with average performance superior to all existing hash functions.  

## Uniform and chaotic distribution of hash values
Zedmee has an absolutely uniform, chaotic distribution of hash values independent of the number, length and type of input values.
Even minimal differences (1 bit) in the input values produce very different hash values.  

![Alt Text](https://raw.githubusercontent.com/matteo65/ZedmeeHash/main/Resource/zmh_distributions.png)

## Minimum number of collisions for a set of reasonably large input values
For all possible input values of fixed length not exceeding 4 (8 for 64-bit) bytes zedmee returns a unique hash with always 0 collisions. For longer input lengths the number of collisions is always very low.  
In the 32-bit version the number of collisions is more dependent on the seeds used, so if the input values are composed of bytes of a narrow range of values, for example only alphanumeric or ASCII printable characters, you can further reduce collisions by choosing suitable seeds.  
Instead the 64-bit version is practically independent of the selected seeds.  

#### 32-bit hash functions: number of collisions for small data input [1-5] bytes   

Data input                                                                        |#Hashes   | Zedmee   | Murmur3 | XX  | Rabin  
----------------------------------------------------------------------------------|----------|----------|---------|-----|--------
4 byte length values 00000000-FFFFFFFF                                            |4,294,967,296|   0      |      0  |  0  |  0    
1 byte 00-FF, 2 bytes 0100-FFFF, 3 bytes 010000-FFFFFF, 4 bytes 01000000-FFFFFFFF |4,294,967,296|   0      |16,711,655 |16,711,713|16,777,216   
1 byte 00-FF, 2 bytes 0000-FFFF, 3 bytes 000000-FFFFFF, 4 bytes 00000000-FFFFFFFF |4,311,810,304|  16,843,008|16,843,008 |16,843,008|16,843,008
5 byte length values 0000000000-FFFFFFFFFF                                        |?|        ? |      ?  |  ?  |   ?    

#### 32-bit hash functions: number of collisions for strings (ASCII 1 byte per char)

Data input                                                  |#Hashes   | Zedmee   | Murmur3 |    XX   |  Rabin
------------------------------------------------------------|----------|----------|---------|---------|---------
Numbers as strings from "0" to "999999999"                  |1,000,000,000| 108,438,583|107,822,463|110,287,893|365,950,432
File Resource words_en.txt                                  | 65,503    |         1|        0|        0|       14
File Resource words_es.txt                                  | 74,571    |         0|        2|        0|       38
File Resource words_it.txt                                  |117,558    |         0|        0|        2|       28
File Resource words_latin.txt                               | 80,007    |         0|        1|        1|       34
File Resource words_en_es_it_latin.txt                      |315,198    |         3|        9|        9|      271
File Resource words_and_numbers.txt                         |429,187    |         8|       20|       19|      251
File Resource first_million_primes.txt                      |1,000,000  |       123|      118|       85|        0
File Resource random_64bit_signed_numbers.txt               |1,000,000  |       116|      110|      143|      122

#### 32-bit hash functions: number of collisions for data input from [19-48] bytes

Data input                                                                             | #Hashes   |  Zedmee   | Murmur3  |     XX   | Rabin
---------------------------------------------------------------------------------------|-----------|-----------|----------|----------|----------
Number as strings from "1234567890123456789" to "1234567890223456789"                  |100,000,000| 1,162,386 | 1,155,789|   808,693|         0      
Strings from "abcdefg1234567890123456789hijklmn" to "abcdefg1234567890223456789hijklmn"|100,000,000| 1,151,093 | 1,152,600| 1,037,151|         0  
Binary 24 bytes [b,b,b,b,b,b], b from 00000000 to 05F5E0FF                             |100,000,000| 1,156,105 | 1,154,653| 1,411,483|         0
Binary 24 bytes [b,b\*3,b\*5,b\*7,b\*11,b\*13], b from 00000000 to 05F5E0FF            |100,000,000| 1,154,359 | 1,154,542| 1,160,003| 1,150,862
Strings 48 length "ssssss", s from "00000000" to "05F5E0FF"                            |100,000,000| 1,184,681 | 1,156,254| 1,155,854|22,595,936


## Vulnerability
Zedmee, like most non-cryptographic functions, is non-secure because it is not specifically designed to be difficult to reverse by an adversary, making it unsuitable for cryptographic purposes. Its use is instead recommended in all other contexts where hash functions are used.  
However, since it uses 6 seeds of 32-bit values (used once in the initialization or instantiation), its security is given by the secrecy of these 4 seeds, the combination of which is 128 bits which make zeemee certainly less vulnerable than all the other non-cryptographic functions.   

## Portability
It is simple, straightforward and can be easily written in virtually any programming language.  
Currently C and Java versions are for Big Endian architecture but mirror functions for Little Endian can be easily written.    
