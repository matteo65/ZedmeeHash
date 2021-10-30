# ZedmeeHash 32/64
Very strong, fast, non-cryptographic 32/64 hash function  

The algorithm is very simple: it processes blocks of 4 bytes (any remaining at the end) (blocks of 8 for the 64-bit), calculates a hash that mixes with the previous value.  
It uses a preloaded table containing a random permutation (6 seeds) of all 256 bytes generated at inizialization or instantiation.
The result is a very strong, fast and simple function with average performance superior to all existing hash functions.  

## Uniform and chaotic distribution of hash values
Zedmee has an absolutely uniform, chaotic distribution of hash values independent of the number, length and type of input values.
Even minimal differences (1 bit) in the input values produce very different hash values.  

![Alt Text](https://raw.githubusercontent.com/matteo65/ZedmeeHash/main/Resource/zmh_distributions.png)

## Minimum number of collisions for a set of reasonably large input values
For all possible input values not exceeding 4 bytes for the 32-bit version (8 bytes for the 64-bit version)  
zeemee returns a unique hash with always 0 collisions. For longer input lengths the number of collisions is always very low.  
If the input values are composed of bytes of a narrow range of values, for example only alphanumeric or ASCII printable characters,
you can further reduce collisions by choosing a suitable seed.  
If, on the other hand, the input values are absolutely random, the number of collisions is almost independent of the chosen seed.  

#### 32-bit hash functions: number of collisions for small data input [1-5] bytes   

Data input                                                                        |#Hashes   | Zedmee   | Murmur3 | XX  | Rabin  
----------------------------------------------------------------------------------|----------|----------|---------|-----|--------
4 byte length values 00000000-FFFFFFFF                                            |4294967296|   0      |      0  |  0  |  0    
1 byte 00-FF, 2 bytes 0100-FFFF, 3 bytes 010000-FFFFFF, 4 bytes 01000000-FFFFFFFF |4294967296|   0      |16711655 |16711713|16777216   
1 byte 00-FF, 2 bytes 0000-FFFF, 3 bytes 000000-FFFFFF, 4 bytes 00000000-FFFFFFFF |4311810304|  16843008|16843008 |16843008|16843008
5 byte length values 0000000000-FFFFFFFFFF                                        |?|        ? |      ?  |  ?  |   ?    

#### 32-bit hash functions: number of collisions for strings (ASCII 1 byte per char)

Data input                                                  |#Hashes   | Zedmee   | Murmur3|    XX    | Rabin
------------------------------------------------------------|----------|----------|--------|----------|-------
Numbers as strings from "0" to "999999999"                  |1000000000| 106488311|107822463|110287893|365950432
File Resource words_en.txt                                  | 65503    |    0     |   0     |    0    | 14
File Resource words_es.txt                                  | 74571    |    0     |        2|    0    | 38
File Resource words_it.txt                                  |117558    |    1     |    0    |     2   | 28
File Resource words_latin.txt                               | 80007    |    0     |        1|     1   | 34
File Resource words_en_es_it_latin.txt                      |315198    |    4     |        9|     9   | 271
File Resource words_and_numbers.txt                         |429187    |    4     |       20|    19   | 251
File Resource first_million_primes.txt                      |1000000   |     104  |      118|    85   |  0
File Resource random_64bit_signed_numbers.txt               |1000000   |     124  |      110|    143  | 122

#### 32-bit hash functions: number of collisions for data input from [15-40] bytes

Data input                                                                             | #Hashes  | Zedmee   | Murmur3|    XX   | Rabin
---------------------------------------------------------------------------------------|----------|----------|--------|---------|-------
Number as strings from "1234567890123456789" to "1234567890223456789"                  |100000000 | 940512   | 1155789|  808693 | 0      
Strings from "abcdefg1234567890123456789hijklmn" to "abcdefg1234567890223456789hijklmn"|100000000 | 1080583  | 1152600| 1037151 | 0  
Binary 24 bytes [b,b,b,b,b,b], b from 00000000 to 05F5E0FF                             |100000000 | 1155320  |1154653 | 1411483 | 0
Binary 24 bytes [b,b\*3,b\*5,b\*7,b\*11,b\*13], b from 00000000 to 05F5E0FF            |100000000 | 1155115  |1154542 | 1160003 | 1150862
Strings 48 length "XXXXXX" X from "00000000" to "05F5E0FF"                             |100000000 | 70454096 |


## Vulnerability
Zedmee, like most non-cryptographic functions, is not secure because it is not specifically designed to be difficult to reverse by an adversary, making it unsuitable for cryptographic purposes. Its use is instead recommended in all other contexts where hash functions are used.  
However, since it uses 6 seeds of 32-bit values (used once in the initialization or instantiation), its security is given by the secrecy of these 6 seeds, the combination of which is 192 bits which make zeemee certainly less vulnerable than all the other non-cryptographic functions.   

## Portability
It is simple, straightforward and can be easily written in virtually any programming language.  
Currently C and Java versions are for Big Endian architecture but mirror functions for Little Endian can be easily written.    
