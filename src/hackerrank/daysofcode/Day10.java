import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Day10 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int count = 0;
        int maxcount = 0;
        int temp = n;
        while (temp>0){
            if (temp%2 == 0){
                maxcount = (count>maxcount)?count:maxcount;
                count = 0;
            } else {
                count++;
            }
            temp=temp/2;            
        }
        maxcount = (count>maxcount)?count:maxcount;
        System.out.println(maxcount);
    }
}
