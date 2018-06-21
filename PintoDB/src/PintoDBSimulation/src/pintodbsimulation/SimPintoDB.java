package pintodbsimulation;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author b65477@ecci.ucr.ac.cr
 */
public class SimPintoDB extends Thread {

    private int timesToRunSimulation;
    private double simClock;
    private double maxSimClock;
    private int k;
    private int n;
    private int m;
    private int p;
    private double t;
    private LinkedList<ClientQuery> clients;
    private final PriorityQueue<Event> systemEventList;//*
    private final Statistics stats;//*
    private Module connectionModule;
    private Module processManagemnteModule;
    private Module queryProcessorModule;
    private Module executionModule;
    private Module transactionModule;
    private final MainForm interFace;

    /**
     *
     */
    public final static int SLEEP_TIME = 750;

    /**
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
     *
     * @return
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
        pM = this.processManagemnteModule.getQueryQueue().size();
        qM = this.queryProcessorModule.getQueryQueue().size();
        tM = this.transactionModule.getQueryPriorityQueue().size();
        eM = this.executionModule.getQueryQueue().size();
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
    public void setConnectionModule( Module connectionModule ) {
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
    public void setProcessManagemnteModule( Module processManagemnteModule ) {
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
    public void setQueryProcessorModule( Module queryProcessorModule ) {
        this.queryProcessorModule = queryProcessorModule;
    }

    /**
     *
     * @return
     */
    public Module getExecutionModule() {
        return executionModule;
    }

    /**
     *
     * @param ExecutionModule
     */
    public void setExecutionModule( Module ExecutionModule ) {
        this.executionModule = ExecutionModule;
    }

    /**
     *
     * @return
     */
    public Module getTransactionModule() {
        return transactionModule;
    }

    /**
     *
     * @param TransactionModule
     */
    public void setTransactionModule( Module TransactionModule ) {
        this.transactionModule = TransactionModule;
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
    public void setTransactionModule( LinkedList<ClientQuery> clients ) {
        this.clients = clients;
    }

    /**
     *
     * @return
     */
    PriorityQueue<Event> getSistemEventList() {        
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
    public void setSimClock( double simClock ) {
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
    public void setMaxSimClock( double maxSimClock ) {
        this.maxSimClock = maxSimClock;
    }

    /**
     *
     * @return
     */
    int gettimesToRunSimulation() {
        return timesToRunSimulation;
    }

    /**
     *
     * @param timesToRunSimulation
     */
    public void setTimesToRunSimulation( int timesToRunSimulation ) {
        this.timesToRunSimulation = timesToRunSimulation;
    }

    /**
     *
     * @return
     */
    public MainForm getInterFace() {
        return interFace;
    }

    /**
     *
     * @return
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
