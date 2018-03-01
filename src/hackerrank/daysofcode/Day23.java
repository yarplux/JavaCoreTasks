import java.io.*;
import java.util.*;
class Node{
    Node left,right;
    int data;
    Node(int data){
        this.data=data;
        left=right=null;
    }
}
class Solution{
	static void levelOrder(Node root){
      //Write your code here
        if (root == null) {
            return;
        }
        Queue<Node> level = new LinkedList<>();
        level.add(root);
        recursiveLeftOrder(level);        
    }
    
    static Queue<Node> recursiveLeftOrder(Queue<Node> level) {
        if (level == null || level.size() == 0){
            return null;
        }
        Queue<Node> nextlevel = new LinkedList<>();
            for (Node temp : level) {
                System.out.print(temp.data+" ");                
                if (temp.left != null) {
                    nextlevel.add(temp.left);
                }
                if (temp.right != null) {
                    nextlevel.add(temp.right);
                }
            }
        return recursiveLeftOrder(nextlevel);
    }                        
    
    public static Node insert(Node root,int data){
        if(root==null){
            return new Node(data);
        }
        else{
            Node cur;
            if(data<=root.data){
                cur=insert(root.left,data);
                root.left=cur;
            }
            else{
                cur=insert(root.right,data);
                root.right=cur;
            }
            return root;
        }
    }
    public static void main(String args[]){
            Scanner sc=new Scanner(System.in);
            int T=sc.nextInt();
            Node root=null;
            while(T-->0){
                int data=sc.nextInt();
                root=insert(root,data);
            }
            levelOrder(root);
        }	
}
