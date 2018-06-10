package pintodbsimulation;

import java.util.PriorityQueue;

public class ExecutionModule extends Module {

    public ExecutionModule(int servers, int maxServers, SimPintoDB simPintoDBPointer, Module nextModule) {
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
            if (queryQueue.size() > 0) { //If there are waiting clients on the module queue
                System.out.println("TimeOut: El cliente: " + outgoingCQ.clientID + " fue sacado de ser antendido"
<<<<<<< HEAD
                        + "del modulo " + "ejecución de consultas" + " y el tiempo actual es " + e.getClockTime() );
                        try {
            // thread to sleep for 1000 milliseconds
            Thread.sleep( SimPintoDB.sleepTime );
        } catch (Exception ex) {
            System.out.println(ex);
        }
=======
                        + "del modulo " + "ejecución de consultas" + " y el tiempo actual es " + e.getClockTime());
                try {
                    // thread to sleep for 1000 milliseconds
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
>>>>>>> 2b36555a96401cf6cafbc2d46c0d6f0831ca00b8
                generateAction(this.queryQueue.poll()); //I need to generate the LEAVE of the waiting client that I put to be attended
                queueSizeRegister.add(queryQueue.size());
            } else { //If there isn't client waiting to be attended
                --servers;
            }
        }
        System.out.println("TimeOut: El cliente: " + outgoingCQ.clientID + " fue sacado de la cola "
                + "del modulo " + "ejecución de consultas" + " y el tiempo actual es " + e.getClockTime());
        try {
            // thread to sleep for 1000 milliseconds
            Thread.sleep( SimPintoDB.sleepTime );
        } catch (Exception ex) {
            System.out.println(ex);
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
            System.out.println("Arrive: El cliente: " + arrivingCQ.clientID + " fue pasado de ser antendido "
                    + "en el modulo " + "ejecución de consultas" + " y el tiempo actual es " + e.getClockTime());
<<<<<<< HEAD
                    try {
            // thread to sleep for 1000 milliseconds
            Thread.sleep( SimPintoDB.sleepTime );
        } catch (Exception ex) {
            System.out.println(ex);
        }
=======
            try {
                // thread to sleep for 1000 milliseconds
                Thread.sleep(1000);
            } catch (Exception ex) {
                System.out.println(ex);
            }
>>>>>>> 2b36555a96401cf6cafbc2d46c0d6f0831ca00b8
            ++servers;
            generateAction(arrivingCQ);
        } else {
            System.out.println("Arrive: El cliente: " + arrivingCQ.clientID + " fue encolado "
                    + "en el modulo " + "ejecución de consultas" + " y el tiempo actual es " + e.getClockTime());
<<<<<<< HEAD
                    try {
            // thread to sleep for 1000 milliseconds
            Thread.sleep( SimPintoDB.sleepTime );
        } catch (Exception ex) {
            System.out.println(ex);
        }
=======
            try {
                // thread to sleep for 1000 milliseconds
                Thread.sleep(1000);
            } catch (Exception ex) {
                System.out.println(ex);
            }
>>>>>>> 2b36555a96401cf6cafbc2d46c0d6f0831ca00b8
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
                    + "ejecución de consultas" + " y el tiempo actual es " + e.getClockTime());
<<<<<<< HEAD
                    try {
            // thread to sleep for 1000 milliseconds
           Thread.sleep( SimPintoDB.sleepTime );
        } catch (Exception ex) {
            System.out.println(ex);
        }
=======
            try {
                // thread to sleep for 1000 milliseconds
                Thread.sleep(1000);
            } catch (Exception ex) {
                System.out.println(ex);
            }
>>>>>>> 2b36555a96401cf6cafbc2d46c0d6f0831ca00b8
            generateAction(queryQueue.poll()); //I need to generate the LEAVE of the waiting client that I put to be attended
            queueSizeRegister.add(queryQueue.size());
        } else { //If there isn't client waiting to be attended
            System.out.println("Leave: El cliente: " + leavingCQ.clientID + " sale del modulo "
                    + "ejecución de consultas" + " y el tiempo actual es " + e.getClockTime());
<<<<<<< HEAD
                    try {
            // thread to sleep for 1000 milliseconds
            Thread.sleep( SimPintoDB.sleepTime );
        } catch (Exception ex) {
            System.out.println(ex);
        }
=======
            try {
                // thread to sleep for 1000 milliseconds
                Thread.sleep(1000);
            } catch (Exception ex) {
                System.out.println(ex);
            }
>>>>>>> 2b36555a96401cf6cafbc2d46c0d6f0831ca00b8
            --servers;
        }

        //I need to generate an ARRIVE on the next module for the leavingCQ
        generateNextModuleAction(leavingCQ);
    }

    @Override
    public void generateAction(ClientQuery clientQuery) {
        //I need to create a new LEAVE type event on this module for the client clientQuery
        System.out.println("Generate Action: Se genera una salida del cliente: " + clientQuery.clientID + " del modulo "
<<<<<<< HEAD
                + " ejecución de consultas" + " y el tiempo actual es " + simPintoDBPointer.getSimClock());
                try {
=======
                + "ejecución de consultas" + " y el tiempo actual es " + simPintoDBPointer.getSimClock());
        try {
>>>>>>> 2b36555a96401cf6cafbc2d46c0d6f0831ca00b8
            // thread to sleep for 1000 milliseconds
            Thread.sleep( SimPintoDB.sleepTime );
        } catch (Exception ex) {
            System.out.println(ex);
        }
        Event e;
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
        //I need to check if the client clientQuery will have a timeout
        QueryStatistics qS = clientQuery.getQueryStatistics();
        if (eTime - qS.getSystemArriveTime() > simPintoDBPointer.getT()) {
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
        //I need to create a new LEAVE type event on the next module for the client clientQuery
        System.out.println("Generate Next Action: Se genera una salida del cliente: " + clientQuery.clientID + " del modulo "
<<<<<<< HEAD
                + "ejecución de consultas" + " al modulo " + "de conexiones" + " y el tiempo actual es " + simPintoDBPointer.getSimClock());
                try {
=======
                + "ejecución de consultas" + "al modulo" + this.nextModule.getClass().getName() + " y el tiempo actual es " + simPintoDBPointer.getSimClock());
        try {
>>>>>>> 2b36555a96401cf6cafbc2d46c0d6f0831ca00b8
            // thread to sleep for 1000 milliseconds
            Thread.sleep( SimPintoDB.sleepTime );
        } catch (Exception ex) {
            System.out.println(ex);
        }
        Event e = new Event(clientQuery, SimEvent.LEAVE, nextModule, simPintoDBPointer.getSimClock());

        //I need to add the new event to the systemEventList
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        eQ.add(e);
    }
}
