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
public class QueryStatistics {
    
    private double  systemArriveTime;
    private double  systemLeaveTime;
    private double  moduleArriveTime;
    private double  moduleLeaveTime;
    private double  timeInQueryProcMod;
    private double  timeInConnectionMod;
    private double  timeInExecMod;
    private double  timeInTransMod;
    private double  timeInProcMgmtMod;
    private int     usedBlocks;

    /**
     *
     */
    public QueryStatistics() {
    }

     /**
     *
     * @return 
     */
    public double getSystemArriveTime() {
        return systemArriveTime;
    }

     /**
     *
     * @param systemArriveTime
     */    
    public void setSystemArriveTime(double systemArriveTime) {
        this.systemArriveTime = systemArriveTime;
    }
    
     /**
     *
     * @return 
     */
    public double getSystemLeaveTime() {
        return systemLeaveTime;
    }

     /**
     *
     * @param systemLeaveTime
     */    
    public void setSystemLeaveTime(double systemLeaveTime) {
        this.systemLeaveTime = systemLeaveTime;
    }

     /**
     *
     * @return 
     */    
    public double getModuleArriveTime() {
        return moduleArriveTime;
    }

     /**
     *
     * @param moduleArriveTime
     */
    public void setModuleArriveTime(double moduleArriveTime) {
        this.moduleArriveTime = moduleArriveTime;
    }

     /**
     *
     * @return 
     */
    public double getModuleLeaveTime() {
        return moduleLeaveTime;
    }

    /**
     *
     * @param moduleLeaveTime
     */
    public void setModuleLeaveTime(double moduleLeaveTime) {
        this.moduleLeaveTime = moduleLeaveTime;
    }

    /**
     *
     * @return 
     */
    public double getTimeInQueryProcMod() {
        return timeInQueryProcMod;
    }

     /**
     *
     * @param timeInQueryProcMod
     */
    public void setTimeInQueryProcMod(double timeInQueryProcMod) {
        this.timeInQueryProcMod = timeInQueryProcMod;
    }
    
     /**
     *
     * @return 
     */
    public double getTimeInConnectionMod() {
        return timeInConnectionMod;
    }
    
     /**
     *
     * @param timeInConnectionMod
     */
    public void setTimeInConnectionMod(double timeInConnectionMod) {
        this.timeInConnectionMod = timeInConnectionMod;
    }
    
     /**
     *
     * @return 
     */
    public double getTimeInExecMod() {
        return timeInExecMod;
    }

     /**
     *
     * @param timeInExecMod
     */    
    public void setTimeInExecMod(double timeInExecMod) {
        this.timeInExecMod = timeInExecMod;
    }

     /**
     *
     * @return 
     */    
    public double getTimeInTransMod() {
        return timeInTransMod;
    }

     /**
     *
     * @param timeInTransMod
     */    
    public void setTimeInTransMod(double timeInTransMod) {
        this.timeInTransMod = timeInTransMod;
    }

     /**
     *
     * @return 
     */    
    public double getTimeInProcMgmtMod() {
        return timeInProcMgmtMod;
    }

     /**
     *
     * @param timeInProcMgmtMod
     */    
    public void setTimeInProcMgmtMod(double timeInProcMgmtMod) {
        this.timeInProcMgmtMod = timeInProcMgmtMod;
    }

     /**
     *
     * @return 
     */    
    public int getUsedBlocks() {
        return usedBlocks;
    }

     /**
     *
     * @param usedBlocks
     */    
    public void setUsedBlocks(int usedBlocks) {
        this.usedBlocks = usedBlocks;
    }
}
