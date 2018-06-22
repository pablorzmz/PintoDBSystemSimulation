package pintodbsimulation;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class that is responsible for handling the simulation
 *
 * @author b65477@ecci.ucr.ac.cr
 */
public class SimPintoDB extends Thread {

    //Members declaration block.
    /**
     * Times to run simulation.
     */
    private int timesToRunSimulation;
    
    /**
     * Simulation clock.
     */
    private double simClock;
    
    /**
     * Time the simulation will run.
     */
    private double maxSimClock;
    
    /**
     * Number of concurrent connections that the system can handle.
     */
    private int k;
    
    /**
     * Number of processes available for processing queries concurrent 
     * that the system can handle.
     */
    private int n;
    
    /**
     * Number of processes available to execute queries.
     */
    private int m;
    
    /**
     * Number of processes available for the execution of transactions.
     */
    private int p;
    
    /**
     * Number of seconds of timeout of the connections.
     */
    private double t;
    
    /**
     * Queue for the clients waiting to be attended.
     */
    private LinkedList<ClientQuery> clients;
    
    /**
     * Queue for the system event list.
     */
    private final PriorityQueue<Event> systemEventList;//*
    
    /**
     * Pointer to the Statistics class.
     */
    private final Statistics stats;//*
    
    /**
     * Instance of the connection module in the execution of the simulation.
     */
    private Module connectionModule;
    
    /**
     * Instance of the process management module in the execution of the simulation.
     */
    private Module processManagemnteModule;
    
    /**
     * Instance of the query processor module in the execution of the simulation.
     */
    private Module queryProcessorModule;
    
    /**
     * Instance of the execution module in the execution of the simulation.
     */
    private Module executionModule;
    
    /**
     * Instance of the transaction module in the execution of the simulation.
     */
    private Module transactionModule;
    
    /**
     * Pointer to the MainForm class.
     */
    private final MainForm interFace;

    /**
     *Assignment of sleep time
     */
    public final static int SLEEP_TIME = 750;

    /**
     *Set the setSimParams fields to the value (int, int, int, int, int, double, double) past as argument.
     * 
     * @param k
     * @param m
     * @param n
     * @param p
     * @param timesToRunSim
     * @param maxTimeToRunSim
     * @param t
     */
    public void setSimParams(int k, int m, int n, int p, int timesToRunSim, double maxTimeToRunSim, double t) {
        this.k = k;
        this.m = m;
        this.n = n;
        this.t = t;
        this.p = p;
        this.maxSimClock = maxTimeToRunSim;
        this.timesToRunSimulation = timesToRunSim;
    }

    /**
     * Returns the pointer to the class Statistics.
     *
     * @return stats pointer
     */
    public Statistics getStats() {
        return stats;
    }

    /**
     *
     * @param intf
     */
    public SimPintoDB(MainForm intf) {
        //pointer to interface
        this.interFace = intf;

        // list of clients and stats
        this.clients = new LinkedList<>();
        stats = new Statistics(this);

        // Construct modules
        this.executionModule = new ExecutionModule(0, 1, this, null);
        this.transactionModule = new TransactionAndDiskModule(0, 1, this, executionModule);
        this.queryProcessorModule = new QueryProcessorModule(0, 1, this, transactionModule);
        this.processManagemnteModule = new ProcessManagmentModule(0, 1, this, queryProcessorModule);
        this.connectionModule = new ConnectionModule(0, 1, this, processManagemnteModule);
        this.executionModule.setNextModule(connectionModule);

        //Construct event list
        EventComparator e = new EventComparator();
        this.systemEventList = new PriorityQueue<>(e);
    }

