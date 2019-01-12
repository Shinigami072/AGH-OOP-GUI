package gui.model;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.scene.chart.XYChart;
import lab0.dataframe.DataFrame;
import lab0.dataframe.exceptions.DFColumnTypeException;
import lab0.dataframe.groupby.GroupBy;
import lab0.dataframe.values.NumericValue;
import lab0.dataframe.values.Value;

import java.util.*;
import java.util.function.UnaryOperator;


public class ObservableDataframe extends ObservableListBase<Value[]> {
    private DataFrame container;
    private HashMap<String,Boolean> numericCols;

    public ObservableDataframe(DataFrame df) {
        super();
        container = df;
        numericCols=new HashMap<String, Boolean>();
    }
    public ObservableList<XYChart.Series<Object,Object>> getSeries(boolean xString,boolean yString,String x,String... y) {
        DataFrame.Column X = container.get(x);
        DataFrame Y = null;

        try {
            Y = container.get(y,false);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        Hashtable<Integer,ObservableList<XYChart.Data<Object,Object>>> series = new Hashtable<>();

        for (int j = 0; j <y.length ; j++) {
            series.put(j,FXCollections.observableArrayList(new ArrayList<>(X.size())));
        }

        for (int i = 0; i < X.size(); i++) {
            Object x_val =xString?X.get(i).getValue().toString() : X.get(i).getValue();
            Value[] y_val = Y.getRecord(i);
            for(int j=0;j<y.length;j++)
            {
                Object y_val2=yString? y_val[j].getValue().toString():y_val[j].getValue();
                series.get(j).add(new XYChart.Data<>(x_val,y_val2));
            }
        }

        ArrayList<XYChart.Series<Object,Object>> full_series = new ArrayList<>(y.length);
        for (int j = 0; j <y.length ; j++) {
            full_series.add(j,new XYChart.Series<Object, Object>(x+"("+y[j]+")",series.get(j)));
        }

        ObservableList<XYChart.Series<Object,Object>> result=FXCollections.observableArrayList(full_series);

        return result;
    }

    public ObservableList<XYChart.Series<Object,Object>> getSeries(boolean xString,boolean yString,String x,Collection<String> y){
       return getSeries(xString,yString,x,y.toArray(new String[0]));
    }
    @Override
    public Value[] get(int i) {
        return container.getRecord(i);
    }

    public boolean isNumeric(String colname){
        if(!numericCols.containsKey(colname)){
            numericCols.put(colname, NumericValue.class.isAssignableFrom(container.get(colname).getType()));
        }
        return numericCols.get(colname);
    }

    @Override
    public int size() {
        return container.size();
    }

    public String[] getNames() {
        return container.getNames();
    }

    public int colCount() {
        return container.getColCount();
    }

    public GroupBy groupBy(String... colnames) throws CloneNotSupportedException {
        return container.groupBy(colnames);

    }


//    @Override
//    public int size() {
//        return container.size();
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return container.size() == 0;
//    }
//
//    @Override
//    public boolean contains(Object o) {
//        throw new UnsupportedOperationException("contains");
//    }
//
//    @Override
//    public Iterator<Value[]> iterator() {
//        throw new UnsupportedOperationException("iterator");
//    }
//
//    @Override
//    public Object[] toArray() {
//        throw new UnsupportedOperationException("toArray");
//    }
//
//    @Override
//    public <T> T[] toArray(T[] ts) {
//        throw new UnsupportedOperationException("toArrayT");
//    }
//
//    @Override
//    public boolean add(Value[] values) {
//        try {
//            container.addRecord(values);
//            return true;
//        } catch (DFColumnTypeException e) {
//            throw new IllegalArgumentException(e);
//        }
//    }
//
//    @Override
//    public boolean remove(Object o) {
//        throw new UnsupportedOperationException("remove");
//    }
//
//    @Override
//    public boolean containsAll(Collection<?> collection) {
//        throw new UnsupportedOperationException("containsAll");
//    }
//
//    @Override
//    public boolean addAll(Collection<? extends Value[]> collection) {
//        for (Value[] row : collection) {
//            add(row);
//        }
//        return true;
//    }
//
//    @Override
//    public boolean addAll(int i, Collection<? extends Value[]> collection) {
//        throw new UnsupportedOperationException("containsAll i");
//    }
//
//    @Override
//    public boolean removeAll(Collection<?> collection) {
//        throw new UnsupportedOperationException("removeAll");
//    }
//
//    @Override
//    public boolean retainAll(Collection<?> collection) {
//        throw new UnsupportedOperationException("retainAll");
//    }
//
//    @Override
//    public void replaceAll(UnaryOperator<Value[]> operator) {
//        throw new UnsupportedOperationException("replaceAll");
//    }
//
//    @Override
//    public void sort(Comparator<? super Value[]> c) {
//        throw new UnsupportedOperationException("sort");
//
//    }
//
//    @Override
//    public void clear() {
//        throw new UnsupportedOperationException("clear");
//
//    }
//
//    @Override
//    public Value[] get(int i) {
//        return container.getRecord(i);
//    }
//
//    @Override
//    public Value[] set(int i, Value[] values) {
//        throw new UnsupportedOperationException("set");
//    }
//
//    @Override
//    public void add(int i, Value[] values) {
//        throw new UnsupportedOperationException("add i");
//
//    }
//
//    @Override
//    public Value[] remove(int i) {
//        throw new UnsupportedOperationException("remove i");
//    }
//
//    @Override
//    public int indexOf(Object o) {
//        if (!(o instanceof Value[]))
//            return -1;
//        else
//            for (int i = 0; i < size(); i++)
//                if (Arrays.equals((Value[]) o, get(i)))
//                    return i;
//        return -1;
//    }
//
//    @Override
//    public int lastIndexOf(Object o) {
//        if (!(o instanceof Value[]))
//            return -1;
//        else
//            for (int i = size()-1; i >=0; i--)
//                if (Arrays.equals((Value[]) o, get(i)))
//                    return i;
//        return -1;
//    }
//
//    @Override
//    public ListIterator<Value[]> listIterator() {
//        throw new UnsupportedOperationException("listIterator");
//    }
//
//    @Override
//    public ListIterator<Value[]> listIterator(int i) {
//        throw new UnsupportedOperationException("listIterator i");
//    }
//
//    @Override
//    public List<Value[]> subList(int from, int to) {
//        try {
//            return new ObservableDataframe(container.iloc(from,to));//todo: fix exceptions
//        } catch (DFColumnTypeException e) {
//            e.printStackTrace();
//        }
//        throw new UnsupportedOperationException("retainAll");
//    }
//
//    @Override
//    public Spliterator<Value[]> spliterator() {
//        throw new UnsupportedOperationException("spliterator i");
//    }
//
//    @Override
//    public boolean addAll(Value[]... elements) {
//        for (Value[] row:elements) {
//            add(row);
//        };
//        return true;
//    }
//
//    @Override
//    public boolean setAll(Value[]... elements) {
//        container = new DataFrame(container.getNames(),container.getTypes());
//        for (Value[] row:elements) {
//            try {
//                container.addRecord(row);
//            } catch (DFColumnTypeException e) {
//                e.printStackTrace();
//            }
//        };
//        return true;
//    }
//
//    @Override
//    public boolean setAll(Collection<? extends Value[]> col) {
//        Value[][] t = new Value[container.colCount()][0];
//        return setAll(col.toArray(t));
//    }
//
//    @Override
//    public boolean removeAll(Value[]... elements) {
//        throw new UnsupportedOperationException("removeAll");
//    }
//
//    @Override
//    public boolean retainAll(Value[]... elements) {
//        throw new UnsupportedOperationException("retainAll");
//
//    }
//
//    @Override
//    public void remove(int from, int to) {
//        throw new UnsupportedOperationException("remove");
//
//    }

}
