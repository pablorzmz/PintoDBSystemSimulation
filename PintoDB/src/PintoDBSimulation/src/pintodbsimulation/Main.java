package pintodbsimulation;

import java.util.PriorityQueue;

public class Main {

        public void TestingEventList()
    {                
        PriorityQueue<Event> pq = new PriorityQueue<>(new EventComparator());
        ClientQuery dummy = new ClientQuery(StatementType.UPDATE, null);
        Event e;
        
        e = new Event(dummy, SimEvent.LEAVE, null, 4);
        pq.add(e);
        e = new Event(dummy, SimEvent.LEAVE, null, 3);
        pq.add(e);
        e = new Event(dummy, SimEvent.LEAVE, null, 2);
        pq.add(e);
        
        
        e = new Event(dummy, SimEvent.ARRIVE, null, 3);
        pq.add(e);
        e = new Event(dummy, SimEvent.ARRIVE, null, 2);
        pq.add(e);
        e = new Event(dummy, SimEvent.ARRIVE, null, 3);
        pq.add(e);
        
        
        e = new Event(dummy, SimEvent.TIMEOUT, null, 3);
        pq.add(e);
        e = new Event(dummy, SimEvent.TIMEOUT, null, 2);
        pq.add(e);
        e = new Event(dummy, SimEvent.TIMEOUT, null, 1);
        pq.add(e);
        
        for(int w = 0; w < 9; ++w )
        {
            Event temp = pq.poll();
            System.out.println(temp.getEventType() + ": " + temp.getClockTime() );
        }
    }
    public static void main(String[] args) {
        SimPintoDB pintoDB = new SimPintoDB();
        //pintoDB.run();
        Main m = new Main();
        m.TestingEventList();
        
    }    
}
