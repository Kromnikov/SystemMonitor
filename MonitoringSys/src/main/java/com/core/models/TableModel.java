package com.core.models;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {
    private String[] columnNames ;
    private Object[][] data ;

    public TableModel(String[] columnNames, Object[][] data) {
        this.columnNames = columnNames;
        this.data = data;
    }
    public TableModel() {
    }
    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int i) {
        return columnNames[i];
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return getValueAt(0, i).getClass();
    }

    @Override
    public boolean isCellEditable(int i, int i2) {
        return false;
    }

    @Override
    public Object getValueAt(int i, int i2) {
        return data[i][i2];
    }

    @Override
    public void setValueAt(Object o, int i, int i2) {
        return;
//        data[i][i2] = o;
//        fireTableCellUpdated(i, i2);
    }

    @Override
    public void addTableModelListener(TableModelListener tableModelListener) {

    }

    @Override
    public void removeTableModelListener(TableModelListener tableModelListener) {

    }
}
