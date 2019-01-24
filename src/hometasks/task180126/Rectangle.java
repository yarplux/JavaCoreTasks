package hometasks.task180126;

//import java.math.*;

public class Rectangle {

    private static String check(double[][] points){
        if (points.length != 2) {
            return "Array of points is incorrect! It must contain 2 points only!";
        }
        for (int i=0; i<points.length; i++){
            if (points[i].length != 2) {
                return "Point "+i+" is incorrect! It must contain 2 coordinate values only only!";
            }
        }
        return "";
    }

    private static void print(double[][] points){
        String checkOut = check(points);
        if (checkOut.length() == 0) {
            for (int i = 0; i < points.length; i++) {
                System.out.print("Point " + i + "  x:" + points[i][0] + "  y:" + points[i][1]);
                System.out.println("");
            }
        } else {
            System.out.println(checkOut);
        }
    }

    // Рассчёт площади данного по 2 точкам прямоугольника
    // TO DO: можно добавить c задание с углом поворота относительно горизонтали
    public static double area (double[][] points ) {
        String checkOut = check(points);
        if (checkOut.length() == 0) {
            return Math.abs(points[0][0] - points[1][0]) * Math.abs(points[0][1] - points[1][1]);
        } else {
            System.out.println(checkOut);
            return 0;
        }
    }
    // Рассчёт площади пересечения
    public static double intersectArea(double[][] rect1, double[][] rect2){

        double[][][] rects = {rect1, rect2};

        // Проверка входных параметров
        for (int i=0; i<2; i++) {
            String checkOut = check(rects[i]);
            if (checkOut.length() > 0){
                System.out.println("Error in the "+(i+1)+" argument:");
                System.out.println(checkOut);
                return 0;
            }

        }

        // ширина / высота
        double[] size = {0,0};

        // последний индекс 0 ось X, индекс 1 ось Y
        // вначале поиск ширины, потом высоты
        for (int i = 0; i<2; i++) {

            //  относительное положение многоугольников  0 - left/down, 1 - right/up
            int[] side = {0,0};
            side[0] = (Math.min(rects[0][0][i], rects[0][1][i]) < Math.min(rects[1][0][i], rects[1][1][i]))?0:1;
            side[1] = (Math.max(rects[0][0][i], rects[0][1][i]) > Math.max(rects[1][0][i], rects[1][1][i]))?0:1;

            // если один полностью внутри другого, то его ширина(высота) и есть ширина(высота) пересечения
            if (side[0] == side[1]) {
                int other = (side[0] == 0)?1:0;
                size[i] = Math.abs(rects[other][0][i] - rects[other][1][i]);
            } else {
                size[i] = Math.max(rects[side[0]][0][i], rects[side[0]][1][i]) - Math.min(rects[side[1]][0][i], rects[side[1]][1][i]);
            }

            // если не пересекаются - пересечение 0
            if (side[i] < 0) {
                side[i] = 0;
            }
        }
        return size[0]*size[1];
    }

    public static void main (String[] args){

        // TO DO: input different rectangles from Console / from another class

        double[][] rect1 = {{-8.1,97.3},{15.4,37.2}};
        print(rect1);
        System.out.printf("\nArea of this rectangle: %.3f\n\n",area(rect1));

        double[][] rect2 = {{-22.01,103.68},{20, 20}};
        print(rect2);
        System.out.printf("\nArea of this rectangle: %.3f\n\n",area(rect2));

        System.out.printf("\nArea of intersection: %.3f\n\n",intersectArea(rect2, rect1));
    }
}
