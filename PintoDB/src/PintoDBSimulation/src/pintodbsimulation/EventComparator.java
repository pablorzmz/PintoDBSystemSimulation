/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pintodbsimulation;

import java.util.Comparator;

/**
 *
 * @author pablo
 */
public class EventComparator implements Comparator< Event > {

    @Override
    public int compare( Event a, Event b ) {
        
        SimEvent eA = a.getEventType();
        SimEvent eB = b.getEventType();
        
        if ( eA == SimEvent.TIMEOUT && ( eB == SimEvent.ARRIVE || eB == SimEvent.LEAVE ))
        {
            return -1;
            
        }else if ( eA == eB )
        {
            if ( a.getClockTime() > b.getClockTime() )
            {
                return 1;
                
            }else if ( a.getClockTime() < b.getClockTime() )
            {
                return -1;
                
            }else
            {
                return 0;
            }
            
        }else if ( eA == SimEvent.LEAVE && eB == SimEvent.ARRIVE )
        {
            return -1;
            
        }else if ( eA == SimEvent.ARRIVE && eB == SimEvent.LEAVE )
        {
            return 1;
            
        }else if ( ( eA == SimEvent.ARRIVE || eA ==  SimEvent.LEAVE ) && eB ==SimEvent.TIMEOUT )
        {
            return 1;
        }        
        return 0;
    }
    
}
