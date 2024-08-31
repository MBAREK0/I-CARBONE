package services.UiElements;

public class Loader {
    public static void loading() {
        // Spinner characters
        char[] spinner = { '/', '-', '\\'};
        int spinnerIndex = 0;

        // Number of iterations for the spinner (adjust for desired duration)
        int iterations = 10;

        try {
            System.out.println("\n\n\n\n");
            for (int i = 0; i < iterations; i++) {
                // Print spinner character and use \r to overwrite the line
                System.out.print("\r\t\t\t\t\t\t\t\t   " + spinner[spinnerIndex]);

                // Update spinner index
                spinnerIndex = (spinnerIndex + 1) % spinner.length;
                if( i == iterations - 2) {
                    spinner[spinnerIndex] = ' ';
                }
                // Pause to create animation effect
                Thread.sleep(100); // Adjust the delay as needed
            }
            System.out.println("\n");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
