import java.util.Random;
import java.util.Scanner;

public class UtilFn {
	public static double[] copyDoubleArray(double[] a){
		int length = 0;
		for(int i = 0; i < a.length; i++){
			if(a[i] == 0.0){
				break;
			}
			else{
				length++;
			}
		}
		double[] b = new double[length];
		for(int j = 0; j < length; j++){
			b[j] = a[j];
		}
		return b;
	}
	
	public static int[] copyIntArray(int[] a){
		int length = 0;
		for(int i = 0; i < a.length; i++){
			if(a[i] == 0){
				break;
			}
			else{
				length++;
			}
		}
		int[] b = new int[length];
		for(int j = 0; j < length; j++){
			b[j] = a[j];
		}
		return b;
	}
	
	public static boolean containsInt(int[] d, int x){
		for(int i = 0; i < d.length; i++){
			if(d[i] == x){
				return true;
			}
		}
		return false;
	}
	
	public static String input(){
		Scanner input = new Scanner(System.in);;
		String out = input.nextLine();
		return out;
	}
	
	//Generate a random number between 0 and 1 (uniform distribution)
	public static double randomNumberGenerator(){
		Random rand = new Random();
		double randomDouble = rand.nextDouble();
		return randomDouble;
	}
	
	public static double roundDecimalPoints(double x, int n){
		double trunc;
		trunc = Math.floor(x*Math.pow(10,n));
		trunc = trunc/Math.pow(10,n);
		return trunc;
	}
}
