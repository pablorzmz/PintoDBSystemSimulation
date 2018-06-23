package pintodbsimulation;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * Comparator implementation for the {@code PriorityQueue} to compare
 * {@code Event} instances.
 *
 * @author B65477 B65728 B55830
 * @see Comparator
 * @see Event
 */
public class EventComparator implements Comparator< Event> {

    @Override
    public int compare(Event a, Event b) {
        SimEvent eA = a.getEventType();
        SimEvent eB = b.getEventType();

        String mA = a.getMod() == null ? "" : a.getMod().getClass().getSimpleName();
        String mB = b.getMod() == null ? "" : b.getMod().getClass().getSimpleName();

        int priorityEventA = returnEventPriority(eA);
        int priorityEventB = returnEventPriority(eB);

        int priorityModuleA = returnModulePriority(mA);
        int priorityModuleB = returnModulePriority(mB);

        forSorting eventA;
        eventA = new forSorting(a.getClockTime(), priorityEventA, priorityModuleA);

        forSorting eventB;
        eventB = new forSorting(b.getClockTime(), priorityEventB, priorityModuleB);

        LinkedList< forSorting> ls = new LinkedList<>();

        ls.add(eventA);
        ls.add(eventB);

        // Now sorting
        ls.sort(Comparator.comparing(forSorting::getModule));
        ls.sort(Comparator.comparing(forSorting::getEvent));
        ls.sort(Comparator.comparing(forSorting::getTime));

        // now comparing
        if (a.getClockTime() == b.getClockTime()
                && priorityEventA == priorityEventB
                && priorityModuleA == priorityModuleB) {
            return 0;

        } else if (ls.get(0).getTime() == a.getClockTime()
                && ls.get(0).getModule() == priorityModuleA
                && ls.get(0).getEvent() == priorityEventA) {
            return -1;

        } else {
            return 1;
        }
    }

    /**
     * Class use to implement the {@code SimEvent} comparator
     */
    private class forSorting {

        public forSorting(double time, int event, int module) {
            this.time = time;
            this.event = event;
            this.module = module;
        }

        public double getTime() {
            return time;
        }

        public int getEvent() {
            return event;
        }

        public int getModule() {
            return module;
        }

        private final double time;
        private final int event;
        private final int module;
    }

    /**
     * Returns an int value between 0 and 2 associated with each {@code SimEvent} value, 
     * which represent each type of event priority.
     *
     * @param event
     * @return Integer value associated with each event priority.
     * @see Event
     */
    private int returnEventPriority(SimEvent event) {
        int returnValue = -1;

        switch (event) {
            case TIMEOUT:
                returnValue = 0;
                break;
            case LEAVE:

                returnValue = 1;
                break;
            case ARRIVE:
                returnValue = 2;
                break;
        }
        return returnValue;
    }

    /**
     * Return an int value between 0 and 5 associated to the module name, 
     * which represent each module priority. 
     * Where the modules closer to the end of the service have more priority.
     *
     * @param nameM
     * @return Interger value associated with each module priority
     * @see Module
     */
    private int returnModulePriority(String nameM) {
        int returnValue = -1;
        String connectionM = ConnectionModule.class.getSimpleName();
        String procM = ProcessManagmentModule.class.getSimpleName();
        String queryProcM = QueryProcessorModule.class.getSimpleName();
        String transactionM = TransactionAndDiskModule.class.getSimpleName();
        String executionM = ExecutionModule.class.getSimpleName();

        if (connectionM.equals(nameM)) {
            returnValue = 5;
        } else if (procM.equals(nameM)) {
            returnValue = 4;
        } else if (queryProcM.equals(nameM)) {
            returnValue = 3;
        } else if (transactionM.equals(nameM)) {
            returnValue = 2;
        } else if (executionM.equals(nameM)) {
            returnValue = 1;
        } else if (nameM.equals("")) {
            returnValue = 0;
        }
        return returnValue;
    }
}
