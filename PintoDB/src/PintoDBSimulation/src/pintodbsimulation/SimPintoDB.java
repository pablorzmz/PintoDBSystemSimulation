package pintodbsimulation;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class that is responsible for handling the simulation
 *
 * @author B65477 B65728 B55830
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
     * Number of processes available for processing queries concurrent that the
     * system can handle.
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
     * Instance of the process management module in the execution of the
     * simulation.
     */
    private Module processManagemnteModule;

    /**
     * Instance of the query processor module in the execution of the
     * simulation.
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
     * Assignment of sleep time
     */
    public final static int SLEEP_TIME = 750;

    /**
     * Set the setSimParams fields to the value (int, int, int, int, int,
     * double, double) past as argument.
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
     * Class constructor.
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
     * Run the simulation with the parameters that the user indicated through
     * the UI
     */
    @Override
    public void run() {
        // Initialize simulation
        initializeSimCicle();

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

                    if (interFace.sleepMode == true) {
                        try {
                            Thread.sleep(SLEEP_TIME);
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
            this.interFace.refreshIterationStats(stats.getCurrentIterationStats().resultStats(false));
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
            this.interFace.refreshFinalIterationStats("Final results");
            this.interFace.refreshFinalIterationStats(stats.getFinalIterationStats().resultStats(true));
            this.interFace.refreshFinalIterationStats(stats.getConfidentInterval());
        }
        interFace.activeRunButton(true);
        interFace.activeTextFiles(true);
    }

    /**
     * Update the user interface data.
     */
    private void refreshInterfaceInfo(Event currentEvent) {
        int pM, qM, tM, eM;
        this.interFace.refreshClockTime(simClock);
        this.interFace.refreshDeniendConnection(((ConnectionModule) connectionModule).getDeniedConnectionCounter());
        this.interFace.refreshConsoleAreaContent("Current clock time: " + simClock);
        this.interFace.refreshConsoleAreaContent("Next Event: " + currentEvent.getEventType());
        pM = this.processManagemnteModule.getMyQueue().size();
        qM = this.queryProcessorModule.getMyQueue().size();
        tM = this.transactionModule.getMyQueue().size();
        eM = this.executionModule.getMyQueue().size();
        this.interFace.refresQueueSizesPerModule(pM, qM, tM, eM);
    }

    /**
     * Set the simulation fields to their initial values.
     */
    private void initializeSimCicle() {
        this.connectionModule.setMaxServers(k);
        this.executionModule.setMaxServers(m);
        this.transactionModule.setMaxServers(p);
        this.queryProcessorModule.setMaxServers(n);
        this.processManagemnteModule.setMaxServers(1);

        RandomNumberGenerator r = new RandomNumberGenerator();
        ClientQuery firstOne = new ClientQuery(0, r.getConnectionStatementType(), connectionModule);
        Event firstEvent = new Event(firstOne, SimEvent.ARRIVE, connectionModule, 0.0);
        this.systemEventList.add(firstEvent);
        this.simClock = 0;
    }

    /**
     * Returns the module connectionModule
     *
     * @return connectionModule module
     */
    public Module getConnectionModule() {
        return connectionModule;
    }

    /**
     * Set the connectionModule field to the value(Module) past as argument.
     *
     * @param connectionModule
     */
    public void setConnectionModule(Module connectionModule) {
        this.connectionModule = connectionModule;
    }

    /**
     * Returns the module processManagemnteModule
     *
     * @return processManagemnteModule module
     */
    public Module getProcessManagemnteModule() {
        return processManagemnteModule;
    }

    /**
     * Set the processManagemnteModule field to the value(Module) past as
     * argument.
     *
     * @param processManagemnteModule
     */
    public void setProcessManagemnteModule(Module processManagemnteModule) {
        this.processManagemnteModule = processManagemnteModule;
    }

    /**
     * Returns the module queryProcessorModule
     *
     * @return queryProcessorModule module
     */
    public Module getQueryProcessorModule() {
        return queryProcessorModule;
    }

    /**
     * Set the queryProcessorModule field to the value(Module) past as argument.
     *
     * @param queryProcessorModule
     */
    public void setQueryProcessorModule(Module queryProcessorModule) {
        this.queryProcessorModule = queryProcessorModule;
    }

    /**
     * Returns the module executionModule
     *
     * @return executionModule module
     */
    public Module getExecutionModule() {
        return executionModule;
    }

    /**
     * Set the ExecutionModul field to the value(Module) past as argument.
     *
     * @param ExecutionModule
     */
    public void setExecutionModule(Module ExecutionModule) {
        this.executionModule = ExecutionModule;
    }

    /**
     * Returns the module transactionModule
     *
     * @return transactionModule module
     */
    public Module getTransactionModule() {
        return transactionModule;
    }

    /**
     * Set the TransactionModule field to the value(Module) past as argument.
     *
     * @param TransactionModule
     */
    public void setTransactionModule(Module TransactionModule) {
        this.transactionModule = TransactionModule;
    }

    /**
     * Returns the {@code LinkedList<ClientQuery> } clients
     *
     * @return clients LinkedList
     */
    public LinkedList<ClientQuery> getClients() {
        return clients;
    }

    /**
     * Set the clients field to the value({@code LinkedList<ClientQuery>}) past
     * as argument.
     *
     * @param clients
     */
    public void setTransactionModule(LinkedList<ClientQuery> clients) {
        this.clients = clients;
    }

    /**
     * Returns this {@code PriorityQueue<Event>} systemEventList
     *
     * @return systemEventList PriorityQueue
     */
    PriorityQueue<Event> getSistemEventList() {
        return this.systemEventList;
    }

    /**
     * Returns simulation clock simClock
     *
     * @return simClock double
     */
    double getSimClock() {
        return simClock;
    }

    /**
     * Set the simClock field to the value(double) past as argument.
     *
     * @param simClock
     */
    public void setSimClock(double simClock) {
        this.simClock = simClock;
    }

    /**
     * Returns time the simulation will run maxSimClock
     *
     * @return maxSimClock double
     */
    double getmaxSimClock() {
        return maxSimClock;
    }

    /**
     * Set the maxSimClock field to the value(double) past as argument.
     *
     * @param maxSimClock
     */
    public void setMaxSimClock(double maxSimClock) {
        this.maxSimClock = maxSimClock;
    }

    /**
     * Returns times to run simulation timesToRunSimulation
     *
     * @return timesToRunSimulation int
     */
    int gettimesToRunSimulation() {
        return timesToRunSimulation;
    }

    /**
     * Set the timesToRunSimulation field to the value(int) past as argument.
     *
     * @param timesToRunSimulation
     */
    public void setTimesToRunSimulation(int timesToRunSimulation) {
        this.timesToRunSimulation = timesToRunSimulation;
    }

    /**
     * Returns pointer to the MainForm class interFace
     *
     * @return interFace MainForm
     */
    public MainForm getInterFace() {
        return interFace;
    }

    /**
     * Returns the number of seconds of timeout of the connections
     *
     * @return t double
     */
    public double getT() {
        return this.t;
    }
}
