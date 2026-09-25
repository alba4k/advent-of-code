import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Machine {
    final ArrayList<Integer>[] buttons;
    final int[] requirements;
    final int[] joltages;

    @SuppressWarnings("unchecked")
    public Machine(ArrayList<ArrayList<Integer>> buttons, ArrayList<Integer> requirements) {
        this.buttons = buttons.toArray((ArrayList<Integer>[]) new ArrayList[0]);
        
        this.requirements = new int[requirements.size()];
        for(int i = 0; i < requirements.size(); i++)
            this.requirements[i] = requirements.get(i);

        this.joltages = new int[requirements.size()];
    }

    public int min_presses() {
        // rows: joltages to set
        // columns: buttons that set it
        int[][] A = new int[joltages.length][buttons.length];

        for(int i = 0; i < buttons.length; i++)
            for(int val : buttons[i])
                A[val][i] = 1;

        // delegate to the Solver class, solves Ax = b
        // x should have positive integer components and lowest L1 norm
        Solver solver = new Solver();
        return solver.solve(A, requirements);
    }
}

public class Part2 {
    static ArrayList<Machine> parseInput(String filename)
    throws FileNotFoundException {
        ArrayList<Machine> machines = new ArrayList<>();
        Scanner input = new Scanner(new File(filename));

        // patterns: [lights] (button1) (button2) ... {requirements}
        // lights are ignored for part 2
        Pattern pattern = Pattern.compile("\\[([.#]+)\\]\\s*(.*?)\\s*\\{([0-9,\\s]+)\\}");
        Pattern int_pattern = Pattern.compile("\\d+");
        Pattern buttons_pattern = Pattern.compile("\\(([^)]+)\\)");

        while(input.hasNextLine()) {
            String line = input.nextLine().trim();
            if(line.isEmpty()) continue;

            Matcher m = pattern.matcher(line);
            if(!m.find()) continue;

            // parse buttons: (1) (2,3)
            ArrayList<ArrayList<Integer>> buttons = new ArrayList<>();
            Matcher buttonMatcher = buttons_pattern.matcher(m.group(2));
            while(buttonMatcher.find()) {
                ArrayList<Integer> button = new ArrayList<>();
                Matcher digit_m = int_pattern.matcher(buttonMatcher.group(1));

                while(digit_m.find())
                    button.add(Integer.parseInt(digit_m.group()));
    
                buttons.add(button);
            }

            // parse requirements: {1,2,3}
            ArrayList<Integer> requirements = new ArrayList<>();
            Matcher reqMatcher = int_pattern.matcher(m.group(3));
            while(reqMatcher.find())
                requirements.add(Integer.parseInt(reqMatcher.group()));

            machines.add(new Machine(buttons, requirements));
        }

        input.close();
        return machines;
    }

    public static void main(String[] args) 
    throws FileNotFoundException {
        ArrayList<Machine> machines = parseInput("input");

        int total_presses = 0;
        for(Machine m : machines)
            total_presses += m.min_presses();
        
        System.out.println(total_presses);
    }
}

// this part is mainly vibe-coded:
class Solver {
    public int solve(int[][] A, int[] b) {
        int m = A.length;
        int n = A[0].length;

        // build augmented matrix
        int[][] mat = new int[m][n+1];
        for(int i = 0; i < m; i++) {
            for(int j = 0; j < n; j++)
                mat[i][j] = A[i][j];
            mat[i][n] = b[i];
        }

        // RREF using exact rational arithmetic: row[c] / den[r]
        // To avoid fraction classes, work with integer Gaussian elimination
        int rank = 0;
        int[] pivotCol = new int[m];
        Arrays.fill(pivotCol, -1);

        for(int c = 0; c < n && rank < m; c++) {
            int pivotRow = -1;
            for(int r = rank; r < m; r++) {
                if(mat[r][c] != 0) {
                    pivotRow = r;
                    break;
                }
            }
            if(pivotRow == -1) continue;

            // swap to current rank row
            int[] tmp = mat[rank];
            mat[rank] = mat[pivotRow];
            mat[pivotRow] = tmp;

            pivotCol[rank] = c;

            // eliminate column c in other rows
            for(int r = 0; r < m; r++) {
                if(r != rank && mat[r][c] != 0) {
                    int factor1 = mat[r][c];
                    int factor2 = mat[rank][c];
                    int g = gcd(Math.abs(factor1), Math.abs(factor2));
                    factor1 /= g;
                    factor2 /= g;

                    for(int k = 0; k <= n; k++)
                        mat[r][k] = mat[r][k] * factor2 - mat[rank][k] * factor1;

                    // Normalize row to keep numbers small
                    int rowGcd = 0;
                    for(int k = 0; k <= n; k++) rowGcd = gcd(rowGcd, Math.abs(mat[r][k]));
                    if(rowGcd > 1)
                        for(int k = 0; k <= n; k++)
                            mat[r][k] /= rowGcd;
                }
            }
            rank++;
        }

        // Check for contradiction: 0 == non-zero
        for(int r = rank; r < m; r++)
            if(mat[r][n] != 0) return -1;

        // Identify free variable columns
        boolean[] isPivot = new boolean[n];
        for(int r = 0; r < rank; r++)
            isPivot[pivotCol[r]] = true;

        List<Integer> freeCols = new ArrayList<>();
        for(int j = 0; j < n; j++)
            if(!isPivot[j]) freeCols.add(j);

        // Case 1: Unique solution (no free variables)
        if(freeCols.isEmpty()) {
            int total = 0;
            for(int r = 0; r < rank; r++) {
                int num = mat[r][n];
                int den = mat[r][pivotCol[r]];
                if(num % den != 0) return -1;
                int val = num / den;
                // Use val <= 0 if strictly positive (val >= 1) is required
                if(val < 0) return -1; 
                total += (int) val;
            }
            return total;
        }

        // Case 2: Underdetermined (search over free variables)
        int[] best = new int[]{Integer.MAX_VALUE};
        int[] freeVals = new int[freeCols.size()];
        int maxBound = 0;
        for(int i = 0; i < b.length; i++)
            maxBound = Math.max(maxBound, b[i]);

        searchFree(0, freeCols, freeVals, mat, pivotCol, rank, n, maxBound, best);

        return best[0] == Integer.MAX_VALUE ? -1 : best[0];
    }

    private void searchFree(int idx, List<Integer> freeCols, int[] freeVals,
                            int[][] mat, int[] pivotCol, int rank, int n,
                            int maxBound, int[] best) {
        if(idx == freeCols.size()) {
            // Evaluate pivot variables
            int currentSum = 0;
            for(int fv : freeVals) currentSum += fv;
            if(currentSum >= best[0]) return;

            for(int r = 0; r < rank; r++) {
                int rhs = mat[r][n];
                for(int f = 0; f < freeCols.size(); f++)
                    rhs -= mat[r][freeCols.get(f)] * freeVals[f];

                int den = mat[r][pivotCol[r]];
                if(rhs % den != 0) return;
                int x = rhs / den;
                // Change to x < 1 if variables must be strictly positive
                if(x < 0) return; 
                currentSum += (int) x;
            }

            best[0] = Math.min(best[0], currentSum);
            return;
        }

        // Bound can be adjusted according to puzzle constraints
        for(int v = 0; v <= maxBound; v++) {
            freeVals[idx] = v;
            searchFree(idx + 1, freeCols, freeVals, mat, pivotCol, rank, n, maxBound, best);
        }
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }
}
