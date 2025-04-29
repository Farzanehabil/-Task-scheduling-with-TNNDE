/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainproject;
import java.util.Random;
import org.apache.commons.math3.special.Gamma;  


/**
 *
 * @author farzaneh
 */
public class LevyFlight {
    private double alpha ;  // نمای توزیع  
    private Random rand = new Random();   

   public  LevyFlight(double alpha) {
       if (alpha <= 1 || alpha > 2) {  
            throw new IllegalArgumentException("Alpha must be in the range (1, 2]");  
        }  
        this.alpha = alpha;  
        this.rand = new Random();  
   }

    private double calculateSigmaX() {  
        double numerator = Gamma.gamma(1 + alpha) * Math.sin(Math.PI * alpha / 2);  
        double denominator = Math.pow((1 + alpha) / Math.pow((alpha - 1), 2), alpha / 2);  
        return Math.sqrt(numerator / denominator);  
    }  

    public  double levyFlight() { 
        if (alpha <= 1 || alpha > 2) {  
            throw new IllegalArgumentException("Alpha must be in the range (1, 2]");  
        }  
          
        double sigmaX = calculateSigmaX(); // محاسبه σx  
        double x = sigmaX * rand.nextGaussian(); // تولید x از توزیع نرمال  
        double y = rand.nextGaussian(); // تولید y از توزیع نرمال با σy = 1  
        
        // جلوگیری از تقسیم بر صفر  
        if (y == 0) {  
            y = 1e-10; // مقدار کوچک برای جلوگیری از تقسیم بر صفر  
        }  

        // محاسبه قدم بر اساس توزیع لویی  
        return 0.05 * x / Math.abs(y) * Math.pow(1, -1 / alpha);  
    }  

}
