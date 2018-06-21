package pintodbsimulation;

import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Abstract and father class to be extended by all the module classes of this
 * simulator.
 *
 * @author
 */
public abstract class Module {

    //Members declaration block.
    /**
     * Busy servers counter.
     */
    protected int servers;

    /**
     * Max number of available servers
     */
    protected int maxServers;

    /**
     * RandomNumberGenerator instance, use to generate random numbers and random
     * variables values, respecting several distributions.
     */
    protected RandomNumberGenerator randNoGen;

    /**
     * Queue for the clients waiting to be attended.
     */
    protected LinkedList<ClientQuery> queryQueue;

    /**
     * Priority queue for the clientes waiting to be attended.
     */
    protected PriorityQueue<ClientQuery> queryPriorityQueue;

    /**
     * Count the number of times that the number of clients waiting change. Used
     * to calculate statistics.
     */
    protected int queueSizesCounter;

    /**
     * Accumulate the number of clients waiting to be attended, each time this
     * number change. Used to calculeta statistics.
     */
    protected int queueSizesAccumulator;

    /**
     * Instance of the controller class.
     */
    protected SimPintoDB simPintoDBPointer;

    /**
     * Instance of the next module in the execution of the simulation.
     */
    protected Module nextModule;

    /**
     * Class constructor.
     *
     * @param servers
     * @param maxServers
     * @param simPintoDBPointer
     * @param nextModule
     */
    public Module(int servers, int maxServers, SimPintoDB simPintoDBPointer, Module nextModule) {
        this.servers = servers;
        this.maxServers = maxServers;
        this.simPintoDBPointer = simPintoDBPointer;
        this.nextModule = nextModule;
        this.randNoGen = new RandomNumberGenerator();
        this.queryQueue = new LinkedList<>();
        this.queryPriorityQueue = new PriorityQueue<>(new ClientQueryComparator());
        this.queueSizesCounter = 0;
        this.queueSizesAccumulator = 0;
    }

    /**
     * Set some of the class fields (counters and queues) to their initial
     * value.
     */
    public void clear() {
        servers = 0;
        queryQueue.clear();
        queryPriorityQueue.clear();
        //queueSizeRegister.clear();
        queueSizesCounter = 0;
        queueSizesAccumulator = 0;
    }

    /**
     * Set the nextModule field to the value(Module) past as argument.
     *
     * @param nextModule
     */
    public void setNextModule(Module nextModule) {
        this.nextModule = nextModule;
    }

    /**
     * Returns this class queueSizesCounter field current value.
     *
     * @return queueSiszesCounter field
     */
    public int getQueueSizesCounter() {
        return this.queueSizesCounter;
    }

    /**
     * Returns this class queueSizesAccumulator current field value.
     *
     * @return queueSizesAccumulator field
     */
    public int getQueueSizesAccumulator() {
        return this.queueSizesAccumulator;
    }

    /**
     * Returns this class queryQueue field, which type is
     * {@code LinkedList<ClientQuery>}.
     *
     * @return queryQueue field
     */
    public LinkedList<ClientQuery> getQueryQueue() {
        return queryQueue;
    }

    /**
     * Returns this class queryPriorityQueue field, which type is
     * {@code PriorityQueue<ClientQuery>}.
     *
     * @return queryPriorityQueue field
     */
    public PriorityQueue<ClientQuery> getQueryPriorityQueue() {
        return queryPriorityQueue;
    }

    /**
     * Set the maxServer field to the value (int) past as argument.
     *
     * @param maxServers
     */
    public void setMaxServers(int maxServers) {
        this.maxServers = maxServers;
    }

    /**
     * Abstract method to be implemente by the classes who extends this class.
     *
     * @see ProcessManagmentModule
     * @see QueryProcessorModule
     * @see ExecutionModule
     * @see TransactionAndDiskModule
     * @see ConnectionModule
     */
    public abstract void processTimeOut();

    /**
     * Abstract method to be implemente by the classes who extends this class.
     *
     * @see ProcessManagmentModule
     * @see QueryProcessorModule
     * @see ExecutionModule
     * @see TransactionAndDiskModule
     * @see ConnectionModule
     */
    public abstract void processArrive();

    /**
     * Abstract method to be implemente by the classes who extends this class.
     *
     * @see ProcessManagmentModule
     * @see QueryProcessorModule
     * @see ExecutionModule
     * @see TransactionAndDiskModule
     * @see ConnectionModule
     */
    public abstract void processExit();

    /**
     * Abstract method to be implemente by the classes who extends this class.
     *
     * @param clientQuery
     * @see ProcessManagmentModule
     * @see QueryProcessorModule
     * @see ExecutionModule
     * @see TransactionAndDiskModule
     * @see ConnectionModule
     */
    public abstract void generateAction(ClientQuery clientQuery);

    /**
     * Abstract method to be implemente by the classes who extends this class.
     *
     * @param clientQuery
     * @see ProcessManagmentModule
     * @see QueryProcessorModule
     * @see ExecutionModule
     * @see TransactionAndDiskModule
     * @see ConnectionModule
     */
    public abstract void generateNextModuleAction(ClientQuery clientQuery);
}
