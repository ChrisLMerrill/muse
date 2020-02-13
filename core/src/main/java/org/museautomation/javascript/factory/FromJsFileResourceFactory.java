package org.museautomation.javascript.factory;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.origin.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.javascript.support.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused,WeakerAccess")  // discovered via reflection
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
                    List<FromJavascriptResourceFactory> factories = class_locator.getInstances(FromJavascriptResourceFactory.class);
                    for (FromJavascriptResourceFactory factory : factories)
                        resources.addAll(factory.createResources(origin, type, runner.getScriptEngine()));
                    }
                catch (Exception e)
                    {
                    LOG.error("Unable to eval() the javascript: " + origin, e);
                    }
                }
            }
        return resources;
        }

    private final static Logger LOG = LoggerFactory.getLogger(FromJsFileResourceFactory.class);
    }


