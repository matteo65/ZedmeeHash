# ZeemeeHash 32/64
Very strong, fast, non-cryptographic 32/64 hash function  

The algorithm is divided into two phases. In the first phase, the first 4 bytes (8 in the 64-bit version) are used to generate a unique random hash; for this purpose it uses a preloaded table containing a random permutation of all 256 bytes generated at inizialization or instantiation. In the second phase, instead, an algorithm is similar to a linear congruential generator.
The result is a very strong, fast and simple function with average performance superior to all existing hash functions.  

## Uniform and chaotic distribution of hash values
Zeemee has an absolutely uniform, chaotic distribution of hash values independent of the number, length and type of input values.
Even minimal differences (1 bit) in the input values produce very different hash values.  

## Minimum number of collisions for a set of reasonably large input values
For all possible input values not exceeding 4 bytes for the 32-bit version (8 bytes for the 64-bit version)  
zeemee returns a unique hash with always 0 collisions. For longer input lengths the number of collisions is always very low.  
If the input values are composed of bytes of a narrow range of values, for example only alphanumeric or ASCII printable characters,  
you can further reduce collisions by choosing a suitable seed.  
If, on the other hand, the input values are absolutely random, the number of collisions is almost independent of the chosen seed.  

#### 32-bit hash functions: number of collisions for small data input [1-5] bytes   

Data input                                                                        |#Hashes   | Zeemee   | Murmur3 | XX  | Rabin  
----------------------------------------------------------------------------------|----------|----------|---------|-----|--------
4 byte length values 00000000-FFFFFFFF                                            |4294967296|        0 |      0  |  0  |   0    
1 byte 00-FF, 2 bytes 0100-FFFF, 3 bytes 010000-FFFFFF, 4 bytes 01000000-FFFFFFFF |4294967296|        0 |16711655 |16711713|16777216   
1 byte 00-FF, 2 bytes 0000-FFFF, 3 bytes 000000-FFFFFF, 4 bytes 00000000-FFFFFFFF |4311810304|  16843008|16843008 |16843008|16843008
5 byte length values 0000000000-FFFFFFFFFF                                        |?|        ? |      ?  |  ?  |   ?    

#### 32-bit hash functions: number of collision for strings

Data input                                                                  |#Hashes   | Zeemee   | Murmur | XX | Rabin
----------------------------------------------------------------------------|----------|----------|--------|----|-------
Numbers as strings from "0" to "999999999"                                  |1000000000| 106488311|        |    |365950432

## Vulnerability
Zeemee, like most non-cryptographic functions, is not secure because it is not specifically designed to be difficult to reverse by an adversary, making it unsuitable for cryptographic purposes. Its use is instead recommended in all other contexts where hash functions are used.  
However, since it uses 6 seeds of 32-bit values (used once in the initialization or instantiation), its security is given by the secrecy of these 6 seeds, the combination of which is 192 bits which make zeemee certainly less vulnerable than all the other non-cryptographic functions.   

## Portability
It is simple, straightforward and can be easily written in virtually any programming language; returns different hash values
on Little Endian or Big Endian architectures, but the quality is identical.  
