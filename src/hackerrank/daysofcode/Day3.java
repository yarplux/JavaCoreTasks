import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Day3 {
   
   public static void main(String[] args) {
      Scanner scan = new Scanner(System.in);
      int n = scan.nextInt(); 
      scan.close();
      String ans="";
          
      // if 'n' is NOT evenly divisible by 2 (i.e.: n is odd)
      if(n%2==1 || (n>=6 && n<=20)){
         ans = "Weird";
      }
      else{
         // Complete the code
         ans = "Not Weird";
      }
      System.out.println(ans);
   }
}
