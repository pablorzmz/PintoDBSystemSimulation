package pintodbsimulation;

import java.util.PriorityQueue;

public class TransactionAndDiskModule extends Module {

    public TransactionAndDiskModule(int servers, int maxServers, SimPintoDB simPintoDBPointer, Module nextModule) {
        super(servers, maxServers, simPintoDBPointer, nextModule);
    }

    @Override
    public void processTimeOut() {
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        Event e = eQ.poll(); //I need to delete the current event
        ClientQuery outgoingCQ = e.getClientQuery();
        QueryStatistics outgoingQS = outgoingCQ.getQueryStatistics();
        outgoingQS.setModuleLeaveTime(e.getClockTime()); //I need to update the outgoing client data
        outgoingCQ.updateStats();

        //I need to check where the outgoing client is
        if (!queryPriorityQueue.remove(outgoingCQ)) { //If the outgoing client wasn't on the module queue, it must be being attended
            if (queryPriorityQueue.size() > 0) { //If there are waiting clients on the module queue
                ClientQuery nextCQ = this.queryPriorityQueue.peek();
                if (nextCQ.getQueryType() == StatementType.DDL) {
                    if (servers == 1) {
                        queryPriorityQueue.poll();
                        generateAction(nextCQ); //I need to generate the LEAVE of the waiting client that I put to be attended
                        servers = maxServers;
                        queueSizeRegister.add(queryPriorityQueue.size());
                    } else {
                        --servers;
                    }
                } else {
                    queryPriorityQueue.poll();
                    generateAction(nextCQ); //I need to generate the LEAVE of the waiting client that I put to be attended
                    queueSizeRegister.add(queryPriorityQueue.size());
                }
            } else { //If there isn't client waiting to be attended
                --servers;
            }
        }
    }

    @Override
    public void processArrive() {
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        Event e = eQ.poll(); //I need to delete the current event
        ClientQuery arrivingCQ = e.getClientQuery();
        QueryStatistics arrivingQS = arrivingCQ.getQueryStatistics();
        arrivingQS.setModuleArriveTime(e.getClockTime()); //I need to update the outgoing client data

        ClientQuery nextCQ = queryPriorityQueue.peek();
        if (nextCQ.getQueryType() != StatementType.DDL) {
            if (servers < maxServers) {
                ++servers;
                generateAction(arrivingCQ);
            } else {
                queryPriorityQueue.add(arrivingCQ);
                queueSizeRegister.add(queryPriorityQueue.size());
            }
        } else if (servers == 0) {
            servers = maxServers;
            generateAction(arrivingCQ);
        } else {
            queryPriorityQueue.add(arrivingCQ);
            queueSizeRegister.add(queryPriorityQueue.size());
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

        if (queryPriorityQueue.size() > 0) {
            generateAction(queryPriorityQueue.poll()); //I need to generate the LEAVE of the waiting client that I put to be attended
            queueSizeRegister.add(queryPriorityQueue.size());
        } else { //If there isn't client waiting to be attended
            --servers;
        }

        //I need to generate an ARRIVE on the next module for the leavingCQ
        generateNextModuleAction(leavingCQ);
    }

    @Override
    public void generateAction(ClientQuery clientQuery) {
        //I need to create a new LEAVE type event on this module for the client cQ
        double eTime = simPintoDBPointer.getSimClock();
        StatementType cST = clientQuery.getQueryType();
        if (null != cST) {
            switch (cST) {
                case SELECT:
                    eTime += 0.10;
                    break;
                case JOIN:
                    QueryStatistics cQS = clientQuery.getQueryStatistics();
                    int uB = cQS.getUsedBlocks();
                    eTime += uB * 0.10;

                    break;
                default:
                    break;
            }
        }
        eTime += servers * 0.03;
        Event e = new Event(clientQuery, SimEvent.LEAVE, this, eTime);

        //I need to add the new event to the systemEventList
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        eQ.add(e);
    }

    @Override
    public void generateNextModuleAction(ClientQuery clientQuery) {
        //I need to create a new ARRIVE type event on the next module for the client cQ
        Event e = new Event(clientQuery, SimEvent.ARRIVE, nextModule, simPintoDBPointer.getSimClock());

        //I need to add the new event to the systemEventList
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        eQ.add(e);
    }
}
