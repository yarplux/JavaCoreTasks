import java.util.*;

public class RansomNote {
    Map<String, Integer> magazineMap;
    Map<String, Integer> noteMap;
    
    public RansomNote(String magazine, String note) {
        this.magazineMap = new HashMap<>();
        this.noteMap = new HashMap<>();
        
        String[] magazineArr = magazine.split(" ");
        for (int i=0; i<magazineArr.length; i++) {
            Integer val = magazineMap.get(magazineArr[i]);
            if (val == null) {
                magazineMap.put(magazineArr[i], 1);
            } else {
                magazineMap.put(magazineArr[i], val+1);
            }
        }
        String[] noteArr = note.split(" ");
        for (int i=0; i<noteArr.length; i++) {
            Integer val = noteMap.get(noteArr[i]);
            if (val == null) {
                noteMap.put(noteArr[i], 1);
            } else {
                noteMap.put(noteArr[i], val+1);
            }
        }
    }
    
    public boolean solve() {
        for (String key : noteMap.keySet()){
            if (magazineMap.get(key) == null || noteMap.get(key) > magazineMap.get(key)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        int n = scanner.nextInt();
        
        // Eat whitespace to beginning of next line
        scanner.nextLine();
        
        RansomNote s = new RansomNote(scanner.nextLine(), scanner.nextLine());
        scanner.close();
        
        boolean answer = s.solve();
        if(answer)
            System.out.println("Yes");
        else System.out.println("No");
      
    }
}
