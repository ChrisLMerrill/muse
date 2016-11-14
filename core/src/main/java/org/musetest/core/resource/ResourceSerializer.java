package org.musetest.core.resource;

import org.musetest.core.*;
import org.musetest.core.util.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ResourceSerializer
    {
    MuseResource readFromStream(InputStream stream, TypeLocator locator) throws IOException;
    void writeToStream(MuseResource resource, OutputStream stream, TypeLocator locator) throws IOException;
    }


