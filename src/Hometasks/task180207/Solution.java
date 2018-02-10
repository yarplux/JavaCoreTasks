package Hometasks.task180207;

import java.util.*;

/* Solved with recursion and static methods
Main private method to pass elements and save them in order of pass to an array
with different input parameters
and additional methods to choose right order/parameters and print passed elements
Is it better to use in this case:
- static or usual methods of class
- recursion or loops or recursion with loops
*/

public class Solution {

    public static void main(String[] args) {

        int height = (int)(Math.round(Math.random()*5)+3);
        int width = (int)(Math.round(Math.random()*5)+3);

        System.out.println("Matrix of height:"+height+" and width:"+width);

        int count = 0;
        int[][] m = new int [height][];
        for (int i=0; i<height; i++){
            m[i] = new int[width];
            System.out.print("[");
            for (int j=0;j<width;j++) {
                m[i][j] = count++;
                System.out.printf("%2d", m[i][j]);
                if (j != width - 1) System.out.print(", ");
            }
            System.out.println("]");
        }

        printHelixInRight(m);
        printHelixInLeft(m);
        printHelixOutRight(m);
        printHelixOutLeft(m);
    }

    private static void printHelixInRight(int[][] m){
        if (m.length < 1) return;
        if (m[0].length < 1) return;

        System.out.println("\nClockwise to center:");
        int[] helix = new int[m.length*m[0].length];
        Helix(m, helix, 0, 0, true);
        System.out.println(Arrays.toString(helix));
    }

    private static void printHelixInLeft(int[][] m){
        if (m.length < 1) return;
        if (m[0].length < 1) return;

        System.out.println("\nCounterclockwise to the center:");
        int[] helix = new int[m.length*m[0].length];
        Helix(m, helix, 0, 0, false);
        System.out.println(Arrays.toString(helix));
    }

    private static void printHelixOutRight(int[][] m){
        if (m.length < 1) return;
        if (m[0].length < 1) return;

        System.out.println("\nFrom the center clockwise:");
        int[] helix = new int[m.length*m[0].length];
        Helix(m, helix, 0, 0, false);
        System.out.print("[");
        for (int i=helix.length-1; i>=0; i--) {
            System.out.print(helix[i]);
            if (i != 0) System.out.print(", ");
        }
        System.out.println("]");
    }

    private static void printHelixOutLeft(int[][] m){
        if (m.length < 1) return;
        if (m[0].length < 1) return;

        System.out.println("\nFrom the center counterclockwise:");
        int[] helix = new int[m.length*m[0].length];
        Helix(m, helix, 0, 0, true);
        System.out.print("[");
        for (int i=helix.length-1; i>=0; i--) {
            System.out.print(helix[i]);
            if (i != 0) System.out.print(", ");
        }
        System.out.println("]");
    }

    private static int[] Helix(int[][] m, int[] helix, int circle, int index, boolean rightOrder) {

        if (circle > (Math.min(m.length,m[0].length)-1)/2 || circle < 0) return helix;

        int i = index;
        int h = m.length;
        int w = m[0].length;

        for (int j=0; j<4; j++){
            boolean horizontal =    (j%2 == 0);
            boolean first =         (j<2);

            int start, end, delta, line;

            if (horizontal){
                if (rightOrder){
                    start =     (first)?circle:w-1-circle;
                    end =       (first)?w-1-circle:circle;
                } else {
                    start =     (first)?w-1-circle:circle;
                    end =       (first)?circle:w-1-circle;
                }
                line =      (first)?circle:h-1-circle;
            } else {
                start =     (first)?circle:h-1-circle;
                end =       (first)?h-1-circle:circle;
                if (rightOrder){
                    line =      (first)?w-1-circle:circle;
                } else {
                    line =      (first)?circle:w-1-circle;
                }
            }

            if (rightOrder) {
                delta = (first)?1:-1;
            } else {
                delta = (j == 0 || j==3)?-1:1;
            }

            int k=start;
            while (k != end || start == end){
                if (horizontal) {
                    helix[i++]=m[line][k];
                } else {
                    helix[i++]=m[k][line];
                }
                if (start == end) return helix;
                k = k+delta;
            }
        }
        return Helix(m, helix,circle+1, i, rightOrder);
    }
}
