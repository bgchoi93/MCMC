import java.util.Scanner;
import java.util.Arrays;

public class test{
	public static void main(String args[]){
		System.out.println(Arrays.toString(arrayIn()));
	}
	public static String input(){
		Scanner input = new Scanner(System.in);
		String s = input.nextLine();
		return s;
	}
	public static int[] arrayIn(){
		System.out.print("Enter the array size: ");
		int size = Integer.parseInt(input());
		int[] a = new int[size];
		for(int i = 0; i < size; i++){
			System.out.print("Enter the element: ");
			a[i] = Integer.parseInt(input());
		}
		System.out.println("Complete.");
		return a;
	}
}