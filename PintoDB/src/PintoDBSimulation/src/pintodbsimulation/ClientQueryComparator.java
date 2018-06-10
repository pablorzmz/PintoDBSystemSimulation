/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pintodbsimulation;

/**
 *
 * @author luis
 */
import java.util.Comparator;

class ClientQueryComparator implements Comparator<ClientQuery> {
    
    @Override
    public int compare(ClientQuery t, ClientQuery t1) {
        StatementType tS = t.getQueryType();
        StatementType t1S = t1.getQueryType();
        if(null != tS) switch (tS) {
            case DDL:
                if(t1S != StatementType.DDL){
                    return -1; //DDL is the one with most priority
                }   
                break;
            case UPDATE:
                if(t1S == StatementType.DDL){
                    return 1; //t is greater than t1
                }
                else if(t1S != StatementType.UPDATE){
                    return -1; //t is less than t1
                }   
                break;
            case JOIN:
                if(t1S == StatementType.DDL || t1S == StatementType.UPDATE){
                    return 1; //t is greater than t1
                }
                else if(t1S != StatementType.JOIN){
                    return -1; //t is less than t1
                }
                break;
            default: //SELECT
                if(t1S != StatementType.SELECT){
                    return 1; //SELECT is the one with less priority
                }
                break;
        }
        return 0; //t is equals to t1
    }
}
