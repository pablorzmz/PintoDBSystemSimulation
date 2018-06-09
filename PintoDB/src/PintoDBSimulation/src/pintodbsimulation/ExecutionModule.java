package pintodbsimulation;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class ExecutionModule extends Module {

    public ExecutionModule(int servers, int maxServers, SimPintoDB simPintoDBPointer, Module nextModule) {
        super(servers, maxServers, simPintoDBPointer, nextModule);
        this.queryQueue = new LinkedList<>();
        this.queueSizeRegister = new LinkedList<>();
    }

    @Override
    public void processTimeOut() {
        PriorityQueue<Event> eQ;
        eQ = simPintoDBPointer.getSistemEventList();
        Event e = eQ.poll(); //I need to delete the current event
        ClientQuery outgoingCQ = e.getClientQuery();
        QueryStatistics outgoingQS = outgoingCQ.getQueryStatistics();
        outgoingQS.setModuleLeaveTime(e.getClockTime()); //I need to update the outgoing client data
        outgoingCQ.updateStats();

        //I need to check where the outgoing client is
        if (!queryQueue.remove(outgoingCQ)) { //If the outgoing client wasn't on the module queue, it must be being attended
            if (queryQueue.size() > 0) { //If there are waiting clients on the module queue
                generateAction(this.queryQueue.poll()); //I need to generate the LEAVE of the waiting client that I put to be attended
                queueSizeRegister.add(queryQueue.size());
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

        if (servers < maxServers) {
            ++servers;
            generateAction(arrivingCQ);
        } else {
            queryQueue.add(arrivingCQ);
            queueSizeRegister.add(queryQueue.size());
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

        if (queryQueue.size() > 0) {
            generateAction(queryQueue.poll()); //I need to generate the LEAVE of the waiting client that I put to be attended
            queueSizeRegister.add(queryQueue.size());
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
                case DDL:
                    eTime += 0.5;
                    break;
                case UPDATE:
                    eTime += 1;
                    break;
                default:
                    QueryStatistics cQS = clientQuery.getQueryStatistics();
                    int uB = cQS.getUsedBlocks();
                    eTime += uB * uB + uB / 64;
                    break;
            }
        }
        Event e = new Event(clientQuery, SimEvent.LEAVE, this, eTime);

        //I need to add the new event to the systemEventList
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        eQ.add(e);
    }

    @Override
    public void generateNextModuleAction(ClientQuery clientQuery) {
        //I need to create a new LEAVE type event on the next module for the client cQ
        Event e = new Event(clientQuery, SimEvent.LEAVE, nextModule, simPintoDBPointer.getSimClock());

        //I need to add the new event to the systemEventList
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        eQ.add(e);
    }
}
