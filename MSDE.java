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
public class MSDE {
    private double lb=0, ub;
    private int pop;
    private int dim, iter;
    private double[][] x;
    private double[] cost;
    private double[] xbest;
    private double best_fit= Double.MAX_VALUE;
    private List<Cloudlet> cloudletList;  
    private List<Vm> vmList; 
    Random rand = new Random();
    private double[] best_fit_iter;
    
   public  MSDE(int populationSize, int maxIterations, List<Cloudlet> cloudletList, List<Vm> vmList){
        this.cloudletList = cloudletList;  
        this.vmList = vmList;  
        this.dim = cloudletList.size();  
        this.ub = vmList.size() - 1; 
        this.pop = populationSize;
        this.iter = maxIterations;
        this.x = new double[pop][dim];
        this.cost = new double[pop];
        this.xbest = new double[dim];
        this.best_fit_iter= new double[iter];
   } 
   
    private void init_X(){
        for(int i=0; i<pop ; i++){
            for(int j=0 ; j< dim ; j++){
               x[i][j]= Math.round(lb + (ub - lb)* rand.nextDouble()) ;
            }
            cost[i]= Calculator.calculateFitness(x[i], cloudletList, vmList);
        }        
    }
    private double[] sortarray(){
        double[] tem;
        for(int i=0; i<pop; i++){
            for(int j=0; j<pop-i-1; j++){
                if(cost[j] > cost[j+1]){
                      tem = x[j].clone();
                      x[j] = x[j+1].clone();
                      x[j+1]= tem.clone();
                 }
            }
        }  
       best_fit= cost[0];
        return  x[0];
    }
    private double[] check_bound(double[] s){
        for(int j=0; j< dim ; j++){
                if( s[j]< lb ) {
                    s[j]= lb;
                } else
                    if(s[j]> ub){
                       s[j]= ub; 
                    }                       
        }
        return s;
    }
    private double[] DE(double[] s){
        double gama= 0.5;   //mutation scale factor
        double cp = 0.3;    //crossover constant
        int sn = rand.nextInt(dim);
        int r1= rand.nextInt(pop/2);
        int r2= rand.nextInt(pop/2);
        int r3= rand.nextInt(pop/2);
        double[] v =new double[dim];
        double[] u =new double[dim];
        //mutation
        for(int j=0; j< dim ; j++){
            v[j] = x[r1][j] + gama * (x[r2][j]- x[r3][j]);    
        }
        //crossover
        for(int j=0; j< dim ; j++){     
            double r = rand.nextDouble();
            if (r <= cp || sn==j){
                u[j]= v[j];
            }
            else{
                u[j]= s[j];
            }
        }
        u = check_bound(u);
        double fit_u= Calculator.calculateFitness(u, cloudletList, vmList);
        double fit_s=Calculator.calculateFitness(s, cloudletList, vmList);
       if( fit_u < fit_s ){
           s = u.clone();
       }
     
       return s;
    }
    
   
   public int[] execute_MSA(){
       init_X();
       double epsilon= 0.8;
       double Smax= 1;    // maxStepSize
       double landa =0.5;   //mutation scale factor
       double pi = 0.618;           //(Math.sqrt(5)- 1 )/2 ;   // goldenRatio = 0.618
       //Levy levy =new Levy();
       LevyFlight levyFlight = new LevyFlight(1.5); 
       //double[] Lv ;
       double sum = 0;
       xbest = sortarray();
       for(int t=0 ; t< iter ; t++){           
           
           for(int i=0; i<pop/2; i++){
               sum += cost[i];  
           }
           
           for(int i=0; i<pop/2 ; i++){               
               double pr= cost[i]/sum;
               if(pr < epsilon){     //DE                     
                       x[i]= DE(x[i]);                   
               }
               else{
                  //Lv = levy.levy(dim);
                  for(int j=0; j<dim ; j++){
                      x[i][j] = x[i][j] + ((Smax /Math.pow(t, 2)) * levyFlight.levyFlight() ) ;
                  }                  
               }
               x[i]= check_bound(x[i]);
           }
           
           for(int i=pop/2; i<pop ; i++){
               double prob= rand.nextDouble();
               if(prob <= 0.5){
                   for(int j=0; j< dim ; j++){
                       x[i][j] = landa * (x[i][j] + pi * (xbest[j] - x[i][j]));
                   }
               }
               else{
                   for(int j=0; j< dim ; j++){
                       x[i][j] = landa * (x[i][j] + (1 / pi) * (xbest[j] - x[i][j]));
                   }
               }
               x[i]= check_bound(x[i]);
           }
           for(int i=0 ; i< pop ; i++){
               cost[i]= Calculator.calculateFitness(x[i], cloudletList, vmList);
           }
           xbest = sortarray();
           best_fit_iter[t]= Calculator.calculateFitness(xbest, cloudletList, vmList);
       }
       return Arrays.stream(xbest).map(Math::round).mapToInt((xx) -> (int) xx).toArray();   
   }
   
   public double[] getBestFitness() {  
        return best_fit_iter;  
    }  
}

