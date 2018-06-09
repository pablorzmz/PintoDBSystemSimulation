package pintodbsimulation;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class SimPintoDB {
    
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
