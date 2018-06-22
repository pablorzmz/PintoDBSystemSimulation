package pintodbsimulation;

import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * This class simulate the Query Processor Module described by the project.
 * This class extends from the abstract class Module.
 * @author B65477
 * @see Module
 */
public class QueryProcessorModule extends Module {

    /**
     * Class Constructor.
     * 
     * @param servers
     * @param maxServers
     * @param simPintoDBPointer
     * @param nextModule
     */
    public QueryProcessorModule(int servers, int maxServers, SimPintoDB simPintoDBPointer, Module nextModule) {
        super(servers, maxServers, simPintoDBPointer, nextModule);
        this.myQueue = new LinkedList<>();
    }

    @Override
    public void processTimeOut() {
        PriorityQueue<Event> eQ;
        eQ = simPintoDBPointer.getSistemEventList();
        Event e = eQ.poll(); //I need to delete the current event
        ClientQuery outgoingCQ = e.getClientQuery();

        //I need to check where the outgoing client is
        if (!myQueue.remove(outgoingCQ)) { //If the outgoing client wasn't on the module queue, it must be being attended
            this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("TimeOut: El cliente: " + outgoingCQ.clientID + " fue sacado de ser antendido"
                    + "del modulo " + " Procesamiento de consultas"
                    + " porque ya lleva en el sistema " + (e.getClockTime() - outgoingCQ.getQueryStatistics().getSystemArriveTime())
                    + " > " + simPintoDBPointer.getT());

            if (myQueue.size() > 0) { //If there are waiting clients on the module queue
                generateAction(this.myQueue.poll()); //I need to generate the LEAVE of the waiting client that I put to be attended
                queueSizesAccumulator += myQueue.size();
                ++queueSizesCounter;
            } else { //If there isn't client waiting to be attended
                --servers;
            }
        } else {
            this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("TimeOut: El cliente: " + outgoingCQ.clientID + " fue sacado de la cola "
                    + "del modulo " + "Procesamiento de consultas " + " y su tiempo en el sistema es de: "
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
            this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Arrive: El cliente: " + arrivingCQ.clientID + " fue pasado de ser antendido "
                    + "en el modulo " + "Procesamiento de consultas" + " y su tiempo en el sistema es de: "
                    + (simPintoDBPointer.getSimClock() - arrivingCQ.getQueryStatistics().getSystemArriveTime()));

            ++servers;
            generateAction(arrivingCQ);
        } else {
            this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Arrive: El cliente: " + arrivingCQ.clientID + " fue encolado "
                    + "en el modulo " + "Procesamiento de consultas" + " y su tiempo en el sistema es de: "
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

        if (myQueue.size() > 0) {
            this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Leave: El cliente: " + leavingCQ.clientID + " sale del modulo "
                    + "Procesamiento de consultas" + " y su tiempo en el sistema es de: "
                    + (simPintoDBPointer.getSimClock() - leavingCQ.getQueryStatistics().getSystemArriveTime()));

            generateAction(myQueue.poll()); //I need to generate the LEAVE of the waiting client that I put to be attended
            queueSizesAccumulator += myQueue.size();
            ++queueSizesCounter;
        } else { //If there isn't client waiting to be attended
            this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Leave: El cliente: " + leavingCQ.clientID + " sale del modulo "
                    + "Procesamiento de consultas" + " y su tiempo en el sistema es de: "
                    + (simPintoDBPointer.getSimClock() - leavingCQ.getQueryStatistics().getSystemArriveTime()));

            --servers;
        }

        //I need to generate an ARRIVE on the next module for the leavingCQ
        generateNextModuleAction(leavingCQ);

    }

    @Override
    public void generateAction(ClientQuery clientQuery) {
        //I need to create a new LEAVE type event on this module for the client clientQuery
        this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Generate Action: Se genera una salida del cliente: " + clientQuery.clientID + " del modulo "
                + "Procesamiento de consultas" + " y su tiempo en el sistema es de: "
                + (simPintoDBPointer.getSimClock() - clientQuery.getQueryStatistics().getSystemArriveTime()));

        Event e;
        double eTime = simPintoDBPointer.getSimClock();
        eTime += 0.1; //Lexical Validation
        eTime += randNoGen.getTimeUsingUniformDist(0, 1); //Syntactic Validation
        eTime += randNoGen.getTimeUsingUniformDist(0, 2); //Semantic Validation
        eTime += randNoGen.getTimeUsingExponencialDist(1 / 0.7); //Permits Verification
        if (clientQuery.getQueryType() == StatementType.JOIN || clientQuery.getQueryType() == StatementType.SELECT) { //Query Optimization 
            eTime += 0.1; //ReadOnly
        } else {
            eTime += 0.25; //NoReadOnly
        }
        //I need to add the new event to the systemEventList
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        e = new Event(clientQuery, SimEvent.LEAVE, this, eTime);
        eQ.add(e);
    }

    @Override
    public void generateNextModuleAction(ClientQuery clientQuery) {
        //I need to create a new ARRIVE type event on the next module for the client clientQuery
        this.simPintoDBPointer.getInterFace().refreshConsoleAreaContent("Generate Next Action: Se genera una llegada del cliente: " + clientQuery.clientID + " del modulo "
                + "Procesamiento de consultas" + " al modulo " + "Transacciones"
                + " y su tiempo en el sistema es de: "
                + (simPintoDBPointer.getSimClock() - clientQuery.getQueryStatistics().getSystemArriveTime()));
        Event e = new Event(clientQuery, SimEvent.ARRIVE, nextModule, simPintoDBPointer.getSimClock());

        //I need to add the new event to the systemEventList
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        eQ.add(e);
    }
}
