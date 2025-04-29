/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainproject;

import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;


/**
 *
 * @author farzaneh
 */
public class Calculator {
    
    public static double calculateFitness(double[] position, List<Cloudlet> cloudletList, List<Vm> vmList){
        int[] assignment = new int[cloudletList.size()];  
        for (int i = 0; i < cloudletList.size(); i++) {  
            assignment[i] = (int) Math.round(position[i]); // موقعیت رو به شماره VM تبدیل می‌کنیم  
        }  
        double makespan = calculateMakespan( cloudletList, vmList, assignment);
        double degreeOfImbalance=calculateDegreeOfIm( cloudletList, vmList, assignment);
        double ru=calculateRUR ( cloudletList, vmList, assignment);
        double th=calculateThroughput( cloudletList, vmList, assignment);
        double w=0.25;
        double fitness= (w*makespan)+(w*degreeOfImbalance)+(w*ru)+(w*th);
        return fitness;
    }
    
    public static double calculateRUR(List<Cloudlet> cloudlets, List<Vm> vms, int[] allocations) {
        double[] vmCompletionTimes = new double[vms.size()];
        
        for (int i = 0; i < cloudlets.size(); i++) {
            Cloudlet cloudlet = cloudlets.get(i);
            int vmIndex = allocations[i];
            double executionTime = cloudlet.getCloudletLength() / vms.get(vmIndex).getMips();
            vmCompletionTimes[vmIndex]  += executionTime;
        }
        
        // محاسبه مجموع CTj ها
        double sumCT = 0;
        for (double ct : vmCompletionTimes) {
            sumCT += ct;
        }
        
        // محاسبه makespan (بیشترین زمان تکمیل)
        double makespan = 0;
        for (double ct : vmCompletionTimes) {
            makespan = Math.max(makespan, ct);
        }
        
        // محاسبه RUR طبق فرمول
        // RUR = ∑(CTj) / (Makespan * m)
        int m = vms.size(); // تعداد ماشین‌های مجازی
        double rur = sumCT / (makespan * m);
        
        return rur;
    }


    public static double calculateThroughput(List<Cloudlet> cloudlets, List<Vm> vms, int[] allocations) {
        
        int numberOfTasks = cloudlets.size();
        
        double makespan = calculateMakespan(cloudlets, vms, allocations);
        
        double throughput =  numberOfTasks / makespan;
        
        return throughput;
    }

    public static double calculateDegreeOfIm(List<Cloudlet> cloudlets, List<Vm> vms, int[] allocations) {
        // محاسبه زمان تکمیل برای هر ماشین مجازی
        double[] vmCompletionTimes = calculateVMCompletionTimes(cloudlets, vms, allocations);
        
        // پیدا کردن بیشترین و کمترین زمان تکمیل
        double maxCompletionTime = Double.MIN_VALUE;
        double minCompletionTime = Double.MAX_VALUE;
        double totalCompletionTime = 0;
        
        for (double completionTime : vmCompletionTimes) {
            maxCompletionTime = Math.max(maxCompletionTime, completionTime);
            minCompletionTime = Math.min(minCompletionTime, completionTime);
            totalCompletionTime += completionTime;
        }
        
        // محاسبه میانگین زمان تکمیل
        double avgCompletionTime = totalCompletionTime / vms.size();
        
        // محاسبه درجه عدم تعادل
        return (maxCompletionTime - minCompletionTime) / avgCompletionTime;
    }
    
    private static double[] calculateVMCompletionTimes(List<Cloudlet> cloudlets, List<Vm> vms, int[] allocations) {
        double[] vmCompletionTimes = new double[vms.size()];
        
        // محاسبه زمان تکمیل برای هر کلادلت روی ماشین مجازی مربوطه
        for (int i = 0; i < cloudlets.size(); i++) {
            Cloudlet cloudlet = cloudlets.get(i);
            int vmIndex = allocations[i];
            
            // محاسبه زمان اجرای کلادلت
            double executionTime = cloudlet.getCloudletLength() / vms.get(vmIndex).getMips();
            vmCompletionTimes[vmIndex] += executionTime;
        }
        
        return vmCompletionTimes;
    }
    
    
    public static double calculateMakespan(List<Cloudlet> cloudlets, List<Vm> vms, int[] allocations) {
        // آرایه‌ای برای نگهداری زمان تکمیل هر ماشین مجازی
        double[] vmCompletionTimes = new double[vms.size()];
        
        // محاسبه زمان اجرای هر کلادلت روی ماشین مجازی تخصیص داده شده
        for (int i = 0; i < cloudlets.size(); i++) {
            Cloudlet cloudlet = cloudlets.get(i);
            int vmIndex = allocations[i];
            
            // محاسبه زمان اجرای کلادلت
            double executionTime = calculateExecutionTime(cloudlet, vms.get(vmIndex));
            
            // به‌روزرسانی زمان تکمیل ماشین مجازی
            vmCompletionTimes[vmIndex] += executionTime;
        }
        
        // پیدا کردن بیشترین زمان تکمیل در بین تمام ماشین‌های مجازی
        double makespan = 0;
        for (double completionTime : vmCompletionTimes) {
            makespan = Math.max(makespan, completionTime);
        }
        
        return makespan;
    }
    
    private static double calculateExecutionTime(Cloudlet cloudlet, Vm vm) {
        // محاسبه زمان اجرا بر اساس طول کلادلت و قدرت پردازشی ماشین مجازی
        return cloudlet.getCloudletLength() / vm.getMips();
    }
    
   public static double calculateEnergyConsumption(List<Cloudlet> cloudlets, List<Vm> vms, int[] allocations) {  
        double totalEC = 0; // انرژی کل در حالت فعال  
        double MI = calculateRealisticMI( cloudlets,  vms,  allocations);
        
        for (int i = 0; i < cloudlets.size(); i++) {  
            Cloudlet cloudlet = cloudlets.get(i);  
            int vmIndex = allocations[i];  

            double vmMIPS = vms.get(vmIndex).getMips(); // MIPS VM  
            double EC = (10e-8) * Math.pow(vmMIPS, 2) * MI; // محاسبه انرژی در حالت فعال  
            totalEC += EC; // جمع انرژی فعال  
        }  
    
        double totalIE = 0; // انرژی کل در حالت بیکار  
        totalIE = 0.6 * totalEC; // محاسبه انرژی در حالت بیکار  

        double totalEnergy = totalEC + totalIE;  
        return totalEnergy;  
    }  
   private static double calculateRealisticMI(List<Cloudlet> cloudlets, List<Vm> vms, int[] allocations) {  
    double totalExecutionTime = 0.0;  
    double totalMIPS = 0.0;  
    int cloudletCount = cloudlets.size();  

    for (int i = 0; i < cloudletCount; i++) {  
        Cloudlet cloudlet = cloudlets.get(i);  
        int vmIndex = allocations[i];  
        
        double vmMIPS = vms.get(vmIndex).getMips();  
        
        // محاسبه زمان مورد نیاز برای Cloudlet  
        double executionTime = cloudlet.getCloudletLength() / vmMIPS; // طول Cloudlet به MIPS تقسیم می‌شود  
        totalExecutionTime += executionTime;  
        totalMIPS += vmMIPS;  
    }  

    // میانگین MIPS  
    double averageMIPS = totalMIPS / cloudletCount;  

    // محاسبه MI به صورت نسبتی نسبت به میانگین  
    return (totalExecutionTime / cloudletCount) * (averageMIPS / averageMIPS);  
}  
   

    
    
    
}
