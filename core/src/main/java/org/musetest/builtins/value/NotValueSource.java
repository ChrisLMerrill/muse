package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
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
@MuseSubsourceDescriptor(displayName = "Value", description = "subsource to invert (expects a boolean value)", type = SubsourceDescriptor.Type.Single)
public class NotValueSource extends BaseValueSource
    {
//    @SuppressWarnings("unused")  // used via reflection
    public NotValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        if (config.getSource() != null)
            _subsource = config.getSource().createSource(project);
        else
            throw new MuseInstantiationException("Missing required parameter (subsource)");
        }

    @Override
    public Object resolveValue(StepExecutionContext context) throws ValueSourceResolutionError
        {
        Object value = _subsource.resolveValue(context);
        Boolean result;
        if (value instanceof Boolean)
            {
            result = !(Boolean) value;
            context.raiseEvent(new ValueSourceResolvedEvent(getDescription(), result));
            return result;
            }
        else
            {
            if (value == null)
                throw new ValueSourceResolutionError("Expected the sub-source to resolve to a boolean. Instead, it is null");
            throw new ValueSourceResolutionError(String.format("Expected the sub-source to resolve to a boolean. Instead, got: %s (which is a %s)", value.toString(), value.getClass().getSimpleName()));
            }
        }

    private MuseValueSource _subsource;

    public final static String TYPE_ID = NotValueSource.class.getAnnotation(MuseTypeId.class).value();
    }


