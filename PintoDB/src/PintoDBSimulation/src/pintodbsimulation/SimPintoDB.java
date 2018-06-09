package pintodbsimulation;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class SimPintoDB {
    
    private int timeToRunSimulation;
    private double simClock;
    private double maxSimClock;
    private int k;
    private int n;
    private int m;
    private int p;
    private double t;
    private LinkedList<ClientQuery> clients;
    private Module connectionModule;
    private Module processManagemnteModule;
    private Module queryProcessorModule;
    private Module ExecutionModule;
    private Module TransactionModule;

    /**
     * 
     * @return 
     */
    public Module getConnectionModule() {
        return connectionModule;
    }
    
    /**
     * 
     * @return 
     */
    public Module getProcessManagemnteModule() {
        return processManagemnteModule;
    }
    
    /**
     * 
     * @return 
     */
    public Module getQueryProcessorModule() {
        return queryProcessorModule;
    }

    /**
     * 
     * @return 
     */
    public Module getExecutionModule() {
        return ExecutionModule;
    }

    /**
     * 
     * @return 
     */
    public Module getTransactionModule() {
        return TransactionModule;
    }       
    
    /**
     * 
     * @return 
     */        
    public LinkedList<ClientQuery> getClients() {
        return clients;
    }        

    /**
     * 
     * @return 
     */
    PriorityQueue<Event> getSistemEventList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @return 
     */
    double getSimClock() {
        return simClock;
    }
    
    /**
     *
     */
    public void run() {
        
    }

    double getT() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
