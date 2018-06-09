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
     */
    public void generateStatistics()
    {
        // We need to refer to the cliente list
        LinkedList<ClientQuery> cl = this.pointerSimPintoDB.getClients();
        
        for (int index = 0; index < cl.size(); ++ index )
        {
            if ( null != cl.get(index).getQueryType() )
            // Sum time for UPDATE queries
            switch (cl.get(index).getQueryType()) {
                case UPDATE:
                    // Time in connection module
                    currentIterationStats.getConnectionModStats().setUpdateStatementTime
                        (currentIterationStats.getConnectionModStats().getUpdateStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInConnectionMod());
                    // Time in execution module
                    currentIterationStats.getExecutionModStats().setUpdateStatementTime
                        (currentIterationStats.getExecutionModStats().getUpdateStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInExecMod());
                    // Time in process managment mod
                    currentIterationStats.getProcessModStats().setUpdateStatementTime
                        (currentIterationStats.getProcessModStats().getUpdateStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInProcMgmtMod());
                    // Time in transaction processor module
                    currentIterationStats.getTransactionModStats().setUpdateStatementTime
                        (currentIterationStats.getTransactionModStats().getUpdateStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInTransMod());
                    //Time in query processor module
                    currentIterationStats.getQueryProcModStats().setUpdateStatementTime
                        (currentIterationStats.getQueryProcModStats().getUpdateStatementTime()
                                + cl.get(index).getQueryStatistics().getTimeInQueryProcMod());
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
        }    
    }
    
    /**
     * 
     */
    public void generateFinalStatistics ()
    {
        
    }
}
