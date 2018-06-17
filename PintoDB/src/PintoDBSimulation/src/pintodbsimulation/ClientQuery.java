package pintodbsimulation;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pablo
 */
public class ClientQuery {
    
    private StatementType queryType;
    private Module currentMod;
    private final QueryStatistics queryStatistics;
    //Test
    public int clientID;

     /**
     *
     * @param queryType
     * @param currentMod
     */
    public ClientQuery(StatementType queryType,Module currentMod) {
        this.queryType = queryType;
        this.currentMod = currentMod;
        queryStatistics = new QueryStatistics();
    }
    
    /**
     *
     */
    public void updateStats()
    {
        double timeInModule;
        String currentModuleName = currentMod.getClass().getSimpleName();
        String procM = ProcessManagmentModule.class.getSimpleName();
        String connectionM = ConnectionModule.class.getSimpleName();
        String queryProcM = QueryProcessorModule.class.getSimpleName();
        String executionM = ExecutionModule.class.getSimpleName();
        String transactionM = TransactionAndDiskModule.class.getSimpleName();
        timeInModule = queryStatistics.getModuleLeaveTime()- queryStatistics.getModuleArriveTime();

        if ( currentModuleName.equals( procM) )
        {            
            queryStatistics.setTimeInProcMgmtMod( timeInModule );
            
        }else if ( currentModuleName.equals(connectionM) )
        {
            queryStatistics.setTimeInConnectionMod( 0.0 );            
             
        }else if ( currentModuleName.equals(queryProcM) )
        {
            queryStatistics.setTimeInQueryProcMod( timeInModule );            
            
        }else if ( currentModuleName.equals(executionM) )
        {
            queryStatistics.setTimeInExecMod( timeInModule );
            
        }else if ( currentModuleName.equals(transactionM) )
        {
            queryStatistics.setTimeInTransMod( timeInModule );
        }
        queryStatistics.setModuleArriveTime( Statistics.INVALID_TIME );
        queryStatistics.setModuleLeaveTime( Statistics.INVALID_TIME );        
    }    

    /**
     *
     * @param queryType
     */
    public void setQueryType(StatementType queryType) {
        this.queryType = queryType;
    }

    /**
     *
     * @param currentMod
     */
    public void setCurrentMod(Module currentMod) {
        this.currentMod = currentMod;
    }

    /**
     *
     * @return 
     */
    public StatementType getQueryType() {
        return queryType;
    }

    /**
     *
     * @return 
     */
    public Module getCurrentMod() {
        return currentMod;
    }

     /**
     *
     * @return 
     */
    public QueryStatistics getQueryStatistics() {
        return queryStatistics;
    }             
}
