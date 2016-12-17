package org.musetest.core.resource.csv;

import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class BasicDataTable extends BaseMuseResource implements DataTable
    {
    public BasicDataTable()
        {
        }

    public BasicDataTable(String[] column_names, String[][] rows)
        {
        _column_names = column_names;
        _rows = rows;
        }

    @Override
    public int getNumberColumns()
        {
        return _column_names.length;
        }

    @Override
    public int getNumberRows()
        {
        return _rows.length;
        }

    @Override
    public String[] getColumnNames()
        {
        return _column_names;
        }

    @Override
    public Object[] getDataRow(int row)
        {
        return _rows[row];
        }

    @Override
    public Object getData(int column, int row)
        {
        return _rows[row][column];
        }

    @Override
    public Object getData(String column, int row)
        {
        return _rows[row][getColumnNumber(column)];
        }

    private int getColumnNumber(String column_name)
        {
        for (int i = 0; i < _column_names.length; i++)
            if (column_name.equals(_column_names[i]))
                return i;
        throw new IllegalArgumentException("Table does not contain column " + column_name);
        }

    @Override
    public ResourceType getType()
        {
        return null;
        }

    private String[] _column_names;
    private String[][] _rows;
    }
