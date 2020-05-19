package org.museautomation.builtins.plugins.output;

import com.opencsv.*;
import org.museautomation.core.datacollection.*;

import javax.annotation.*;
import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class CsvResultData implements TaskResultData
    {
    public void setColumnNames(String[] names)
        {
        _names = names;
        }

    public void addRow(String[] row)
        {
        _rows.add(row);
        }

    @Override
   	public String getName()
   		{
   		return _name;
   		}

   	@Override
   	public void setName(@Nonnull String name)
   		{
   		_name = name;
   		}

    @Override
    public String suggestFilename()
        {
        return _name + ".csv";
        }

    @Override
    public void write(@Nonnull OutputStream outstream) throws IOException
        {
        CSVWriter writer = new CSVWriter(new OutputStreamWriter(outstream));
        writer.writeNext(_names);
        writer.writeAll(_rows);
        writer.close();
        }

    @Override
    public Object read(@Nonnull InputStream instream)
        {
        return null;
        }

    private String _name = "output";
    private String[] _names;
    private final List<String[]> _rows = new ArrayList<>();
    }