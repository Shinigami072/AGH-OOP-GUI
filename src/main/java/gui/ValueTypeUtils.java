package gui;

import javafx.util.StringConverter;
import lab0.dataframe.values.*;

import java.util.LinkedList;
import java.util.List;

public class ValueTypeUtils {
    public static String classToName(Class<? extends Value> c) {

        String[] name = c.toString().split("\\.");
        return name[name.length - 1];
    }

    public static List<Class<? extends Value>> getTypeList() {
        List<Class<? extends Value>> types = new LinkedList<>();
        for (int i = 0; i < getTypeCount(); i++) {
            types.add(getType(i));
        }
        return types;
    }
    public static StringConverter<Class<? extends Value>> getConverter(){
        return new StringConverter<>() {

            @Override
            public String toString(Class<? extends Value> object) {
                return object.getSimpleName();
            }

            @Override
            public Class<? extends Value> fromString(String string) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static int getTypeCount(){
        return 5;
    }
    public static Class<? extends Value> getType(int type) {
        switch (type) {
            case 0:
            default:
                return StringValue.class;
            case 1:
                return DoubleValue.class;
            case 2:
                return FloatValue.class;
            case 3:
                return IntegerValue.class;
            case 4:
                return DateTimeValue.class;
        }
    }

}
