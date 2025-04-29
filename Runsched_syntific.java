/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainproject;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;  
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
/**
 *
 * @author farzaneh
 */
public class Runsched_syntific {
    int num_vm,num_task,pop , max_iter;
    int N = 10;
    int M = 5 ;     
    
     

//1 parameter FPGWO
    
    double[] RU_fpa = new double[N];  
    double[] TH_fpa = new double[N];  
    double[] deg_fpa = new double[N];
    double[] MAK_fpa = new double[N];
   
//2 parameter MSDE
     
    double[] RU_msa = new double[N];  
    double[] TH_msa = new double[N];  
    double[] deg_msa = new double[N];
    double[] MAK_msa = new double[N];
    
//3 parameter EMPA
     
    double[] RU_empa = new double[N];  
    double[] TH_empa = new double[N];  
    double[] deg_empa = new double[N];
    double[] MAK_empa = new double[N];
    
//4 parameter HGHHC
      
    double[] RU_hghhc = new double[N];  
    double[] TH_hghhc = new double[N];  
    double[] deg_hghhc = new double[N];
    double[] MAK_hghhc = new double[N];
   
//5 parameter IBWC
     
    double[] RU_ibwc = new double[N];  
    double[] TH_ibwc = new double[N];  
    double[] deg_ibwc = new double[N];
    double[] MAK_ibwc = new double[N];
  
//6 parameter TNNDE
      
    double[] RU_tnnde = new double[N];  
    double[] TH_tnnde = new double[N];  
    double[] deg_tnnde = new double[N];
    double[] MAK_tnnde = new double[N];
    
