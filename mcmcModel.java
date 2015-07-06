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
	double[] eCost;
	double[] eInventory;
	double[] eRevenue;
	double[] eProfit;
	double meanProfit;
	double variance;
	double stdDev;
	double[] ci95;
	
	/*																			*/
	/*								Main Method									*/
	/*																			*/

	public static void main(String[] args){
		mcmcModel test = new mcmcModel();
		double[] probTest = {0.15, 0.3, 0.25, 0.2, 0.1};
		int[] demTest = {2000, 2500, 3000, 3500, 4000};
		int[] prodAmount = {2500, 3000, 3500, 4000};
		test.setProbDistribution(test, probTest, demTest);
		test.setProfitModel(test, 5, 3, 1);
		test.setRandomNumbers(test, 500);
		test.setSupply(test, prodAmount);
		test.calculateExpectedValues(test, 2);
		test.getMeanProfit(test);
		test.getVarStdDev(test);
		test.get95percentCI(test);
		//System.out.println("Probability Test: " + Arrays.toString(probTest(test)));
		//System.out.println("Expected Demands: " + Arrays.toString(test.eDemand));
		//System.out.println("Revenue: " + Arrays.toString(test.eRevenue));
		//System.out.println("Costs: " + Arrays.toString(test.eCost));
		//System.out.println("Inventory Profit: " + Arrays.toString(test.eInventory));
		//System.out.println("Profits: " + Arrays.toString(test.eProfit));
		System.out.println("Mean Profit: " + test.meanProfit);
		System.out.println("Std Dev: " + test.stdDev);
		System.out.println("95% CI: (" + test.ci95[0] + ", " + test.ci95[1] + ")");
		
	}
	
	//array of model with given information including price, cost, and profit from inventory
	public void setProfitModel(mcmcModel a, double p, double c, double pI) {
		a.price = p;
		a.cost = c;
		a.profitInv = pI;
	}
	
	//array of model with given information 
	//including probability distribution with demand amount
	//and the cumulative probability distribution
	public void setProbDistribution(mcmcModel a, double[] probability, int[] demandAmount){
		/* Exception
		double sumProb = 0; 
		for(int i = 0; i < probability.length; i++){
			sumProb += probability[i];
		}
		*/
		
		a.prob = probability;
		a.demand = demandAmount;
		a.cumProb = new double[prob.length];
		a.cumProb[0] = 0;
		for(int i = 1; i < a.cumProb.length; i++){
			a.cumProb[i] = a.cumProb[i-1]+prob[i-1];
		}
	}
	
	//set produced amount to compare
	public void setSupply(mcmcModel a, int[] b){
		a.supplyAmount = b;
	}
	
	//generates an array of random numbers with size of n for n simulations
	public void setRandomNumbers(mcmcModel a, int n){
		a.randNumbers = new double[n];
		for(int i=0; i < n; i++){
			a.randNumbers[i] = randomNumberGenerator();
		}
	}

	//return estimated cumulative probability; test purpose
	public double[] estimateCumProb(mcmcModel a){
		double[] cp = a.cumProb;	
		double[] approx = new double[a.randNumbers.length];
		for(int k = 0; k < a.randNumbers.length; k++){
			approx[k] = cp[closestValueIndex(a.cumProb, a.randNumbers[k])];
		}
		return approx;
	}
	
	//calculate expected Profit by ((Price - Cost) * MIN{demand, supply}) - Profit from the inventory redundancy. 
	public void calculateExpectedValues(mcmcModel a, int supplyIndex){
		int length = a.randNumbers.length;
		a.eCost = new double[length];
		a.eInventory = new double[length];
		a.eDemand = new int[length];
		a.eProfit = new double[length];
		a.eRevenue = new double[length];
		int probIndex = -1;
		for(int i = 0; i < length; i++){
			probIndex = closestValueIndex(a.cumProb, a.randNumbers[i]);
			a.eDemand[i] = a.demand[probIndex];
			
			if(a.eDemand[i] < a.supplyAmount[supplyIndex]){
				a.eInventory[i] = (a.supplyAmount[supplyIndex] - a.eDemand[i]);
			}
			else{
				a.eInventory[i] = 0;
			}
			a.eRevenue[i] = a.price * Math.min(a.eDemand[i], a.supplyAmount[supplyIndex]);
		}
	
		for(int k = 0; k < length; k++){
			a.eCost[k] = a.cost * a.supplyAmount[supplyIndex];
			a.eProfit[k] = a.eRevenue[k] - a.eCost[k] + (a.eInventory[k] * a.profitInv);
		}
	}
	
	//get the mean profit of the sample obtained from n times simulations
	public void getMeanProfit(mcmcModel a){
		double sum = 0;
		for(int i = 0; i < a.eProfit.length; i++){
			sum += a.eProfit[i];
		}
		double average = sum/a.eProfit.length;
		a.meanProfit = average;
	}
	
	//get the variance and the standard deviation of the sample
	public void getVarStdDev(mcmcModel a){
		double avg = a.meanProfit;
		double temp = 0;
		for(int i = 0; i < a.eProfit.length; i++){
			temp += Math.pow(eProfit[i] - avg, 2);
		}
		a.variance = temp/(a.eProfit.length);
		a.stdDev = Math.sqrt(temp/(a.eProfit.length - 1));
	}
	
	//Assuming the sample is normally distributed, get 95% Confidence Interval
	public void get95percentCI(mcmcModel a){
		a.ci95 = new double[2];
		ci95[0] = a.meanProfit - 1.96 * (a.stdDev/a.randNumbers.length);
		ci95[1] = a.meanProfit + 1.96 * (a.stdDev/a.randNumbers.length);
	}
	
	/*																			*/
	/*								Helper Methods								*/
	/*																			*/
	
	//Generate a random number between 0 and 1
	public static double randomNumberGenerator(){
		Random rand = new Random();
		double randomDouble = rand.nextDouble();
		return randomDouble;
	}
	
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
