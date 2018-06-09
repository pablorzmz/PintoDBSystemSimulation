/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pintodbsimulation;

/**
 *
 * @author pablo
 */
public class Event {
    
    private final ClientQuery clientQuery;
    private final SimEvent eventType;
    private final Module mod;
    private final double clockTime;

     /**
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
     *
     * @return 
     */    
    public ClientQuery getClientQuery() {
        return clientQuery;
    }

     /**
     *
     * @return 
     */    
    public SimEvent getEventType() {
        return eventType;
    }

     /**
     *
     * @return 
     */    
    public Module getMod() {
        return mod;
    }

     /**
     *
     * @return 
     */    
    public double getClockTime() {
        return clockTime;
    }
    
    
    
}
