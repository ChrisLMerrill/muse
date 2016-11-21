package org.musetest.core.resource;

import org.musetest.core.*;
import org.musetest.core.resource.json.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultSerializers
    {
    public static ResourceSerializer get(MuseResource resource)
        {
        return new JsonResourceSerializer();
        }
    }


