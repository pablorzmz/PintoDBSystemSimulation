package pintodbsimulation;

import java.util.PriorityQueue;

public class ConnectionModule extends Module {

    private int deniedConnectionCounter;

    public ConnectionModule(int servers, int maxServers, SimPintoDB simPintoDBPointer, Module nextModule) {
        super(servers, maxServers, simPintoDBPointer, nextModule);
        deniedConnectionCounter = 0;
    }

    @Override
    public void processTimeOut() {
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        Event e = eQ.poll(); //I need to delete the current event
        ClientQuery outgoingCQ = e.getClientQuery();
        QueryStatistics outgoingQS = outgoingCQ.getQueryStatistics();
        outgoingQS.setSystemLeaveTime(e.getClockTime()); //I need to update the outgoing client data
        outgoingCQ.updateStats();

        --servers;
    }

    @Override
    public void processArrive() {
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        Event e = eQ.poll(); //I need to delete the current event
        ClientQuery arrivingCQ = e.getClientQuery();
        QueryStatistics arrivingQS = arrivingCQ.getQueryStatistics();
        arrivingQS.setSystemArriveTime(e.getClockTime()); //I need to update the outgoing client data

        if (servers < maxServers) {
            ++servers;
            generateNextModuleAction(arrivingCQ);
        } else {
            ++deniedConnectionCounter;
        }

        //I need to generate a new ARRIVE
        ClientQuery newCQ = new ClientQuery(randNoGen.getConnectionStatementType(), this);
        generateAction(newCQ);
    }

    @Override
    public void processExit() {
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        Event e = eQ.poll(); //I need to delete the current event
        ClientQuery leavingCQ = e.getClientQuery();
        QueryStatistics leavingQS = leavingCQ.getQueryStatistics();
        leavingQS.setSystemLeaveTime(e.getClockTime()); //I need to update the outgoing client data
        leavingCQ.updateStats();

        --servers;
    }

    @Override
    public void generateAction(ClientQuery clientQuery) {

        //I need to create a new ARRIVE type event on this module for the client cQ
        double eTime = simPintoDBPointer.getSimClock() + randNoGen.getTimeUsingExponencialDist(30);
        Event e = new Event(clientQuery, SimEvent.ARRIVE, this, eTime);

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
