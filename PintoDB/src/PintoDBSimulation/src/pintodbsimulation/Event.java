package pintodbsimulation;

/**
 * This class stores necessary information of an event for this simulation.
 *
 * @author B65477 B65728 B55830
 */
public class Event {

    private final ClientQuery clientQuery;
    private final SimEvent eventType;
    private final Module mod;
    private final double clockTime;

    /**
     * Class constructor.
     *
     * @param clientQuery
     * @param eventType
     * @param mod
     * @param clockTime
     */
    public Event(ClientQuery clientQuery, SimEvent eventType, Module mod, double clockTime) {
        this.clientQuery = clientQuery;
        this.eventType = eventType;
        this.mod = mod;
        this.clockTime = clockTime;
    }

    /**
     * Return this class clientQuery field value({@code ClientQuery}).
     *
     * @return clientQuery field
     * @see ClientQuery
     */
    public ClientQuery getClientQuery() {
        return clientQuery;
    }

    /**
     * Return this class eventType field valie ({@code SimEvent}).
     *
     * @return eventType field.
     * @see SimEvent
     */
    public SimEvent getEventType() {
        return eventType;
    }

    /**
     * Return this class mod field value ({@code Module}).
     *
     * @return mod field
     * @see Module
     */
    public Module getMod() {
        return mod;
    }

    /**
     * Return this class clockTime field value (double).
     *
     * @return clockTime field
     */
    public double getClockTime() {
        return clockTime;
    }
}
