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
public class IterationStatistics {
    
    private double averageQueryLifeTime;
    private double averageQueueSizeConnectionM;
    private double averageQueueSizeProcessM;
    private double averageQueueSizeQueryM;
    private double averageQueueSizeTransM;
    private double averageQueueSizeExecM;
    private double counterOfDeniedConnection;
    private StatementPerModuleStats connectionModStats;
    private StatementPerModuleStats processModStats;
    private StatementPerModuleStats queryProcModStats;
    private StatementPerModuleStats transactionModStats;
    private StatementPerModuleStats executionModStats;

    /**
    * 
    */    
    public IterationStatistics() {
        averageQueryLifeTime        = 0.0;
        averageQueueSizeConnectionM = 0.0;
        averageQueueSizeProcessM    = 0.0;
        averageQueueSizeQueryM      = 0.0;
        averageQueueSizeTransM      = 0.0;
        averageQueueSizeExecM       = 0.0;
        counterOfDeniedConnection   = 0.0;
        connectionModStats  = new StatementPerModuleStats();
        processModStats     = new StatementPerModuleStats();
        queryProcModStats   = new StatementPerModuleStats();
        transactionModStats = new StatementPerModuleStats();
        executionModStats   = new StatementPerModuleStats();
    }
    
    /**
     * 
     */
    public void printValues()
    {
        System.out.println("\nThis statistics were calculated with clients who finished service in system or in each module");
        System.out.println();        
        System.out.println("\tAverage denied connections: " + this.counterOfDeniedConnection );
        System.out.println("\tAverage query lifetime in system: " + this.averageQueryLifeTime );
        System.out.println("\tAverage Connection Module queue size: " + this.averageQueueSizeConnectionM );
        System.out.println("\tAverage Process Managment Module queue size: " + this.averageQueueSizeProcessM );
        System.out.println("\tAverage Query Processor Module queue size: " + this.averageQueueSizeQueryM );
        System.out.println("\tAverage Execution Module queue size: " + this.averageQueueSizeExecM );
        System.out.println("\tAverage Transaction Module queue size: " + this.averageQueueSizeTransM );
        System.out.println();        
        System.out.println("Average time of UPDATE statements per module");
        System.out.println("\tConnection Module: " + this.getConnectionModStats().getUpdateStatementTime() );
        System.out.println("\tProcess Managment Module: " + this.getProcessModStats().getUpdateStatementTime() );
        System.out.println("\tQuery Processor Module: " + this.getQueryProcModStats().getUpdateStatementTime() );
        System.out.println("\tExecution Module: " + this.getExecutionModStats().getUpdateStatementTime() );
        System.out.println("\tTransaction Module: " + this.getTransactionModStats().getUpdateStatementTime() );
        System.out.println();        
        System.out.println("Average time of DDL statements per module");
        System.out.println("\tConnection Module: " + this.getConnectionModStats().getDLLStatementTime() );
        System.out.println("\tProcess Managment Module: " + this.getProcessModStats().getDLLStatementTime() );
        System.out.println("\tQuery Processor Module: " + this.getQueryProcModStats().getDLLStatementTime() );
        System.out.println("\tExecution Module: " + this.getExecutionModStats().getDLLStatementTime() );
        System.out.println("\tTransaction Module: " + this.getTransactionModStats().getDLLStatementTime() );
        System.out.println();        
        System.out.println("Average time of SELECT statements per module");
        System.out.println("\tConnection Module: " + this.getConnectionModStats().getSelectStatementTime() );
        System.out.println("\tProcess Managment Module: " + this.getProcessModStats().getSelectStatementTime() );
        System.out.println("\tQuery Processor Module: " + this.getQueryProcModStats().getSelectStatementTime() );
        System.out.println("\tExecution Module: " + this.getExecutionModStats().getSelectStatementTime() );
        System.out.println("\tTransaction Module: " + this.getTransactionModStats().getSelectStatementTime() );        
        System.out.println();        
        System.out.println("Average time of JOIN statements per module");
        System.out.println("\tConnection Module: " + this.getConnectionModStats().getJoinStatementTime() );
        System.out.println("\tProcess Managment Module: " + this.getProcessModStats().getJoinStatementTime() );
        System.out.println("\tQuery Processor Module: " + this.getQueryProcModStats().getJoinStatementTime() );
        System.out.println("\tExecution Module: " + this.getExecutionModStats().getJoinStatementTime() );
        System.out.println("\tTransaction Module: " + this.getTransactionModStats().getJoinStatementTime() ); 
    }
    
    
    /**
     * 
     * @param other 
     */
    public void addOtherValues( IterationStatistics other )
    {
        this.averageQueryLifeTime        += other.averageQueryLifeTime;
        this.averageQueueSizeConnectionM += other.averageQueueSizeConnectionM;
        this.averageQueueSizeExecM       += other.averageQueueSizeExecM;
        this.averageQueueSizeProcessM    += other.averageQueueSizeProcessM;
        this.averageQueueSizeQueryM      += other.averageQueueSizeQueryM;
        this.averageQueueSizeTransM      += other.averageQueueSizeTransM;
        this.counterOfDeniedConnection   += other.counterOfDeniedConnection;
        this.connectionModStats.addOtherValues( other.getConnectionModStats() );
        this.processModStats.addOtherValues( other.getProcessModStats() );
        this.queryProcModStats.addOtherValues( other.getQueryProcModStats() );        
        this.transactionModStats.addOtherValues( other.getTransactionModStats() );
        this.executionModStats.addOtherValues( other.getExecutionModStats() );
    }
    
