package org.musetest.javascript.factory;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.origin.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.util.*;
import org.musetest.javascript.support.*;
import org.slf4j.*;

import javax.script.*;
import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // discovered via reflection
public class FromJsFileResourceFactory implements MuseResourceFactory
    {
    @Override
    public List<MuseResource> createResources(ResourceOrigin origin, ClassLocator class_locator) throws IOException
        {
        ResourceTypes resource_types = new ResourceTypes(class_locator);
        List<MuseResource> resources = new ArrayList<>();
        if (origin instanceof FileResourceOrigin)
            {
            File file = ((FileResourceOrigin)origin).getFile();
            if (file.getName().endsWith(".js"))
                {
                try
                    {
                    // evaluate the script
                    JavascriptRunner runner = new JavascriptStepRunner();
                    runner.evalScript(origin);

                    // look for the variable that declares the resource type for this script
                    ResourceType type = resource_types.forIdIgnoreCase(runner.getScriptEngine().get("MuseResourceType").toString());
                    if (type == null)
                        return Collections.emptyList();

                    // find factories for this type and try to load it
                    List<FromJavascriptResourceFactory> factories = new FactoryLocator(class_locator).findFactories(FromJavascriptResourceFactory.class);
                    for (FromJavascriptResourceFactory factory : factories)
                        resources.addAll(factory.createResources(origin, type, runner.getScriptEngine()));
                    }
                catch (ScriptException e)
                    {
                    LOG.error("Unable to eval() the javascript: " + origin, e);
                    }
                }
            }
        return resources;
        }

    final static Logger LOG = LoggerFactory.getLogger(FromJsFileResourceFactory.class);
    }


