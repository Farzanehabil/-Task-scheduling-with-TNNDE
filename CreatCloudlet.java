/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainproject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.util.WorkloadFileReader;

/**
 *
 * @author farzaneh
 */
public class CreatCloudlet {
    
    public static void resetCloudlet(Cloudlet cloudlet) throws Exception{  
            cloudlet.setCloudletStatus(Cloudlet.CREATED); // یا Cloudlet.READY اگر وجود داشته باشد  
            cloudlet.setResourceParameter(0, 0, 0); // تنظیم مجدد منابع استفاده شده  
            cloudlet.setVmId(-1); // تخصیص زدایی از VM  
            cloudlet.setExecStartTime(0); // تنظیم مجدد زمان شروع اجرا  
        //    cloudlet.setFinishTime(0); // تنظیم مجدد زمان پایان اجرا  
    } 
    public static List<Cloudlet> createCloudlet_syntic(int userId, int cloudlets) {  
        // Creates a container to store Cloudlets  
        LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();  

        //cloudlet parameters  
        long length = 40000;  
        long fileSize = 300;  
        long outputSize = 300;  
        int pesNumber = 1;  
        UtilizationModel utilizationModel = new UtilizationModelFull();  

        Cloudlet[] cloudlet = new Cloudlet[cloudlets];  

        for (int i = 0; i < cloudlets; i++) {  
            cloudlet[i] = new Cloudlet(i, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);  
            cloudlet[i].setUserId(userId);  
            list.add(cloudlet[i]);  
        }  

        return list;  
    }  
    
    public static List<Cloudlet> createCloudlet_HP(int userId, int cloudlets) {
        List<Cloudlet> cloudletList = new ArrayList<>();
       
        try{
         WorkloadFileReader workloadFileReader = new WorkloadFileReader("C:\\Users\\farzaneh\\Documents\\NetBeansProjects\\hurestic_sched\\src\\HPC2N.swf",1);
             cloudletList = workloadFileReader.generateWorkload().subList(0, cloudlets);
                for (Cloudlet cloudlet : cloudletList) {
                    cloudlet.setUserId(userId);

                }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
		}
    //------------------------------------------------------------------------------                        
      return cloudletList;
      
	}
    
    
    
    
 }
