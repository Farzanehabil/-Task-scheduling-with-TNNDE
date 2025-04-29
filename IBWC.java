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
import org.apache.commons.math3.special.Gamma;  
/**
 *
 * @author farzaneh
 */
public class IBWC {
    private int pop,iter,dim;
    private int lb=0, ub;
    private double[][] whales;
    private double[] wcost; 
    private List<Cloudlet> cloudletList;  
    private List<Vm> vmList; 
    double[] bestwhale;
    double fit_bestwhale;
    Random rand = new Random();
    private double[] best_fit_iter;
    
    public IBWC(int populationSize, int maxIterations, List<Cloudlet> cloudletList, List<Vm> vmList){
        this.pop = populationSize;
        this.iter = maxIterations;
        this.cloudletList = cloudletList;  
        this.vmList = vmList;  
        this.dim = cloudletList.size();  
        this.ub = vmList.size() - 1; 
        this.whales = new double[pop][dim];
        this.wcost = new double[pop];   
        this.bestwhale = new double[dim];
        this.fit_bestwhale = Double.MAX_VALUE;
        this.best_fit_iter= new double[iter];
    }
    
     private  double[][] initializePopulation() {  
        double[][] population = new double[pop][dim]; 
        for (int i = 0; i < pop; i++) {  
            for (int j = 0; j < dim; j++) {  
                population[i][j] = lb + (ub - lb)*rand.nextDouble();      
            }  
        }  
        return population; 
    }
    private double[] check_bound(double[] s){
        for(int j=0; j< dim ; j++){
            if( s[j]< lb )   s[j]= lb;
            if(s[j]> ub)     s[j]= ub;
        }
        return s;
    }
    private double[][] createQuasiOppositePopulation(double[][] population) {  
        double[][] QOBL = new double[pop][dim];  
        double[] H = new double[dim];  
        for (int j = 0; j < dim; j++) {  
            H[j] = (ub + lb) / 2;  
        } 
        double[] currentXi= new double[dim];
        for (int i = 0; i < pop; i++) {  
            for (int j = 0; j < dim; j++) { 
                currentXi[j] = (ub + lb) - population[i][j];
                if (currentXi[j] < H[j]) {  
                    QOBL[i][j] = H[j] + (currentXi[j] - H[j]) * rand.nextDouble();  
                } else {  
                    QOBL[i][j] = currentXi[j] + (H[j] - population[i][j]) * rand.nextDouble();  
                }  
            }  check_bound(QOBL[i]);
        }  
        return QOBL;  
    } 
    private double[][] combinePopulations(double[][] original, double[][] quasiOpposite) {  
        double[][] combined = new double[2 * pop][dim];  
        System.arraycopy(original, 0, combined, 0, pop);  
        System.arraycopy(quasiOpposite, 0, combined, pop, pop);   
        double[] fitness = new double[combined.length];  
        for (int i = 0; i < combined.length; i++) {  
            fitness[i] = Calculator.calculateFitness(combined[i], cloudletList, vmList);
        }  
        combined =  sort_array(combined ,fitness ); 
        double[][] ary = new double[pop][dim];
        System.arraycopy(combined, 0, ary, 0, pop);
        return ary;
    } 
    private double[][] sort_array(double[][] X, double[] cost){
        double temp;
        double[] tem;
        for(int i=0; i<X.length; i++){
            for(int j=0; j<X.length-i-1;j++){
                if(cost[j] > cost[j+1]){
                      tem = X[j].clone();
                      X[j] = X[j+1].clone();
                      X[j+1]= tem.clone();
                      temp = cost[j];
                      cost[j]=cost[j+1];
                      cost[j+1]= temp;                                    
                }
            }
        }
        return X;
    }
    
    
    private double LevyFlight() {  
       // محاسبه σ  
        double beta = 1.5; // مقدار ثابت beta  
        double sigma = Math.pow(  
            (Gamma.gamma(1 + beta) * Math.sin(Math.PI * beta / 2)) /  
            (Gamma.gamma((1 + beta) / 2) * Math.pow(2, (1 - beta) / 2)),  
            1.0 / beta);   
        double v = rand.nextGaussian(); // عدد تصادفی با توزیع نرمال  
        double vMagnitude = Math.abs(v); // قدر مطلق v برای معیار  
        double L_VF = 0.05 * (v * sigma) / Math.pow(vMagnitude, (1.0 / beta));  
        return L_VF;  
    }  

    private double randomCauchy() {  
        return Math.tan(Math.PI * (rand.nextDouble() - 0.5)); 
    }   
    
    public int[] Execute_IBWC(){
        double[][] originalPopulation = initializePopulation();
        double[][] quasiOppositePopulation = createQuasiOppositePopulation(originalPopulation); 
        whales = combinePopulations(originalPopulation, quasiOppositePopulation); 
        for(int i=0 ; i<pop ; i++){
            wcost[i]= Calculator.calculateFitness(whales[i], cloudletList, vmList);
            if(wcost[i] < fit_bestwhale){
                bestwhale = whales[i].clone();
                fit_bestwhale = wcost[i];                
            }
        }
        for(int t=0; t< iter; t++){
            double Wf = 0.1 - (0.05 * t / iter);  
            for(int i=0 ; i<pop; i++){
                double Bf0 = rand.nextDouble(); 
                double Bf = Bf0 * (1 -  t / (2 * iter));
                
                if (Bf > 0.5) {
                    for(int j=0; j<dim; j++){
                        double r2 = rand.nextDouble(); 
                        double r = rand.nextDouble(); 
                        int p1 = (int) (rand.nextDouble() * dim);
                        double lv= LevyFlight();
                        double step = (bestwhale[p1] - whales[i][p1]) * (1 + lv);  
                        if (j % 2 == 0) { 
                            whales[i][j] += step * Math.exp(-((i) / (r * iter)) * Math.sin(2 * Math.PI * r2));  
                                     
                        } else { 
                            whales[i][j] += step * Math.exp(-((i) / (r * iter)) * Math.cos(2 * Math.PI * r2));  
                        } 
                    }
                } else { 
                    double r4 = rand.nextDouble(); 
                    double C1 = 2 * r4 * (1 - (double) t / iter);
                    for (int j = 0; j < dim; j++) {  
                        double lv= LevyFlight();
                        whales[i][j] = bestwhale[j] * (1 + randomCauchy()) - C1 * whales[i][j] + lv * (bestwhale[j] - whales[i][j]);  
                    }   
                } 
            }
           for(int i=0; i<pop; i++){
               double Bf0 = rand.nextDouble();
               double Bf = Bf0 * (1 -  t / (2 * iter));
               if(Bf < Wf){
                    double C2 = 2 * Wf * pop;  
                    double X_step = (ub - lb) * Math.exp(-C2 * t / iter); 
                    for (int j = 0; j < dim; j++) { 
                       double r5 = rand.nextDouble();  
                       double r6 = rand.nextDouble();  
                       double r7 = rand.nextDouble();
                       whales[i][j] = r5 * whales[i][j] - r6 * bestwhale[j] + r7 * X_step;  
                    }
                }
               check_bound(whales[i]);
               wcost[i]= Calculator.calculateFitness(whales[i], cloudletList, vmList);
                if(wcost[i] < fit_bestwhale){
                    bestwhale = whales[i].clone();
                    fit_bestwhale = wcost[i];                
                }
            }
           best_fit_iter[t]= fit_bestwhale;
        }
        
        return  Arrays.stream(bestwhale).map(Math::round).mapToInt((xx) -> (int) xx).toArray();
    }
    public double[] getBestFitness() {  
        return best_fit_iter;   
    }
    
}
