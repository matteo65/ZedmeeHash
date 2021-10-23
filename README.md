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

## Portability
It is simple, straightforward and can be easily written in virtually any programming language; returns different hash values
on Little Endian or Big Endian architectures, but the quality is identical.  
