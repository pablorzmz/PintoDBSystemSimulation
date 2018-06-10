package pintodbsimulation;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class ConnectionModule extends Module {

    private int deniedConnectionCounter;
    public int clientCounter;

    public ConnectionModule(int servers, int maxServers, SimPintoDB simPintoDBPointer, Module nextModule) {
        super(servers, maxServers, simPintoDBPointer, nextModule);
        deniedConnectionCounter = 0;
        clientCounter = 0;
    }

    /**
     *
     * @return
     */
    public int getDeniedConnectionCounter() {
        return deniedConnectionCounter;
    }

    @Override
    public void processTimeOut() {
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        Event e = eQ.poll(); //I need to delete the current event
        ClientQuery outgoingCQ = e.getClientQuery();
        //QueryStatistics outgoingQS = outgoingCQ.getQueryStatistics();
        //outgoingQS.setSystemLeaveTime( e.getClockTime() ); //I need to update the outgoing client data
        //outgoingCQ.updateStats();
        System.out.println("TimeOut: El cliente: " + outgoingCQ.clientID + " fue sacado de ser antendido"
                + " del modulo " + "Conexión");

        --servers;
    }

    @Override
    public void processArrive() {
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        Event e = eQ.poll(); //I need to delete the current event
        ClientQuery arrivingCQ = e.getClientQuery();
        QueryStatistics arrivingQS = arrivingCQ.getQueryStatistics();
        arrivingCQ.setCurrentMod(this);
        arrivingQS.setSystemArriveTime(e.getClockTime()); //I need to update the outgoing client data

        if (servers < maxServers) {
            System.out.println("Arrive: El cliente: " + arrivingCQ.clientID + " fue pasado de ser antendido "
                    + "en el modulo " + "Conexión" + " en el tiempo " + e.getClockTime());
            ++servers;
            //I need to add the new cliente to the system, ie the clients list.
            LinkedList<ClientQuery> simClients = simPintoDBPointer.getClients();
            simClients.add(arrivingCQ);
            generateNextModuleAction(arrivingCQ);
        } else {
            System.out.println("Arrive: El cliente: " + arrivingCQ.clientID + " fue rechazado "
                    + "en el modulo " + "Conexión");
            ++deniedConnectionCounter;
        }

        //I need to generate a new ARRIVE
        ClientQuery newCQ = new ClientQuery(randNoGen.getConnectionStatementType(), this);
        ++clientCounter;
        newCQ.clientID = clientCounter;
        System.out.println("Nuevo cliente con id:" + newCQ.clientID + " y el tiempo actual es " + e.getClockTime());
        try {
            // thread to sleep for 1000 milliseconds
            Thread.sleep(SimPintoDB.sleepTime);
        } catch (Exception ex) {
            System.out.println(ex);
        }
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
        System.out.println("Leave: El cliente: " + leavingCQ.clientID + " sale del modulo "
                + "Conexión");

        --servers;
    }

    @Override
    public void generateAction(ClientQuery clientQuery) {
        //I need to create a new ARRIVE type event on this module for the client clientQuery
        System.out.println("Generate Action: Se genera una llegada del cliente: " + clientQuery.clientID + " al modulo "
                + " de conexiones");
        double eTime = simPintoDBPointer.getSimClock() + randNoGen.getTimeUsingExponencialDist(1 / (30.0 / 60.0));
        Event e = new Event(clientQuery, SimEvent.ARRIVE, this, eTime);

        //I need to add the new event to the systemEventList
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        eQ.add(e);
    }

    @Override
    public void generateNextModuleAction(ClientQuery clientQuery) {
        //I need to create a new ARRIVE type event on the next module for the client clientQuery
        System.out.println("Generate Next Action: Se genera una llegada del cliente: " + clientQuery.clientID + " del modulo "
                + "modulo de conexiones" + " al modulo" + " administrador de proc");
        Event e = new Event(clientQuery, SimEvent.ARRIVE, nextModule, simPintoDBPointer.getSimClock());

        //I need to add the new event to the systemEventList
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        eQ.add(e);
    }
}
