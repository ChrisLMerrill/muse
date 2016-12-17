package org.musetest.core.resource.csv;

import au.com.bytecode.opencsv.*;
import com.fasterxml.jackson.databind.*;
import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.json.*;
import org.musetest.core.resource.origin.*;
import org.musetest.core.util.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // via reflection
public class FromCsvFileResourceFactory implements MuseResourceFactory
    {
    @Override
    public List<MuseResource> createResources(ResourceOrigin origin, ClassLocator classes) throws IOException
        {
        TypeLocator type_locator = new TypeLocator(classes);
        List<MuseResource> resources = new ArrayList<>();
        if (origin instanceof FileResourceOrigin)
            {
            try
                {
                File file = ((FileResourceOrigin)origin).getFile();
                if (file.getName().endsWith(".csv"))
                    createResources(origin, resources, type_locator);
                }
            catch (Exception e)
                {
                LOG.warn("Cannot load resource from origin: " + origin.getDescription() + " - " + e.getMessage());
                }
            }
        else if (origin instanceof StreamResourceOrigin)
            {
            try
                {
                createResources(origin, resources, type_locator);
                }
            catch (Exception e)
                {
                // do nothing...it may not even be JSON in the stream.
                }
            }
        return resources;
        }

    private void createResources(ResourceOrigin origin, List<MuseResource> resources, TypeLocator type_locator) throws IOException
        {
        try
            {
            InputStream instream = origin.asInputStream();
            CSVReader reader = new CSVReader(new InputStreamReader(instream));
            String[] column_names = reader.readNext();
            List<String[]> row_list = reader.readAll();
            String[][] rows = row_list.toArray(new String[row_list.size()][]);
            resources.add(new BasicDataTable(column_names, rows));
            }
        catch (Exception e)
            {
            LOG.error("Unable to read a resource from " + origin.getDescription() + " - " + e.getMessage());
            }
        }

    private final static Logger LOG = LoggerFactory.getLogger(FromCsvFileResourceFactory.class);
    }


