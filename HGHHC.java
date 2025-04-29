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
public class HGHHC {
    private int pop, dim , Maxiter;
    private double lb=0, ub;
    private double[][] population;            // جمعیت  
    private double[] fitness;                 // مقادیر قدرت  
    private double[] bestSolution;            // بهترین راه حل  
    private double bestfit = Double.MAX_VALUE;
    private List<Cloudlet> cloudletList;  
    private List<Vm> vmList; 
    private Random rand;
    private static final double c1 = 0.1;  
    private static final double c2 = 0.2;
   
    public HGHHC(int populationSize, int maxIterations, List<Cloudlet> cloudletList, List<Vm> vmList){
         rand = new Random(); 
         this.pop = populationSize;
         this.dim= cloudletList.size(); 
         this.ub = vmList.size() - 1; 
         this.Maxiter = maxIterations;
        this.population = new double[pop][dim];
        this.cloudletList = cloudletList;
        this.vmList = vmList;
        this.fitness = new double[pop];  
        this.bestSolution = new double[dim];  
       
        
    }
     private void initializePopulation() {  
        for (int i = 0; i < pop; i++) {  
            for (int j = 0; j < dim; j++) {  
                double alpha = rand.nextDouble(); // مقدار تصادفی بین 0 و 1  
                population[i][j] = Math.floor(lb + alpha * (ub - lb)); // مقداردهی اولیه با تابع floor  
            }  
        }  
    } 
     private double[] check_bound(double[] s){
        for(int j=0; j< dim ; j++){
            if( s[j]< lb )   s[j]= lb;
            if(s[j]> ub)     s[j]= ub;
        }
        return s;
    }
    private double sumFitness() {  
        double sum = 0;  
        for (double f : fitness) {  
            sum += f;  
        }  
        return sum;  
    }
    public void updateCoefficients(int iterations) {  
    
}  
    
    private void determineWorstSolutions(int t) {  
        int Nw = (int) (pop * rand.nextDouble() * (c2 - c1) + c1);   
        int[] worstIndices = new int[Nw]; 
        for (int i = 0; i < Nw; i++) {  
            double maxFitness = Double.NEGATIVE_INFINITY; 
            int worstIndex = -1; 
            for (int j = 0; j < pop; j++) {  
                if (fitness[j] > maxFitness) { 
                    maxFitness = fitness[j];  
                    worstIndex = j;  
                }  
            }  
            worstIndices[i] = worstIndex; 
            fitness[worstIndex] = Double.NEGATIVE_INFINITY; 
        }  
        for (int index : worstIndices) {  
            double[] worstSolution = population[index];  
            double[] X_opposite = oppositionSolution(worstSolution, lb, ub, t, Maxiter);  
         
            check_bound(X_opposite);  

            double x_opposite = Calculator.calculateFitness(X_opposite, cloudletList, vmList);  

            if (x_opposite < fitness[index]) { 
                population[index] = X_opposite; 
                fitness[index] = x_opposite;    
            }  
        }    
}  
    
    // Function to calculate Levy Flight  
    public static double levyFlight(double u, double sigma, double beta, double v) {  
        double r = u * sigma / Math.pow(Math.abs(v), beta);  
        double value = 0.01 * r * (1 + beta * Math.sin(Math.PI * beta / 2)) /   
                       (beta - 1);  
        return value;  
    } 
    public  double[] reflectedExtendedOpposition(double[] X, double lb, double Ub) {  
        double[] X_reo = new double[X.length];  
        for (int j = 0; j < X.length; j++) {  
            double Cbj = (lb + ub) / 2;  
            if (X[j] > Cbj) {  
                X_reo[j] = X[j] + (ub - X[j]) * rand.nextDouble();  
            } else {  
                X_reo[j] = lb + (X[j] - lb) * rand.nextDouble();  
            }  
        }  
        return X_reo; // Equation (22)  
    }  
    public  double[] quasiReflectedOpposition(double[] X, double lb, double Ub) {  
        double[] X_qr = new double[X.length];  
        double Cbj = (lb + ub) / 2;  
        for (int j = 0; j < X.length; j++) {  
            X_qr[j] = X[j] + (Cbj - X[j]) * rand.nextDouble(); // Equation (23)  
        }  
        return X_qr;  
    } 
    public  double[] quasiOpposite(double[] X, double lb, double ub) {  
        double[] X_qo = new double[X.length];  
        double Cbj = (lb + ub) / 2;  
        for (int j = 0; j < X.length; j++) {  
            X_qo[j] = Cbj + (X[j] - Cbj) * rand.nextDouble(); // Equation (24)  
        }  
        return X_qo;  
    }  
    
