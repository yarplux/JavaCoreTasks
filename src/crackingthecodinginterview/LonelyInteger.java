import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class LonelyInteger {
    
    public static int lonelyInteger(int[] a) {
        if (a.length < 2) return a[0];
        int[] nums = new int[101];
        for (int i=0; i<a.length; i++){
            nums[a[i]]++;
        }
        for (int i=0; i<nums.length;i++){
            if (nums[i] == 1) return i;
        }
        return 0;
    }
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int a[] = new int[n];
        for(int a_i=0; a_i < n; a_i++){
            a[a_i] = in.nextInt();
        }
        System.out.println(lonelyInteger(a));
    }
}
