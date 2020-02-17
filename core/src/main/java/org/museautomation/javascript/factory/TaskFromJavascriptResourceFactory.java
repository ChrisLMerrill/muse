package org.museautomation.javascript.factory;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.javascript.*;
import org.slf4j.*;

import javax.script.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskFromJavascriptResourceFactory implements FromJavascriptResourceFactory
    {
    @Override
    public List<MuseResource> createResources(ResourceOrigin origin, ResourceType type, ScriptEngine engine)
        {
        List<MuseResource> resources = new ArrayList<>();

        // Did the script declare it is a Task?
        if (!type.equals(new MuseTask.TaskResourceType()))
            return resources;

        // Does the script implement the right method?
        Invocable inv = (Invocable) engine;
        try
            {
            inv.invokeFunction(JavascriptTask.FUNCTION_NAME, new ProjectExecutionContext(new SimpleProject()));
            resources.add(new JavascriptTask(origin));
            }
        catch (ScriptException e)
            {
            // this is ok, we expect the script may be expecting certain environment to be present
            resources.add(new JavascriptTask(origin));
            }
        catch (NoSuchMethodException e)
            {
            LOG.error("Script is declared as a Task, but does not provide the '" + JavascriptTask.FUNCTION_NAME + "()' function: " + origin.getDescription());
            }

        return resources;
        }

    private final static Logger LOG = LoggerFactory.getLogger(TaskFromJavascriptResourceFactory.class);
    }
