package pintodbsimulation;

/**
 * This class stores needed information for statistics calculation purpose.
 * Specificaly time spend by the clients of each statement type on each module
 * of the system.
 *
 * @author B65477 B65728 B55830
 * @see StatementType
 * @see ClientQuery
 */
public class StatementPerModuleStats {

    private double updateStatementTime;
    private double selectStatementTime;
    private double joinStatementTime;
    private double DLLStatementTime;

    /**
     * Add each field of this class the same field of the instand of this class
     * pass by argument.
     *
     * @param other
     */
    public void addOtherValues(StatementPerModuleStats other) {
        this.DLLStatementTime += other.DLLStatementTime;
        this.joinStatementTime += other.joinStatementTime;
        this.selectStatementTime += other.selectStatementTime;
        this.updateStatementTime += other.updateStatementTime;
    }

    /**
     * Class constructor.
     */
    public StatementPerModuleStats() {
    }

    /**
     * Reset this class fields to their initial value.
     */
    public void clean() {
        updateStatementTime = 0.0;
        selectStatementTime = 0.0;
        joinStatementTime = 0.0;
        DLLStatementTime = 0.0;
    }

    /**
     * Returns this class updateStatementTime field current value.
     *
     * @return updateStatementTime field
     */
    public double getUpdateStatementTime() {
        return updateStatementTime;
    }

    /**
     * Set this class field updateStatementTime to the double pass as argument.
     *
     * @param updateStatementTime
     */
    public void setUpdateStatementTime(double updateStatementTime) {
        this.updateStatementTime = updateStatementTime;
    }

    /**
     * Returns this class selectStatementTime field current value.
     *
     * @return selectStatementTime field
     */
    public double getSelectStatementTime() {
        return selectStatementTime;
    }

    /**
     * Set this class field selectStatementTime to the double pass as argument.
     *
     * @param selectStatementTime
     */
    public void setSelectStatementTime(double selectStatementTime) {
        this.selectStatementTime = selectStatementTime;
    }

    /**
     * Returns this class joinStatementTime field current value.
     *
     * @return joinStatementTime field
     */
    public double getJoinStatementTime() {
        return joinStatementTime;
    }

    /**
     * Set this class field joinStatementTime to the double pass as argument.
     *
     * @param joinStatementTime
     */
    public void setJoinStatementTime(double joinStatementTime) {
        this.joinStatementTime = joinStatementTime;
    }

    /**
     * Returns this class DLLStatementTime field current value.
     *
     * @return DLLStatementTime field
     */
    public double getDLLStatementTime() {
        return DLLStatementTime;
    }

    /**
     * Set this class field DLLStatementTime to the double pass as argument.
     *
     * @param DLLStatementTime
     */
    public void setDLLStatementTime(double DLLStatementTime) {
        this.DLLStatementTime = DLLStatementTime;
    }
}