     public  double[] extendedOpposition(double[] X, double Lb, double Ub) {  
        double[] X_eo = new double[X.length];  
        for (int j = 0; j < X.length; j++) {  
            if (X[j] > (Lb + Ub) / 2) {  
                X_eo[j] = X[j] + (Ub - X[j]) * rand.nextDouble(); // Equation (25)  
            } else {  
                X_eo[j] = Lb + (X[j] - Lb) * rand.nextDouble();
                
            }
        }
        return X_eo;
     }
    public double[] oppositionSolution(double[] X, double lb, double Ub,int t, int iter ) {  
        double gamma = rand.nextDouble();  
        double[] X_co = new double[X.length]; 
        double Preo,Pqr, Pqo;
        if ((t/iter) >= 0 && (t/iter) <= (43.0 / 192.0)) {  
        Preo = 0.01;  
        Pqr = 0.01;  
        Pqo = 0.54;  
    } else if ((t/iter) > (43.0 / 192.0) && (t/iter) <= (139.0 / 192.0)) {  
        Preo = 0.1;  
        Pqr = 0.1;  
        Pqo = 0; // Adjusted from -0.44 to 0  
    } else { // (t/iter) > (139.0 / 192.0)  
        Preo = 0.97;  
        Pqr = 0.01;  
        Pqo = 0.01;  
    }  
        // تعیین نوع انتخاب بر اساس احتمالات  
        if (gamma <= Preo) {  
            X_co = reflectedExtendedOpposition(X, lb, Ub);  
        } else if (gamma <= Preo + Pqr) {  
            X_co = quasiReflectedOpposition(X, lb, Ub);  
        } else if (gamma <= Preo + Pqr + Pqo) {  
            X_co = quasiOpposite(X, lb, Ub);  
        } else {  
            X_co = extendedOpposition(X, lb, Ub);  
        }  

        return X_co; // بازگشت راه‌حل انتخاب شده  
    }  
   public int[] execute_HGHHC(){
         initializePopulation();
        //int numGasTypes = 2;
        double[] H = new double[dim];
        double[][] P = new double[pop][dim];
        double[] C = new double[dim];
        double[][] S = new double[pop][dim];
        double K = 1.0;
        double l1 = 5e-03;  
        double l2 = 100;  
        double l3 = 1e-02; 
        double T_theta = 298.15; // Temperature constant 
        double alpha = 1.0; // تأثیر ذرات دیگر  
        double beta = 1.0; // ثابت  
        double epsilon = 0.05;
        for (int i = 0; i < pop; i++) {
                 fitness[i] = Calculator.calculateFitness(population[i], cloudletList, vmList);
                 if(fitness[i] < bestfit) {
                    bestfit = fitness[i];
                    bestSolution = population[i];
                 }
            }
        
        
        for(int t=0 ; t< Maxiter; t++){
            double sumfit = sumFitness();
            
            for (int i = 0; i < pop; i++){
                double Pri = fitness[i] / sumfit;
                double L_pr = 0.0; double U_pr = 1.0;
                double rpr = L_pr + rand.nextDouble() * (U_pr - L_pr);
                if (Pri >= rpr) { 
                    for (int j = 0; j < dim; j++) {  
                        H[j] = l1 * rand.nextDouble(); // H_j^0  
                    }
                    for (int j = 0; j < dim; j++) {  
                        P[i][j] = l2 * rand.nextDouble(); // P_ij^0  
                    }  
                    for (int j = 0; j < dim; j++) {  
                        C[j] = l3 * rand.nextDouble(); // C_j^0  
                    }  
                    double T_t = Math.exp(-((double) t / Maxiter));  
                    for (int j = 0; j < H.length; j++) {   
                        H[j] = H[j] * Math.exp(-C[j] * (1.0 / T_t - 1.0 / T_theta));  
                    }
                    for (int j = 0; j < dim; j++) {  
                        S[i][j] = K * H[j] * P[i][j];  
                    }
                    for (int j = 0; j < dim; j++) {  
                        double f = (rand.nextBoolean() ? 1 : -1); // تصادفی بین 1 و -1  
                        double randomValue1 = rand.nextDouble(); // مقدار تصادفی 1  
                        double randomValue2 = rand.nextDouble(); // مقدار تصادفی 2  
                        double flag = (rand.nextBoolean() ? 1 : -1); // تصادفی بین 1 و -1  
                        double phi_j = beta * Math.exp(-(bestfit + epsilon) / (fitness[i] + epsilon));  
                        population[i][j] = population[i][j] + f * randomValue1 * phi_j * (bestSolution[j] - population[i][j]) +  
                            flag * randomValue2 * alpha * (S[i][j] * bestSolution[j] - population[i][j]);  
                    }
                    check_bound(population[i]);
                } else {  
                    double[] E = new double[dim];  
                    for (int d = 0; d < dim; d++) {  
                        for (int j = 0; j < pop; j++) {  
                            E[d] += population[j][d];  
                        }  
                        E[d] /= pop; // Mean position  
                    }
                    double[] X = new double[dim];  
                    double[] Z = new double[dim]; 
                    double u = rand.nextDouble(); // random number in (0, 1)  
                    double v = rand.nextDouble(); // random number in (0, 1)  
                    double beta1 = 1.5; // Specific constant
                    for (int d = 0; d < dim; d++) {  
                        X[d] = population[i][d] - E[d] * Math.abs(population[i][d]);  
                        Z[d] = X[d] + rand.nextDouble() * levyFlight(u, 1, beta1, v); 
                    } 
                    check_bound(X);
                    check_bound(Z);
                    double fit_x = Calculator.calculateFitness(X, cloudletList, vmList); 
                    double fit_z = Calculator.calculateFitness(Z , cloudletList, vmList);
                      
                    if (fit_x > fit_z) {  
                        population[i] =Z.clone();
                    } else {  
                          population[i] =X.clone();
                    }  
                }  
            } 
         for (int i = 0; i < pop; i++) {
            fitness[i] = Calculator.calculateFitness(population[i], cloudletList, vmList);
            if(fitness[i] < bestfit) {
                bestfit = fitness[i];
                bestSolution = population[i];
            }
        }
           determineWorstSolutions(t);    
           
    }
        return  Arrays.stream(bestSolution).map(Math::round).mapToInt((xx) -> (int) xx).toArray();
}

     
    
}
