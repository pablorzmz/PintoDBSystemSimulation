package pintodbsimulation;

import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * This class simulate the Connection Managment Module described by the project.
 * This class extends from the abstract class Module.
 *
 * @author B65477 B65728 B55830
 * @see Module
 */
public class ConnectionModule extends Module {

    private int deniedConnectionCounter;
    private int clientCounter;

    /**
     * Class constructor.
     *
     * @param servers
     * @param maxServers
     * @param simPintoDBPointer
     * @param nextModule
     */
    public ConnectionModule(int servers, int maxServers, SimPintoDB simPintoDBPointer, Module nextModule) {
        super(servers, maxServers, simPintoDBPointer, nextModule);
        deniedConnectionCounter = 0;
        clientCounter = 0;
    }

    /**
     * Return the current value (int) of the class field
     * {@link deniedConnectionCounter}.
     *
     * @return deniedConnectionCounter field
     */
    public int getDeniedConnectionCounter() {
        return deniedConnectionCounter;
    }

    /**
     * Set the deniedConnectionCounter field to the value(int) pass as argument.
     *
     * @param deniedConnectionCounter
     */
    public void setDeniedConnectionCounter(int deniedConnectionCounter) {
        this.deniedConnectionCounter = deniedConnectionCounter;
    }

    @Override
    public void processTimeOut() {
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        Event e = eQ.poll(); //I need to delete the current event
        ClientQuery outgoingCQ = e.getClientQuery();

        this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent(
                "TimeOut: El cliente: " + outgoingCQ.getClientID() + " fue sacado de ser antendido "
                + "del " + "Connection Module");

        outgoingCQ.setFinishService(true);
        --servers;

    }

    @Override
    public void processArrive() {
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        Event e = eQ.poll(); //I need to delete the current event

        ClientQuery arrivingCQ = e.getClientQuery();
        QueryStatistics arrivingQS = arrivingCQ.getQueryStatistics();
        arrivingCQ.setCurrentMod(this);
        arrivingCQ.setFinishService(false);
        arrivingQS.setSystemArriveTime(e.getClockTime()); //I need to update the outgoing client data

        double old = this.simPintoDBPointer.getStats().getCurrentIterationStats().getTotalConnections();
        this.simPintoDBPointer.getStats().getCurrentIterationStats().setTotalConnections(old + 1);

        if (servers < maxServers) {
            this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Arrive: El cliente: " + arrivingCQ.getClientID() + " fue pasado de ser antendido "
                    + "en el " + "Connection Module" + " en el tiempo " + e.getClockTime());
            ++servers;
            //I need to add the new cliente to the system, ie the clients list.
            LinkedList<ClientQuery> simClients = simPintoDBPointer.getClients();
            simClients.add(arrivingCQ);
            generateNextModuleAction(arrivingCQ);
            //I need to generate the posible timeout event for the new client in the system
            Event timeoutE;
            timeoutE = new Event(arrivingCQ, SimEvent.TIMEOUT, this, e.getClockTime() + this.simPintoDBPointer.getT());
            eQ.add(timeoutE);
            timeoutE = new Event(arrivingCQ, SimEvent.TIMEOUT, null, e.getClockTime() + this.simPintoDBPointer.getT());
            eQ.add(timeoutE);
        } else {
            this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Arrive: El cliente: " + arrivingCQ.getClientID() + " fue rechazado "
                    + "en el " + "Connection Module");
            ++deniedConnectionCounter;
        }

        //I need to generate a new ARRIVE
        ++clientCounter;
        ClientQuery newCQ = new ClientQuery(clientCounter, randNoGen.getConnectionStatementType(), this);
        generateAction(newCQ);
    }

    @Override
    public void processExit() {
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        Event e = eQ.poll(); //I need to delete the current event
        ClientQuery leavingCQ = e.getClientQuery();

        leavingCQ.setCurrentMod(this);
        //lo agrega Pablo
        leavingCQ.setFinishService(true);
        ////
        QueryStatistics leavingQS = leavingCQ.getQueryStatistics();
        leavingQS.setSystemLeaveTime(e.getClockTime()); //I need to update the outgoing client data
        leavingCQ.updateStats();
        this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Leave: El cliente: " + leavingCQ.getClientID() + " sale del "
                + "Connection Module");
        --servers;

    }

    @Override
    public void generateAction(ClientQuery clientQuery) {
        //I need to create a new ARRIVE type event on this module for the client clientQuery
        this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Generate Action: Se genera una llegada del cliente: " + clientQuery.getClientID() + " al "
                + " Connection Module");
        double eTime = simPintoDBPointer.getSimClock() + randNoGen.getTimeUsingExponencialDist((30.0 / 60.0));
        Event e = new Event(clientQuery, SimEvent.ARRIVE, this, eTime);

        //I need to add the new event to the systemEventList
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        eQ.add(e);
    }

    @Override
    public void generateNextModuleAction(ClientQuery clientQuery) {
        //I need to create a new ARRIVE type event on the next module for the client clientQuery
        this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Generate Next Action: Se genera una llegada del cliente: " + clientQuery.getClientID() + " del "
                + "Connection Module" + " al " + " Process Managment Module");
        Event e = new Event(clientQuery, SimEvent.ARRIVE, nextModule, simPintoDBPointer.getSimClock());

        //I need to add the new event to the systemEventList
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        eQ.add(e);
    }
}
