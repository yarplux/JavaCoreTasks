import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner in = new Scanner (System.in);
        int[][] dates = new int[3][];
        for (int j=0; j<2; j++) {
            dates[j] = new int[3];
            for (int i=0; i<3; i++) {
                dates[j][i] = in.nextInt();
            }
        }
        int lateYear = dates [0][2] - dates[1][2];
        int lateMonth = dates [0][1] - dates[1][1];
        int lateDay = dates [0][0] - dates[1][0];
        
        int fine = 0;
        if (lateYear > 0){
            fine = 10000;
        }
        else if (lateYear == 0) {
            if (lateMonth > 0) {
                fine = lateMonth*500;
            } 
            else if (lateMonth == 0) {
                fine = (lateDay > 0)?lateDay*15:0;
            }
            // if lateMonth < 0
            else {
                fine = 0;
            }
        }
        // if lateYear < 0
        else {
            fine = 0;
        }
        System.out.println(fine);        
    }
}
