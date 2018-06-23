package pintodbsimulation;

/**
 * This class stores needed information for statistics calculation purpose.
 * Specificaly average iteration statistics.
 *
 * @author B65477 B65728 B55830
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
    private double totalConnections;
    private double timeOutsCounter;

    /**
     * Class constructor.
     */
    public IterationStatistics() {
        averageQueryLifeTime = 0.0;
        averageQueueSizeConnectionM = 0.0;
        averageQueueSizeProcessM = 0.0;
        averageQueueSizeQueryM = 0.0;
        averageQueueSizeTransM = 0.0;
        averageQueueSizeExecM = 0.0;
        counterOfDeniedConnection = 0.0;
        totalConnections = 0.0;
        timeOutsCounter = 0.0;
        connectionModStats = new StatementPerModuleStats();
        processModStats = new StatementPerModuleStats();
        queryProcModStats = new StatementPerModuleStats();
        transactionModStats = new StatementPerModuleStats();
        executionModStats = new StatementPerModuleStats();
    }

    /**
     * Print calculated statistics.
     *
     * @param averages
     * @return String with the calculate estatistics
     */
    public String resultStats(boolean averages) {
        String info = averages ? "Average" : "Amount of";
        String data
                = "\nThis statistics were calculated with clients who finished service in system or in each module\n"
                + "\n"
                + "\t" + info + " denied connections: " + this.counterOfDeniedConnection + " and it is a " + (this.counterOfDeniedConnection / totalConnections) * 100
                + " % of " + totalConnections + " connections\n"
                + "\t" + info + " timeouts: " + this.timeOutsCounter + "\n"
                + "\t" + info + " query lifetime in system: " + this.averageQueryLifeTime + "\n"
                + "\tAverage Connection Module queue size: " + this.averageQueueSizeConnectionM + "\n"
                + "\tAverage Process Managment Module queue size: " + this.averageQueueSizeProcessM + "\n"
                + "\tAverage Query Processor Module queue size: " + this.averageQueueSizeQueryM + "\n"
                + "\tAverage Execution Module queue size: " + this.averageQueueSizeExecM + "\n"
                + "\tAverage Transaction Module queue size: " + this.averageQueueSizeTransM + "\n"
                + "\n"
                + "Average time of UPDATE statements per module" + "\n"
                + "\tConnection Module: " + this.getConnectionModStats().getUpdateStatementTime() + "\n"
                + "\tProcess Managment Module: " + this.getProcessModStats().getUpdateStatementTime() + "\n"
                + "\tQuery Processor Module: " + this.getQueryProcModStats().getUpdateStatementTime() + "\n"
                + "\tExecution Module: " + this.getExecutionModStats().getUpdateStatementTime() + "\n"
                + "\tTransaction Module: " + this.getTransactionModStats().getUpdateStatementTime() + "\n"
                + "\n"
                + "Average time of DDL statements per module" + "\n"
                + "\tConnection Module: " + this.getConnectionModStats().getDLLStatementTime() + "\n"
                + "\tProcess Managment Module: " + this.getProcessModStats().getDLLStatementTime() + "\n"
                + "\tQuery Processor Module: " + this.getQueryProcModStats().getDLLStatementTime() + "\n"
                + "\tExecution Module: " + this.getExecutionModStats().getDLLStatementTime() + "\n"
                + "\tTransaction Module: " + this.getTransactionModStats().getDLLStatementTime() + "\n"
                + "\n"
                + "Average time of SELECT statements per module" + "\n"
                + "\tConnection Module: " + this.getConnectionModStats().getSelectStatementTime() + "\n"
                + "\tProcess Managment Module: " + this.getProcessModStats().getSelectStatementTime() + "\n"
                + "\tQuery Processor Module: " + this.getQueryProcModStats().getSelectStatementTime() + "\n"
                + "\tExecution Module: " + this.getExecutionModStats().getSelectStatementTime() + "\n"
                + "\tTransaction Module: " + this.getTransactionModStats().getSelectStatementTime() + "\n"
                + "\n"
                + "Average time of JOIN statements per module" + "\n"
                + "\tConnection Module: " + this.getConnectionModStats().getJoinStatementTime() + "\n"
                + "\tProcess Managment Module: " + this.getProcessModStats().getJoinStatementTime() + "\n"
                + "\tQuery Processor Module: " + this.getQueryProcModStats().getJoinStatementTime() + "\n"
                + "\tExecution Module: " + this.getExecutionModStats().getJoinStatementTime() + "\n"
                + "\tTransaction Module: " + this.getTransactionModStats().getJoinStatementTime() + "\n\n";
        // clear current stats
        clean();
        return data;
    }

    /**
     * Add each field of this class the same field of the instand of this class
     * pass by argument.
     *
     * @param other
     */
    public void addOtherValues(IterationStatistics other) {
        this.averageQueryLifeTime += other.averageQueryLifeTime;
        this.averageQueueSizeConnectionM += other.averageQueueSizeConnectionM;
        this.averageQueueSizeExecM += other.averageQueueSizeExecM;
        this.averageQueueSizeProcessM += other.averageQueueSizeProcessM;
        this.averageQueueSizeQueryM += other.averageQueueSizeQueryM;
        this.averageQueueSizeTransM += other.averageQueueSizeTransM;
        this.counterOfDeniedConnection += other.counterOfDeniedConnection;
        this.totalConnections += other.totalConnections;
        timeOutsCounter += other.timeOutsCounter;
        this.connectionModStats.addOtherValues(other.getConnectionModStats());
        this.processModStats.addOtherValues(other.getProcessModStats());
        this.queryProcModStats.addOtherValues(other.getQueryProcModStats());
        this.transactionModStats.addOtherValues(other.getTransactionModStats());
        this.executionModStats.addOtherValues(other.getExecutionModStats());
    }

    /**
     * Reset this class fields to their initial value.
     */
    public void clean() {
        averageQueryLifeTime = 0.0;
        averageQueueSizeConnectionM = 0.0;
        averageQueueSizeProcessM = 0.0;
        averageQueueSizeQueryM = 0.0;
        averageQueueSizeTransM = 0.0;
        averageQueueSizeExecM = 0.0;
        counterOfDeniedConnection = 0.0;
        totalConnections = 0.0;
        timeOutsCounter = 0.0;
        connectionModStats.clean();
        processModStats.clean();
        queryProcModStats.clean();
        transactionModStats.clean();
        executionModStats.clean();
    }

    /**
     *
     * @param timeOutsCounter
     */
    public void setTimeOutsCounter(double timeOutsCounter) {
        this.timeOutsCounter = timeOutsCounter;
    }

    /**
     * Increase by one the timeOutsCounter current value.
     */
    public void increaseTimeOutsCounter() {
        ++this.timeOutsCounter;
    }

    /**
     * Return this class timeOutsCounter field current value.
     *
     * @return timeOutsCounter field
     */
    public double getTimeOutsCounter() {
        return timeOutsCounter;
    }

    /**
     * Return this class totalConnections field current value.
     *
     * @return totalConnections field
     */
    public double getTotalConnections() {
        return totalConnections;
    }

    /**
     * Set this class totalConnections field with the value pass as argument
     *
     * @param totalConnections
     */
    public void setTotalConnections(double totalConnections) {
        this.totalConnections = totalConnections;
    }

    /**
     * Return this class counterOfDeniedConnection field current value.
     *
     * @return counterOfDeniedConnection field
     */
    public double getCounterOfDeniedConnection() {
        return counterOfDeniedConnection;
    }

    /**
     * Set this class counterOfDeniedConnection field with the value pass as
     * argument
     *
     * @param counterOfDeniedConnection
     */
    public void setCounterOfDeniedConnection(double counterOfDeniedConnection) {
        this.counterOfDeniedConnection = counterOfDeniedConnection;
    }

    /**
     * Return this class averageQueryLifeTime field current value.
     *
     * @return averageQueryLifeTime field
     */
    public double getAverageQueryLifeTime() {
        return averageQueryLifeTime;
    }

    /**
     * Set this class averageQueryLifeTime field with the value pass as argument
     *
     * @param averageQueryLifeTime
     */
    public void setAverageQueryLifeTime(double averageQueryLifeTime) {
        this.averageQueryLifeTime = averageQueryLifeTime;
    }

    /**
     * Return this class averageQueueSizeConnectionM field current value.
     *
     * @return averageQueueSizeConnectionM field
     */
    public double getAverageQueueSizeConnectionM() {
        return averageQueueSizeConnectionM;
    }

    /**
     * Set this class averageQueueSizeConnectionM field with the value pass as
     * argument
     *
     * @param averageQueueSizeConnectionM
     */
    public void setAverageQueueSizeConnectionM(double averageQueueSizeConnectionM) {
        this.averageQueueSizeConnectionM = averageQueueSizeConnectionM;
    }

    /**
     * Return this class averageQueueSizeProcessM field current value.
     *
     * @return averageQueueSizeProcessM field
     */
    public double getAverageQueueSizeProcessM() {
        return averageQueueSizeProcessM;
    }

    /**
     * Set this class averageQueueSizeProcessM field with the value pass as
     * argument
     *
     * @param averageQueueSizeProcessM
     */
    public void setAverageQueueSizeProcessM(double averageQueueSizeProcessM) {
        this.averageQueueSizeProcessM = averageQueueSizeProcessM;
    }

    /**
     * Return this class averageQueueSizeQueryM field current value.
     *
     * @return averageQueueSizeQueryM field
     */
    public double getAverageQueueSizeQueryM() {
        return averageQueueSizeQueryM;
    }

    /**
     * Set this class averageQueueSizeQueryM field with the value pass as
     * argument
     *
     * @param averageQueueSizeQueryM
     */
    public void setAverageQueueSizeQueryM(double averageQueueSizeQueryM) {
        this.averageQueueSizeQueryM = averageQueueSizeQueryM;
    }

    /**
     * Return this class averageQueueSizeTransM field current value.
     *
     * @return averageQueueSizeTransM field
     */
    public double getAverageQueueSizeTransM() {
        return averageQueueSizeTransM;
    }

    /**
     * Set this class averageQueueSizeTransM field with the value pass as
     * argument
     *
     * @param averageQueueSizeTransM
     */
    public void setAverageQueueSizeTransM(double averageQueueSizeTransM) {
        this.averageQueueSizeTransM = averageQueueSizeTransM;
    }

    /**
     * Return this class averageQueueSizeExecM field current value.
     *
     * @return averageQueueSizeExecM field
     */
    public double getAverageQueueSizeExecM() {
        return averageQueueSizeExecM;
    }

    /**
     * Set this class averageQueueSizeExecM field with the value pass as
     * argument
     *
     * @param averageQueueSizeExecM
     */
    public void setAverageQueueSizeExecM(double averageQueueSizeExecM) {
        this.averageQueueSizeExecM = averageQueueSizeExecM;
    }

    /**
     * Return this class connectionModStats field current value.
     *
     * @return connectionModStats field
     */
    public StatementPerModuleStats getConnectionModStats() {
        return connectionModStats;
    }

    /**
     * Set this class connectionModStats field with the value pass as argument
     *
     * @param connectionModStats
     */
    public void setConnectionModStats(StatementPerModuleStats connectionModStats) {
        this.connectionModStats = connectionModStats;
    }

    /**
     * Return this class processModStats field current value.
     *
     * @return processModStats field
     */
    public StatementPerModuleStats getProcessModStats() {
        return processModStats;
    }

    /**
     * Set this class processModStats field with the value pass as argument
     *
     * @param processModStats
     */
    public void setProcessModStats(StatementPerModuleStats processModStats) {
        this.processModStats = processModStats;
    }

    /**
     * Return this class queryProcModStats field current value.
     *
     * @return queryProcModStats field
     */
    public StatementPerModuleStats getQueryProcModStats() {
        return queryProcModStats;
    }

    /**
     * Set this class queryProcModStats field with the value pass as argument
     *
     * @param queryProcModStats
     */
    public void setQueryProcModStats(StatementPerModuleStats queryProcModStats) {
        this.queryProcModStats = queryProcModStats;
    }

    /**
     * Return this class transactionModStats field current value.
     *
     * @return transactionModStats field
     */
    public StatementPerModuleStats getTransactionModStats() {
        return transactionModStats;
    }

    /**
     * Set this class transactionModStats field with the value pass as argument
     *
     * @param transactionModStats
     */
    public void setTransactionModStats(StatementPerModuleStats transactionModStats) {
        this.transactionModStats = transactionModStats;
    }

    /**
     * Return this class executionModStats field current value.
     *
     * @return executionModStats field
     */
    public StatementPerModuleStats getExecutionModStats() {
        return executionModStats;
    }

    /**
     * Set this class executionModStats field with the value pass as argument
     *
     * @param executionModStats
     */
    public void setExecutionModStats(StatementPerModuleStats executionModStats) {
        this.executionModStats = executionModStats;
    }
}
