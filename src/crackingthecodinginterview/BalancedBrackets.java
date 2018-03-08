import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class BalancedBrackets {
    
    public static boolean isBalanced(String expression) {       
        Stack<Character> brackets = new Stack<>();
        for (int i=0; i<expression.length();i++){
            char c = expression.charAt(i);
            if (c == '{' || c == '(' || c == '[') brackets.push(c);            
            if (c == '}' && (brackets.empty() || brackets.pop() != '{')) return false;
            if (c == ']' && (brackets.empty() || brackets.pop() != '[')) return false;
            if (c == ')' && (brackets.empty() || brackets.pop() != '(')) return false;            
        }
        return brackets.empty();
    }
  
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for (int a0 = 0; a0 < t; a0++) {
            String expression = in.next();
            System.out.println( (isBalanced(expression)) ? "YES" : "NO" );
        }
    }
}
