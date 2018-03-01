import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. */
        Scanner sc=new Scanner(System.in);
        int T=sc.nextInt();
        for (int i=0; i<T;i++){
            int n=sc.nextInt();
            if (prime(n)){
                System.out.println("Prime");
            } else {
                System.out.println("Not prime");                
            }
        }
    }
    
    static boolean prime (int n){
        if (n < 2 || (n != 2 && n%2 == 0)) return false;
        for (int i=3; i<=Math.sqrt(n); i+=2){
            if (n%i == 0) {
                return false;         
            }
        }
        return true;
    }
}
