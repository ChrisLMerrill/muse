package org.musetest.core.resource.json;

import com.fasterxml.jackson.databind.*;
import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.util.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JsonResourceSerializer implements ResourceSerializer
    {
    @Override
    public MuseResource readFromStream(InputStream stream, TypeLocator locator) throws IOException
        {
        return getMapper(locator).readValue(stream, MuseResource.class);
        }

    @Override
    public void writeToStream(MuseResource resource, OutputStream stream, TypeLocator locator) throws IOException
        {
        getMapper(locator).writerWithDefaultPrettyPrinter().writeValue(stream, resource);
        }

    private ObjectMapper getMapper(TypeLocator locator)
        {
        return JsonMapperFactory.createMapper(locator);
        }
    }


