package org.museautomation.core.resource;

import org.museautomation.core.*;
import org.museautomation.core.util.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ResourceSerializer
    {
    MuseResource readFromStream(InputStream stream, TypeLocator locator) throws IOException;
    void writeToStream(MuseResource resource, OutputStream stream, TypeLocator locator) throws IOException;

    String suggestFilename(MuseResource resource);
    }


