package pintodbsimulation;

import java.util.PriorityQueue;

public class Main {

    public void TestingEventList() {
        PriorityQueue<Event> pq = new PriorityQueue<>(new EventComparator());
        ClientQuery dummy = new ClientQuery(StatementType.UPDATE, null);
        Event e;
        SimPintoDB x = new SimPintoDB();
        
        Module c, pm, qp, tr, ex;
        
        c = new ConnectionModule(0, 2, x, null);
        pm = new ProcessManagmentModule(0, 2, x, null);
        qp = new QueryProcessorModule(0, 2, x, null);
        tr = new TransactionAndDiskModule(0, 2, x, null);
        ex = new ExecutionModule(0, 2, x, null);
        
        
        e = new Event(dummy, SimEvent.LEAVE, c, 2);
        pq.add(e);
        e = new Event(dummy, SimEvent.LEAVE, tr, 2);
        pq.add(e);
        e = new Event(dummy, SimEvent.LEAVE, c, 2);
        pq.add(e);
        
        
        e = new Event(dummy, SimEvent.ARRIVE, c, 2);
        pq.add(e);
        e = new Event(dummy, SimEvent.ARRIVE, qp, 2);
        pq.add(e);
        e = new Event(dummy, SimEvent.ARRIVE, tr, 2);
        pq.add(e);
        
        
        e = new Event(dummy, SimEvent.TIMEOUT, pm, 2);
        e.setQueueTimeOut(true);
        pq.add(e);
        e = new Event(dummy, SimEvent.TIMEOUT, ex, 2);
        pq.add(e);
        e = new Event(dummy, SimEvent.TIMEOUT, qp, 2);
        pq.add(e);
        
        for( int w = 0; w < 9; ++w )
        {
            Event temp = pq.poll();
            System.out.println(temp.getEventType() + ", " + temp.getClockTime() 
                    + ", " + temp.getMod().getClass().getSimpleName() );
        }
        
        /*
        PriorityQueue<ClientQuery> cq = new PriorityQueue<>(10, new ClientQueryComparator());
        ClientQuery cQ;

        cQ = new ClientQuery(StatementType.UPDATE, null);
        cq.add(cQ);
        cQ = new ClientQuery(StatementType.SELECT, null);
        cq.add(cQ);
        cQ = new ClientQuery(StatementType.JOIN, null);
        cq.add(cQ);
        cQ = new ClientQuery(StatementType.UPDATE, null);
        cq.add(cQ);
        cQ = new ClientQuery(StatementType.DDL, null);
        cq.add(cQ);
        cQ = new ClientQuery(StatementType.JOIN, null);
        cq.add(cQ);
        cQ = new ClientQuery(StatementType.SELECT, null);
        cq.add(cQ);
        cQ = new ClientQuery(StatementType.DDL, null);
        cq.add(cQ);

        for (int w = 0; w < 8; ++w) {
            ClientQuery temp = cq.poll();
            System.out.println(temp.getQueryType());
        }*/
    }

    /*public static void main(String[] args) {
        SimPintoDB pintoDB = new SimPintoDB();
        pintoDB.run();
        MainForm mf = new MainForm();
        
        Main m = new Main();
        //m.TestingEventList();

    }
*/
}
