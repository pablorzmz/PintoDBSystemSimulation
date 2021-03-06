package pintodbsimulation;

import java.util.PriorityQueue;

/**
 * This class simulate the Transaction and Disk Access Module described by the
 * project. This class extends from the abstract class Module.
 *
 * @author B65477 B65728 B55830
 * @see Module
 */
public class TransactionAndDiskModule extends Module {

    /**
     * Class constructor.
     *
     * @param servers
     * @param maxServers
     * @param simPintoDBPointer
     * @param nextModule
     */
    public TransactionAndDiskModule(int servers, int maxServers, SimPintoDB simPintoDBPointer, Module nextModule) {
        super(servers, maxServers, simPintoDBPointer, nextModule);
        this.myQueue = new PriorityQueue<>(new ClientQueryComparator());
    }

    @Override
    public void processTimeOut() {
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        Event e = eQ.poll(); //I need to delete the current event
        ClientQuery outgoingCQ = e.getClientQuery();

        //I need to check where the outgoing client is
        if (!myQueue.remove(outgoingCQ)) { //If the outgoing client wasn't on the module queue, it must be being attended
            if (myQueue.size() > 0) { //If there are waiting clients on the module queue
                ClientQuery nextCQ = this.myQueue.peek();
                if (nextCQ.getQueryType() == StatementType.DDL) {
                    if (servers == 1) {
                        this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("TimeOut: El cliente: " + outgoingCQ.getClientID() + " de tipo " + outgoingCQ.getQueryType() + " fue sacado de ser antendido "
                                + "del " + "Transaction and Disk Access Module" + " y su tiempo en el sistema es de: "
                                + (simPintoDBPointer.getSimClock() - outgoingCQ.getQueryStatistics().getSystemArriveTime()));

                        myQueue.poll();
                        generateAction(nextCQ); //I need to generate the LEAVE of the waiting client that I put to be attended
                        servers = maxServers;
                        queueSizesAccumulator += myQueue.size();
                        ++queueSizesCounter;
                    } else {
                        --servers;
                        this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("TimeOut: El cliente: " + outgoingCQ.getClientID() + " de tipo " + outgoingCQ.getQueryType() + " fue sacado de ser antendido "
                                + "del " + "Transaction and Disk Access Module" + " y su tiempo en el sistema es de: "
                                + (simPintoDBPointer.getSimClock() - outgoingCQ.getQueryStatistics().getSystemArriveTime()));
                    }
                } else {
                    this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("TimeOut: El cliente: " + outgoingCQ.getClientID() + " de tipo " + outgoingCQ.getQueryType() + " fue sacado de ser antendido "
                            + "del " + "Transaction and Disk Access Module" + " y su tiempo en el sistema es de: "
                            + (simPintoDBPointer.getSimClock() - outgoingCQ.getQueryStatistics().getSystemArriveTime()));

                    // if the current client was a DDL and the next one no
                    if (outgoingCQ.getQueryType() == StatementType.DDL) {
                        servers = 1;
                    }
                    myQueue.poll();
                    generateAction(nextCQ); //I need to generate the LEAVE of the waiting client that I put to be attended
                    queueSizesAccumulator += myQueue.size();
                    ++queueSizesCounter;
                }
            } else //If there isn't client waiting to be attended
            if (outgoingCQ.getQueryType() == StatementType.DDL) {
                servers = 0;
            } else {
                --servers;
            }
        } else {
            this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("TimeOut: El cliente: " + outgoingCQ.getClientID() + " de tipo " + outgoingCQ.getQueryType() + " fue sacado de la cola "
                    + "del " + "Transaction and Disk Access Module" + " y su tiempo en el sistema es de: "
                    + (simPintoDBPointer.getSimClock() - outgoingCQ.getQueryStatistics().getSystemArriveTime()));
        }

    }

