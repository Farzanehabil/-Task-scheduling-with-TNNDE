/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mainproject;

/**
 *
 * @author farzaneh
 */
public class Main {

        static int pop = 100;
        static int Maxiter = 500;
        static int vm = 25;
        static int num_task = 1000;
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args)throws Exception {
        
//            Runsched_syntific dd = new Runsched_syntific(pop,Maxiter,vm,num_task);
//            dd.Execute();
            
            Runsched_HPC2N hpc2n = new Runsched_HPC2N(pop,Maxiter,vm,num_task);
            hpc2n.Execute();
            
//            Runsched_GOCJ GOCJ = new Runsched_GOCJ(pop,Maxiter,vm,num_task);
//            GOCJ.Execute();

//              Runsched_VM_HPC2N vm_hp = new Runsched_VM_HPC2N(pop,Maxiter, 20 ,num_task);
//              vm_hp.Execute();
//            
    }
    
}