    Print_result print= new Print_result();
    Plat_result plat = new Plat_result();
    
    
    public  Runsched_syntific(int pop, int iter,int vm, int task) {
        this.pop= pop;
        this.max_iter = iter;
        this.num_vm= vm;
        this.num_task = task;
        
        
    }
    public  void Execute()throws Exception{    
        
        int num_user = 1;  
        Calendar calendar = Calendar.getInstance();  
        boolean trace_flag = false; 
        CloudSim.init(num_user, calendar, trace_flag);

        Datacenter datacenter0 = createDatacenter("Datacenter_0");
        DatacenterBroker broker = createBroker(); 
        int brokerId = broker.getId(); 
        
        
        List<Cloudlet> originalCloudletList = CreatCloudlet.createCloudlet_syntic(brokerId, num_task);
        
        int[] cloudletCounts = new int[N];    
        for(int k=0; k< M; k++){
            System.out.println("Execute Run ------------------------------------------------------------>> : "+(k+1) );
            int cloudletCount = 100; 
            for(int i=0 ; i<N; i++){  
               cloudletCounts[i]= cloudletCount;
               System.out.println("Execute simulatin with ------------------>> : "+cloudletCount );


    
    //----------start FPGWO---------------------------------------------
            System.out.println("------FPGWO-----------------"+  "RUN  : " + (k+1)+ "iter : " + (i+1));
            CloudSim.init(num_user, calendar, trace_flag);  
            datacenter0 = createDatacenter("Datacenter_0");  
            broker = createBroker();  
            brokerId = broker.getId();
            List<Vm> vmList = createVM(brokerId, num_vm);
            broker.getVmList().clear();  
            broker.submitVmList(vmList);  
            List<Cloudlet> cloudletList = new ArrayList<>(originalCloudletList.subList(0, cloudletCount));
            for (Cloudlet cloudlet : cloudletList) {  
                   CreatCloudlet.resetCloudlet(cloudlet);    
            } 
           broker.submitCloudletList(cloudletList);
           
           FPGWDE fpa = new FPGWDE(pop, max_iter, cloudletList, vmList);
           
           int[] bestAlloc_fpa = fpa.ececute_FPA();
           
            for (int j = 0; j < cloudletList.size(); j++) {  
                int vmId = (int) Math.round(bestAlloc_fpa[j]);  
                broker.bindCloudletToVm(cloudletList.get(j).getCloudletId(), vmList.get(vmId).getId());  
            } 
            CloudSim.startSimulation();
            List<Cloudlet> newList = broker.getCloudletReceivedList(); 
            
            double ru = Calculator.calculateRUR(cloudletList,vmList,bestAlloc_fpa); 
            double throughput = Calculator.calculateThroughput(cloudletList,vmList,bestAlloc_fpa);  
            double degreeOfImbalance = Calculator.calculateDegreeOfIm( cloudletList, vmList,bestAlloc_fpa);
            double Makespan = Calculator.calculateMakespan( cloudletList, vmList,bestAlloc_fpa);
           
            RU_fpa[i] += ru;  
            TH_fpa[i] += throughput;  
            deg_fpa[i] += degreeOfImbalance;
            MAK_fpa[i] += Makespan;
            
            
            broker.getCloudletSubmittedList().clear();  
            broker.getCloudletReceivedList().clear(); // پاکسازی لیست Cloudletهای دریافتی  
            cloudletList.clear(); 
            
            CloudSim.stopSimulation();
             
    //-------------End FPGWO-------------------------------
    
    //---------start MSDE---------------------------------------------
            System.out.println("------MSA-----------------"+  "RUN  : " + (k+1)+ "iter : " + (i+1));
            CloudSim.init(num_user, calendar, trace_flag);  
            datacenter0 = createDatacenter("Datacenter_0");  
            broker = createBroker();  
            brokerId = broker.getId();
            vmList = createVM(brokerId, num_vm);
            broker.getVmList().clear();  
            broker.submitVmList(vmList);  
             cloudletList = new ArrayList<>(originalCloudletList.subList(0, cloudletCount));
            for (Cloudlet cloudlet : cloudletList) {  // **تغییر وضعیت Cloudletها به STATUS_READY قبل از ارسال مجدد**
                   CreatCloudlet.resetCloudlet(cloudlet);    
            } 
           broker.submitCloudletList(cloudletList);
           
           MSDE msa = new MSDE(pop, max_iter, cloudletList, vmList);
           int[] bestAlloc_msa = msa.execute_MSA();
           
            for (int j = 0; j < cloudletList.size(); j++) {  
                int vmId = (int) Math.round(bestAlloc_msa[j]);  
                broker.bindCloudletToVm(cloudletList.get(j).getCloudletId(), vmList.get(vmId).getId());  
            } 
            CloudSim.startSimulation();
             newList = broker.getCloudletReceivedList(); 
            
            ru = Calculator.calculateRUR(cloudletList,vmList,bestAlloc_msa); 
            throughput = Calculator.calculateThroughput(cloudletList,vmList,bestAlloc_msa);  
            degreeOfImbalance = Calculator.calculateDegreeOfIm( cloudletList, vmList,bestAlloc_msa);
            Makespan = Calculator.calculateMakespan( cloudletList, vmList,bestAlloc_msa);
            
            RU_msa[i] += ru;  
            TH_msa[i] += throughput;  
            deg_msa[i] += degreeOfImbalance;
            MAK_msa[i] += Makespan;
            
            broker.getCloudletSubmittedList().clear();  
            broker.getCloudletReceivedList().clear(); // پاکسازی لیست Cloudletهای دریافتی  
            cloudletList.clear(); 
            
            CloudSim.stopSimulation();
             
    //-------------End MSDE-------------------------------
    
    //---------start EMPA--------------------------------------
            System.out.println("------EMPA-----------------"+  "RUN  : " + (k+1)+ "iter : " + (i+1));
            CloudSim.init(num_user, calendar, trace_flag);  
            datacenter0 = createDatacenter("Datacenter_0");  
            broker = createBroker();  
            brokerId = broker.getId();
             vmList = createVM(brokerId, num_vm);
            broker.getVmList().clear();  
            broker.submitVmList(vmList);  
             cloudletList = new ArrayList<>(originalCloudletList.subList(0, cloudletCount));
            for (Cloudlet cloudlet : cloudletList) {  // **تغییر وضعیت Cloudletها به STATUS_READY قبل از ارسال مجدد**
                   CreatCloudlet.resetCloudlet(cloudlet);    
            } 
           broker.submitCloudletList(cloudletList);
           
           
           EMPA mpa = new EMPA(pop, max_iter, cloudletList, vmList);
           int[] bestAlloc_mpa = mpa.MPA();
           
            for (int j = 0; j < cloudletList.size(); j++) {  
                int vmId = (int) Math.round(bestAlloc_mpa[j]);  
                broker.bindCloudletToVm(cloudletList.get(j).getCloudletId(), vmList.get(vmId).getId());  
            } 
            CloudSim.startSimulation();
             newList = broker.getCloudletReceivedList(); 
            
           ru = Calculator.calculateRUR(cloudletList,vmList,bestAlloc_mpa); 
           throughput = Calculator.calculateThroughput(cloudletList,vmList,bestAlloc_mpa);  
           degreeOfImbalance = Calculator.calculateDegreeOfIm( cloudletList, vmList,bestAlloc_mpa);
           Makespan = Calculator.calculateMakespan( cloudletList, vmList,bestAlloc_mpa);
            
            RU_empa[i] += ru;  
            TH_empa[i] += throughput;  
            deg_empa[i] += degreeOfImbalance;
            MAK_empa[i] += Makespan;
            
            broker.getCloudletSubmittedList().clear();  
            broker.getCloudletReceivedList().clear();  
            cloudletList.clear(); 
            
            CloudSim.stopSimulation();
             
    //-------------End EMPA-----------------------------
    
    //-------------start HGHHC Algorithm--------------------
            System.out.println("------HGHHC Algorithm-----------------"+  "RUN  : " + (k+1)+ "iter : " + (i+1));
            CloudSim.init(num_user, calendar, trace_flag);  
            datacenter0 = createDatacenter("Datacenter_0");  
            broker = createBroker();  
            brokerId = broker.getId();
             vmList = createVM(brokerId, num_vm);
            broker.getVmList().clear();  
            broker.submitVmList(vmList);  
             cloudletList = new ArrayList<>(originalCloudletList.subList(0, cloudletCount));
            for (Cloudlet cloudlet : cloudletList) { 
                   CreatCloudlet.resetCloudlet(cloudlet);    
            } 
           broker.submitCloudletList(cloudletList);
           
           HGHHC hghhc = new HGHHC(pop, max_iter, cloudletList, vmList);
           int[] bestAlloc_hghhc = hghhc.execute_HGHHC();
           
            for (int j = 0; j < cloudletList.size(); j++) {  
                int vmId = (int) Math.round(bestAlloc_hghhc[j]);  
                broker.bindCloudletToVm(cloudletList.get(j).getCloudletId(), vmList.get(vmId).getId());  
            } 
            CloudSim.startSimulation();
             newList = broker.getCloudletReceivedList(); 
            
            ru = Calculator.calculateRUR(cloudletList,vmList,bestAlloc_hghhc); 
            throughput = Calculator.calculateThroughput(cloudletList,vmList,bestAlloc_hghhc);  
            degreeOfImbalance = Calculator.calculateDegreeOfIm( cloudletList, vmList,bestAlloc_hghhc);
            Makespan = Calculator.calculateMakespan( cloudletList, vmList,bestAlloc_hghhc);
           
            RU_hghhc[i] += ru;  
            TH_hghhc[i] += throughput;  
            deg_hghhc[i] += degreeOfImbalance;
            MAK_hghhc[i] += Makespan;
            
            broker.getCloudletSubmittedList().clear();  
            broker.getCloudletReceivedList().clear(); // پاکسازی لیست Cloudletهای دریافتی  
            cloudletList.clear(); 
            
            CloudSim.stopSimulation();
    
    //---------------- End HGHHC Algorithm----------------------
    
 
            System.out.println("------ IBWC -----------------"+  "RUN  : " + (k+1)+ "iter : " + (i+1));
            CloudSim.init(num_user, calendar, trace_flag);  
            datacenter0 = createDatacenter("Datacenter_0");  
            broker = createBroker();  
            brokerId = broker.getId();
            vmList = createVM(brokerId, num_vm);
            broker.getVmList().clear();  
            broker.submitVmList(vmList);  
             cloudletList = new ArrayList<>(originalCloudletList.subList(0, cloudletCount));
            for (Cloudlet cloudlet : cloudletList) {  // **تغییر وضعیت Cloudletها به STATUS_READY قبل از ارسال مجدد**
                   CreatCloudlet.resetCloudlet(cloudlet);    
            } 
           broker.submitCloudletList(cloudletList);
           
           IBWC ibwc = new IBWC(pop, max_iter, cloudletList, vmList);
           int[] bestAlloc_ibwc = ibwc.Execute_IBWC();
           
            for (int j = 0; j < cloudletList.size(); j++) {  
                int vmId = (int) Math.round(bestAlloc_ibwc[j]);  
                broker.bindCloudletToVm(cloudletList.get(j).getCloudletId(), vmList.get(vmId).getId());  
            } 
            CloudSim.startSimulation();
             newList = broker.getCloudletReceivedList(); 
            
            ru = Calculator.calculateRUR(cloudletList,vmList,bestAlloc_ibwc); 
            throughput = Calculator.calculateThroughput(cloudletList,vmList,bestAlloc_ibwc);  
            degreeOfImbalance = Calculator.calculateDegreeOfIm( cloudletList, vmList,bestAlloc_ibwc);
            Makespan = Calculator.calculateMakespan( cloudletList, vmList,bestAlloc_ibwc);
            
            RU_ibwc[i] += ru;  
            TH_ibwc[i] += throughput;  
            deg_ibwc[i] += degreeOfImbalance;
            MAK_ibwc[i] += Makespan;
            
            broker.getCloudletSubmittedList().clear();  
            broker.getCloudletReceivedList().clear(); // پاکسازی لیست Cloudletهای دریافتی  
            cloudletList.clear(); 
            
            CloudSim.stopSimulation();
    
    //--------- End IBWC--------------------------------------

            System.out.println("------TNNDE-----------------"+  "RUN  : " + (k+1)+ "iter : " + (i+1));
            CloudSim.init(num_user, calendar, trace_flag);  
            datacenter0 = createDatacenter("Datacenter_0");  
            broker = createBroker();  
            brokerId = broker.getId();
             vmList = createVM(brokerId, num_vm);
            broker.getVmList().clear();  
            broker.submitVmList(vmList);  
             cloudletList = new ArrayList<>(originalCloudletList.subList(0, cloudletCount));
            for (Cloudlet cloudlet : cloudletList) {  // **تغییر وضعیت Cloudletها به STATUS_READY قبل از ارسال مجدد**
                   CreatCloudlet.resetCloudlet(cloudlet);    
            } 
           broker.submitCloudletList(cloudletList);
           
           TNNDE tnnde = new TNNDE(pop, max_iter, cloudletList, vmList);
           int[] bestAlloc_tnnde = tnnde.execute_TLNND();
           
            for (int j = 0; j < cloudletList.size(); j++) {  
                int vmId = (int) Math.round(bestAlloc_tnnde[j]);  
                broker.bindCloudletToVm(cloudletList.get(j).getCloudletId(), vmList.get(vmId).getId());  
            } 
            CloudSim.startSimulation();
             newList = broker.getCloudletReceivedList(); 
            
            ru = Calculator.calculateRUR(cloudletList,vmList,bestAlloc_tnnde); 
            throughput = Calculator.calculateThroughput(cloudletList,vmList,bestAlloc_tnnde);  
            degreeOfImbalance = Calculator.calculateDegreeOfIm( cloudletList, vmList,bestAlloc_tnnde);
            Makespan = Calculator.calculateMakespan( cloudletList, vmList,bestAlloc_tnnde);
            
            RU_tnnde[i] += ru;  
            TH_tnnde[i] += throughput;  
            deg_tnnde[i] += degreeOfImbalance;
            MAK_tnnde[i] += Makespan;
            
            broker.getCloudletSubmittedList().clear();  
            broker.getCloudletReceivedList().clear(); 
            cloudletList.clear(); 
            
            CloudSim.stopSimulation();             
    //-------------End TNNDE-----------------------------
             
           cloudletCount+=100;
        }//for
        
    }
        average();
    //----------------print of Result-----------------------------------------------------------
        print.print("Resource utilization",cloudletCounts,RU_fpa,RU_msa,RU_empa,RU_hghhc,RU_ibwc, RU_tnnde );
        print.print("Makespan",cloudletCounts,MAK_fpa,MAK_msa,MAK_empa,MAK_hghhc,MAK_ibwc,MAK_tnnde);
        print.print("throughput",cloudletCounts,TH_fpa,TH_msa,TH_empa,TH_hghhc,TH_ibwc, TH_tnnde);
        print.print("Degree of imbalance",cloudletCounts,deg_fpa,deg_msa,deg_empa,deg_hghhc, deg_ibwc, deg_tnnde);
        
        
    //---------------------plot of Result----------------------------------------------------------------    
        plat.plot_column("Resource utilization(%)",cloudletCounts,RU_fpa,RU_msa,RU_empa,RU_hghhc,RU_ibwc, RU_tnnde );
        plat.plot_column("throughput",cloudletCounts,TH_fpa,TH_msa,TH_empa,TH_hghhc,TH_ibwc, TH_tnnde);
        plat.plot_column("Degree of imbalance",cloudletCounts,deg_fpa,deg_msa,deg_empa,deg_hghhc, deg_ibwc, deg_tnnde);
        plat.plot_column("Makespan(Seconds)",cloudletCounts,MAK_fpa,MAK_msa,MAK_empa,MAK_hghhc,MAK_ibwc,MAK_tnnde);
    }

