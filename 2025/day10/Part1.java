import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Machine {
    final boolean[] lights;
    final boolean[] wanted_lights;
    final ArrayList<Integer>[] buttons;

    @SuppressWarnings("unchecked")
    public Machine(ArrayList<Boolean> lights, ArrayList<ArrayList<Integer>> buttons) {
        this.lights = new boolean[lights.size()];
        this.wanted_lights = new boolean[lights.size()];
        for(int i = 0; i < lights.size(); i++)
            this.wanted_lights[i] = lights.get(i);
        
        this.buttons = buttons.toArray((ArrayList<Integer>[]) new ArrayList[0]);
    }

    private void push(int button) {
        if(button < 0 || button >= buttons.length) throw new IllegalArgumentException();

        for(int i : buttons[button])
            lights[i] = !lights[i];
    }

    private boolean canActivate() {
        for(int i = 0; i < lights.length; i++)
            if(lights[i] != wanted_lights[i]) return false;
        return true;
    }
    public int min_presses(int current_presses, int pressing) {
        // each button is either pressed once or not at all, we check the minimum in both cases for all buttons
        if(canActivate()) return current_presses;
        // can't activate from these states
        if(current_presses >= buttons.length) return buttons.length + 1;
        if(pressing >= buttons.length) return buttons.length + 1;

        // if we DON'T press
        int presses = min_presses(current_presses, pressing+1);

        // if we DO press
        push(pressing);
        presses = Math.min(presses, min_presses(current_presses+1, pressing+1));

        push(pressing); // restore initial light state for next calls

        return presses;
    }
}

public class Part1 {
    static ArrayList<Machine> parseInput(String filename)
    throws FileNotFoundException {
        ArrayList<Machine> machines = new ArrayList<>();
        Scanner input = new Scanner(new File(filename));

        // patterns: [lights] (button1) (button2) ... {requirements}
        // requirements are ignored for part 1
        Pattern pattern = Pattern.compile("\\[([.#]+)\\]\\s*(.*?)\\s*\\{([0-9,\\s]+)\\}");
        Pattern int_pattern = Pattern.compile("\\d+");
        Pattern buttons_pattern = Pattern.compile("\\(([^)]+)\\)");

        while(input.hasNextLine()) {
            String line = input.nextLine().trim();
            if (line.isEmpty()) continue;

            Matcher m = pattern.matcher(line);
            if (!m.find()) continue;

            // parse lights: [.##..]
            ArrayList<Boolean> lights = new ArrayList<>();
            for (char c : m.group(1).toCharArray()) {
                lights.add(c == '#');
            }

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
            machines.add(new Machine(lights, buttons));
        }

        input.close();
        return machines;
    }

    public static void main(String[] args) 
    throws FileNotFoundException {
        ArrayList<Machine> machines = parseInput("input");

        int total_presses = 0;
        for(Machine m : machines) {
            total_presses += m.min_presses(0, 0);
        }
        System.out.println(total_presses);
    }
}
