package hackerrank.daysofcode;

import java.util.*;

public class Day1 {

    public static void main(String[] args) {
        int i = 4;
        double d = 4.0;
        String s = "HackerRank ";

        Scanner scan = new Scanner(System.in);
        /* Declare second integer, double, and String variables. */
        /* Read and save an integer, double, and String to your variables.*/
        int iI = scan.nextInt();
        double iD = scan.nextDouble();
        scan.nextLine();
        String iS = scan.nextLine();

        // Note: If you have trouble reading the entire String, please go back and review the Tutorial closely.

        /* Print the sum of both integer variables on a new line. */
        System.out.println(i+iI);
        /* Print the sum of the double variables on a new line. */
        System.out.println(d+iD);
        /* Concatenate and print the String variables on a new line;
        	the 's' variable above should be printed first. */
        System.out.println(s+iS);
        scan.close();
    }
}
