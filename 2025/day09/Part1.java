import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class Part1 {
    static ArrayList<Point> parseInput(String filename) 
    throws FileNotFoundException {
        ArrayList<Point> points = new ArrayList<>();
        Scanner input = new Scanner(new File(filename));

        while(input.hasNextLine()) {
            Scanner line = new Scanner(input.nextLine());
            line.useDelimiter("\\s*,\\s*");
            int x = line.nextInt();
            int y = line.nextInt();

            points.add(new Point(x, y));

            line.close();
        }
        
        input.close();

        return points;
    }

    public static void main(String[] args)
    throws FileNotFoundException {
        ArrayList<Point> points = parseInput("input");

        long max_area = Long.MIN_VALUE;

        for(Point point1 : points) {
            for(Point point2 : points) {
                if(point1 == point2) continue; // unnecessary as area is 0 anyway

                long area = Math.abs((long)(point1.x - point2.x + 1) * (point1.y - point2.y + 1));
                if(area > max_area)
                    max_area = area;
            }
        }

        System.out.println(max_area);
    }
}
