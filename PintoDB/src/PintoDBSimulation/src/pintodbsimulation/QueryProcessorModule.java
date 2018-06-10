package pintodbsimulation;

import java.util.PriorityQueue;

public class QueryProcessorModule extends Module {

    public QueryProcessorModule(int servers, int maxServers, SimPintoDB simPintoDBPointer, Module nextModule) {
        super(servers, maxServers, simPintoDBPointer, nextModule);
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
            System.out.println("TimeOut: El cliente: " + outgoingCQ.clientID + " fue sacado de ser antendido"
                    + "del modulo " + this.getClass().getName());
            if (queryQueue.size() > 0) { //If there are waiting clients on the module queue
                generateAction(this.queryQueue.poll()); //I need to generate the LEAVE of the waiting client that I put to be attended
                queueSizeRegister.add(queryQueue.size());
            } else { //If there isn't client waiting to be attended
                --servers;
            }
        }
        System.out.println("TimeOut: El cliente: " + outgoingCQ.clientID + " fue sacado de la cola "
                + "del modulo " + this.getClass().getName());
    }

    @Override
    public void processArrive() {
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        Event e = eQ.poll(); //I need to delete the current event
        ClientQuery arrivingCQ = e.getClientQuery();
        QueryStatistics arrivingQS = arrivingCQ.getQueryStatistics();
        arrivingQS.setModuleArriveTime(e.getClockTime()); //I need to update the outgoing client data

        if (servers < maxServers) {
            System.out.println("Arrive: El cliente: " + arrivingCQ.clientID + " fue pasado de ser antendido "
                    + "en el modulo " + this.getClass().getName());
            ++servers;
            generateAction(arrivingCQ);
        } else {
            System.out.println("Arrive: El cliente: " + arrivingCQ.clientID + " fue encolado "
                    + "en el modulo " + this.getClass().getName());
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
            System.out.println("Leave: El cliente: " + leavingCQ.clientID + " sale del modulo "
                    + this.getClass().getName());
            generateAction(queryQueue.poll()); //I need to generate the LEAVE of the waiting client that I put to be attended
            queueSizeRegister.add(queryQueue.size());
        } else { //If there isn't client waiting to be attended
            System.out.println("Leave: El cliente: " + leavingCQ.clientID + " sale del modulo "
                    + this.getClass().getName());
            --servers;
        }

        //I need to generate an ARRIVE on the next module for the leavingCQ
        generateNextModuleAction(leavingCQ);
    }

    @Override
    public void generateAction(ClientQuery clientQuery) {
        //I need to create a new LEAVE type event on this module for the client clientQuery
        System.out.println("Generate Action: Se genera una salida del cliente: " + clientQuery.clientID + " del modulo "
                + this.getClass().getName());
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
        //I need to check if the client clientQuery will have a timeout
        QueryStatistics qS = clientQuery.getQueryStatistics();
        if (eTime - qS.getSystemArriveTime() < simPintoDBPointer.getT()) {
            e = new Event(clientQuery, SimEvent.TIMEOUT, this, eTime);
        } else {
            e = new Event(clientQuery, SimEvent.LEAVE, this, eTime);
        }

        //I need to add the new event to the systemEventList
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        eQ.add(e);
    }

    @Override
    public void generateNextModuleAction(ClientQuery clientQuery) {
        //I need to create a new ARRIVE type event on the next module for the client clientQuery
        System.out.println("Generate Next Action: Se genera una llegada del cliente: " + clientQuery.clientID + " del modulo "
                + this.getClass().getName() + "al modulo" + this.nextModule.getClass().getName());
        Event e = new Event(clientQuery, SimEvent.ARRIVE, nextModule, simPintoDBPointer.getSimClock());

        //I need to add the new event to the systemEventList
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        eQ.add(e);
    }
}