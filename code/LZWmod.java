
import java.security.CodeSigner;

/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *
 *************************************************************************/

public class LZWmod {
    private static final int R = 256;        // number of input chars
    private static int W;         // codeword width
    private static final int maxCodwordLength=16;
    private static int L; // number of codewords = 2^W
            // codeword width

    public static void compress(String args) {    
   
      if(args.equals("n"))
      {
        //indicator for the expand method
        BinaryStdOut.write("n");
        W=9;
        L=(int) Math.pow(2, W); 
       TSTmod<Integer> st = new TSTmod<Integer>();
       for (int i = 0; i < R; i++)
           st.put(new StringBuilder("" + (char) i), i);
       int code = R+1;  // R is codeword for EOF
       //initialize the current string
        StringBuilder current = new StringBuilder();
        //read and append the first char
        char c = BinaryStdIn.readChar();
        current.append(c);
        Integer codeword = st.get(current);
        while (!BinaryStdIn.isEmpty()) {
          codeword = st.get(current);
          //increase the space in the st when full
          if( code==L && W<maxCodwordLength)
              {
                ++W;
                L=(int) Math.pow(2, W);  
              } 
            c = BinaryStdIn.readChar();
            current.append(c);
            if(!st.contains(current)){
              BinaryStdOut.write(codeword, W);
            
              if(code < L)    // Add to symbol table if not full
              {  
                st.put(current, code++);
              }
              current=new StringBuilder();
              current.append(c);
            }
          }
        //TODO: Write the codeword of whatever remains
        //in current
        codeword = st.get(current);
        BinaryStdOut.write(codeword, W);
        BinaryStdOut.write(R, W); //Write EOF
        BinaryStdOut.close();
    } 
    else if(args.equals("r"))
    {
      //indicator for expand method
      BinaryStdOut.write("r");
      W=9;
      L=(int) Math.pow(2, W); 
     TSTmod<Integer> st = new TSTmod<Integer>();
     for (int i = 0; i < R; i++)
         st.put(new StringBuilder("" + (char) i), i);
     int code = R+1;  // R is codeword for EOF
     //initialize the current string
      StringBuilder current = new StringBuilder();
        //read and append the first char
        char c = BinaryStdIn.readChar();
        current.append(c);
        Integer codeword = st.get(current);
        while (!BinaryStdIn.isEmpty()) {
          //When full max space bigger
          if( code==L && W<maxCodwordLength)
          {
            ++W;
            L=(int) Math.pow(2, W);  
          }
          //when at max capacity reset the enitre codebook and intialize ascii
          if(code==(int) Math.pow(2,16))
              {
                st = new TSTmod<Integer>();
               //apply ascii back in tst
                for (int i = 0; i < R; i++)
                    st.put(new StringBuilder("" + (char) i), i);
                code = R+1;
                W=9;
                L=(int) Math.pow(2, W);
                
              } 
          
          codeword = st.get(current);
            c = BinaryStdIn.readChar();
            current.append(c);
            if(!st.contains(current)){
              BinaryStdOut.write(codeword, W);
              //System.err.println(codeword);
              if (code < L)    // Add to symbol table if not full
              {  
                st.put(current, code++);
              }
              
              current=new StringBuilder();
              current.append(c);
            }
        }
    codeword = st.get(current);
    BinaryStdOut.write(codeword, W);
    BinaryStdOut.write(R, W); //Write EOF
    BinaryStdOut.close();
  }
  }
   
  public static void expand() {
        W=9;
        L=(int) Math.pow(2, W);
        String[] st = new String[L];
        int i; // next availible codeword value
        // initialize symbol table with all 1-character strings
        for(i = 0;i < R;i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF
        //checks to see if reset is needed
        char checker = BinaryStdIn.readChar();
        if(checker=='r')
        {
          int codeword = BinaryStdIn.readInt(W);
          
          String val = st[codeword];
          while(true) {
            //used to see if full and reset... use -1 because it is an array  
            if(i==(int) Math.pow(2, 16)-1)
            {
              
              W=9;
              L=(int) Math.pow(2, W);
              st=new String[L];
              for (i = 0; i < R; i++)
                st[i] = "" + (char) i;
              st[i++] = "";
              //BinaryStdOut.write(val);
              //codeword = BinaryStdIn.readInt(W);
              //val = st[codeword];
            }    
           //make array bigger and hard copy values back into st
            if(i==L-1 && W!=maxCodwordLength)
            {
              W++;
              String[] newst = new String[L];
              L = (int) Math.pow(2, W);
              for (int x=0; x<newst.length ; x++)
              {
                newst[x] = st[x];
              }
              st = new String[L];
              for (int x = 0; x<newst.length; x++)
              {
                st[x] = newst[x];
              } 
            } 
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
              if (codeword == R) break;
              String s = st[codeword];
              if (i == codeword)
                s = val + val.charAt(0);   // special case hack
              if (i < L)
              {
                st[i++] = val +s.charAt(0);
              }
              val = s; 
             
               
           
        } 
        BinaryStdOut.close();
      }
        else if(checker=='n')
        {
       
          int codeword = BinaryStdIn.readInt(W);
          String val = st[codeword];
          while(true) {
            //when array is full hard copy values into bigger array  
            if(i==L-1 && W!=maxCodwordLength)
              {
                W++;
                String[] newst = new String[L];
                
                L = (int) Math.pow(2, W);
                for (int x=0; x<newst.length; x++)
                {
                  newst[x] = st[x];
                }
                st = new String[L];
                for (int x = 0; x<newst.length; x++)
                {
                  st[x] = newst[x];
                } 
              }
              BinaryStdOut.write(val);
              codeword = BinaryStdIn.readInt(W);
              if (codeword == R) break;
              String s = st[codeword];
              if (i == codeword)
                s = val + val.charAt(0);   // special case hack
              if (i < L)
              {
                st[i++] = val +s.charAt(0);
              }
              val = s; 
            }  
        BinaryStdOut.close();
        }
    }
    public static void main(String[] args) {
        if      (args[0].equals("-")) compress(args[1]);
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");
    }
}