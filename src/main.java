import java.util.Scanner;

/**
 * This is the entry of the program
 *
 * @author Jingning Qian
 */
public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of ticks to be performed: ");
        String n = scanner.nextLine();
        int num = Integer.parseInt(n);

        Rebellion rebellion = new Rebellion();
        rebellion.runRebellion(num);
    }
}
