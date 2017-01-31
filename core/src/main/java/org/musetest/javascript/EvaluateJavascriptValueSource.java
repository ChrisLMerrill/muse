package org.musetest.javascript;

import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.test.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.javascript.support.*;

import javax.script.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("evaluateJS")
@MuseValueSourceName("Eval JS")
@MuseValueSourceShortDescription("Evaluate a snippet of Javascript")
@MuseValueSourceLongDescription("Resolves the sub-source. If it is a string, evaulate that as Javascript and return the result.")
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
            context.raiseEvent(new ValueSourceResolvedEvent("JavaScript evaluation", result));
            return result;
            }
        catch (ScriptException e)
            {
            context.raiseEvent(new ScriptFailureEvent("Unable to evaluate Javascript", e));
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


