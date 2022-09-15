import java.util.Scanner;

public class simulator {

    public static void main(String[] args) {
        room.initialize();
        room.printRoom();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of iterations: ");
        int iter = scanner.nextInt();

        boolean loop = true;
        while (loop) {
            for (int cur = 0; cur < iter; cur++) {
                room.charSort();
                room.moveChar();
            }
            System.out.println("Completed: " + iter + " iterations");
            room.printRoom();
            System.out.println("Enter number of iterations (to END: enter 0): ");
            iter = scanner.nextInt();
            if (iter == 0) {
                loop = false;
            }
        }
        System.out.println("Simulation ended");
    }
}


