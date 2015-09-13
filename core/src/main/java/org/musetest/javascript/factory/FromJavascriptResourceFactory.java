package org.musetest.javascript.factory;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;

import javax.script.*;
import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface FromJavascriptResourceFactory extends MuseDynamicLoadable
    {
    List<MuseResource> createResources(ResourceOrigin origin, ResourceType type, ScriptEngine engine, String script) throws IOException;
    }

