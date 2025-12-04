import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Part1 {
    public static int[] findMax(int[] array, int start, int end) {
        int[] result = new int[2]; // result[0] is the max, result[1] is the index
        result[0] = Integer.MIN_VALUE;
        result[1] = -1;
        for(int i = start; i < end; i++)
            if(result[0] < array[i]) {
                result[0] = array[i];
                result[1] = i;
            }

        return result;
    }

    public static void main(String[] args) {
        File f;
        Scanner file;
        try {
            f = new File("input");
            file = new Scanner(f);
        } catch (FileNotFoundException e) {
            return;
        }

        int sum = 0;

        while(file.hasNextLine()) {
            String line = file.nextLine();
            int[] numbers = new int[line.length()];

            for(int i = 0; i < line.length(); i++)
                numbers[i] = line.charAt(i) - 48;

            int[][] digits = new int[2][];
            digits[0] = findMax(numbers, 0, numbers.length-(digits.length-1));
            for(int i = 1; i < digits.length; i++)
                digits[i] = findMax(numbers, digits[i-1][1]+1, numbers.length-(digits.length-1-i));

            int result = 0;
            for(int i = 0; i < digits.length; i++)
                result += Math.pow(10, digits.length-1-i) * digits[i][0];
            sum += result;
        }

        file.close();
        System.out.println(sum);
    }
}