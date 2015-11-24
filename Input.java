import java.util.Scanner;
public class Input {
	public static String input(){
		Scanner input = new Scanner(System.in);;
		String out = input.nextLine();
		input.close();
		return out;
	}
	public static double inputDouble(){
		Scanner input = new Scanner(System.in);;
		double out = input.nextInt();
		input.close();
		return out;
	}
}
