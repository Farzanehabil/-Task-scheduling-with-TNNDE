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
public class EMPA {
    private double lb=0, ub;
    private int pop;
    private int dim, maxiter;
    private double p = 0.5;
    private double[][] prey;
    private double[][] stepsize;
    private double[][] elite; 
    private double[] fit;
    private double[] Top_predator_pos;
    private double Top_predator_fit = Double.MAX_VALUE; ;
    private List<Cloudlet> cloudletList;  
    private List<Vm> vmList;
    Random rand = new Random();
    private double[] best_fit_iter;
    public EMPA(int populationSize, int maxIterations, List<Cloudlet> cloudletList, List<Vm> vmList){
        this.cloudletList = cloudletList;  
        this.vmList = vmList;  
        this.dim = cloudletList.size();  
        this.ub = vmList.size() - 1;  
        this.pop = populationSize;
        this.maxiter = maxIterations;
        this.prey = new double[pop][dim];
        this.stepsize = new double[pop][dim];
        this.elite= new double[pop][dim];
        this.fit = new double[pop];
        this.Top_predator_pos = new double[dim];
        this.best_fit_iter= new double[maxiter];
    }
    public double[][] initialization(int SearchAgents_no, int dim, double ub, double lb) {  
        double[][] Prey = new double[SearchAgents_no][dim];  
        for (int i = 0; i < SearchAgents_no; i++) {  
            for (int j = 0; j < dim; j++) {  
                Prey[i][j] = lb + rand.nextDouble() * (ub - lb);  
            }  
        }  
        return Prey;  
    }  
    private double[] check_bound(double[] s){
        for(int j=0; j< dim ; j++){
                if( s[j]< lb )     s[j]= lb;
                if(s[j]> ub)       s[j]= ub; 
         }
        return s;
    }
    
    public  double[] calculateWOA(double[] x_woa, double[] xbest, int t) {  
        double a = 2 - ((2*t)/maxiter);                 
        for(int j=0; j<dim; j++){ 
            double r = rand.nextDouble();
            double A = (2 * a *r )- a;   
            double c = 2*r;
            double l = r *2 -1;  
            double pp = rand.nextDouble();
            double b =1.0;
            if(pp < 0.5){
                if(Math.abs(A) < 1){
                    double d = Math.abs((c*xbest[j])-x_woa[j]);
                    x_woa[j] = xbest[j]- (A * d);
                }else{
                    int rnd = rand.nextInt(pop);
                    double[] xrand = prey[rnd].clone();                            
                    double d = Math.abs(c * xrand[j] - x_woa[j]);
                    x_woa[j] = xrand[j] -(A * d);
                    }
                }
                if(pp >= 0.5){
                    double dprim= Math.abs(xbest[j]- x_woa[j]);  
                    x_woa[j] = dprim * Math.exp(l * b) * Math.cos(2.0* Math.PI * l) + xbest[j];
                } 
        }
        
        check_bound(x_woa);
        return x_woa;
    }
    public  double[] calculatempa(double[] x_mpa ,double[] Elite, double[] RB, double[] stpsize ){
        double R = rand.nextDouble(); 
        for (int j = 0; j < dim; j++) {  
                    stpsize[j] = RB[j] * (Elite[j] - RB[j] * x_mpa[j]);  
                    x_mpa[j] += p * R * stpsize[j];  
                } 
        check_bound(x_mpa);
        return x_mpa;
    }
   