    @Override
    public void processArrive() {
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        Event e = eQ.poll(); //I need to delete the current event
        ClientQuery arrivingCQ = e.getClientQuery();

        QueryStatistics arrivingQS = arrivingCQ.getQueryStatistics();
        arrivingCQ.setCurrentMod(this);
        arrivingQS.setModuleArriveTime(e.getClockTime()); //I need to update the outgoing client data

        if (servers < maxServers) {
            if (arrivingCQ.getQueryType() == StatementType.DDL) {
                if (servers == 0) {
                    this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Arrive: El cliente: " + arrivingCQ.getClientID() + " de tipo : "
                            + " (" + arrivingCQ.getQueryType() + ") " + " fue pasado de ser antendido "
                            + "en el " + "Transaction and Disk Access Module" + " y su tiempo en el sistema es de: "
                            + (simPintoDBPointer.getSimClock() - arrivingCQ.getQueryStatistics().getSystemArriveTime()));

                    servers = maxServers;
                    generateAction(arrivingCQ);
                } else {
                    this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Arrive: El cliente: " + arrivingCQ.getClientID()
                            + " de tipo :(" + arrivingCQ.getQueryType() + ") fue encolado "
                            + "en el " + "Transaction and Disk Access Module" + " y su tiempo en el sistema es de: "
                            + (simPintoDBPointer.getSimClock() - arrivingCQ.getQueryStatistics().getSystemArriveTime()));

                    myQueue.add(arrivingCQ);
                    queueSizesAccumulator += myQueue.size();
                    ++queueSizesCounter;
                }
            } else if (myQueue.size() > 0) {
                this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Arrive: El cliente: " + arrivingCQ.getClientID() + " de tipo : "
                        + " (" + arrivingCQ.getQueryType() + ") " + " fue encolado "
                        + "en el " + "Transaction and Disk Access Module" + " y su tiempo en el sistema es de: "
                        + (simPintoDBPointer.getSimClock() - arrivingCQ.getQueryStatistics().getSystemArriveTime()));

