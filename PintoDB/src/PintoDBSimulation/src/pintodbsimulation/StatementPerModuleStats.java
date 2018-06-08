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
public class StatementPerModuleStats {
    
    private double updateStatementTime;
    private double selectStatementTime;
    private double joinStatementTime;
    private double DLLStatementTime;

    /**
    * 
    */    
    public StatementPerModuleStats() {
    }

    /**
    * 
     * @return 
    */      
    public double getUpdateStatementTime() {
        return updateStatementTime;
    }

    /**
    * 
     * @param updateStatementTime
    */      
    public void setUpdateStatementTime(double updateStatementTime) {
        this.updateStatementTime = updateStatementTime;
    }

    /**
    * 
     * @return 
    */      
    public double getSelectStatementTime() {
        return selectStatementTime;
    }

    /**
    * 
     * @param selectStatementTime
    */      
    public void setSelectStatementTime(double selectStatementTime) {
        this.selectStatementTime = selectStatementTime;
    }

    /**
    * 
     * @return 
    */      
    public double getJoinStatementTime() {
        return joinStatementTime;
    }

    /**
    * 
     * @param joinStatementTime
    */      
    public void setJoinStatementTime(double joinStatementTime) {
        this.joinStatementTime = joinStatementTime;
    }

    /**
    * 
     * @return 
    */      
    public double getDLLStatementTime() {
        return DLLStatementTime;
    }

    /**
    * 
     * @param DLLStatementTime
    */      
    public void setDLLStatementTime(double DLLStatementTime) {
        this.DLLStatementTime = DLLStatementTime;
    }
    
    
    
}
