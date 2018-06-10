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
    public final static int sleepTime = 1000;

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
        this.k = 30;
        this.m = 1;
        this.n = 1;
        this.p = 1;
        this.t = 2.0;
        this.maxSimClock = 20.0;
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
        RandomNumberGenerator r = new RandomNumberGenerator();
        ClientQuery firstOne = new ClientQuery(r.getConnectionStatementType(), connectionModule);
        Event firstEvent = new Event(firstOne, SimEvent.ARRIVE, connectionModule, 0.0);
        this.systemEventList.add(firstEvent);
        this.simClock = 0;

        while (simClock < maxSimClock) {
            Event currentEvent = this.systemEventList.peek();
            Module currentMod = currentEvent.getMod();
            simClock = currentEvent.getClockTime();
            System.out.println("Current clock time: " + simClock);
            System.out.println("Next Event: " + currentEvent.getEventType());

            switch (currentEvent.getEventType()) {
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
            System.out.println();

            //I need to check if there are clients waiting that already have a timeout
            //and if there are, I need to generate their timeout
            checkGenerateTimeout();
        }
        // print stats
        stats = new Statistics(this);
        stats.generateStatistics();
        stats.generateFinalStatistics();
        IterationStatistics f = stats.getFinalIterationStats();
        f.printValues();
    }

    double getT() {
        return this.t;
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
            this.systemEventList.add(e);
            e = new Event(cQ, SimEvent.TIMEOUT, this.connectionModule, simClock);
            this.systemEventList.add(e);
        }
    }
}
