/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pintodbsimulation;

import java.util.Comparator;
import java.util.LinkedList;

/**
 *
 * @author pablo
 */
public class EventComparator implements Comparator< Event > {

    @Override
    public int compare( Event a, Event b ) {                            
        SimEvent eA = a.getEventType();                       
        SimEvent eB = b.getEventType();
        
        String  mA = a.getMod().getClass().getSimpleName();
        String  mB = b.getMod().getClass().getSimpleName();
        
        int priorityEventA = returnEventPriority(eA ,a.isQueueTimeOut() );
        int priorityEventB = returnEventPriority(eB, b.isQueueTimeOut() );
        
        int priorityModuleA = returnModulePriority(mA);
        int priorityModuleB = returnModulePriority(mB);
        
        forSorting eventA;
        eventA = new forSorting( a.getClockTime(), priorityEventA, priorityModuleA );
        
        forSorting eventB;
        eventB = new forSorting( b.getClockTime(), priorityEventB, priorityModuleB );
        
        LinkedList< forSorting > ls = new LinkedList<>();
        
        ls.add( eventA );
        ls.add( eventB );
        
        // Now sorting
        ls.sort( Comparator.comparing( forSorting::getModule ));
        ls.sort( Comparator.comparing( forSorting::getEvent )); 
        ls.sort( Comparator.comparing( forSorting::getTime ));               
        
        
        // now comparing
        if ( a.getClockTime() == b.getClockTime() 
                && priorityEventA == priorityEventB 
                && priorityModuleA == priorityModuleB )
        {
            return 0;
            
        }else if ( ls.get( 0 ).getTime() == a.getClockTime() 
                && ls.get( 0 ).getModule() == priorityModuleA 
                && ls.get( 0 ).getEvent() == priorityEventA ) 
        {
            return -1 ;
            
        }else
        {
            return 1;
        }                                         
    }
    
    /**
     * 
     */
    private class forSorting
    {

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
     * 
     * @param event
     * @return 
     */
    private int returnEventPriority( SimEvent event, boolean queueTimeOut )
    {
        int returnValue = -1;
        
        switch( event )
        {
            case TIMEOUT:
                if ( queueTimeOut )
                { 
                    returnValue = 0;
                }else
                {
                    returnValue = 1;
                }                
            break;
            
            case LEAVE:
                
                    returnValue = 3;          
            break;
            
            case ARRIVE:
                returnValue = 4;
            break;            
        }
        
        return  returnValue;
    }
    
    /**
     * 
     * @param nameM
     * @return 
     */
    private int returnModulePriority( String nameM)
    {
        int returnValue = -1;
        String connectionM = ConnectionModule.class.getSimpleName();
        String procM = ProcessManagmentModule.class.getSimpleName();
        String queryProcM = QueryProcessorModule.class.getSimpleName();
        String transactionM = TransactionAndDiskModule.class.getSimpleName();
        String executionM = ExecutionModule.class.getSimpleName();   
        
        if ( connectionM.equals( nameM ) )
        {
            returnValue = 5;
            
        }else if ( procM.equals( nameM ) )
        {
            returnValue = 4;
        }else if ( queryProcM.equals( nameM ) )
        {
            returnValue = 3;
        }else if ( transactionM.equals( nameM ) )
        {
            returnValue = 2;
            
        }else if ( executionM.equals( nameM ) )
        {
            returnValue = 1;
        }
        
        return returnValue;
    }    
        
}
