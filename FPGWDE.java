/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainproject;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;

/**
 *
 * @author farzaneh
 */
public class FPGWDE {
    private double p = 0.8;  //switch probitlity
    private double lb=0, ub;
    private int pop;
    private int dim, iter;
    private double[][] s;
    private double[] cost;
    private double[] Fglobal;
    private double fitglobal= Double.MAX_VALUE;
    double min_cost= Double.MAX_VALUE;
    private List<Cloudlet> cloudletList;  
    private List<Vm> vmList;
    Random rand = new Random();
    private double[] best_fit_iter;
    
    public  FPGWDE(int populationSize, int maxIterations, List<Cloudlet> cloudletList, List<Vm> vmList){
        this.vmList = vmList;
        this.cloudletList = cloudletList;
        this.dim = cloudletList.size();  
        this.ub = vmList.size() - 1;  
        this.pop = populationSize;
        this.iter = maxIterations;
        this.s = new double[pop][dim];
        this.cost = new double[pop];
        this.Fglobal= new double[dim];
        this.best_fit_iter= new double[iter];
    }
    public  double[][] initialPosition(int flowers, double minValues, double maxValues) {  
        double[][] pos = new double[flowers][dim];  
        for (int i = 0; i < flowers; i++) {  
            for (int j = 0; j < dim; j++) {  
                pos[i][j] = minValues + (maxValues - minValues) * rand.nextDouble();  
            }  
        }  
        return pos;  
    }  
    private double[] XBest(double[][] xx, double[] fit){
        double minfit= Double.MAX_VALUE;
        double[] minx = new double[dim];
        for(int i=0; i<pop; i++){
            if(fit[i] < minfit ){
                minfit = fit[i];
                minx = xx[i].clone();
            }
        }
        return minx;
    }
    private double[] check_bound(double[] s){
        for(int j=0; j< dim ; j++){
            if( s[j]< lb )        s[j]= lb;
            if(s[j]> ub)          s[j]= ub; 
        }
        return s;
    }

    
    private double [] crossover(double[] m){
        double cr = 0.7;
        double f = 0.5;
        int sn = rand.nextInt(dim);
        for(int j=0; j<dim ; j++){
            double r = rand.nextDouble();
            if (r <= cr || sn== j){                
                 int r1= rand.nextInt(pop);
                 int r2= rand.nextInt(pop);
                 int r3= rand.nextInt(pop);
                m[j] = s[r1][j] + (f * (s[r2][j]- s[r3][j]));
            }           
        }
        return m;
    }
    private void sortpopulation(double[][] s){        
        double[] tem;
        double temp;
        for(int i=0; i<pop; i++){
            for(int j=0; j<pop-i-1;j++){
                if(cost[j] > cost[j+1]){
                      tem = s[j].clone();
                      s[j] = s[j+1].clone();
                      s[j+1]= tem.clone();
                      temp = cost[j];
                      cost[j]=cost[j+1];
                      cost[j+1]= temp; 
                }
            }
        }
        Fglobal= s[0];
        fitglobal= cost[0];
    } 
   
    public int[] ececute_FPA(){
        double[] candidate = new double[dim];
        s = initialPosition(pop, lb, ub);
        for(int i=0 ; i< pop ; i++){
            cost[i]= Calculator.calculateFitness(s[i], cloudletList, vmList);
            if(cost[i] < min_cost){
                fitglobal = cost[i];
                Fglobal = s[i].clone();
            }
        }
        for(int t=0 ; t< iter ; t++){
             
            LevyFlight levyFlight = new LevyFlight(1.5);  
            for(int i=0 ; i< pop ; i++){
                if(Math.random() < p){                   
                    for(int j=0 ; j< dim ; j++){
                       candidate[j] = s[i][j] + levyFlight.levyFlight() * (s[i][j] - Fglobal[j]);
                    }                    
                }
                else{
                    int r1 = rand.nextInt(pop);
                    int r2 ;
                    do {  
                        r2 = rand.nextInt(pop);  
                    } while (r1 == r2); 
                    double epsilon = rand.nextDouble();
                    for (int j = 0; j < dim; j++){
                        candidate[j] = s[i][j] + (epsilon * (s[r1][j] - s[r2][j]));           
                        }
                } // else end
               candidate = check_bound(candidate); 
               double fit_condidate = Calculator.calculateFitness(candidate, cloudletList, vmList);
               if( fit_condidate < cost[i] ){
                   s[i]= candidate.clone();
                   cost[i]= fit_condidate;
               }
               else{
                   candidate = crossover(s[i]);
                   candidate = check_bound(candidate);
                   fit_condidate =Calculator.calculateFitness(candidate, cloudletList, vmList);
                    if( fit_condidate < cost[i]){
                             s[i]= candidate.clone();
                             cost[i]= fit_condidate;
                    }
               }
            } // End For
            
            for(int i=0 ; i< pop ; i++){
                  cost[i]= Calculator.calculateFitness(s[i], cloudletList, vmList);
            }
            sortpopulation(s);
            double[] salpha= s[0];
            double[] xbeta= s[1];
            double[] xgama=s[2];
            double[] xworst= new double[dim];
            for(int j=0 ; j< dim ; j++){
                xworst[j] = (salpha[j] + xbeta[j] + xgama[j])/3;
            }
            if(Calculator.calculateFitness(xworst, cloudletList, vmList)<Calculator.calculateFitness(s[pop-1], cloudletList, vmList)){
                s[pop-1] = xworst.clone();
            }
              best_fit_iter[t]= fitglobal;       
        }  //End iteration
        return Arrays.stream(Fglobal).map(Math::round).mapToInt((xx) -> (int) xx).toArray();
    } // execute Fpa
    
    public double[] getBestFitness() {  
        return best_fit_iter;  
    }  
}
