package org.museautomation.core.resource;

import org.museautomation.core.*;
import org.museautomation.core.resource.csv.*;
import org.museautomation.core.resource.types.*;

/**
 * A resource that provides data values in a row and column format.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface DataTable extends MuseResource
    {
    int getNumberColumns();
    int getNumberRows();
    String[] getColumnNames();
    Object[] getDataRow(int row);
    Object getData(int column, int row);
    Object getData(String column, int row);

    @SuppressWarnings("unused,WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    class DataTableResourceType extends ResourceType
        {
        public DataTableResourceType()
            {
            super("table", "Data Table", DataTable.class);
            }

        @Override
        public MuseResource create()
            {
            return new BasicDataTable();
            }
        }

    }

