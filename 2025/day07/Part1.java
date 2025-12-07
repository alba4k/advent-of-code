import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Part1 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(new File("input"));

        ArrayList<String> lines = new ArrayList<String>();

        lines.addLast(input.nextLine().replace("S", "|"));

        int y = 1;
        int splits = 0;
        // assume "^^" doesn't happen
        while(input.hasNextLine()) {
            lines.addLast(input.nextLine());
            for(int x = 0; x < lines.get(0).length(); x++) {
                if(!(lines.get(y-1).charAt(x) == '|'))
                    continue;

                if(lines.get(y).charAt(x) == '^') {
                    lines.set(y, lines.get(y).substring(0, x-1) + "|^|" + lines.get(y).substring(x+2)); // not checking i for my input
                    splits++;
                }
                else
                    lines.set(y, lines.get(y).substring(0, x) + "|" + lines.get(y).substring(x+1));
            }
            y++;
        }

        System.out.println(splits);

        input.close();
    }
}
