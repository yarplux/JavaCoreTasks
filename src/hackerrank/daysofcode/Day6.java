import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Day6 {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        int size = (T>0)?T:0;
        String[] words = new String[size];
        sc.nextLine();
        
        for (int i=0;i<words.length;i++) words[i] = sc.nextLine();
        for (int i=0; i<words.length;i++){
            for (int j=0;j<words[i].length();j++) if (j%2 == 0) System.out.print(words[i].charAt(j));
            System.out.print(" ");
            for (int j=0;j<words[i].length();j++) if (j%2 == 1) System.out.print(words[i].charAt(j));
            System.out.print("\n");
        }
    }
}
