package org.museautomation.javascript.factory;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;

import javax.script.*;
import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess")
public interface FromJavascriptResourceFactory
    {
    List<MuseResource> createResources(ResourceOrigin origin, ResourceType type, ScriptEngine engine) throws IOException;
    }

