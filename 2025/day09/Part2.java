import java.util.ArrayList;
import java.util.Arrays;
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

class Rectangle implements java.lang.Comparable<Rectangle> {
    // a will always be either to the left or on top of b (if they're horizontal/vertical)
    public final Point a;
    public final Point b;
    final long area;

    public Rectangle(Point a, Point b) {
        if(a.x < b.x) {
            this.a = a;
            this.b = b;
        }
        else if(b.x < a.x) {
            this.a = b;
            this.b = a;
        }
        else { // a.x == b.x
            if(a.y < b.y) {
                this.a = a;
                this.b = b;
            }
            else {
                this.a = b;
                this.b = a;
            }
        }
        area = (Math.abs(a.x - b.x) + 1L) * (Math.abs(a.y - b.y) + 1L);
    }

    @Override
    // REVERSE of the expected order, bigger rectangles come first
    public int compareTo(Rectangle other) {
        if(this.area < other.area) return +1;
        if(this.area > other.area) return -1;
        return 0;
    }

    public boolean equals(Rectangle other) {
        return (this.a == other.a) && (this.b == other.b); // requires same reference
    }
}

public class Part2 {
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
        ArrayList<Rectangle> rectangles = new ArrayList<>();
        ArrayList<Rectangle> v_bounds = new ArrayList<>();
        ArrayList<Rectangle> h_bounds = new ArrayList<>();

        for(int i = 0; i < points.size(); i++) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % points.size());

            if(p1.x == p2.x)
                v_bounds.add(new Rectangle(p1, p2));
            else // p1.y == p2.y
                h_bounds.add(new Rectangle(p1, p2));
        }

        for(Point p1 : points) {
            for(Point p2 : points) {
                if(p1 != p2)
                    rectangles.add(new Rectangle(p1, p2));
            }
        }
        rectangles.sort(null);

        Rectangle bestRectangle = null;

        rectangleLoop:
        for (Rectangle r : rectangles) {
            int minX = Math.min(r.a.x, r.b.x);
            int maxX = Math.max(r.a.x, r.b.x);
            int minY = Math.min(r.a.y, r.b.y);
            int maxY = Math.max(r.a.y, r.b.y);

            // check if any vertical boundary slices through the interior of r
            for (Rectangle b : v_bounds) {
                if(b.a.x > minX && b.a.x < maxX)
                    if(Math.max(minY, b.a.y) < Math.min(maxY, b.b.y))
                        continue rectangleLoop;
            }

            // check if any horizontal boundary slices through the interior of r
            for (Rectangle b : h_bounds) {
                if(b.a.y > minY && b.a.y < maxY)
                    if (Math.max(minX, b.a.x) < Math.min(maxX, b.b.x))
                        continue rectangleLoop;
            }

            // cast horizontal ray from the center to make sure it's in our polygon
            double midX = (minX + maxX) / 2;
            double midY = (minY + maxY) / 2;
            int crossings = 0;

            for(Rectangle b : v_bounds)
                if(b.a.y <= midY && midY <= b.b.y && b.a.x < midX)
                    crossings++;

            if(crossings % 2 == 1) { // we're inside
                bestRectangle = r;
                break; // descending area => first valid is the answer
            }
        }

        if(bestRectangle != null)
            System.out.println(bestRectangle.area);
    }
}
