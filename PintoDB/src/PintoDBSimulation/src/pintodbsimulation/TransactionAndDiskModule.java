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
                        System.out.println("TimeOut: El cliente: " + outgoingCQ.clientID + " de tipo " + outgoingCQ.getQueryType() + " fue sacado de ser antendido"
                                + "del modulo " + this.getClass().getName() + " y el tiempo actual es " + e.getClockTime());

                        try {
                            // thread to sleep for 1000 milliseconds
                            Thread.sleep(1000);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }

                        queryPriorityQueue.poll();
                        generateAction(nextCQ); //I need to generate the LEAVE of the waiting client that I put to be attended
                        servers = maxServers;
                        queueSizeRegister.add(queryPriorityQueue.size());
                    } else {
                        --servers;
                        System.out.println("TimeOut: El cliente: " + outgoingCQ.clientID + " de tipo " + outgoingCQ.getQueryType() + " fue sacado de ser antendido"
                                + "del modulo " + this.getClass().getName() + " y el tiempo actual es " + e.getClockTime());
                        try {
                            // thread to sleep for 1000 milliseconds
                            Thread.sleep(1000);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }
                } else {
                    System.out.println("TimeOut: El cliente: " + outgoingCQ.clientID + " de tipo " + outgoingCQ.getQueryType() + " fue sacado de ser antendido"
                            + "del modulo " + this.getClass().getName() + " y el tiempo actual es " + e.getClockTime());
                    try {
                        // thread to sleep for 1000 milliseconds
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    queryPriorityQueue.poll();
                    generateAction(nextCQ); //I need to generate the LEAVE of the waiting client that I put to be attended
                    queueSizeRegister.add(queryPriorityQueue.size());
                }
            } else { //If there isn't client waiting to be attended
                --servers;
            }
        }
        System.out.println("TimeOut: El cliente: " + outgoingCQ.clientID + " de tipo " + outgoingCQ.getQueryType() + " fue sacado de la cola "
                + "del modulo " + this.getClass().getName() + " y el tiempo actual es " + e.getClockTime());
        try {
            // thread to sleep for 1000 milliseconds
            Thread.sleep(1000);
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
        arrivingQS.setModuleArriveTime(e.getClockTime()); //I need to update the outgoing client data

        if (queryPriorityQueue.size() > 0) {
            ClientQuery nextCQ = queryPriorityQueue.peek();
            if (nextCQ.getQueryType() != StatementType.DDL) {
                if (servers < maxServers) {
                    System.out.println("Arrive: El cliente: " + arrivingCQ.clientID + " de tipo " + arrivingCQ.getQueryType() + " fue pasado de ser antendido "
                            + "en el modulo " + this.getClass().getName() + " y el tiempo actual es " + e.getClockTime());
                    try {
                        // thread to sleep for 1000 milliseconds
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    ++servers;
                    generateAction(arrivingCQ);
                } else {
                    System.out.println("Arrive: El cliente: " + arrivingCQ.clientID + " de tipo " + arrivingCQ.getQueryType() + " fue encolado "
                            + "en el modulo " + this.getClass().getName() + " y el tiempo actual es " + e.getClockTime());
                    try {
                        // thread to sleep for 1000 milliseconds
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    queryPriorityQueue.add(arrivingCQ);
                    queueSizeRegister.add(queryPriorityQueue.size());
                }
            } else if (servers == 0) {
                System.out.println("Arrive: El cliente: " + arrivingCQ.clientID + " de tipo " + arrivingCQ.getQueryType() + " fue pasado de ser antendido "
                        + "en el modulo " + this.getClass().getName() + " y el tiempo actual es " + e.getClockTime());
                try {
                    // thread to sleep for 1000 milliseconds
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                servers = maxServers;
                generateAction(arrivingCQ);
            } else {
                System.out.println("Arrive: El cliente: " + arrivingCQ.clientID + " de tipo " + arrivingCQ.getQueryType() + " fue encolado "
                        + "en el modulo " + this.getClass().getName() + " y el tiempo actual es " + e.getClockTime());
                try {
                    // thread to sleep for 1000 milliseconds
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                queryPriorityQueue.add(arrivingCQ);
                queueSizeRegister.add(queryPriorityQueue.size());
            }
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

        if (queryPriorityQueue.size() > 0) { //If there are waiting clients on the module queue
            ClientQuery nextCQ = this.queryPriorityQueue.peek();
            if (nextCQ.getQueryType() == StatementType.DDL) {
                if (servers == 1) {
                    System.out.println("Leave: El cliente: " + leavingCQ.clientID + " de tipo: " + leavingCQ.getQueryType() + " y sale del modulo "
                            + this.getClass().getName() + " y el tiempo actual es " + e.getClockTime());
                    try {
                        // thread to sleep for 1000 milliseconds
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    queryPriorityQueue.poll();
                    generateAction(nextCQ); //I need to generate the LEAVE of the waiting client that I put to be attended
                    servers = maxServers;
                    queueSizeRegister.add(queryPriorityQueue.size());
                } else {
                    --servers;
                    System.out.println("Leave: El cliente: " + leavingCQ.clientID + " de tipo: " + leavingCQ.getQueryType() + " y sale del modulo "
                            + this.getClass().getName() + " y el tiempo actual es " + e.getClockTime());
                    try {
                        // thread to sleep for 1000 milliseconds
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            } else {
                System.out.println("Leave: El cliente: " + leavingCQ.clientID + " de tipo: " + leavingCQ.getQueryType() + " y sale del modulo "
                        + this.getClass().getName() + " y el tiempo actual es " + e.getClockTime());
                try {
                    // thread to sleep for 1000 milliseconds
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                queryPriorityQueue.poll();
                generateAction(nextCQ); //I need to generate the LEAVE of the waiting client that I put to be attended
                queueSizeRegister.add(queryPriorityQueue.size());
            }
        } else { //If there isn't client waiting to be attended
            --servers;
        }
        //I need to generate an ARRIVE on the next module for the leavingCQ
        generateNextModuleAction(leavingCQ);
    }

    @Override
    public void generateAction(ClientQuery clientQuery) {
        //I need to create a new LEAVE type event on this module for the client clientQuery
        System.out.println("Generate Action: Se genera una salida del cliente: " + clientQuery.clientID + " de tipo " + clientQuery.getQueryType() + " del modulo "
                + this.getClass().getName() + " y el tiempo actual es " + simPintoDBPointer.getSimClock());
        try {
            // thread to sleep for 1000 milliseconds
            Thread.sleep(1000);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        Event e;
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
        eTime += maxServers * 0.03;
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
        System.out.println("Generate Next Action: Se genera una llegada del cliente: " + clientQuery.clientID + " de tipo " + clientQuery.getQueryType() + " del modulo "
                + this.getClass().getName() + "al modulo" + this.nextModule.getClass().getName() + " y el tiempo actual es " + simPintoDBPointer.getSimClock());
        try {
            // thread to sleep for 1000 milliseconds
            Thread.sleep(1000);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        Event e = new Event(clientQuery, SimEvent.ARRIVE, nextModule, simPintoDBPointer.getSimClock());
        //I need to add the new event to the systemEventList
        PriorityQueue<Event> eQ = simPintoDBPointer.getSistemEventList();
        eQ.add(e);
    }
}
