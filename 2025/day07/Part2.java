import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Part2 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(new File("input"));

        String line = input.nextLine();

        ArrayList<long[]> possibilities = new ArrayList<>();
        possibilities.addLast(new long[line.length()]);
        possibilities.get(0)[line.indexOf("S")] = 1;

        int y = 1;
        // assume "^^" doesn't happen
        while(input.hasNextLine()) {
            line = input.nextLine();
            possibilities.addLast(new long[line.length()]);
            for(int x = 0; x < line.length(); x++) {
                if(line.charAt(x) == '^') { // x doesn't need to be checked for AOC inputs
                    possibilities.get(y)[x-1] += possibilities.get(y-1)[x];
                    possibilities.get(y)[x+1] += possibilities.get(y-1)[x];
                }
                else
                    possibilities.get(y)[x] += possibilities.get(y-1)[x];
            }
            y++;
        }

        long paths = 0;

        for(int j = 0; j < possibilities.get(0).length; j++)
            paths += possibilities.get(possibilities.size()-1)[j];

        System.out.println(paths);

        input.close();
    }
}