                myQueue.add(arrivingCQ);
                queueSizesAccumulator += myQueue.size();
                ++queueSizesCounter;

            } else {
                this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Arrive: El cliente: " + arrivingCQ.getClientID() + " de tipo : "
                        + " (" + arrivingCQ.getQueryType() + ") "
                        + " fue pasado a ser atendido "
                        + "en el " + "Transaction and Disk Access Module" + " y su tiempo en el sistema es de: "
                        + (simPintoDBPointer.getSimClock() - arrivingCQ.getQueryStatistics().getSystemArriveTime()));

                ++servers;
                generateAction(arrivingCQ);
            }
        } else {
            this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Arrive: El cliente: " + arrivingCQ.getClientID() + " de tipo : "
                    + " (" + arrivingCQ.getQueryType() + ") "
                    + " fue encolado "
                    + "en el " + "Transaction and Disk Access Module" + " y su tiempo en el sistema es de: "
                    + (simPintoDBPointer.getSimClock() - arrivingCQ.getQueryStatistics().getSystemArriveTime()));

            myQueue.add(arrivingCQ);
            queueSizesAccumulator += myQueue.size();
            ++queueSizesCounter;
        }
    }

    @Override
    public void processExit() {
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        Event e = eQ.poll(); //I need to delete the current event
        ClientQuery leavingCQ = e.getClientQuery();

        QueryStatistics leavingQS = leavingCQ.getQueryStatistics();
        leavingQS.setModuleLeaveTime(e.getClockTime()); //I need to update the outgoing client data
        leavingCQ.updateStats();

        if (myQueue.size() > 0) { //If there are waiting clients on the module queue
            ClientQuery nextCQ = this.myQueue.peek();
            if (nextCQ.getQueryType() == StatementType.DDL) {
                if (leavingCQ.getQueryType() == StatementType.DDL) {
                    this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Leave: El cliente: " + leavingCQ.getClientID() + " de tipo: " + leavingCQ.getQueryType() + " y sale del "
                            + "Transaction and Disk Access Module" + " y su tiempo en el sistema es de: "
                            + (simPintoDBPointer.getSimClock() - leavingCQ.getQueryStatistics().getSystemArriveTime()));

                    nextCQ = myQueue.poll();
                    generateAction(nextCQ); //I need to generate the LEAVE of the waiting client that I put to be attended
                    servers = maxServers;
                    queueSizesAccumulator += myQueue.size();
                    ++queueSizesCounter;
                } else {
                    this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Leave: El cliente: " + leavingCQ.getClientID() + " de tipo: " + leavingCQ.getQueryType() + " y sale del "
                            + "Transaction and Disk Access Module" + " y su tiempo en el sistema es de: "
                            + (simPintoDBPointer.getSimClock() - leavingCQ.getQueryStatistics().getSystemArriveTime()));

                    --servers;
                    if (servers == 0) // I was the last one no DDL and the next one is a DDL
                    {
                        nextCQ = myQueue.poll();
                        generateAction(nextCQ); //I need to generate the LEAVE of the waiting client that I put to be attended
                        servers = maxServers;
                        queueSizesAccumulator += myQueue.size();
                        ++queueSizesCounter;
                    }
                }
            } else {
                this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Leave: El cliente: " + leavingCQ.getClientID() + " de tipo: " + leavingCQ.getQueryType() + " y sale del "
                        + "Transaction and Disk Access Module" + " y su tiempo en el sistema es de: "
                        + (simPintoDBPointer.getSimClock() - leavingCQ.getQueryStatistics().getSystemArriveTime()));

                // if the current client was a DDL and the next one no
                if (leavingCQ.getQueryType() == StatementType.DDL) {
                    servers = 1;
                }

                myQueue.poll();
                generateAction(nextCQ); //I need to generate the LEAVE of the waiting client that I put to be attended
                queueSizesAccumulator += myQueue.size();
                ++queueSizesCounter;
            }
        } else //If there isn't client waiting to be attended
        {
            if (leavingCQ.getQueryType() == StatementType.DDL) {
                servers = 0;

            } else {
                --servers;
            }
        }
        //I need to generate an ARRIVE on the next module for the leavingCQ
        generateNextModuleAction(leavingCQ);

    }

    @Override
    public void generateAction(ClientQuery clientQuery) {
        //I need to create a new LEAVE type event on this module for the client clientQuery
        this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Generate Action: Se genera una salida del cliente: " + clientQuery.getClientID() + " de tipo " + clientQuery.getQueryType() + " del "
                + "Transaction and Disk Access Module" + " y su tiempo en el sistema es de: "
                + (simPintoDBPointer.getSimClock() - clientQuery.getQueryStatistics().getSystemArriveTime()));

        Event e;
        double eTime = simPintoDBPointer.getSimClock();
        StatementType cST = clientQuery.getQueryType();
        eTime += maxServers * 0.03;
        QueryStatistics qS = clientQuery.getQueryStatistics();

        switch (cST) {
            case DDL:
            case UPDATE:
                qS.setUsedBlocks(0);
                break;
            case JOIN:
                int blocks = (int) (1 + (new RandomNumberGenerator().getRandNumb()) * 64);
                qS.setUsedBlocks(blocks);
                break;
            default:
                qS.setUsedBlocks(1);
                break;
        }
        eTime += qS.getUsedBlocks() * (0.10);
        //I need to add the new event to the systemEventList
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        e = new Event(clientQuery, SimEvent.LEAVE, this, eTime);
        eQ.add(e);
    }

    @Override
    public void generateNextModuleAction(ClientQuery clientQuery) {
        this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Generate Next Action: Se genera una llegada del cliente: " + clientQuery.getClientID() + " de tipo " + clientQuery.getQueryType() + " del "
                + "Transaction and Disk Access Module" + " al " + "Execution Module" + " y su tiempo en el sistema es de: "
                + (simPintoDBPointer.getSimClock() - clientQuery.getQueryStatistics().getSystemArriveTime()));
        //I need to create a new ARRIVE type event on the next module for the client clientQuery        

        Event e = new Event(clientQuery, SimEvent.ARRIVE, nextModule, simPintoDBPointer.getSimClock());
        //I need to add the new event to the systemEventList
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        eQ.add(e);
    }
}
