package org.musetest.builtins.value;

import org.musetest.builtins.value.property.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("property")
@MuseValueSourceName("Property")
@MuseValueSourceShortDescription("get a named property from an object")
@MuseValueSourceLongDescription("Evaluates the 'name' source to a string and then looks for a property matching that name in the 'target' source.")
// TODO @MuseStringExpressionSupportImplementation(PropertySourceStringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Name", description = "Name of the property to access", type = SubsourceDescriptor.Type.Named, name = "name")
@MuseSubsourceDescriptor(displayName = "Target", description = "The object to look for the property in", type = SubsourceDescriptor.Type.Named, name = "target")
public class PropertySource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public PropertySource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException, RequiredParameterMissingError
        {
        super(config, project);
        _name = getValueSource(config, NAME_PARAM, true, project);
        _target = getValueSource(config, TARGET_PARAM, true, project);
        }

    public MuseValueSource getName()
        {
        return _name;
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String name = getValue(_name, context, false, String.class);
        Object target = getValue(_target, context, false);

        List<PropertyResolver> resolvers = getProject().getPropertyResolvers().getPropertyResolvers();
        for (PropertyResolver resolver : resolvers)
            try
                {
                if (resolver.canResolve(target, name))
                    {
                    Object result = resolver.resolve(target, name);
                    context.raiseEvent(new ValueSourceResolvedEvent(getDescription(), result));
                    return result;
                    }
                }
            catch (Exception e)
                {
                throw new ValueSourceResolutionError(e.getMessage());
                }
        return null;
        }

    private MuseValueSource _name = null;
    private MuseValueSource _target = null;

    public final static String NAME_PARAM = "name";
    public final static String TARGET_PARAM = "target";

    public final static String TYPE_ID = PropertySource.class.getAnnotation(MuseTypeId.class).value();
    }
