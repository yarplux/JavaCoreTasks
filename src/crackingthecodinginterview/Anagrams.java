import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
public class Anagrams {
    public static int numberNeeded(String first, String second) {

    Map<Character, Integer> chars = new HashMap<Character, Integer>();
                
    for (int i=0; i<first.length(); i++){
        Integer val = chars.get(first.charAt(i));
        if (val == null) {
          chars.put(first.charAt(i), 1);
        } else {
          chars.put(first.charAt(i), val+1);      
        }
    }

    for (int i=0; i<second.length(); i++){
        Integer val = chars.get(second.charAt(i));
        if (val == null) {
          chars.put(second.charAt(i), -1);
        } else {
          chars.put(second.charAt(i), val-1);      
        }
    }
        
    int out = 0;
    for (Integer i : chars.values()) {
        out += Math.abs(i);
    }
        
    return out;
}
  
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String a = in.next();
        String b = in.next();
        System.out.println(numberNeeded(a, b));
    }
}
