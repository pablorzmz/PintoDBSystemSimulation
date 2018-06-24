package pintodbsimulation;

/**
 * This class stores necessary information of a client for the simulation.
 *
 * @author B65477 B65728 B55830
 */
public class ClientQuery {

    private StatementType queryType;
    private Module currentMod;
    private final QueryStatistics queryStatistics;
    private boolean finishedService;
    private final int clientID;

    /**
     * Class constructor.
     *
     * @param clientID
     * @param queryType
     * @param currentMod
     */
    public ClientQuery(int clientID, StatementType queryType, Module currentMod) {
        this.clientID = clientID;
        this.queryType = queryType;
        this.currentMod = currentMod;
        this.queryStatistics = new QueryStatistics();
        this.finishedService = false;
    }

    /**
     * Update each module and system statistics (arrive time, leave time, ...)
     */
    public void updateStats() {
        double timeInModule;
        String currentModuleName = currentMod.getClass().getSimpleName();
        String procM = ProcessManagmentModule.class.getSimpleName();
        String connectionM = ConnectionModule.class.getSimpleName();
        String queryProcM = QueryProcessorModule.class.getSimpleName();
        String executionM = ExecutionModule.class.getSimpleName();
        String transactionM = TransactionAndDiskModule.class.getSimpleName();
        timeInModule = queryStatistics.getModuleLeaveTime() - queryStatistics.getModuleArriveTime();

        if (currentModuleName.equals(procM)) {
            queryStatistics.setTimeInProcMgmtMod(timeInModule);

        } else if (currentModuleName.equals(connectionM)) {
            queryStatistics.setTimeInConnectionMod(0.0);

        } else if (currentModuleName.equals(queryProcM)) {
            queryStatistics.setTimeInQueryProcMod(timeInModule);

        } else if (currentModuleName.equals(executionM)) {
            queryStatistics.setTimeInExecMod(timeInModule);

        } else if (currentModuleName.equals(transactionM)) {
            queryStatistics.setTimeInTransMod(timeInModule);
        }
        queryStatistics.setModuleArriveTime(Statistics.INVALID_TIME);
        queryStatistics.setModuleLeaveTime(Statistics.INVALID_TIME);
    }

    /**
     * Return this class clientID field current value (int).
     *
     * @return clientID field
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * Set this class queryType field with the {@code StatementType} pass as
     * argument.
     *
     * @param queryType
     * @see StatementType
     */
    public void setQueryType(StatementType queryType) {
        this.queryType = queryType;
    }

    /**
     * Set this class currentMod field with the {@code Module} pass as argument.
     *
     * @param currentMod
     * @see Module
     */
    public void setCurrentMod(Module currentMod) {
        this.currentMod = currentMod;
    }

    /**
     * Return this class queryType field value{@code StatementType}).
     *
     * @return queryType field
     * @see StatementType
     */
    public StatementType getQueryType() {
        return queryType;
    }

    /**
     * Return this class currentMod field value({@code Module}).
     *
     * @return currentMod field
     * @see Module
     */
    public Module getCurrentMod() {
        return currentMod;
    }

    /**
     * Return this class queryStatistic field value({@code QueryStatistics}).
     *
     * @return queryStatistics field
     * @see QueryStatistics
     */
    public QueryStatistics getQueryStatistics() {
        return queryStatistics;
    }

    /**
     * Return this class finishedService field value(boolean).
     *
     * @return finishedService field
     */
    public boolean getFinishService() {
        return this.finishedService;
    }

    /**
     * Set this class finishedService field with the boolean pass as argument.
     *
     * @param serviceState
     */
    public void setFinishService(boolean serviceState) {
        this.finishedService = serviceState;
    }
}
