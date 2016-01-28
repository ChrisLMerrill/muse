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
@SuppressWarnings("unused")  // factory invoked by reflection
public class StepFromJavascriptResourceFactory implements FromJavascriptResourceFactory
    {
    @Override
    public List<MuseResource> createResources(ResourceOrigin origin, ResourceType type, ScriptEngine engine, String script) throws IOException
        {
        List<MuseResource> resources = new ArrayList<>();

        // Does the script implement the right method?
        Invocable inv = (Invocable) engine;
        try
            {
            inv.invokeFunction(JavascriptStep.FUNCTION_NAME, new DefaultTestExecutionContext());
            resources.add(new JavascriptStepResource(origin, script));
            }
        catch (ScriptException e)
            {
            // this is ok, we expect the script may be expecting certain environment to be present
            resources.add(new JavascriptStepResource(origin, script));
            }
        catch (NoSuchMethodException e)
            {
            LOG.error("Script is declared as a Step, but does not provide the '" + JavascriptStep.FUNCTION_NAME + "()' function: " + origin.getDescription());
            }

        return resources;
        }

    final static Logger LOG = LoggerFactory.getLogger(StepFromJavascriptResourceFactory.class);
    }


