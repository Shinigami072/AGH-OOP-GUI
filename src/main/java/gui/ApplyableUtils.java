package gui;

import lab0.dataframe.DataFrame;
import lab0.dataframe.exceptions.DFApplyableException;
import lab0.dataframe.groupby.GroupBy;

public class ApplyableUtils {
    public enum AP_OPERTATION{
        MIN("Minimum"),
        MAX("Maximum"),
        SUM("Sum"),
        MEAN("Mean"),
        VAR("Variance"),
        SD("Standard Deviation");
        String name;
        AP_OPERTATION(String name){
            this.name=name;
        }

        @Override
        public String toString() {
            return name;
        }

        public DataFrame apply(GroupBy gb) throws DFApplyableException {
            switch (this){
                default:
                    throw new UnsupportedOperationException("operation not added");
                case MIN:
                    return gb.min();
                case MAX:
                    return gb.max();
                case SUM:
                    return gb.sum();
                case MEAN:
                    return gb.mean();
                case VAR:
                    return gb.var();
                case SD:
                    return gb.std();
            }
        }
    }
}
