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
@SuppressWarnings({"unused", "WeakerAccess"})  // factory invoked by reflection
public class StepFromJavascriptResourceFactory implements FromJavascriptResourceFactory
    {
    @Override
    public List<MuseResource> createResources(ResourceOrigin origin, ResourceType type, ScriptEngine engine)
        {
        List<MuseResource> resources = new ArrayList<>();

        // Does the script implement the right method?
        Invocable inv = (Invocable) engine;
        try
            {
            inv.invokeFunction(JavascriptStep.EXECUTE_FUNCTION, new ProjectExecutionContext(new SimpleProject()));
            }
        catch (ScriptException e)
            {
            // this is ok, we expect the script may be expecting certain environment to be present
            }
        catch (NoSuchMethodException e)
            {
            LOG.error("Script is declared as a Step, but does not provide the '" + JavascriptStep.EXECUTE_FUNCTION + "()' function: " + origin.getDescription());
            return resources;
            }
        resources.add(new JavascriptStepResource(origin, inv));
        return resources;
        }

    private final static Logger LOG = LoggerFactory.getLogger(StepFromJavascriptResourceFactory.class);
    }
