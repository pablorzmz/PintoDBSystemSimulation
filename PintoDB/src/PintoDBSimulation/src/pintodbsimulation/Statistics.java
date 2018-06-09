/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pintodbsimulation;

import java.util.LinkedList;

/**
 *
 * @author pablo
 */
public class Statistics {
    
    private IterationStatistics currentIterationStats;
    private IterationStatistics finalIterationStats;
    private SimPintoDB pointerSimPintoDB;
    public final double INVALID_TIME = -1.0;

    /**
     * 
     * @param pointerSimPintoDB 
     */
    public Statistics(SimPintoDB pointerSimPintoDB) {
        this.pointerSimPintoDB = pointerSimPintoDB;
        this.currentIterationStats = new IterationStatistics();
        this.finalIterationStats = new IterationStatistics();
    }
    
    /**
     * 
     * @param m 
     */
    private void calculateQueueAverageSizes(Module m)
    {
        int queueSize;
        double value = 0.0;
                
        //Connection Module will be zero, no queue
        // Process Managment Module
        queueSize = m.getQueueSizeRegister().size();
        for( int index = 0; index < queueSize; ++index )
        {
            value += m.getQueueSizeRegister().get(index);
        }
        value /= queueSize;
        currentIterationStats.setAverageQueueSizeProcessM(value);
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
                currentIterationStats.setAverageQueryLifeTime( currentIterationStats.getAverageQueryLifeTime() + systemLifeTime );
                clientsWhoFinishedSystemProcess += 1;
            }            
        }// end for
        // For each module calculate its average queue size               // For each module calculate its average queue size               
                
        //Connection Module will be zero, no queue
        // Process Managment Module
        this.calculateQueueAverageSizes( this.pointerSimPintoDB.getProcessManagemnteModule() );
        // Query Processor Module
        this.calculateQueueAverageSizes( this.pointerSimPintoDB.getQueryProcessorModule() );
        // Execution Module
        this.calculateQueueAverageSizes( this.pointerSimPintoDB.getExecutionModule() );
        // Transaction Module
        this.calculateQueueAverageSizes( this.pointerSimPintoDB.getTransactionModule() );    
        
        // Store denied connection counter
        ConnectionModule cm = (ConnectionModule)this.pointerSimPintoDB.getConnectionModule();
        currentIterationStats.setCounterOfDeniedConnection( 0 );        
    }
    
    /**
     * 
     */
    public void generateFinalStatistics ()
    {
        
    }
}
