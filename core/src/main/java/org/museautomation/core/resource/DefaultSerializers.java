package org.museautomation.core.resource;

import org.museautomation.core.*;
import org.museautomation.core.resource.json.*;

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


