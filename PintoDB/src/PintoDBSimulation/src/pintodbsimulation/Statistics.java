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
        int clientsWhoFinishesService[] = new int[5];
        // initialize vector
        for (int x = 0; x < 5 ; ++x)
             clientsWhoFinishesService[ x ] = 0;
        
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
                        clientsWhoFinishesService[0] +=1;
                    }                    
                    // Time in execution module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInExecMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getExecutionModStats().setUpdateStatementTime
                        (currentIterationStats.getExecutionModStats().getUpdateStatementTime()
                                + currentClientTimeInModule );
                        clientsWhoFinishesService[1] +=1;
                    }                    
                    // Time in process managment mod
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInProcMgmtMod();
                    if ( INVALID_TIME !=  currentClientTimeInModule )
                    {
                        currentIterationStats.getProcessModStats().setUpdateStatementTime
                        (currentIterationStats.getProcessModStats().getUpdateStatementTime()
                                + currentClientTimeInModule );
                        clientsWhoFinishesService[2] +=1;
                    }                     
                    // Time in transaction processor module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInTransMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getTransactionModStats().setUpdateStatementTime
                        (currentIterationStats.getTransactionModStats().getUpdateStatementTime()
                                + currentClientTimeInModule );
                        clientsWhoFinishesService[3] +=1;
                    }                    
                    //Time in query processor module
                    currentClientTimeInModule = cl.get(index).getQueryStatistics().getTimeInQueryProcMod();
                    if ( INVALID_TIME != currentClientTimeInModule )
                    {
                        currentIterationStats.getQueryProcModStats().setUpdateStatementTime
                        (currentIterationStats.getQueryProcModStats().getUpdateStatementTime()
                                + currentClientTimeInModule );
                        clientsWhoFinishesService[4] +=1;
                    }                    
                    break;
                case DDL:
                    // Sum time for DDL queries                    
                    // Time in connection module
                    currentIterationStats.getConnectionModStats().setDLLStatementTime
                        (currentIterationStats.getConnectionModStats().getDLLStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInConnectionMod());
                    // Time in execution module
                    currentIterationStats.getExecutionModStats().setDLLStatementTime
                        (currentIterationStats.getExecutionModStats().getDLLStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInExecMod());
                    // Time in process managment mod
                    currentIterationStats.getProcessModStats().setDLLStatementTime
                        (currentIterationStats.getProcessModStats().getDLLStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInProcMgmtMod());
                    // Time in transaction processor module
                    currentIterationStats.getTransactionModStats().setDLLStatementTime
                        (currentIterationStats.getTransactionModStats().getDLLStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInTransMod());
                    //Time in query processor module
                    currentIterationStats.getQueryProcModStats().setDLLStatementTime
                        (currentIterationStats.getQueryProcModStats().getDLLStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInQueryProcMod());
                    break;
                case SELECT:
                    // Sum time for SELECT queries
                    
                    // Time in connection module
                    currentIterationStats.getConnectionModStats().setSelectStatementTime
                        (currentIterationStats.getConnectionModStats().getSelectStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInConnectionMod());
                    // Time in execution module
                    currentIterationStats.getExecutionModStats().setSelectStatementTime
                        (currentIterationStats.getExecutionModStats().getSelectStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInExecMod());
                    // Time in process managment mod
                    currentIterationStats.getProcessModStats().setSelectStatementTime
                        (currentIterationStats.getProcessModStats().getSelectStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInProcMgmtMod());
                    // Time in transaction processor module
                    currentIterationStats.getTransactionModStats().setSelectStatementTime
                        (currentIterationStats.getTransactionModStats().getSelectStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInTransMod());
                    //Time in query processor module
                    currentIterationStats.getQueryProcModStats().setSelectStatementTime
                        (currentIterationStats.getQueryProcModStats().getSelectStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInQueryProcMod());                    
                    break;
                case JOIN:
                    // Sum time for JOIN queries
                    
                    // Time in connection module
                    currentIterationStats.getConnectionModStats().setJoinStatementTime
                        (currentIterationStats.getConnectionModStats().getJoinStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInConnectionMod());
                    // Time in execution module
                    currentIterationStats.getExecutionModStats().setJoinStatementTime
                        (currentIterationStats.getExecutionModStats().getJoinStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInExecMod());
                    // Time in process managment mod
                    currentIterationStats.getProcessModStats().setJoinStatementTime
                        (currentIterationStats.getProcessModStats().getJoinStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInProcMgmtMod());
                    // Time in transaction processor module
                    currentIterationStats.getTransactionModStats().setJoinStatementTime
                        (currentIterationStats.getTransactionModStats().getJoinStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInTransMod());
                    //Time in query processor module
                    currentIterationStats.getQueryProcModStats().setJoinStatementTime
                        (currentIterationStats.getQueryProcModStats().getJoinStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInQueryProcMod());                     
                    break;
                default:
                    break;
            }
            // Add final query system lifetime            
            systemLifeTime = (cl.get(index).getQueryStatistics().getSystemLeaveTime()- cl.get(index).getQueryStatistics().getSystemArriveTime());
            currentIterationStats.setAverageQueryLifeTime( currentIterationStats.getAverageQueryLifeTime() + systemLifeTime );
        }
        // For each module calculate its average queue size               
                
        //Connection Module will be zero, no queue
        // Process Managment Module
        this.calculateQueueAverageSizes( this.pointerSimPintoDB.getProcessManagemnteModule() );
        // Query Processor Module
        this.calculateQueueAverageSizes( this.pointerSimPintoDB.getQueryProcessorModule() );
        // Execution Module
        this.calculateQueueAverageSizes( this.pointerSimPintoDB.getExecutionModule() );
        // Transaction Module
        this.calculateQueueAverageSizes( this.pointerSimPintoDB.getTransactionModule() );                
    }
    
    /**
     * 
     */
    public void generateFinalStatistics ()
    {
        
    }
}
