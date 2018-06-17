package pintodbsimulation;

import java.util.LinkedList;
import java.util.PriorityQueue;

public abstract class Module {

    /*
    * Members declaration block
     */
    protected int servers;
    protected int maxServers;
    protected RandomNumberGenerator randNoGen;
    protected LinkedList<ClientQuery> queryQueue;
    protected PriorityQueue<ClientQuery> queryPriorityQueue;
    //protected LinkedList<Integer> queueSizeRegister;
    protected int queueSizesCounter;
    protected int queueSizesAccumulator;
    protected SimPintoDB simPintoDBPointer;
    protected Module nextModule;

    /**
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
        this.queryPriorityQueue = new PriorityQueue<>(maxServers, new ClientQueryComparator());
        //this.queueSizeRegister = new LinkedList<>();
        this.queueSizesCounter = 0;
        this.queueSizesAccumulator = 0;
    }

    /**
     * 
     */
    public void clear()
    {
        servers = 0;
        queryQueue.clear();
        queryPriorityQueue.clear();
        //queueSizeRegister.clear();
        queueSizesCounter = 0;
        queueSizesAccumulator = 0;
    }
    
    /**
     *
     * @param nextModule
     */
    public void setNextModule(Module nextModule) {
        this.nextModule = nextModule;
    }

    /**
     *
     * @return
     */
    
    /*public LinkedList<Integer> getQueueSizeRegister() {
        return queueSizeRegister;
    }*/
    
    /**
     *
     * @return
     */
    public int getQueueSizesCounter(){
        return this.queueSizesCounter;
    }
    
    /**
     *
     * @return
     */
    public int getQueueSizesAccumulator(){
        return this.queueSizesAccumulator;
    }

    /**
     *
     */
    public abstract void processTimeOut();

    /**
     *
     */
    public abstract void processArrive();

    /**
     *
     */
    public abstract void processExit();

    /**
     *
     * @param clientQuery
     */
    public abstract void generateAction(ClientQuery clientQuery);

    /**
     *
     * @param clientQuery
     */
    public abstract void generateNextModuleAction(ClientQuery clientQuery);

    /**
     *
     * @return
     */
    public LinkedList<ClientQuery> getQueryQueue() {
        return queryQueue;
    }

    /**
     *
     * @return
     */
    public PriorityQueue<ClientQuery> getPriorityQueryQueue() {
        return queryPriorityQueue;
    }
}