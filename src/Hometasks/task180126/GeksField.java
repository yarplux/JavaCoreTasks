package Hometasks.task180126;

/*
Схема порядка ссылок в рамках гексагонального поля links[i]:

   1     2
    \   /
0 <-  x  -> 3
    /   \
   5     4


 Порядок заполнения поля:

   ...
     2,0 2,1 2,2 2,3 ...
 ^ 1,0 1,1 1,2, 1,3 ...
 |   0,0 0,1 0,2 0,3 ...
 y x ->

 */

import java.util.*;

public class GeksField {

    private Geks[][] field;

    GeksField(int height, int width) {
        this.field = new Geks[height][];
        for (int i = 0; i < height; i++) {
            this.field[i] = new Geks[width];
            for (int j = 0; j < width; j++) {
                this.field[i][j] = new Geks(i, j);
            }
        }
        for (int i = 0; i< height; i++) {
            for (int j = 0; j < width; j++) {
                this.setLinks(this.field[i][j]);
            }
        }
    }

    private Geks getGeks(int y, int x) {
        if (field.length < 2 || field[0].length < 2) return new Geks(0,0);
        if (y >= field.length || x >= field[0].length) return new Geks(0,0);
        //Geks copy = new Geks(y,x);
        //System.arraycopy(field[y][x].getLinks(), 0, copy.getLinks(), 0, field[y][x].getLinks().length);
        //return copy;
        return field[y][x];
    }

    private ArrayList<Geks> minPath(Geks start, Geks end) {
        ArrayList<Geks> path = new ArrayList<>();
        if (field.length < 1 || field[0].length < 1) return path;
        if (start == null || start.getLinks() == null) return path;
        if (end == null || end.getLinks() == null) return path;

        if (start == end) {
            path.add(start);
            return path;
        }

        for (Geks[] str :field) {
            for (Geks v : str) {
                v.setMark(field.length*field[0].length);
            }
        }

        // Алгоритм Дейкстры
        start.setMark(0);
        Geks min = start;
        while(min != null) {
            min.setWatch(false);
            for (Geks v : min.getLinks()) {
                if (v != null && v.getWatch()) {
                    // все рёбра с весом 1
                    if (v.getMark() > min.getMark()+1) {
                        v.setMark(min.getMark()+1);
                    }
                }
            }
            min = minMark();
        }
        // Ищем минимальный из путей
        min = end;
        path.add(end);
        while (min != start) {
            int minMark = min.getMark();
            Geks t = min;
            for (Geks v : min.getLinks()) {
                if (v!= null && v.getMark()<minMark) t = v;
            }
            path.add(0, t);
            min = t;
        }
        return path;
    }

    private Geks minMark(){
        int min=field.length*field[0].length;
        Geks out = null;
        for (Geks[] str : field){
            for (Geks v : str){
                if (v.getWatch() && v.getMark() < min) {
                    min =v.getMark();
                    out = v;
                }
            }
        }
        return out;
    }

    void printField() {
        if (field.length < 1 || field[0].length < 1) return;
        for (int i=field.length-1; i>=0; i--){
            if (i < field.length-1) {
                if (i % 2 == 1) {
                    for (int j = 0; j < field[0].length; j++) {
                        if (j < field[0].length - 1) {
                            System.out.print("   \\  / ");
                        } else {
                            System.out.print("   \\");
                        }
                    }
                    System.out.print("\n  ");
                } else {
                    for (int j = 0; j < field[0].length; j++) {
                        if (j > 0) {
                            System.out.print(" \\    / ");
                        } else {
                            System.out.print("  /   ");
                        }
                    }
                    System.out.println("");
                }
            }
            for (int j = 0; j < field[0].length; j++) {
                System.out.printf("%1$2s,%2$-2s",field[i][j].getY(),field[i][j].getX());
                if (j < field[0].length-1) System.out.print(" - ");
            }
            System.out.println("");
        }
    }

    private void setLinks(Geks node) {
        if (field.length < 2 || field[0].length < 2) return;
        int y = node.getY();
        int x = node.getX();
        int height = field.length;
        int width = field[0].length;

        if (y >= height || x >= width) return;

        Geks[] links = new Geks[6];

        if (x > 0) {
            links[0] = field[y][x - 1];
        }
        if (x < width - 1) {
            links[3] = field[y][x + 1];
        }
        if (y > 0) {
            if (y%2 == 0) {
                if (x != 0) links[5] = field[y-1][x-1];
                links[4] = field[y-1][x];
            }
            if (y%2 != 0) {
                if (x != width-1) links[4] = field[y-1][x+1];
                links[5] = field[y-1][x];
            }
        }
        if (y < height - 1) {
            if (y%2 == 0) {
                if (x != 0) links[1] = field[y+1][x-1];
                links[2] = field[y+1][x];
            }
            if (y%2 != 0) {
                if (x != width-1) links[2] = field[y+1][x+1];
                links[1] = field[y+1][x];
            }
        }
        node.setLinks(links);
    }

    public void minPath(int firstX, int firstY, int secondX, int secondY) {
        Geks start = this.getGeks(firstY,firstX);
        Geks end = this.getGeks(secondY,secondX);
        ArrayList<Geks> path = this.minPath(start, end);
        System.out.print("\nPath from: ["+start.getY()+","+start.getX()+"]");
        System.out.print(" to: ["+end.getY()+","+end.getX()+"]\n");
        if (path.size() == 0) {
            System.out.println("\nThere isn't such path!");
        } else {
            System.out.println("Path.length: " + (path.size()-1));
            for (Geks v : path) {
                System.out.print("[" + v.getY() + "," + v.getX() + "] ");
            }
        }
    }
    public static void main(String[] args){
        //GeksField myField = new GeksField(1000,1000);
        //myField.printField();
        //myField.minPath(0,0,10,10);
    }
}
