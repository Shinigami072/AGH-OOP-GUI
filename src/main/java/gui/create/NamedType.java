package gui.create;

import gui.ValueTypeUtils;
import lab0.dataframe.values.Value;

class NamedType {
    private String name;
    private String loadedName;
    private Class<? extends Value> type;

    NamedType(String name, Class<? extends Value> type, String loadedName) {
        this.name = name;
        this.loadedName = loadedName;
        this.type = type;
    }

    NamedType(String name) {
        this(name, ValueTypeUtils.getType(0), name);
    }

    public Class<? extends Value> getType() {
        return type;
    }

    public String getName(boolean isHeaderInFile) {
        if (isHeaderInFile)
            return name;
        else
            return loadedName;
    }

    public String getLoadedName() {
        return loadedName;
    }

    @Override
    public String toString() {
        return (name) + " : " + ValueTypeUtils.classToName(type);
    }
}
