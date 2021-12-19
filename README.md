# CS 1501 – Algorithm Implementation – Assignment #3

## OVERVIEW
 
**Purpose:** The purpose of this assignment is to fully understand the LZW compression algorithm, its performance and its implementation. We will improve the performance of the textbook's LZW implementation when the input files are large.

_Goal 1_: Read the input file as a stream of bytes (i.e., byte by byte) instead of all at once (feel free to use your Lab8 code for this task). 

_Goal 2_: Avoid the Theta(n) overhead of using `String.substring()` (feel free to use your Lab8 code for this task as well). 

_Goal 3_: Allow the codebook size to increase beyond the `4096` entries in the textbook's implementation using adaptive codeword width.

_Goal 4_: Allow LZW to learn new patterns after the codebook size is reached by giving the user the option to reset the codebook. 

## PROCEDURE

1.	Thoroughly read the description/explanation of the LZW compression algorithm as discussed in lecture and in the Sedgewick text.

2.	Read over and make sure you understand the code provided by Sedgewick in file `LZW.java` in this repository.

3.	There are three fundamental problems with this code as given: 

4. In this assignment you will modify the author's code so as to correct (somewhat) these three problems.  Proceed in the following way:

    - Download the implementation of the algorithm provided and get it to work. Follow the instructions in the comments and run the program on a few test files to get familiar with using it.  Try running the program with a large input file to see the behavior discussed above.
    - Examine the code very carefully, convincing yourself exactly what is accomplished by each function and by each statement within each function.
    - Copy the code to a new file called `LZWmod.java` and modify the code so that during `compress()` the input file is read as a stream of characters (bytes) rather than as a single string.  There are many ways to do this and most of the details are up to you.  However, here are a few requirements:
      - The input file must be read in a single character (byte) at a time.  Look over some classes in `java.io` that might be appropriate to use in this case.  You may also utilize any of the author's IO classes if you wish.  The idea is that rather than using the `longestPrefixOf()` method on a single String, you will find the longest prefix yourself by repeatedly reading and appending characters and looking the prefix up in the symbol table.
      - The "strings" that are looked up in the dictionary must actually be `StringBuilder` objects.  Using `StringBuilder` rather than String will allow the values to be updated more efficiently (ex: appending a character onto the end of the `StringBuilder` will not require a new `StringBuilder` object to be created, as it would with a String).
      - The dictionary in which the `StringBuilder`s are looked up must be some type of symbol table where the keys are `StringBuilder` objects and the values are arbitrary Java types.  The dictionary must have (average) constant lookup time.  Note that the predefined Java `Hashtable` and `HashMap` classes are not appropriate in this case because `StringBuilder` does not override the `hashCode()` method to actually hash the string value.  Some options that could work include modifying one of the author's hash classes, the `TST` class, or the author's `TrieST` class so that they work for `StringBuilder`.  Another option is to modify your DLB code from Lab 5 or Assignment 2 to allow it to store the codewords.
      - Note that you should not have to modify the `expand()` code for this feature at all.  Also note that this modification will take just a few lines of code but it may take a lot of trial and error before you get it working properly.  I strongly recommend getting this to work before moving on to the next two parts below.  However, if you cannot get this to work, you can still get credit for the next two parts by performing it on the original `LZW.java` file.

    - Also modify the code so that the LZW algorithm has a varying number of bits for the codewords, as discussed in lecture.  Your codeword size should vary from 9 bits to 16 bits and should increment the bitcount when all codes for the previous size have been used.  This also does not require a lot of modification to the program, but you must REALLY understand exactly what the program is doing at each step in order to do this successfully (so you can keep the compress and decompress processes **in sync**).  Once you get the program to work, thoroughly test it to make sure it is correct.  If the algorithm is correct, the byte count of the original file and the uncompressed copy should be identical.  Some hints about the variable-length code word implementation are given later on in this assignment.
    - As a partial solution to the issue of the dictionary filling, you will give the user the option to reset the dictionary or not via a command line argument.  See more details on the command line arguments below, but the argument `r` will cause the dictionary to reset once all (16-bit) code words have been used.
      - You will have to add code to reset the dictionary to the `LZWmod.java` file.  As discussed in lecture this option will erase and reset the entire dictionary and start rebuilding it from scratch.  As with the variable bits, be careful to sync both the compress and decompress to reset the dictionary at the same point.
      - Since now a file may be compressed with or without resetting the dictionary, in order to decompress correctly your program must be able to discern this fact.  This can be done quite simply by writing a flag or sentinel at the beginning of the output file (actually only 1 bit is needed for this). Then, before decompression, your program will read this flag and determine whether or not to reset the dictionary when running out of codewords.
5. The author's interface already has a command-line argument to choose compression or decompression.  File input and output can be supplied using the standard redirect operators for standard I/O: Use "<" to redirect the input to be a file and use ">" to redirect the output to be a file.  Modify the interface so that for compression a command-line argument also allows the user to choose how to act when all codewords have been used. This extra argument should be an `n` for "do nothing", or `r` for "reset".  Note that these arguments are only used during compression – for decompression the algorithm should be able to automatically detect which technique was used and decompress accordingly.

For example, assuming your program is called `LZWmod.java`, if you wish to compress the file `bogus.txt` into the file `bogus.lzw`, resetting the dictionary when you run out of codewords, you would enter at the prompt:

```shell
$ java LZWmod - r < bogus.txt > bogus.lzw
```

To prevent headaches (especially during debugging), you should not replace the original file with the new one (i.e., leave the original file unchanged). Thus, make sure you use a name for the output file that is different from the input file.  If you then want to decompress the `bogus.lzw` file, you might enter at the prompt

```shell
$ java LZWmod + < bogus.lzw > bogus2.txt
```

The file `bogus2.txt` should now be identical to the file `bogus.txt` (You can confirm that using `diff` in Linux/MacOS and `fc` in Windows).  Note that there is no flag for what to do when the dictionary fills – this should be obtained from the front of the compressed file itself (which, again, requires only a single bit).

7.	 `a3.md` discusses each of the following:
  - How all four of the lzw variation programs compared to each other (via their compression ratios) for each of the different files.  Where there was a difference between them, be sure to explain (or speculate) why.  To support your assertions, include a table showing all of the results of your tests (original sizes, compressed sizes and compression ratios for each algorithm).
  - For all algorithms, indicate which of the test files gave the best and worst compression ratios, and speculate as to why this was the case.  If any files did not compress at all or compressed very poorly (or even expanded), speculate as to why.
  
