package org.museautomation.javascript;

import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.task.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.museautomation.javascript.support.*;

import javax.script.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("evaluateJS")
@MuseValueSourceName("Eval JS")
@MuseValueSourceShortDescription("Evaluate a snippet of Javascript")
@MuseValueSourceLongDescription("Resolves the sub-source. If it is a string, evaluate that as Javascript and return the result.")
@MuseStringExpressionSupportImplementation(EvaluateJavascriptValueSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Script", description = "The script to evaluate", type = SubsourceDescriptor.Type.Single)
public class EvaluateJavascriptValueSource extends BaseValueSource
    {
    public EvaluateJavascriptValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _script_source = getValueSource(config, true, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String script = getValue(_script_source, context, false, String.class);
        try
            {
            Object result = new JavascriptRunner().evalScript(script);
            if (result instanceof ProjectResourceValueSource)
            context.raiseEvent(ValueSourceResolvedEventType.create("JavaScript evaluation", result));
            return result;
            }
        catch (ScriptException e)
            {
            context.raiseEvent(ScriptFailureEventType.create("Unable to evaluate Javascript: " + e.getMessage(), e));
            return null;
            }
        }

    private final MuseValueSource _script_source;

    public final static String TYPE_ID = EvaluateJavascriptValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "evaluateJavascript";
            }

        @Override
        protected boolean storeSingleArgumentAsSingleSubsource()
            {
            return true;
            }

        @Override
        protected int getNumberArguments()
            {
            return 1;
            }

        @Override
        protected String getTypeId()
            {
            return EvaluateJavascriptValueSource.TYPE_ID;
            }
        }
    }