    /**
     * 
     */
    public void clean()
    {
        averageQueryLifeTime        = 0.0;
        averageQueueSizeConnectionM = 0.0;
        averageQueueSizeProcessM    = 0.0;
        averageQueueSizeQueryM      = 0.0;
        averageQueueSizeTransM      = 0.0;
        averageQueueSizeExecM       = 0.0;
        counterOfDeniedConnection   = 0.0;
        connectionModStats.clean();
        processModStats.clean();
        queryProcModStats.clean();
        transactionModStats.clean();
        executionModStats.clean();
    }

    /**
     * 
     * @return 
     */
    public double getCounterOfDeniedConnection() {
        return counterOfDeniedConnection;
    }

    /**
     * 
     * @param counterOfDeniedConnection 
     */
    public void setCounterOfDeniedConnection(double counterOfDeniedConnection) {
        this.counterOfDeniedConnection = counterOfDeniedConnection;
    }
        
    /**
    * 
     * @return 
    */        
    public double getAverageQueryLifeTime() {
        return averageQueryLifeTime;
    }

    /**
    * 
     * @param averageQueryLifeTime
    */        
    public void setAverageQueryLifeTime(double averageQueryLifeTime) {
        this.averageQueryLifeTime = averageQueryLifeTime;
    }

    /**
    * 
     * @return 
    */        
    public double getAverageQueueSizeConnectionM() {
        return averageQueueSizeConnectionM;
    }

    /**
    * 
     * @param averageQueueSizeConnectionM
    */        
    public void setAverageQueueSizeConnectionM(double averageQueueSizeConnectionM) {
        this.averageQueueSizeConnectionM = averageQueueSizeConnectionM;
    }

    /**
    * 
     * @return 
    */        
    public double getAverageQueueSizeProcessM() {
        return averageQueueSizeProcessM;
    }

    /**
    * 
     * @param averageQueueSizeProcessM
    */        
    public void setAverageQueueSizeProcessM(double averageQueueSizeProcessM) {
        this.averageQueueSizeProcessM = averageQueueSizeProcessM;
    }

    /**
    * 
     * @return 
    */        
    public double getAverageQueueSizeQueryM() {
        return averageQueueSizeQueryM;
    }

    /**
    * 
     * @param averageQueueSizeQueryM
    */        
    public void setAverageQueueSizeQueryM(double averageQueueSizeQueryM) {
        this.averageQueueSizeQueryM = averageQueueSizeQueryM;
    }

    /**
    * 
     * @return 
    */        
    public double getAverageQueueSizeTransM() {
        return averageQueueSizeTransM;
    }

    /**
    * 
     * @param averageQueueSizeTransM
    */        
    public void setAverageQueueSizeTransM(double averageQueueSizeTransM) {
        this.averageQueueSizeTransM = averageQueueSizeTransM;
    }

    /**
    * 
     * @return 
    */        
    public double getAverageQueueSizeExecM() {
        return averageQueueSizeExecM;
    }

    /**
    * 
     * @param averageQueueSizeExecM
    */        
    public void setAverageQueueSizeExecM(double averageQueueSizeExecM) {
        this.averageQueueSizeExecM = averageQueueSizeExecM;
    }

    /**
    * 
     * @return 
    */        
    public StatementPerModuleStats getConnectionModStats() {
        return connectionModStats;
    }

    /**
    * 
     * @param connectionModStats
    */        
    public void setConnectionModStats(StatementPerModuleStats connectionModStats) {
        this.connectionModStats = connectionModStats;
    }

    /**
    * 
     * @return 
    */        
    public StatementPerModuleStats getProcessModStats() {
        return processModStats;
    }

    /**
    * 
     * @param processModStats
    */        
    public void setProcessModStats(StatementPerModuleStats processModStats) {
        this.processModStats = processModStats;
    }

    /**
    * 
     * @return 
    */        
    public StatementPerModuleStats getQueryProcModStats() {
        return queryProcModStats;
    }

    /**
    * 
     * @param queryProcModStats
    */        
    public void setQueryProcModStats(StatementPerModuleStats queryProcModStats) {
        this.queryProcModStats = queryProcModStats;
    }

    /**
    * 
     * @return 
    */        
    public StatementPerModuleStats getTransactionModStats() {
        return transactionModStats;
    }

    /**
    * 
     * @param transactionModStats
    */        
    public void setTransactionModStats(StatementPerModuleStats transactionModStats) {
        this.transactionModStats = transactionModStats;
    }

    /**
    * 
     * @return 
    */        
    public StatementPerModuleStats getExecutionModStats() {
        return executionModStats;
    }

    /**
    * 
     * @param executionModStats
    */        
    public void setExecutionModStats(StatementPerModuleStats executionModStats) {
        this.executionModStats = executionModStats;
    }            
}
