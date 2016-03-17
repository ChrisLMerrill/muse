package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("not")
@MuseValueSourceName("Not")
@MuseValueSourceShortDescription("Returns the boolean complement of the sub-source.")
@MuseValueSourceLongDescription("Resolves the sub-source. If it is a boolean, return the complement. Else error.")
@MuseStringExpressionSupportImplementation(NotValueSourceStringExpressionSupport.class)
public class NotValueSource extends BaseValueSource
    {
//    @SuppressWarnings("unused")  // used via reflection
    public NotValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        if (config.getSourceList() != null && config.getSourceList().size() == 1)
            _subsource = config.getSource(0).createSource(project);
        else
            throw new MuseInstantiationException("Missing required parameter (source list [0])");
        }

    @Override
    public Object resolveValue(StepExecutionContext context) throws StepConfigurationError
        {
        Object value = _subsource.resolveValue(context);
        Boolean result;
        if (value instanceof Boolean)
            {
            result = !(Boolean) value;
            context.getTestExecutionContext().raiseEvent(new ValueSourceResolvedEvent(getDescription(), result));
            return result;
            }
        else
            {
            if (value == null)
                throw new StepConfigurationError("Expected the sub-source to resolve to a boolean. Instead, it is null");
            throw new StepConfigurationError(String.format("Expected the sub-source to resolve to a boolean. Instead, got: %s (which is a %s)", value.toString(), value.getClass().getSimpleName()));
            }
        }

    MuseValueSource _subsource;

    public final static String TYPE_ID = NotValueSource.class.getAnnotation(MuseTypeId.class).value();
    }


