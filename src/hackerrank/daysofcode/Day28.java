import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        Pattern p = Pattern.compile("^*[@]{1}gmail{1}[.]{1}com{1}");
        ArrayList<String> users = new ArrayList<String>();
        for(int a0 = 0; a0 < N; a0++){
            String firstName = in.next();
            String emailID = in.next();
            Matcher m = p.matcher(emailID);
            if (m.find()) users.add(firstName);
        }
        Collections.sort(users);
        for (String s: users){
            System.out.println(s);
        }
    }
}
