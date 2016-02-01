package org.musetest.javascript.factory;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.javascript.*;
import org.slf4j.*;

import javax.script.*;
import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestFromJavascriptResourceFactory implements FromJavascriptResourceFactory
    {
    @Override
    public List<MuseResource> createResources(ResourceOrigin origin, ResourceType type, ScriptEngine engine) throws IOException
        {
        List<MuseResource> resources = new ArrayList<>();

        // Did the script declare it is a Test?
        if (!type.equals(ResourceTypes.Test))
            return resources;

        // Does the script implement the right method?
        Invocable inv = (Invocable) engine;
        try
            {
            inv.invokeFunction(JavascriptTest.FUNCTION_NAME, new DefaultTestExecutionContext());
            resources.add(new JavascriptTest(origin));
            }
        catch (ScriptException e)
            {
            // this is ok, we expect the script may be expecting certain environment to be present
            resources.add(new JavascriptTest(origin));
            }
        catch (NoSuchMethodException e)
            {
            LOG.error("Script is declared as a Test, but does not provide the '" + JavascriptTest.FUNCTION_NAME + "()' function: " + origin.getDescription());
            }

        return resources;
        }

    final static Logger LOG = LoggerFactory.getLogger(TestFromJavascriptResourceFactory.class);
    }


