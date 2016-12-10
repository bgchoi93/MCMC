# Monte Carlo Simulation
This project is a java program executing Monte Carlo Simulation.
Monte Carlo simulation is a very well-known powerful analyzing method using random numbers, and a statistical sample with probability to deal with uncertainty in the future.

In this program, I made a number of assumptions: 

1. We are given statistic of demand with probability distribution from the past years
2. Random numbers are uniformly distributed.
3. To get 95% Confidence Interval, I assumed that the sample is normally distributed.
4. I made the calculation simple: 
   Profit = Revenue - Cost + Profit made from redundant inventory(e.g. by selling it at discounted price,recycle, etc).
   The Revenue is calculated by multiplying min{supply, demand} * price/unit.
   The cost is calculated by multiplying cost/unit * number of unit produced(supply).
   The profit from inventory is # of inventory(supply - demand if supply > demand) * profit from inventory/unit.

Considering the given assumption, this program calculates mean profit, variance, standard deviation, 95% confidence interval for a given number of supply. 
By testing various supply level, the optimized result(supply level with maximum profit) can be derived. 
Make sure that the number of simulation is big enough. 

Example: 

* Sales Price/unit: $5
* Cost to produce/unit: $3
* Profit from the inventory: $1

Probability Distribution: place test.txt file in the same folder with the java files and type in test.txt when asked for a probability distribution or you can manually type in the demand and probabilities.

|Demand|Prob|
|:-----|:---|
|2000  |0.15|
|2500  |0.30|
|3000  |0.25|
|3500  |0.20|
|4000  |0.10|

Supply levels to be tested and the revenue model(price, cost, profit from redundant inventory handling) needs to be manually typed in through stdin. 
I did not implement a text file input or any error handling for these since it is trivial. 

By testing several supply level; I tested 0, 500, 1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000, 5500, 6000
, it gives you the answer that approximate 3000 units of supply gives you the maximum profit.