    public  DatacenterBroker createBroker() {  
        DatacenterBroker broker = null;  
        try {  
            broker = new DatacenterBroker("Broker");  
        } catch (Exception e) {  
        }  
        return broker;  
    }  
    public  Datacenter createDatacenter(String name) throws Exception {  
         
        String arch = "x86";      // سیستم عامل  
        String os = "Linux";          // سیستم عامل  
        String vmm = "Xen";  
        double time_zone = 10.0;         // منطقه زمانی  
        double cost = 3.0;              // هزینه پردازش در واحد زمان  
        double costPerMem = 0.05;		// هزینه استفاده از حافظه در واحد زمان  
        double costPerStorage = 0.001;		// هزینه استفاده از فضای ذخیره سازی  
        double costPerBw = 0.0;			// هزینه استفاده از پهنای باند  
        LinkedList<Storage> storageList = new LinkedList<Storage>();

        // Create host list  
      List<Host> hostList = createHostList(5);  


     DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
            arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);
     
    Datacenter datacenter = null;  
    try {  
        datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);  
    } catch (Exception e) {  
    }  

    return datacenter;  
}  
    private static List<Host> createHostList(int numHosts) { 

        List<Host> hostList = new ArrayList<>();  

        for (int i = 0; i < numHosts; i++) {  
            List<Pe> peList = new ArrayList<>();  
            int mips = 6000; // MIPS (Million Instructions Per Second)
            for(int j=0; j<30; j++){
                peList.add(new Pe(j, new PeProvisionerSimple(mips))); // Single Core 
            }
             

            int ram = 8192; // Host memory (MB)  
            long storage = 1000000; // Host storage  
            int bw = 200000; // Bandwidth  

            hostList.add(  
                    new Host(  
                            i,  
                            new RamProvisionerSimple(ram),  
                            new BwProvisionerSimple(bw),  
                            storage,  
                            peList,  
                            new VmSchedulerTimeShared(peList)  
                    )  
            );  
        }  
        
        return hostList;  
    } 
    private static List<Vm> createVM(int brokerId, int numberOfVms) {  
        List<Vm> list = new ArrayList<>();  

        //int mips = 100 + (int)((1000-100) * Math.random());  
        long size = 10000; 
        int ram = 512;  
        int bw = 1000;  
        String vmm = "Xen";
        int v1= 0;
        int v2= 12;
        int v3= 13;

//        for (int i = 0; i < v1; i++) {  
//            Vm vm = new Vm(i, brokerId, 100, 1, ram, bw, size, vmm,
//                    new CloudletSchedulerTimeShared());  
//            list.add(vm);  
//        }  
        for (int i = 0; i < v2; i++) {  
            int id = v1 + i;
            Vm vm = new Vm(id, brokerId, 500, 1, ram, bw, size, vmm,
                    new CloudletSchedulerTimeShared());  
            list.add(vm);  
        } 
        for (int i = 0; i < v3; i++) { 
            int id = v1+v2 + i;
            Vm vm = new Vm(id, brokerId, 1000, 1, ram, bw, size, vmm,
                    new CloudletSchedulerTimeShared());  
            list.add(vm);  
        }  

        return list;  
    }  
    
     
    
     private  void average(){
         for(int i=0; i<N; i++){
            RU_fpa[i] = RU_fpa[i]/M;  
            TH_fpa[i] = TH_fpa[i]/M;  
            deg_fpa[i] = deg_fpa[i]/M;
            MAK_fpa[i] = MAK_fpa[i]/M;
           
            
            RU_msa[i] = RU_msa[i]/M;  
            TH_msa[i] = TH_msa[i]/M;  
            deg_msa[i] = deg_msa[i]/M;
            MAK_msa[i] = MAK_msa[i]/M;
           
            
            RU_empa[i] = RU_empa[i]/M;  
            TH_empa[i] = TH_empa[i]/M;  
            deg_empa[i] = deg_empa[i]/M;
            MAK_empa[i] = MAK_empa[i]/M;
        
            
            RU_tnnde[i] = RU_tnnde[i]/M;  
            TH_tnnde[i] = TH_tnnde[i]/M;  
            deg_tnnde[i] = deg_tnnde[i]/M;
            MAK_tnnde[i] = MAK_tnnde[i]/M;
           
            
            RU_hghhc[i] = RU_hghhc[i]/M;  
            TH_hghhc[i] = TH_hghhc[i]/M;  
            deg_hghhc[i] = deg_hghhc[i]/M;
            MAK_hghhc[i] = MAK_hghhc[i]/M;
           
            
            RU_ibwc[i] = RU_ibwc[i]/M;  
            TH_ibwc[i] = TH_ibwc[i]/M;  
            deg_ibwc[i] = deg_ibwc[i]/M;
            MAK_ibwc[i] = MAK_ibwc[i]/M;
           
            
         }
     }
    
    
}
