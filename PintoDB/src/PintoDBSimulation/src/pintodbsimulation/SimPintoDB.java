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
     * @param connectionModule 
     */
    public void setConnectionModule(Module connectionModule) {
        this.connectionModule = connectionModule;
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
     * @param processManagemnteModule 
     */
    public void setProcessManagemnteModule(Module processManagemnteModule) {
        this.processManagemnteModule = processManagemnteModule;
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
     * @param queryProcessorModule 
     */
    public void setQueryProcessorModule(Module queryProcessorModule) {
        this.queryProcessorModule = queryProcessorModule;
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
     * @param ExecutionModule 
     */
    public void setExecutionModule(Module ExecutionModule) {
        this.ExecutionModule = ExecutionModule;
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
     * @param TransactionModule 
     */
    public void setTransactionModule(Module TransactionModule) {
        this.TransactionModule = TransactionModule;
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
     * @param clients 
     */
    public void setTransactionModule(LinkedList<ClientQuery> clients) {
        this.clients = clients;
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
     * @param simClock 
     */
    public void setSimClock(double simClock) {
        this.simClock = simClock;
    }

    /**
     * 
     * @return 
     */
    double getmaxSimClock() {
        return maxSimClock;
    }

    /**
     * 
     * @param maxSimClock 
     */
    public void setMaxSimClock(double maxSimClock) {
        this.maxSimClock = maxSimClock;
    }

    /**
     * 
     * @return 
     */
    int gettimeToRunSimulation() {
        return timeToRunSimulation;
    }

    /**
     * 
     * @param timeToRunSimulation 
     */
    public void setTimeToRunSimulation(int timeToRunSimulation) {
        this.timeToRunSimulation = timeToRunSimulation;
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
