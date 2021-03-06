import java.util.*;
import java.io.*;

public class Setter {
	public static void setDistribution(mcmcModel a){
		System.out.println("Setting probability distribution of demand - ");
		boolean repeat = true;
		while(repeat == true){
			System.out.print("Please type a file name : ");
			String inFile = UtilFn.input();
			try{
				File f = new File(inFile);
				FileInputStream file = new FileInputStream(f);
				try{
					BufferedReader reader = new BufferedReader(new InputStreamReader(file));
					String line = null;
					int linecount = 0;
					while((line = reader.readLine())!= null){
						if(linecount == 0){
							String[] d = line.split("\t");
							a.demand = new int[d.length];
							for(int i = 0; i < d.length; i++){
								a.demand[i] = Integer.parseInt(d[i]);
							}
							linecount += 1;
						}
						else if(linecount == 1){
							String[] p = line.split("\t");
							a.prob = new double[p.length];
							for(int j = 0; j < p.length; j++){
								a.prob[j] = Double.parseDouble(p[j]);
							}
							linecount += 1;
							if(checkProb(a.prob) > 1.0){
								System.out.println("The file contains invalid probability: the probabilities should add up to 1.0");
								System.out.println("Found probabilities add up to " + checkProb(a.prob));
								System.out.println("Please try with another file");
								a.demand = null;
								a.prob = null;
								break;
							}
							else{
								repeat = false;
							}
						}
						else{
							reader.close();
							repeat = false;
							break;
						}	
					}
					setCumProb(a);
				} 
				catch(IOException x){
					System.out.println("Invalid file format: " + x);
				}
				catch(NumberFormatException n){
					System.out.println("The file does not have a valid format. Refer to the README file for a valid input.");
				}
				
			}
			catch(FileNotFoundException e){
				System.out.println("File is not found in the current working directory.");
				boolean repeatYN = true;
				while(repeatYN){
					System.out.print("Do you want to manually type in (y/n)? ");
					String yn = UtilFn.input();
					if(yn.equals("y")){
						repeat = false;
						repeatYN = false;
						manuallySet(a);
						return;
					}
					else if(yn.equals("n")){
						System.out.println("Please try again with the correct file path/name");
						repeatYN = false;
					}
					else{
						System.out.println("Invalid. Please choose y or n");
					}
				}
			}		
		}	
	}
	public static void manuallySet(mcmcModel a){
		boolean repeat = true;
		int[] tmp1 = new int[1000];
		double[] tmp2 = new double[1000];
		int index = 0;
		while(repeat){
			System.out.print("Enter the demanded amount: ");
			try{
				int d = Integer.parseInt(UtilFn.input());
				if(UtilFn.containsInt(tmp1, d) == false){
					System.out.print("Enter the probability of the demanded amount: ");
					double p = Double.parseDouble(UtilFn.input());
					if(p <= 1.0){
						if(checkProb(tmp2) + p < 1.0){
							tmp1[index] = d;
							tmp2[index] = p;
							index += 1;
						}
						else if(checkProb(tmp2) + p > 1.0){
							System.out.println("Sum of the probabilities exceeds 1.0. Please try again.");
							tmp1[index] = 0;
							tmp2[index] = 0.0;
						}
						else{
							tmp1[index] = d;
							tmp2[index] = p;
							repeat = false;
						}
					}
					else{
						System.out.println("Invalid Probability. Please Enter the demanded amount and probability again.");
					}
				}
				else{
					System.out.println("The input you entered already exists. Please try again.");
				}
			}
			catch(NumberFormatException nfe){
				System.out.println("Invalid number entered.");
			}
		}
		a.demand = UtilFn.copyIntArray(tmp1);
		a.prob = UtilFn.copyDoubleArray(tmp2);
		setCumProb(a);
		a.supplyAmount = UtilFn.copyIntArray(a.demand);
	}
	
	public static double checkProb(double[] d){
		double sum = 0;
		for(int i = 0; i < d.length; i++){
			sum += d[i]*100;
		}
		sum /= 100;
		return sum;
	}
	
	public static void setCumProb(mcmcModel a){
		a.cumProb = new double[a.prob.length];
		a.cumProb[0] = 0.0;
		for(int i = 1; i < a.prob.length; i++){
			a.cumProb[i] = ((a.prob[i-1]*100)+(a.cumProb[i-1]*100))/100;
		}
	}
	
	public static void setRevenueModel(mcmcModel a) {
		Scanner input = new Scanner(System.in);
		System.out.println("What is the price of your product? (per unit)");
		a.price = input.nextDouble();
		System.out.println("What is the cost of your product to produce? (per unit)");
		a.cost = input.nextDouble();
		System.out.println("How much is the revenue from the redundant inventory handling? (per unit)");
		a.profitInv = input.nextDouble();
	}
	

	//generates an array of random numbers with size of n for n simulations
	public static void setRandomNumbers(mcmcModel a){
		Scanner input = new Scanner(System.in);
		System.out.println("How many simulations?");
		int n = input.nextInt();
		a.randNumbers = new double[n];
		for(int i=0; i < n; i++){
			a.randNumbers[i] = UtilFn.randomNumberGenerator();
		}
	}
	
	public static void setSupplyAmount(mcmcModel a){
		Scanner input = new Scanner(System.in);
		System.out.println("Please specify how many different levels of supply amount you want to test.( 0 < levels)");
		int n = input.nextInt();
		a.supplyAmount = new int[n];
		System.out.println("Enter supply amounts you want to test.");
		for(int i = 0; i < n; i++){
			a.supplyAmount[i] = input.nextInt();
		}
	}
}
