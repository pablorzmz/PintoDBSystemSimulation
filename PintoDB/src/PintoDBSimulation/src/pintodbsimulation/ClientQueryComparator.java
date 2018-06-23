package pintodbsimulation;

import java.util.Comparator;

/**
 * Comparator implementation for the {@code PriorityQueue} to compare
 * {@code ClientQuery} instances.
 *
 * @author B65477 B65728 B55830
 * @see Comparator
 * @see ClientQuery
 */
class ClientQueryComparator implements Comparator<ClientQuery> {

    @Override
    public int compare(ClientQuery t, ClientQuery t1) {
        StatementType tS = t.getQueryType();
        StatementType t1S = t1.getQueryType();
        if (null != tS) {
            switch (tS) {
                case DDL:
                    if (t1S != StatementType.DDL) {
                        return -1; //DDL is the one with most priority
                    }
                    break;
                case UPDATE:
                    if (t1S == StatementType.DDL) {
                        return 1; //t is greater than t1
                    } else if (t1S != StatementType.UPDATE) {
                        return -1; //t is less than t1
                    }
                    break;
                case JOIN:
                    if (t1S == StatementType.DDL || t1S == StatementType.UPDATE) {
                        return 1; //t is greater than t1
                    } else if (t1S != StatementType.JOIN) {
                        return -1; //t is less than t1
                    }
                    break;
                default: //SELECT
                    if (t1S != StatementType.SELECT) {
                        return 1; //SELECT is the one with less priority
                    }
                    break;
            }
        }
        return 0; //t is equals to t1
    }
}
