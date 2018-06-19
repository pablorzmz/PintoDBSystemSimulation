/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pintodbsimulation;

import java.util.LinkedList;
import org.apache.commons.math3.distribution.TDistribution;

/**
 *
 * @author pablo
 */
public class Statistics {
    
    private final IterationStatistics currentIterationStats;
    private final IterationStatistics finalIterationStats;
    private final SimPintoDB pointerSimPintoDB;
    public  final static double INVALID_TIME = -1.0;
    private LinkedList<Double> itemsForVariance;
    private String confidentInterval;

    /**
     * 
     * @param pointerSimPintoDB 
     */
    public Statistics(SimPintoDB pointerSimPintoDB) {
        this.pointerSimPintoDB = pointerSimPintoDB;
        this.currentIterationStats = new IterationStatistics();
        this.finalIterationStats = new IterationStatistics();
        itemsForVariance = new LinkedList<>();
        confidentInterval = "";
    }        
    /**
     * 
     * @return 
     */
    public String getConfidentInterval() {
        String copy = confidentInterval;
        confidentInterval = "";
        return copy;
    }        
    /**
     * 
     * @return 
     */
    public IterationStatistics getCurrentIterationStats() {
        return currentIterationStats;
    }
    /**
     * 
     * @return 
     */
    public IterationStatistics getFinalIterationStats() {
        return finalIterationStats;
    }
    
    
    /**
     * 
     * @param arraySizes
     * @param statement 
     */
    private void calculateAverageTimePerStatementPerModule(int arraySizes[], StatementType statement )
    {
        double dummyValue;
        switch( statement )
        {
            case UPDATE:
                ////Connection Module
                dummyValue = currentIterationStats.getConnectionModStats().getUpdateStatementTime();
                dummyValue /= arraySizes[0]==0?1:arraySizes[0];
                currentIterationStats.getConnectionModStats().setUpdateStatementTime( dummyValue );
                ////Execution Module
                dummyValue = currentIterationStats.getExecutionModStats().getUpdateStatementTime();
                dummyValue /= arraySizes[1]==0?1:arraySizes[1];
                currentIterationStats.getExecutionModStats().setUpdateStatementTime( dummyValue );
                ////Process Managment Module
                dummyValue = currentIterationStats.getProcessModStats().getUpdateStatementTime();
                dummyValue /= arraySizes[2]==0?1:arraySizes[2];
                currentIterationStats.getProcessModStats().setUpdateStatementTime( dummyValue );
                ////Transaction Module
                dummyValue = currentIterationStats.getTransactionModStats().getUpdateStatementTime();
                dummyValue /= arraySizes[3]==0?1:arraySizes[3];
                currentIterationStats.getTransactionModStats().setUpdateStatementTime( dummyValue );
                ////Transaction Module
                dummyValue = currentIterationStats.getQueryProcModStats().getUpdateStatementTime();
                dummyValue /= arraySizes[4]==0?1:arraySizes[4];
                currentIterationStats.getQueryProcModStats().setUpdateStatementTime( dummyValue );                     
            break;
            case DDL:
                ////Connection Module
                dummyValue = currentIterationStats.getConnectionModStats().getDLLStatementTime();
                dummyValue /= arraySizes[0]==0?1:arraySizes[0];
                currentIterationStats.getConnectionModStats().setDLLStatementTime(dummyValue );
                ////Execution Module
                dummyValue = currentIterationStats.getExecutionModStats().getDLLStatementTime();
                dummyValue /= arraySizes[1]==0?1:arraySizes[1];
                currentIterationStats.getExecutionModStats().setDLLStatementTime( dummyValue );
                ////Process Managment Module
                dummyValue = currentIterationStats.getProcessModStats().getDLLStatementTime();
                dummyValue /= arraySizes[2]==0?1:arraySizes[2];
                currentIterationStats.getProcessModStats().setDLLStatementTime( dummyValue );
                ////Transaction Module
                dummyValue = currentIterationStats.getTransactionModStats().getDLLStatementTime();
                dummyValue /= arraySizes[3]==0?1:arraySizes[3];
                currentIterationStats.getTransactionModStats().setDLLStatementTime( dummyValue );
                ////Transaction Module
                dummyValue = currentIterationStats.getQueryProcModStats().getDLLStatementTime();
                dummyValue /= arraySizes[4]==0?1:arraySizes[4];
                currentIterationStats.getQueryProcModStats().setDLLStatementTime( dummyValue );                                     
            break;
            case SELECT:
                ////Connection Module
                dummyValue = currentIterationStats.getConnectionModStats().getSelectStatementTime();
                dummyValue /= arraySizes[0]==0?1:arraySizes[0];
                currentIterationStats.getConnectionModStats().setSelectStatementTime(dummyValue );
                ////Execution Module
                dummyValue = currentIterationStats.getExecutionModStats().getSelectStatementTime();
                dummyValue /= arraySizes[1]==0?1:arraySizes[1];
                currentIterationStats.getExecutionModStats().setSelectStatementTime( dummyValue );
                ////Process Managment Module
                dummyValue = currentIterationStats.getProcessModStats().getSelectStatementTime();
                dummyValue /= arraySizes[2]==0?1:arraySizes[2];
                currentIterationStats.getProcessModStats().setSelectStatementTime( dummyValue );
                ////Transaction Module
                dummyValue = currentIterationStats.getTransactionModStats().getSelectStatementTime();
                dummyValue /= arraySizes[3]==0?1:arraySizes[3];
                currentIterationStats.getTransactionModStats().setSelectStatementTime( dummyValue );
                ////Transaction Module
                dummyValue = currentIterationStats.getQueryProcModStats().getSelectStatementTime();
                dummyValue /= arraySizes[4]==0?1:arraySizes[4];
                currentIterationStats.getQueryProcModStats().setSelectStatementTime(dummyValue );                                                     
            break;    
            case JOIN:
                ////Connection Module
                dummyValue = currentIterationStats.getConnectionModStats().getJoinStatementTime();
                dummyValue /= arraySizes[0]==0?1:arraySizes[0];
                currentIterationStats.getConnectionModStats().setJoinStatementTime(dummyValue );
                ////Execution Module
                dummyValue = currentIterationStats.getExecutionModStats().getJoinStatementTime();
                dummyValue /= arraySizes[1]==0?1:arraySizes[1];
                currentIterationStats.getExecutionModStats().setJoinStatementTime( dummyValue );
                ////Process Managment Module
                dummyValue = currentIterationStats.getProcessModStats().getJoinStatementTime();
                dummyValue /= arraySizes[2]==0?1:arraySizes[2];
                currentIterationStats.getProcessModStats().setJoinStatementTime( dummyValue );
                ////Transaction Module
                dummyValue = currentIterationStats.getTransactionModStats().getJoinStatementTime();
                dummyValue /= arraySizes[3]==0?1:arraySizes[3];
                currentIterationStats.getTransactionModStats().setJoinStatementTime( dummyValue );
                ////Transaction Module
                dummyValue = currentIterationStats.getQueryProcModStats().getJoinStatementTime();
                dummyValue /= arraySizes[4]==0?1:arraySizes[4];
                currentIterationStats.getQueryProcModStats().setJoinStatementTime(dummyValue );                                                                     
            break;
        }                        
    }
    /**
     * 
     * @param m 
     */
    private double calculateQueueAverageSizes(Module m)
    {
        int queueSize;
        double value = 0.0;
                
        //Connection Module will be zero, no queue
        // Process Managment Module
        /*queueSize = m.getQueueSizeRegister().size();
        for( int index = 0; index < queueSize; ++index )
        {                     
            value += m.getQueueSizeRegister().get(index);
        }
        value /= queueSize==0?1:queueSize;*/
        value = m.getQueueSizesAccumulator();
        value /= m.getQueueSizesCounter()==0?1:m.getQueueSizesCounter();
        return value;
    }
    /**
     * 
     */
    public void generateStatistics()
    {
        // We need to refer to the cliente list
        LinkedList<ClientQuery> cl = this.pointerSimPintoDB.getClients();
        double systemLifeTime;
        double currentClientTimeInModule;
        // For counting just clients who finished service per module
        int[] updateClientsWhoFinishedService = new int[5];
        int[] DDLClientsWhoFinishedService =    new int[5];
        int[] selectClientsWhoFinishedService = new int[5];
        int[] joinClientsWhoFinishedService =   new int[5];
        // For counting amount of clients who really finished srevice in system
        int clientsWhoFinishedSystemProcess = 0;
        // initialize vector
        for (int x = 0; x < 5 ; ++x)
        {
            updateClientsWhoFinishedService[ x ] = 0;        
            DDLClientsWhoFinishedService[ x ] = 0;
            selectClientsWhoFinishedService[ x ] = 0;
            joinClientsWhoFinishedService[ x ] = 0;
        }                 
        for (int index = 0; index < cl.size(); ++ index )
        {
            if ( null != cl.get(index).getQueryType() )
            // Sum time for UPDATE queries
            switch (cl.get(index).getQueryType()) {
                case UPDATE:
                    // Time in connection module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInConnectionMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getConnectionModStats().setUpdateStatementTime
                        (currentIterationStats.getConnectionModStats().getUpdateStatementTime()
                                + currentClientTimeInModule);
                        updateClientsWhoFinishedService[0] +=1;
                    }                    
                    // Time in execution module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInExecMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getExecutionModStats().setUpdateStatementTime
                        (currentIterationStats.getExecutionModStats().getUpdateStatementTime()
                                + currentClientTimeInModule );
                        updateClientsWhoFinishedService[1] +=1;
                    }                    
                    // Time in process managment mod
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInProcMgmtMod();
                    if ( INVALID_TIME !=  currentClientTimeInModule )
                    {
                        currentIterationStats.getProcessModStats().setUpdateStatementTime
                        (currentIterationStats.getProcessModStats().getUpdateStatementTime()
                                + currentClientTimeInModule );
                        updateClientsWhoFinishedService[2] +=1;
                    }                     
                    // Time in transaction processor module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInTransMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getTransactionModStats().setUpdateStatementTime
                        (currentIterationStats.getTransactionModStats().getUpdateStatementTime()
                                + currentClientTimeInModule );
                        updateClientsWhoFinishedService[3] +=1;
                    }                    
                    //Time in query processor module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInQueryProcMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getQueryProcModStats().setUpdateStatementTime
                        (currentIterationStats.getQueryProcModStats().getUpdateStatementTime()
                                + currentClientTimeInModule );
                        updateClientsWhoFinishedService[4] +=1;
                    }                    
                    break;
                case DDL:
                    // Sum time for DDL queries                    
                    // Time in connection module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInConnectionMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getConnectionModStats().setDLLStatementTime
                        (currentIterationStats.getConnectionModStats().getDLLStatementTime()
                                + currentClientTimeInModule);
                        DDLClientsWhoFinishedService[0] +=1;
                    }                    
                    // Time in execution module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInExecMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getExecutionModStats().setDLLStatementTime
                        (currentIterationStats.getExecutionModStats().getDLLStatementTime()
                                + currentClientTimeInModule );
                        DDLClientsWhoFinishedService[1] +=1;
                    }                    
                    // Time in process managment mod
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInProcMgmtMod();
                    if ( INVALID_TIME !=  currentClientTimeInModule )
                    {
                        currentIterationStats.getProcessModStats().setDLLStatementTime
                        (currentIterationStats.getProcessModStats().getDLLStatementTime()
                                + currentClientTimeInModule );
                        DDLClientsWhoFinishedService[2] +=1;
                    }                     
                    // Time in transaction processor module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInTransMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getTransactionModStats().setDLLStatementTime
                        (currentIterationStats.getTransactionModStats().getDLLStatementTime()
                                + currentClientTimeInModule );
                        DDLClientsWhoFinishedService[3] +=1;
                    }                    
                    //Time in query processor module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInQueryProcMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getQueryProcModStats().setDLLStatementTime
                        (currentIterationStats.getQueryProcModStats().getDLLStatementTime()
                                + currentClientTimeInModule );
                        DDLClientsWhoFinishedService[4] +=1;
                    }   
                    break;
                case SELECT:
                    // Sum time for SELECT queries
                    // Time in connection module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInConnectionMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getConnectionModStats().setSelectStatementTime
                        (currentIterationStats.getConnectionModStats().getSelectStatementTime()
                                + currentClientTimeInModule);
                        selectClientsWhoFinishedService[0] +=1;
                    }                    
                    // Time in execution module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInExecMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getExecutionModStats().setSelectStatementTime
                        (currentIterationStats.getExecutionModStats().getSelectStatementTime()
                                + currentClientTimeInModule );
                        selectClientsWhoFinishedService[1] +=1;
                    }                    
                    // Time in process managment mod
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInProcMgmtMod();
                    if ( INVALID_TIME !=  currentClientTimeInModule )
                    {
                        currentIterationStats.getProcessModStats().setSelectStatementTime
                        (currentIterationStats.getProcessModStats().getSelectStatementTime()
                                + currentClientTimeInModule );
                        selectClientsWhoFinishedService[2] +=1;
                    }                     
                    // Time in transaction processor module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInTransMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getTransactionModStats().setSelectStatementTime
                        (currentIterationStats.getTransactionModStats().getSelectStatementTime()
                                + currentClientTimeInModule );
                        selectClientsWhoFinishedService[3] +=1;
                    }                    
                    //Time in query processor module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInQueryProcMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getQueryProcModStats().setSelectStatementTime
                        (currentIterationStats.getQueryProcModStats().getSelectStatementTime()
                                + currentClientTimeInModule );
                        selectClientsWhoFinishedService[4] +=1;
                    }                       
                    break;
                case JOIN:
                    // Sum time for JOIN queries
                    // Time in connection module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInConnectionMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getConnectionModStats().setJoinStatementTime
                        (currentIterationStats.getConnectionModStats().getJoinStatementTime()
                                + currentClientTimeInModule);
                        joinClientsWhoFinishedService[0] +=1;
                    }                    
                    // Time in execution module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInExecMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getExecutionModStats().setJoinStatementTime
                        (currentIterationStats.getExecutionModStats().getJoinStatementTime()
                                + currentClientTimeInModule );
                        joinClientsWhoFinishedService[1] +=1;
                    }                    
                    // Time in process managment mod
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInProcMgmtMod();
                    if ( INVALID_TIME !=  currentClientTimeInModule )
                    {
                        currentIterationStats.getProcessModStats().setJoinStatementTime
                        (currentIterationStats.getProcessModStats().getJoinStatementTime()
                                + currentClientTimeInModule );
                        joinClientsWhoFinishedService[2] +=1;
                    }                     
                    // Time in transaction processor module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInTransMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getTransactionModStats().setJoinStatementTime
                        (currentIterationStats.getTransactionModStats().getJoinStatementTime()
                                + currentClientTimeInModule );
                        joinClientsWhoFinishedService[3] +=1;
                    }                    
                    //Time in query processor module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInQueryProcMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getQueryProcModStats().setJoinStatementTime
                        (currentIterationStats.getQueryProcModStats().getJoinStatementTime()
                                + currentClientTimeInModule );
                        joinClientsWhoFinishedService[4] += 1;
                    }                    
                    break;
                default:
                    break;
            }// end switch
            // Add final query system lifetime 
            if ( cl.get(index).getQueryStatistics().getSystemLeaveTime() != INVALID_TIME )
            {                  
                systemLifeTime = (cl.get(index).getQueryStatistics().getSystemLeaveTime()- cl.get(index).getQueryStatistics().getSystemArriveTime());                
                currentIterationStats.setAverageQueryLifeTime( (long)systemLifeTime + currentIterationStats.getAverageQueryLifeTime() );
                clientsWhoFinishedSystemProcess += 1;
            }            
        }// end for
        // For each module calculate its average queue size               // For each module calculate its average queue size               
                
        //Connection Module will be zero, no queue
        double result;
        // Process Managment Module
        result = this.calculateQueueAverageSizes( this.pointerSimPintoDB.getProcessManagemnteModule() );
        this.currentIterationStats.setAverageQueueSizeProcessM( result );        
        // Query Processor Module
        result = this.calculateQueueAverageSizes( this.pointerSimPintoDB.getQueryProcessorModule() );
        this.currentIterationStats.setAverageQueueSizeQueryM( result );
        // Execution Module
        result = this.calculateQueueAverageSizes( this.pointerSimPintoDB.getExecutionModule() );
        this.currentIterationStats.setAverageQueueSizeExecM( result );
        // Transaction Module
        result = this.calculateQueueAverageSizes( this.pointerSimPintoDB.getTransactionModule() );    
        this.currentIterationStats.setAverageQueueSizeTransM( result );
        
        // Store denied connection counter
        ConnectionModule cm = (ConnectionModule)this.pointerSimPintoDB.getConnectionModule();
        currentIterationStats.setCounterOfDeniedConnection( cm.getDeniedConnectionCounter() );
        
        // finally, calculate averages per statement and per module
        calculateAverageTimePerStatementPerModule( updateClientsWhoFinishedService, StatementType.UPDATE );
        calculateAverageTimePerStatementPerModule( DDLClientsWhoFinishedService, StatementType.DDL );
        calculateAverageTimePerStatementPerModule( selectClientsWhoFinishedService, StatementType.SELECT );
        calculateAverageTimePerStatementPerModule( joinClientsWhoFinishedService, StatementType.JOIN );
        
        // calculate average query system lifetime
        
        result = currentIterationStats.getAverageQueryLifeTime()/(clientsWhoFinishedSystemProcess==0?1:clientsWhoFinishedSystemProcess);        
        currentIterationStats.setAverageQueryLifeTime( result );
        itemsForVariance.add( result );                
                       
        // store stats in final stats
        this.finalIterationStats.addOtherValues( currentIterationStats );        
    }
    
    /**
     * 
     */
    public void generateFinalStatistics ()
    {
        //calculate confident interval for 95%
        double alpha = 0.05;
        double average = finalIterationStats.getAverageQueryLifeTime() / itemsForVariance.size() ;
        double variance = 0;                
        if ( pointerSimPintoDB.gettimesToRunSimulation() > 1 )
        {
            TDistribution t = new TDistribution( itemsForVariance.size() - 1 ); 
            double tValue = Math.abs( t.inverseCumulativeProbability( alpha/2 ) );
            for (int index = 0; index < itemsForVariance.size(); ++index )
            {                
                variance += Math.pow((itemsForVariance.get( index ) - average) , 2);
            }            
            variance = variance / ( itemsForVariance.size() - 1 ); 

            double limit1 = average - tValue
                    * (Math.sqrt(variance / itemsForVariance.size() ));
            double limit2 = average + tValue
                    * (Math.sqrt(variance / itemsForVariance.size()));            
            confidentInterval = "With probability of " + ((1-alpha)*100) + "% the average query lifetime in system"+
                    " with " + itemsForVariance.size() + " samples will be in interval: "+
                    " [ " + limit1 + ", " + limit2 + " ]";
        }
               
        // Now just divide each time by the amount of times simulation run          
        // Final average query lifetime       
        finalIterationStats.setAverageQueryLifeTime( average );
        
        double dummy;      
        // Final averages sizes of queues per module
        ////Connection Module
        dummy = finalIterationStats.getAverageQueueSizeConnectionM();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.setAverageQueueSizeConnectionM( dummy );
        ////Process Management Module
        dummy = finalIterationStats.getAverageQueueSizeProcessM();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.setAverageQueueSizeProcessM( dummy );
        ////Query Processor Module
        dummy = finalIterationStats.getAverageQueueSizeQueryM();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.setAverageQueueSizeQueryM( dummy );
        ////Execution Module        
        dummy = finalIterationStats.getAverageQueueSizeExecM();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.setAverageQueueSizeExecM( dummy );
        //// Transaction Module
        dummy = finalIterationStats.getAverageQueueSizeTransM();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.setAverageQueueSizeTransM( dummy );
        
        // Final count of denied connections
        dummy = finalIterationStats.getCounterOfDeniedConnection();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.setCounterOfDeniedConnection( dummy );
        
        // Final average time per statemente and per module
        ////UPDATE
        /////Connection Module
        dummy = finalIterationStats.getConnectionModStats().getUpdateStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getConnectionModStats().setUpdateStatementTime( dummy );
        /////Process Management Module
        dummy = finalIterationStats.getProcessModStats().getUpdateStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getProcessModStats().setUpdateStatementTime( dummy );
        /////Query Processor Module
        dummy = finalIterationStats.getQueryProcModStats().getUpdateStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getQueryProcModStats().setUpdateStatementTime( dummy );
        /////Execution Module
        dummy = finalIterationStats.getExecutionModStats().getUpdateStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getExecutionModStats().setUpdateStatementTime( dummy );
        /////Transaction Module
        dummy = finalIterationStats.getTransactionModStats().getUpdateStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getTransactionModStats().setUpdateStatementTime( dummy );
        
        ////SELECT
        /////Connection Module
        dummy = finalIterationStats.getConnectionModStats().getSelectStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getConnectionModStats().setSelectStatementTime( dummy );
        /////Process Management Module
        dummy = finalIterationStats.getProcessModStats().getSelectStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getProcessModStats().setSelectStatementTime( dummy );
        /////Query Processor Module
        dummy = finalIterationStats.getQueryProcModStats().getSelectStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getQueryProcModStats().setSelectStatementTime( dummy );
        /////Execution Module
        dummy = finalIterationStats.getExecutionModStats().getSelectStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getExecutionModStats().setSelectStatementTime( dummy );
        /////Transaction Module
        dummy = finalIterationStats.getTransactionModStats().getSelectStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getTransactionModStats().setSelectStatementTime( dummy );                
        
        ////JOIN
        /////Connection Module
        dummy = finalIterationStats.getConnectionModStats().getJoinStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getConnectionModStats().setJoinStatementTime(dummy );
        /////Process Management Module
        dummy = finalIterationStats.getProcessModStats().getJoinStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getProcessModStats().setJoinStatementTime( dummy );
        /////Query Processor Module
        dummy = finalIterationStats.getQueryProcModStats().getJoinStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getQueryProcModStats().setJoinStatementTime( dummy );
        /////Execution Module
        dummy = finalIterationStats.getExecutionModStats().getJoinStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getExecutionModStats().setJoinStatementTime( dummy );
        /////Transaction Module
        dummy = finalIterationStats.getTransactionModStats().getJoinStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getTransactionModStats().setJoinStatementTime( dummy );  
        
        ////DDL
        /////Connection Module
        dummy = finalIterationStats.getConnectionModStats().getDLLStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getConnectionModStats().setDLLStatementTime(dummy );
        /////Process Management Module
        dummy = finalIterationStats.getProcessModStats().getDLLStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getProcessModStats().setDLLStatementTime( dummy );
        /////Query Processor Module
        dummy = finalIterationStats.getQueryProcModStats().getDLLStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getQueryProcModStats().setDLLStatementTime( dummy );
        /////Execution Module
        dummy = finalIterationStats.getExecutionModStats().getDLLStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getExecutionModStats().setDLLStatementTime( dummy );
        /////Transaction Module
        dummy = finalIterationStats.getTransactionModStats().getDLLStatementTime();
        dummy /= pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.getTransactionModStats().setDLLStatementTime( dummy ); 
        
        // averge of clientes recieved
        dummy = finalIterationStats.getTotalConnections();
        dummy /= this.pointerSimPintoDB.gettimesToRunSimulation();
        finalIterationStats.setTotalConnections( dummy );
    }
}
