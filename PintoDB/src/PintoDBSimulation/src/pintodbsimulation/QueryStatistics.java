package pintodbsimulation;

/**
 * This class stores needed information for statistics calculation purpose.
 * Specificaly arrive and leave time for each client of the system and number of
 * disk blocks loaded.
 *
 * @author B65477 B65728 B55830
 * @see ClientQuery
 */
public class QueryStatistics {

    private double systemArriveTime;
    private double systemLeaveTime;
    private double moduleArriveTime;
    private double moduleLeaveTime;
    private double timeInQueryProcMod;
    private double timeInConnectionMod;
    private double timeInExecMod;
    private double timeInTransMod;
    private double timeInProcMgmtMod;
    private int usedBlocks;

    /**
     * Class constructor.
     */
    public QueryStatistics() {
        this.systemArriveTime = Statistics.INVALID_TIME;
        this.systemLeaveTime = Statistics.INVALID_TIME;
        this.moduleArriveTime = Statistics.INVALID_TIME;
        this.moduleLeaveTime = Statistics.INVALID_TIME;
        this.timeInConnectionMod = Statistics.INVALID_TIME;
        this.timeInQueryProcMod = Statistics.INVALID_TIME;
        this.timeInProcMgmtMod = Statistics.INVALID_TIME;
        this.timeInExecMod = Statistics.INVALID_TIME;
        this.timeInTransMod = Statistics.INVALID_TIME;
    }

    /**
     * Returns this class systemArriveTime field value (double).
     *
     * @return systemArriveTime field
     */
    public double getSystemArriveTime() {
        return systemArriveTime;
    }

    /**
     * Set this class systemArriveTime to the double pass as argument
     *
     * @param systemArriveTime
     */
    public void setSystemArriveTime(double systemArriveTime) {
        this.systemArriveTime = systemArriveTime;
    }

    /**
     * Returns this class systemLeaveTime field value (double).
     *
     * @return systemLeaveTime field
     */
    public double getSystemLeaveTime() {
        return systemLeaveTime;
    }

    /**
     * Set this class systemLeaveTime to the double pass as argument
     *
     * @param systemLeaveTime
     */
    public void setSystemLeaveTime(double systemLeaveTime) {
        this.systemLeaveTime = systemLeaveTime;
    }

    /**
     * Returns this class moduleArriveTime field value (double).
     *
     * @return moduleArriveTime field
     */
    public double getModuleArriveTime() {
        return moduleArriveTime;
    }

    /**
     * Set this class moduleArriveTime to the double pass as argument
     *
     * @param moduleArriveTime
     */
    public void setModuleArriveTime(double moduleArriveTime) {
        this.moduleArriveTime = moduleArriveTime;
    }

    /**
     * Returns this class moduleLeaveTime field value (double).
     *
     * @return moduleLeaveTime field
     */
    public double getModuleLeaveTime() {
        return moduleLeaveTime;
    }

    /**
     * Set this class moduleLeaveTime to the double pass as argument
     *
     * @param moduleLeaveTime
     */
    public void setModuleLeaveTime(double moduleLeaveTime) {
        this.moduleLeaveTime = moduleLeaveTime;
    }

    /**
     * Returns this class timeInQueryProcMod field value (double).
     *
     * @return timeInQueryProcMod field
     */
    public double getTimeInQueryProcMod() {
        return timeInQueryProcMod;
    }

    /**
     * Set this class timeInQueryProcMod to the double pass as argument
     *
     * @param timeInQueryProcMod
     */
    public void setTimeInQueryProcMod(double timeInQueryProcMod) {
        this.timeInQueryProcMod = timeInQueryProcMod;
    }

    /**
     * Returns this class timeInConnectionMod field value (double).
     *
     * @return timeInConnectionMod field
     */
    public double getTimeInConnectionMod() {
        return timeInConnectionMod;
    }

    /**
     * Set this class timeInConnectionMod to the double pass as argument
     *
     * @param timeInConnectionMod
     */
    public void setTimeInConnectionMod(double timeInConnectionMod) {
        this.timeInConnectionMod = timeInConnectionMod;
    }

    /**
     * Returns this class timeInExecMod field value (double).
     *
     * @return timeInExecMod field
     */
    public double getTimeInExecMod() {
        return timeInExecMod;
    }

    /**
     * Set this class timeInExecMod to the double pass as argument
     *
     * @param timeInExecMod
     */
    public void setTimeInExecMod(double timeInExecMod) {
        this.timeInExecMod = timeInExecMod;
    }

    /**
     * Returns this class timeInTransMod field value (double).
     *
     * @return timeInTransMod field
     */
    public double getTimeInTransMod() {
        return timeInTransMod;
    }

    /**
     * Set this class timeInTransMod to the double pass as argument
     *
     * @param timeInTransMod
     */
    public void setTimeInTransMod(double timeInTransMod) {
        this.timeInTransMod = timeInTransMod;
    }

    /**
     * Returns this class timeInProcMgmtMod field value (double).
     *
     * @return timeInProcMgmtMod field
     */
    public double getTimeInProcMgmtMod() {
        return timeInProcMgmtMod;
    }

    /**
     * Set this class timeInProcMgmtMod to the double pass as argument
     *
     * @param timeInProcMgmtMod
     */
    public void setTimeInProcMgmtMod(double timeInProcMgmtMod) {
        this.timeInProcMgmtMod = timeInProcMgmtMod;
    }

    /**
     * Returns this class usedBlocks field value (int).
     *
     * @return usedBlocks field
     */
    public int getUsedBlocks() {
        return usedBlocks;
    }

    /**
     * Set this class usedBlocks to the int pass as argument
     *
     * @param usedBlocks
     */
    public void setUsedBlocks(int usedBlocks) {
        this.usedBlocks = usedBlocks;
    }
}