    /**
     *
     */
    @Override
    public void run() {
        // Initialize simulation
        initializeSimCicle();
        // descomentar esto para probar un modulo especifico y comentar la parte de arriba entre los @
        //this.forTestingAModule( transactionModule );        
        //stats

        for (int times = 0; times < timesToRunSimulation && !interFace.stopSimulation; ++times) {
            while (simClock < maxSimClock && systemEventList.size() > 0 && !interFace.stopSimulation) {
                Event currentEvent = this.systemEventList.peek();
                ClientQuery currentCQ = currentEvent.getClientQuery();
                if (!currentCQ.getFinishService()) { //If the current client haven't finnished it's service already
                    Module currentMod = currentEvent.getMod();
                    if (currentMod == null) {
                        currentMod = currentCQ.getCurrentMod();
                    }
                    simClock = currentEvent.getClockTime();

                    refreshInterfaceInfo(currentEvent);

                    switch (currentEvent.getEventType()) {
                        case ARRIVE:                          
                            currentMod.processArrive();
                            break;
                        case LEAVE:                            
                            currentMod.processExit();
                            break;
                        case TIMEOUT:
                            stats.getCurrentIterationStats().increaseTimeOutsCounter();
                            currentMod.processTimeOut();
                            break;
                    }
                    this.interFace.refreshConsoleAreaContent("");

                    //We need to check if there are clients waiting that already have a timeout
                    //and if there are, I need to generate their timeout
                    //checkGenerateTimeout();
                    /*checkGenerateTimeout( this.connectionModule );
                checkGenerateTimeout( this.processManagemnteModule );
                checkGenerateTimeout( this.queryProcessorModule );
                checkGenerateTimeout( this.transactionModule );
                checkGenerateTimeout( this.executionModule );*/
                    if ( interFace.sleepMode == true ) {
                        try {
                            Thread.sleep( SLEEP_TIME );
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SimPintoDB.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {                    
                    this.systemEventList.poll();
                }
            }
            // calculate stats            
            stats.generateStatistics();
            this.interFace.refreshIterationStats("Results for iteration number: " + (int) (times + 1));
            this.interFace.refreshIterationStats(stats.getCurrentIterationStats().resultStats( false ));
            // clear everything
            systemEventList.clear();
            clients.clear();
            connectionModule.clear();
            ((ConnectionModule) connectionModule).setDeniedConnectionCounter(0);
            processManagemnteModule.clear();
            queryProcessorModule.clear();
            transactionModule.clear();
            executionModule.clear();
            initializeSimCicle();
        }
        if (!interFace.stopSimulation) {
            stats.generateFinalStatistics();
            this.interFace.refreshFinalIterationStats( "Final results" );
            this.interFace.refreshFinalIterationStats( stats.getFinalIterationStats().resultStats( true ) );
            this.interFace.refreshFinalIterationStats( stats.getConfidentInterval() );
        }
        interFace.activeRunButton(true);
        interFace.activeTextFiles(true);
    }

    private void refreshInterfaceInfo(Event currentEvent) {
        int pM, qM, tM, eM;
        this.interFace.refreshClockTime( simClock );
        this.interFace.refreshDeniendConnection( ((ConnectionModule) connectionModule).getDeniedConnectionCounter() );
        this.interFace.refreshConsoleAreaContent( "Current clock time: " + simClock );
        this.interFace.refreshConsoleAreaContent( "Next Event: " + currentEvent.getEventType() );
        pM = this.processManagemnteModule.getMyQueue().size();
        qM = this.queryProcessorModule.getMyQueue().size();
        tM = this.transactionModule.getMyQueue().size();
        eM = this.executionModule.getMyQueue().size();
        this.interFace.refresQueueSizesPerModule( pM, qM, tM, eM );
    }

    /**
     *
     */
    private void initializeSimCicle() {
        this.connectionModule.setMaxServers( k );
        this.executionModule.setMaxServers( m );
        this.transactionModule.setMaxServers( p );
        this.queryProcessorModule.setMaxServers( n );
        this.processManagemnteModule.setMaxServers( 1 );

        RandomNumberGenerator r = new RandomNumberGenerator();
        ClientQuery firstOne = new ClientQuery( r.getConnectionStatementType(), connectionModule );
        Event firstEvent = new Event( firstOne, SimEvent.ARRIVE, connectionModule, 0.0 );
        this.systemEventList.add( firstEvent );
        this.simClock = 0;
    }

    /**
     *
     */
    /*private void checkGenerateTimeout() {
        //I need to check if there are client with timeout on all the modules queues
        LinkedList<ClientQuery> timeoutCQ = new LinkedList<>();
        Queue<ClientQuery> moduleQ;
        int queueSize;
        ClientQuery cQ;
        Object moduleQA[];

        //ProcessManagmenteModule
        moduleQ = this.processManagemnteModule.getQueryQueue();
        queueSize = moduleQ.size();
        moduleQA = this.processManagemnteModule.getQueryQueue().toArray();
        for (int i = 0; i < queueSize; ++i) {
            cQ = (ClientQuery) moduleQA[i];
            if (simClock - cQ.getQueryStatistics().getSystemArriveTime() >= t) { //If the cliente already have a timeout
                timeoutCQ.add(cQ);
            }
        }
        //QueryProcessorModule
        moduleQ = this.queryProcessorModule.getQueryQueue();
        queueSize = moduleQ.size();
        moduleQA = moduleQ.toArray();
        for (int i = 0; i < queueSize; ++i) {
            cQ = (ClientQuery) moduleQA[i];
            if (simClock - cQ.getQueryStatistics().getSystemArriveTime() >= t) { //If the cliente already have a timeout
                timeoutCQ.add(cQ);
            }
        }
        //ExecutionModule
        moduleQ = this.executionModule.getQueryQueue();
        queueSize = moduleQ.size();
        moduleQA = moduleQ.toArray();
        for (int i = 0; i < queueSize; ++i) {
            cQ = (ClientQuery) moduleQA[i];
            if (simClock - cQ.getQueryStatistics().getSystemArriveTime() >= t) { //If the cliente already have a timeout
                timeoutCQ.add(cQ);
            }
        }
        //TransactionAndDiskModule
        moduleQ = this.transactionModule.getPriorityQueryQueue();
        queueSize = moduleQ.size();
        moduleQA = moduleQ.toArray();
        for (int i = 0; i < queueSize; ++i) {
            cQ = (ClientQuery) moduleQA[i];
            if (simClock - cQ.getQueryStatistics().getSystemArriveTime() >= t) { //If the cliente already have a timeout
                timeoutCQ.add(cQ);
            }
        }
        //I need to generate the timeout event for each cliente with time out
        queueSize = timeoutCQ.size();
        for (int i = 0; i < queueSize; ++i) {
            cQ = timeoutCQ.get(i);
            Event e = new Event(cQ, SimEvent.TIMEOUT, cQ.getCurrentMod(), simClock);
            // this timeout will be for queue
            e.setQueueTimeOut(true);
            this.systemEventList.add(e);
            e = new Event(cQ, SimEvent.TIMEOUT, this.connectionModule, simClock);
            this.systemEventList.add(e);
            this.interFace.refreshConsoleAreaContent("Generate Queue TimeOut : El cliente: " + cQ.clientID + " fue sacado de ser antendido"
                    + " por estar en cola en el modulo " + cQ.getCurrentMod().getClass().getSimpleName()
                    + " porque ya lleva en el sistema " + (simClock - cQ.getQueryStatistics().getSystemArriveTime()) + " > " + this.t);

        }
    }*/
 /*private void checkGenerateTimeout(Module m) {
        if (m != this.connectionModule) {
            //I need to check if there are client with timeout on all the modules queues
            LinkedList<ClientQuery> timeoutCQ = new LinkedList<>();
            Queue<ClientQuery> moduleQ;
            int queueSize = 0;
            ClientQuery cQ;
            Object moduleQA[];
            if (m.getClass().getSimpleName().equals(this.transactionModule.getClass().getSimpleName())) {
                moduleQ = m.getPriorityQueryQueue();
            } else {
                moduleQ = m.getQueryQueue();
            }
            queueSize = moduleQ.size();
            moduleQA = moduleQ.toArray();
            for (int i = 0; i < queueSize; ++i) {
                cQ = (ClientQuery) moduleQA[i];
                if (simClock - cQ.getQueryStatistics().getSystemArriveTime() >= t) { //If the cliente already have a timeout
                    timeoutCQ.add(cQ);
                }
            }
            //I need to generate the timeout event for each cliente with time out
            queueSize = timeoutCQ.size();
            for (int i = 0; i < queueSize; ++i) {
                cQ = timeoutCQ.get(i);
                Event e = new Event(cQ, SimEvent.TIMEOUT, cQ.getCurrentMod(), simClock);
                // this timeout will be for queue
                e.setQueueTimeOut(true);
                this.systemEventList.add(e);
                e = new Event(cQ, SimEvent.TIMEOUT, this.connectionModule, simClock);
                this.systemEventList.add(e);
                this.interFace.refreshConsoleAreaContent("Generate Queue TimeOut : El cliente: " + cQ.clientID + " fue sacado de ser antendido"
                        + " por estar en cola en el modulo " + cQ.getCurrentMod().getClass().getSimpleName()
                        + " porque ya lleva en el sistema " + (simClock - cQ.getQueryStatistics().getSystemArriveTime()) + " > " + this.t);

            }
        }
    }*/
    /**
     *Returns the module connectionModule
     * 
     * @return connectionModule module
     */
    public Module getConnectionModule() {
        return connectionModule;
    }

    /**
     *Set the connectionModule field to the value(Module) past as argument.
     * 
     * @param connectionModule
     */
    public void setConnectionModule( Module connectionModule ) {
        this.connectionModule = connectionModule;
    }

    /**
     *Returns the module processManagemnteModule
     * 
     * @return processManagemnteModule module
     */
    public Module getProcessManagemnteModule() {
        return processManagemnteModule;
    }

    /**
     *Set the processManagemnteModule field to the value(Module) past as argument.
     * 
     * @param processManagemnteModule
     */
    public void setProcessManagemnteModule( Module processManagemnteModule ) {
        this.processManagemnteModule = processManagemnteModule;
    }

    /**
     *Returns the module queryProcessorModule
     * 
     * @return queryProcessorModule module
     */
    public Module getQueryProcessorModule() {
        return queryProcessorModule;
    }

    /**
     *Set the queryProcessorModule field to the value(Module) past as argument.
     * 
     * @param queryProcessorModule
     */
    public void setQueryProcessorModule( Module queryProcessorModule ) {
        this.queryProcessorModule = queryProcessorModule;
    }

    /**
     *Returns the module executionModule
     * 
     * @return executionModule module
     */
    public Module getExecutionModule() {
        return executionModule;
    }

    /**
     *Set the ExecutionModul field to the value(Module) past as argument.
     * 
     * @param ExecutionModule
     */
    public void setExecutionModule( Module ExecutionModule ) {
        this.executionModule = ExecutionModule;
    }

    /**
     *Returns the module transactionModule
     * 
     * @return transactionModule module
     */
    public Module getTransactionModule() {
        return transactionModule;
    }

    /**
     *Set the TransactionModule field to the value(Module) past as argument.
     * 
     * @param TransactionModule
     */
    public void setTransactionModule( Module TransactionModule ) {
        this.transactionModule = TransactionModule;
    }

    /**
     *Returns the {@code LinkedList<ClientQuery> } clients
     * 
     * @return clients LinkedList
     */
    public LinkedList<ClientQuery> getClients() {
        return clients;
    }

    /**
     *Set the clients field to the value({@code LinkedList<ClientQuery>}) past as argument.
     * 
     * @param clients
     */
    public void setTransactionModule( LinkedList<ClientQuery> clients ) {
        this.clients = clients;
    }

    /**
     *Returns this {@code PriorityQueue<Event>} systemEventList
     * 
     * @return systemEventList PriorityQueue
     */
    PriorityQueue<Event> getSistemEventList() {        
        return this.systemEventList;
    }

    /**
     *Returns simulation clock simClock
     * 
     * @return simClock double
     */
    double getSimClock() {
        return simClock;
    }

    /**
     *Set the simClock field to the value(double) past as argument.
     * 
     * @param simClock
     */
    public void setSimClock( double simClock ) {
        this.simClock = simClock;
    }

    /**
     *Returns time the simulation will run maxSimClock
     * 
     * @return maxSimClock double
     */
    double getmaxSimClock() {
        return maxSimClock;
    }

    /**
     *Set the maxSimClock field to the value(double) past as argument.
     * 
     * @param maxSimClock
     */
    public void setMaxSimClock( double maxSimClock ) {
        this.maxSimClock = maxSimClock;
    }

    /**
     *Returns times to run simulation timesToRunSimulation
     * 
     * @return timesToRunSimulation int
     */
    int gettimesToRunSimulation() {
        return timesToRunSimulation;
    }

    /**
     *Set the timesToRunSimulation field to the value(int) past as argument.
     * 
     * @param timesToRunSimulation
     */
    public void setTimesToRunSimulation( int timesToRunSimulation ) {
        this.timesToRunSimulation = timesToRunSimulation;
    }

    /**
     *Returns pointer to the MainForm class interFace
     * 
     * @return interFace MainForm
     */
    public MainForm getInterFace() {
        return interFace;
    }

    /**
     *Returns the number of seconds of timeout of the connections
     * 
     * @return t double
     */
    public double getT() {
        return this.t;
    }

    //Testing use only
    private void forTestingAModule( Module m ) {
        // add events to module
        ClientQuery c;
        Event e;

        c = new ClientQuery(StatementType.DDL, m);
        c.clientID = 0;
        c.getQueryStatistics().setSystemArriveTime(0.01);
        e = new Event(c, SimEvent.ARRIVE, m, 0.01);
        systemEventList.add(e);

        c = new ClientQuery(StatementType.DDL, m);
        c.clientID = 1;
        c.getQueryStatistics().setSystemArriveTime(0.02);
        e = new Event(c, SimEvent.ARRIVE, m, 0.02);
        systemEventList.add(e);

        c = new ClientQuery(StatementType.UPDATE, m);
        c.clientID = 2;
        c.getQueryStatistics().setSystemArriveTime(0.02);
        e = new Event(c, SimEvent.ARRIVE, m, 0.02);
        systemEventList.add(e);

        c = new ClientQuery(StatementType.DDL, m);
        c.clientID = 3;
        c.getQueryStatistics().setSystemArriveTime(0.03);
        e = new Event(c, SimEvent.ARRIVE, m, 0.03);
        systemEventList.add(e);

        c = new ClientQuery(StatementType.JOIN, m);
        c.clientID = 4;
        c.getQueryStatistics().setSystemArriveTime(0.04);
        e = new Event(c, SimEvent.ARRIVE, m, 0.04);
        systemEventList.add(e);

    }
}
