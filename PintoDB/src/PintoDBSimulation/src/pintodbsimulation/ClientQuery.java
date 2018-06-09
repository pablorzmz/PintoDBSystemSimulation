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
