import java.util.*;

public class mcmcModel {
	
	//need to be set
	double price;
	double cost;
	double profitInv;
	double[] prob;
	int[] demand;
	int[] supplyAmount;
	double[] cumProb;
	double[] randNumbers;
	//will be calculated
	int[] eDemand;
	double[][] eCost;
	double[][] eInventory;
	double[][] eRevenue;
	double[][] eProfit;
	double[] meanProfit;
	double[] variance;
	double[] stdDev;
	double[][] ci95;
	int maxProfitSI;
	
	/*																			*/
	/*								Main Method									*/
	/*																			*/

	public static void main(String[] args){

		mcmcModel a = new mcmcModel();
		Setter.setRevenueModel(a);
		Setter.setDistribution(a);
		Setter.setRandomNumbers(a);
		Setter.setSupplyAmount(a);
		calculateExpectedValues(a);
		getMeanProfit(a);
		getVarStdDev(a);
		get95percentCI(a);
		getMaxProfitSI(a);
		
		System.out.println("Supply level with maximum profit:" + a.supplyAmount[a.maxProfitSI]);
		System.out.println("Mean profit: $" + a.meanProfit[a.maxProfitSI]);
		System.out.println("Std dev:" + a.stdDev[a.maxProfitSI]);
		System.out.println("95% CI: ( " + a.ci95[a.maxProfitSI][0] + " , " + a.ci95[a.maxProfitSI][1] + ")");
		
	}
	
	
	//calculate expected Profit by ((Price - Cost) * MIN{demand, supply}) - Profit from the inventory redundancy. 
	public static void calculateExpectedValues(mcmcModel a){
		int length = a.randNumbers.length;
		int slevel = a.supplyAmount.length;
		a.eCost = new double[slevel][length];
		a.eInventory = new double[slevel][length];
		a.eDemand = new int[length];
		a.eProfit = new double[slevel][length];
		a.eRevenue = new double[slevel][length];
		int probIndex = -1;
		
		for(int i = 0; i < length; i++){
			probIndex = closestValueIndex(a.cumProb, a.randNumbers[i]);
			a.eDemand[i] = a.demand[probIndex];
			
			for(int si = 0; si < slevel; si++){
				if(a.eDemand[i] < a.supplyAmount[si]){
					a.eInventory[si][i] = (a.supplyAmount[si] - a.eDemand[i]);
				}
				else{
					a.eInventory[si][i] = 0;
				}
				a.eRevenue[si][i] = a.price * Math.min(a.eDemand[i], a.supplyAmount[si]);
			}
			
		}
	
		for(int k = 0; k < length; k++){
			for(int si = 0; si < slevel; si++){
				a.eCost[si][k] = (a.cost * a.supplyAmount[si]);
				a.eProfit[si][k] = a.eRevenue[si][k] - a.eCost[si][k] + (a.eInventory[si][k] * a.profitInv); 
			}
		}
		
	}
	
	//get the mean profit of the sample obtained from n times simulations
	public static void getMeanProfit(mcmcModel a){
		double sum = 0;
		a.meanProfit = new double[a.supplyAmount.length];
		
		for(int si = 0; si < a.supplyAmount.length; si++){
			for(int i = 0; i < a.randNumbers.length; i++){
				sum += a.eProfit[si][i];
			}
			double average = sum/a.randNumbers.length;
			a.meanProfit[si] = average;
			sum = 0;
		}
		
	}
	
	//get the variance and the standard deviation of the sample
	public static void getVarStdDev(mcmcModel a){
		a.variance = new double[a.supplyAmount.length];
		a.stdDev = new double[a.supplyAmount.length];
		double mean;
		double temp;
		for(int si = 0; si < a.supplyAmount.length; si++){
			mean = a.meanProfit[si];
			temp = 0;
			for(int i = 0; i < a.randNumbers.length; i++){
				temp += Math.pow(a.eProfit[si][i] - mean, 2);
			}
			a.variance[si] = (temp/(a.randNumbers.length));
			a.stdDev[si] = Math.sqrt(temp/(a.randNumbers.length - 1));
		}
	}
	
	//Assuming the sample is normally distributed, get 95% Confidence Interval
	public static void get95percentCI(mcmcModel a){
		a.ci95 = new double[a.supplyAmount.length][2];
		for(int si = 0; si < a.supplyAmount.length; si++){
			a.ci95[si][0] = a.meanProfit[si] - 1.96 * (a.stdDev[si]/a.randNumbers.length);
			a.ci95[si][1] = a.meanProfit[si] + 1.96 * (a.stdDev[si]/a.randNumbers.length);
		}
	}
	
	public static void getMaxProfitSI(mcmcModel a){
		int si = -1;
		double maxProfit = -Double.MIN_VALUE;
		for(int i = 0; i < a.meanProfit.length; i++){
			if(a.meanProfit[i] > maxProfit){
				maxProfit = a.meanProfit[i];
				si = i;
			}
		}
		if(si == -1){
			System.out.println("Non of the given supply level will make any profit!");
		}
		a.maxProfitSI = si;
	}
	
	/*																			*/
	/*								Helper Methods								*/
	/*																			*/
	
	
	
	//compare the random number and the cumulative probability and return the closest cumulative probability's index
	private static int closestValueIndex(double[] cumProbArray, double numberToCompare){
		int probIndex = 0;
		for(int i = 1; i < cumProbArray.length; i++){
			if(numberToCompare > cumProbArray[i]){
				probIndex = i;
			}
			else{
				return probIndex;
			}
		}
		return probIndex;
	}
	
}
