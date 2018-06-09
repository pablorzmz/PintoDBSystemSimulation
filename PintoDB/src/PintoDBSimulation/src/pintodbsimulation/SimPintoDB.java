package pintodbsimulation;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class SimPintoDB {
    
    private LinkedList<ClientQuery> clients;

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
