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
    private PriorityQueue<Event> systemEventList;
    private Statistics stats;
    private Module connectionModule;
    private Module processManagemnteModule;
    private Module queryProcessorModule;
    private Module ExecutionModule;
    private Module TransactionModule;
    
     /**
     *
     */
    public SimPintoDB() {
    }

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
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return this.systemEventList;
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
        // Values for debugging
        this.k = 3;
        this.m = 3;
        this.n = 3;
        this.p = 3;
        this.t = 7.0;
        this.maxSimClock = 13.0;
        // Construct client list
        this.clients = new LinkedList<>();
        // Construct modules
        this.ExecutionModule = new ExecutionModule(0, m, this, null );        
        this.TransactionModule = new TransactionAndDiskModule(0, p, this, ExecutionModule );
        this.queryProcessorModule = new QueryProcessorModule(0, n, this, TransactionModule);
        this.processManagemnteModule = new ProcessManagmentModule(0, 1, this,  queryProcessorModule );
        this.connectionModule = new ConnectionModule(0, k, this, processManagemnteModule );    
        this.ExecutionModule.setNextModule( connectionModule );
        
        //Construct event list
        EventComparator e = new EventComparator();
        this.systemEventList = new PriorityQueue<>( e );
        
        // Initialize simulation
        RandomNumberGenerator r = new RandomNumberGenerator();
        ClientQuery firstOne = new ClientQuery( r.getConnectionStatementType(),connectionModule );
        Event firstEvent = new Event( firstOne, SimEvent.ARRIVE, connectionModule , 0.0 );
        this.systemEventList.add( firstEvent );
        this.simClock = 0;
                        
        
        while ( simClock < maxSimClock )
        {
            Event currentEvent = this.systemEventList.peek();            
            Module currentMod = currentEvent.getMod();
            simClock = currentEvent.getClockTime();
            System.out.println("Current clock time: " + simClock );
            System.out.println("Event: " + currentEvent.getEventType() );
            
            switch ( currentEvent.getEventType() )
            {
                case ARRIVE:
                    currentMod.processArrive();
                break;
                case LEAVE:
                    currentMod.processExit();
                break;
                case TIMEOUT:
                    currentMod.processTimeOut();
                break;
            }
        }
        // print stats
        stats = new Statistics( this );
        stats.generateStatistics();
        stats.generateFinalStatistics();
        IterationStatistics f = stats.getFinalIterationStats();
        f.printValues();
        
        
        
    }

    double getT() {
        return this.t;
    }
}
