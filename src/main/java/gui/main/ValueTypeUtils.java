package gui.main;

import lab0.dataframe.values.*;

public class ValueTypeUtils {
    public static String classToName(Class<? extends Value> c) {

        String[] name = c.toString().split("\\.");
        return name[name.length - 1];
    }

    public static int getTypeCount(){
        return 5;
    }
    public static Class<? extends Value> getType(int type) {
        switch (type) {
            case 0:
            default:
                return IntegerValue.class;
            case 1:
                return DoubleValue.class;
            case 2:
                return FloatValue.class;
            case 3:
                return StringValue.class;
            case 4:
                return DateTimeValue.class;
        }
    }

}
