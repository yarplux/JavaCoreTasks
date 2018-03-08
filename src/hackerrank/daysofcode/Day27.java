import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        
        
        Random rd = new Random();
        int numTests = 5;
        String[] answers = {}
        System.out.println(numTests);
        ArrayList<Integer> N = new ArrayList<>();
        for (int i=0; i<numTests; i++) {
            int t = 3+rd.nextInt(197);
            // Чтобы N было гарантированно разным и от 3
            while (N.contains(t)) {
                t = 3+rd.nextInt(197);
            }
            N.add(t);
            int K = 1+rd.nextInt(t-1);
            System.out.println(t+" "+K);
            for (int j = 0; j<t; j++){ 
                // Чтобы t было = 0, < 0 , > 0 гарантированно
                if (j == 0) {
                    System.out.print(0 + " ");
                } else if (j == 1) {
                    System.out.print(-1000+rd.nextInt(1000)+ " ");
                } else if (j == 2) {
                    System.out.print(rd.nextInt(1001)+ " ");
                } else {
                    System.out.print(-1000+rd.nextInt(1999)+ " ");
                }                
            }
            System.out.println("");
        }
    }
}
