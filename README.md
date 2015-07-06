# MCMC
This project is a java program executing Markov Chain Monte Carlo Simulation, also know as MCMC simulation.
MCMC is a very well-known powerful analyzing method using random numbers, and a statistical sample with probability.

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

Sales Price/unit: $5
Cost to produce/unit: $3
Profit from the inventory: $1

Probability Distribution:
Demand  Prob
2000    0.15
2500    0.30
3000    0.25
3500    0.20
4000    0.10

By testing several supply level, it gives you the answer that approximate 3000 units of supply gives you the maximum profit.

COMMENT: 
GUI will made soon using Swing library of Java. 
For now, The input has to be manually typed within the main method. 
I am planning to add graphical analysis application using java. 
Also, I will add a function that exports the result data set to excel file using APACHE POI API.
Thanks!