    public int[]  MPA(){
      prey = initialization(pop, dim, ub, lb); 
       double[][] Xmin = new double[pop][dim];  
        double[][] Xmax = new double[pop][dim];
        for (int i = 0; i < pop; i++) {  
            for (int j = 0; j < dim; j++) {  
                Xmin[i][j] = lb;  
                Xmax[i][j] = ub;  
            }  
        }  
//        double[] fit_old = new double[pop];  
//        double[][] Prey_old = new double[pop][dim];  
        
        double FADs = 0.2; 
        for (int i = 0; i < prey.length; i++) {  
                 check_bound(prey[i]);
                fit[i] = Calculator.calculateFitness(prey[i], cloudletList, vmList);
                if (fit[i] < Top_predator_fit) {  
                    Top_predator_fit = fit[i];  
                    Top_predator_pos = prey[i].clone();
                }  
        }
        for (int i = 0; i < pop; i++) {  
                elite[i] = Arrays.copyOf(Top_predator_pos, dim);  
            }  
             
            double[][] RL = new double[pop][dim]; // Levy random numbers  
            double[][] RB = new double[pop][dim]; // Brownian random numbers
            LevyFlight levyFlight = new LevyFlight(1.5); // مقدار alpha = 1.5 
            for (int i = 0; i < pop; i++) { 
                for(int j=0; j< dim; j++){
                    RL[i][j] = levyFlight.levyFlight();
                    RB[i][j] =  rand.nextGaussian(); 
                }
            }  
        
        for(int iter=0; iter< maxiter; iter++){
            
             double CF = Math.pow(1 - (double) iter / maxiter, 2 * (double) iter / maxiter); 

            for (int i = 0; i < prey.length; i++) {  
             
            if (iter <maxiter / 3) {  
                // Phase 1  
                double[] x_woa = calculateWOA(prey[i],Top_predator_pos, iter);
                double fit_woa = Calculator.calculateFitness(x_woa, cloudletList, vmList);
                double[] x_mpa = calculatempa(prey[i],elite[i], RB[i], stepsize[i]);
                double fit_mpa = Calculator.calculateFitness(x_mpa, cloudletList, vmList);
                if(fit_woa < fit_mpa){
                    prey[i] = x_woa.clone();
                }else{
                    prey[i] = x_mpa.clone();
                }
            } else if (iter > maxiter / 3 && iter < 2 * maxiter / 3) {  
                // Phase 2  
                double wmin = 0.05;
                double wmax = 0.5;
                int alpha = 3;
                double w = wmin + (wmax - wmin)* Math.exp(-1 * Math.pow(alpha*iter/maxiter , 2));
                if (i < pop / 2) {  
                    for (int j = 0; j < dim; j++) {  
                        double r= rand.nextDouble();
                        stepsize[i][j] =  RL[i][j] * (elite[i][j] -(RL[i][j] * prey[i][j]));
                        prey[i][j] = ( w * prey[i][j]) + (p * r * stepsize[i][j]);
                    }  
                } else {  
                    for(int j=0; j<dim ; j++){
                        stepsize[i][j] = RB[i][j] * (RB[i][j] * elite[i][j] - prey[i][j]);
                        prey[i][j] = (w * elite[i][j]) + (p *  CF * stepsize[i][j]);
                    }
                } 
               // Phase 3 
             } else if( iter > (int) (2*maxiter/3 )){  
                    for (int j = 0; j < dim; j++) {  
                        stepsize[i][j] = RL[i][j] * (RL[i][j] * elite[i][j] - prey[i][j]);  
                        prey[i][j] = elite[i][j] + p * CF * stepsize[i][j];  
                    }  
                }  
        
        }
        
        for(int i=0; i<pop; i++){
            prey[i] = check_bound(prey[i]);
            fit[i] = Calculator.calculateFitness(prey[i], cloudletList, vmList);
            if (fit[i] < Top_predator_fit) {  
                Top_predator_fit = fit[i];  
                Top_predator_pos = prey[i].clone();
            }  
        }  
        for (int i = 0; i < pop; i++) {  
                elite[i] = Arrays.copyOf(Top_predator_pos, dim);  
            }
 
       // **مرحله 4: اثر FADs**  
            if (rand.nextDouble() <= FADs) {  
                for (int i = 0; i < pop; i++) {  
                    for (int j = 0; j < dim; j++) {  
                        boolean U = rand.nextDouble() < FADs;  
                        prey[i][j] += CF * ((Xmin[i][j] + rand.nextDouble() * (Xmax[i][j] - Xmin[i][j])) * (U ? 1.0 : 0.0));  
                    }  
                }  
            } else {  
                double r = rand.nextDouble();  
                for (int i = 0; i < pop; i++) {  
                    for (int j = 0; j < dim; j++) {  
                        double stepsizeVariation = FADs * (1 - r) + r;  
                        prey[i][j] += stepsizeVariation * (prey[rand.nextInt(pop)][j] - prey[rand.nextInt(pop)][j]);  
                    }  
                }  
            } 
            //Golden sine strategy
            double phi = (Math.sqrt(5) - 1) / 2; // s  
            double c1 = Math.PI + (1 - phi) * 2 * Math.PI;   
            double c2 = Math.PI + phi * 2 * Math.PI;  
            for (int i = 0; i < pop; i++) {  
              for(int j=0; j< dim; j++){
                    double R1 = rand.nextDouble() * 2 * Math.PI; // R1 in [0, 2π]  
                    double R2 = rand.nextDouble() * Math.PI; // R2 in [0, π]
                
                    prey[i][j] = (prey[i][j] * Math.abs(Math.sin(R1))) - (R2 * Math.sin(R1) *   
                         Math.abs(c1 * Top_predator_pos[j] - c2 * prey[i][j]));  
                }
            }   
            
            for(int i=0; i<pop; i++){
            prey[i] = check_bound(prey[i]);
            fit[i] = Calculator.calculateFitness(prey[i], cloudletList, vmList);
            if (fit[i] < Top_predator_fit) {  
                Top_predator_fit = fit[i];  
                Top_predator_pos = Arrays.copyOf(prey[i], dim); // کپی موقعیت شکارچی برتر  
            }  
        }  
        for (int i = 0; i < pop; i++) {  
                elite[i] = Arrays.copyOf(Top_predator_pos, dim);  
            }
        best_fit_iter[iter]= Top_predator_fit;
    }// iter
        
    return Arrays.stream( Top_predator_pos).map(Math::round).mapToInt((xx) -> (int) xx).toArray();
  }// mpa()
    
    public double[] getBestFitness() {  
        return best_fit_iter;  
    } 
}

    