package pintodbsimulation;

import java.util.Queue;

/**
 * Abstract and father class to be extended by all the module classes of this
 * simulator.
 *
 * @author B65477 B65728 B55830
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
     * Generic module waiting queue for clients({@link ClientQuery})
     */
    protected Queue<ClientQuery> myQueue;

    /**
     * Count the number of times that the number of clients waiting change. Used
     * to calculate statistics.
     */
    protected int queueSizesCounter;

    /**
     * Accumulate the number of clients waiting to be attended, each time this
     * number change. Used to calculate statistics.
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
        this.queueSizesCounter = 0;
        this.queueSizesAccumulator = 0;
        this.myQueue = null;
    }

    /**
     * Set some of the class fields (counters and queues) to their initial
     * value.
     */
    public void clear() {
        servers = 0;
        if (myQueue != null) {
            myQueue.clear();
        }
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
     * Return this class myQueue field of type {@code Queue<ClientQuery>}.
     *
     * @return myQueue field
     */
    public Queue<ClientQuery> getMyQueue() {
        return myQueue;
    }

    /**
     * Set this class myQueue field value to that pass as argument.
     *
     * @param myQueue
     */
    public void setMyQueue(Queue<ClientQuery> myQueue) {
        this.myQueue = myQueue;
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
     * Pulls out the current event of the event list, and searches for this
     * event client, ie client with timeout, which can be on the module waiting
     * queue(is there is any) or being attended. In the first case the client is
     * simply taken out of the module waiting queue. Otherwise the client stops
     * being attended and a server if released. In the second case the logic
     * before releasing a server is identical to that of {@link processExit()}.
     *
     * @see Event
     * @see ClientQuery
     * @see SimPintoDB
     */
    public abstract void processTimeOut();

    /**
     * Pulls out the current event of the event list and gets the current client
     * from the event, ie the client arriving to the module. Then check if there
     * are available server, if there are the client is pass to be attended and
     * {@link generateAction} is called with the current client as argument. If
     * there are not the client is put on the module waiting queue. If the
     * module do not have an waiting queue the client is simply rejected.
     *
     * @see Event
     * @see ClientQuery
     * @see SimPintoDB
     */
    public abstract void processArrive();

    /**
     * Pulls out the current event of the event list and gets the current client
     * from the event, ie the client leaving to the module. If there are other
     * clients waiting to be attended on the module waiting queue (if there is
     * any) the next client is taken out of the waiting queue and passed to be
     * attended, and {@link generateAction} is called with the next client as
     * argument. Otherwise a server is released, ie {@link servers} is
     * subtracted by one. Finally {@link generateNextModuleAction} is called
     * with the current client as parameter.
     *
     * @see Event
     * @see ClientQuery
     * @see SimPintoDB
     */
    public abstract void processExit();

    /**
     * Creates a new event for this module and the client (clientQuery)
     * and puts it on the simulation event list. This new event type can be
     * {@code SimEvent.LEAVE} o {@code SimEvent.ARRIVE} depending of the needs
     * of the module.
     *
     * @param clientQuery
     * @see Event
     * @see ClientQuery
     * @see SimPintoDB
     */
    public abstract void generateAction(ClientQuery clientQuery);

    /**
     * Creates a new event for the next module ({@link nextModule}) and the
     * client (clientQuery) and puts it on the simulation event list. This
     * new event type can be {@code SimEvent.LEAVE} o {@code SimEvent.ARRIVE}
     * depending of the needs of the module.
     *
     * @param clientQuery
     * @see Event
     * @see ClientQuery
     * @see SimPintoDB
     */
    public abstract void generateNextModuleAction(ClientQuery clientQuery);
}
