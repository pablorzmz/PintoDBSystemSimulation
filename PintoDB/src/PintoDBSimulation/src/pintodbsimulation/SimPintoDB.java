package pintodbsimulation;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class SimPintoDB {

    private int timesToRunSimulation;
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
    private Module executionModule;
    private Module transactionModule;
    private final MainForm interFace;
    public final static int sleepTime = 0;
    public static final String YELLOW = "\033[0;33m";
    public static final String GREEN = "\033[0;32m";
    public static final String RESET = "\033[0m";
    public static final String BLUE = "\033[0;34m";
    public static final String RED = "\033[0;31m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";

    /**
     *
     */
    public SimPintoDB(MainForm intf) {
        this.interFace = intf;
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
        return executionModule;
    }

    /**
     *
     * @param ExecutionModule
     */
    public void setExecutionModule(Module ExecutionModule) {
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
    public void setTransactionModule(Module TransactionModule) {
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
        return timesToRunSimulation;
    }

    /**
     *
     * @param timesToRunSimulation
     */
    public void setTimesToRunSimulation(int timesToRunSimulation) {
        this.timesToRunSimulation = timesToRunSimulation;
    }

    /**
     *
     */
    public void run() {
        // Values for debugging
        this.k = 10;
        this.m = 5;
        this.n = 5;
        this.p = 5;
        this.t = 60.0;
        this.maxSimClock = 60.0;
        this.timesToRunSimulation = 1;
        // Construct client list
        this.clients = new LinkedList<>();
        // Construct modules
        this.executionModule = new ExecutionModule(0, m, this, null);
        this.transactionModule = new TransactionAndDiskModule(0, p, this, executionModule);
        this.queryProcessorModule = new QueryProcessorModule(0, n, this, transactionModule);
        this.processManagemnteModule = new ProcessManagmentModule(0, 1, this, queryProcessorModule);
        this.connectionModule = new ConnectionModule(0, k, this, processManagemnteModule);
        this.executionModule.setNextModule(connectionModule);

        //Construct event list
        EventComparator e = new EventComparator();
        this.systemEventList = new PriorityQueue<>(e);

        // Initialize simulation
        initializeSimCicle();        
        // descomentar esto para probar un modulo especifico y comentar la parte de arriba entre los @
        //this.forTestingAModule( transactionModule );        
        //stats
        stats = new Statistics(this);

        for (int times = 0; times < timesToRunSimulation; ++times) {
            while (simClock < maxSimClock && systemEventList.size() > 0) {
                Event currentEvent = this.systemEventList.peek();
                Module currentMod = currentEvent.getMod();
                simClock = currentEvent.getClockTime();
                System.out.println("Current clock time: " + simClock);

                switch (currentEvent.getEventType()) {
                    case ARRIVE:                                                
                        System.out.println(YELLOW + "Next Event: " + currentEvent.getEventType() + RESET);
                        currentMod.processArrive();
                        break;
                    case LEAVE:
                        System.out.println(GREEN + "Next Event: " + currentEvent.getEventType() + RESET);
                        currentMod.processExit();
                        break;
                    case TIMEOUT:
                        currentMod.processTimeOut();
                        break;
                }
                System.out.println();

                //I need to check if there are clients waiting that already have a timeout
                //and if there are, I need to generate their timeout
                checkGenerateTimeout();
            }
            // calculate stats            
            stats.generateStatistics();
            // clear everything
            systemEventList.clear();
            clients.clear();
            connectionModule.clear();
            processManagemnteModule.clear();
            queryProcessorModule.clear();
            transactionModule.clear();
            executionModule.clear();
            initializeSimCicle();
        }
        stats.generateFinalStatistics();
        IterationStatistics f = stats.getFinalIterationStats();
        f.printValues();
    }

    double getT() {
        return this.t;
    }

    private void initializeSimCicle() {
        RandomNumberGenerator r = new RandomNumberGenerator();
        ClientQuery firstOne = new ClientQuery(r.getConnectionStatementType(), connectionModule);
        Event firstEvent = new Event(firstOne, SimEvent.ARRIVE, connectionModule, 0.0);
        this.systemEventList.add(firstEvent);
        this.simClock = 0;
    }

    /**
     * ******************* METODOS PARA TESTEAR UN MÓDULO
     * **********************************
     */
    private void forTestingAModule(Module m) {
        // add events to module
        ClientQuery c;
        Event e;

        c = new ClientQuery(StatementType.DDL, m);
        c.clientID = 0;
        c.getQueryStatistics().setSystemArriveTime(0.0);
        e = new Event(c, SimEvent.ARRIVE, m, 0.0);
        systemEventList.add(e);

        c = new ClientQuery(StatementType.UPDATE, m);
        c.clientID = 1;
        c.getQueryStatistics().setSystemArriveTime(0.01);
        e = new Event(c, SimEvent.ARRIVE, m, 0.01);
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

    private void checkGenerateTimeout() {
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
            System.out.println("");
            System.out.println(BLUE + "Generate Queue TimeOut : El cliente: " + cQ.clientID + " fue sacado de ser antendido"
                    + " por estar en cola en el modulo " + cQ.getCurrentMod().getClass().getSimpleName()
                    + " porque ya lleva en el sistema " + (simClock - cQ.getQueryStatistics().getSystemArriveTime()) + " > " + this.t + RESET);
            System.out.println("");
            try {
                // thread to sleep for 1000 milliseconds
                Thread.sleep(SimPintoDB.sleepTime);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
}
