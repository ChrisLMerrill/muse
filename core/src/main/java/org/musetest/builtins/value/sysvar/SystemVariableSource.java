package org.musetest.builtins.value.sysvar;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("sysvar")
@MuseValueSourceName("System variable")
@MuseValueSourceShortDescription("get a system-provided variable (by name)")
@MuseValueSourceLongDescription("Evaluates to a system-provided variable. The variable is located by resolving the sub-source to a string and searching for a system variable provider answering to that name.")
@MuseStringExpressionSupportImplementation(SystemVariableSourceStringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Name", description = "Name of the system variable to get", type = SubsourceDescriptor.Type.Single)
public class SystemVariableSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public SystemVariableSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _name = getValueSource(config, true, project);
        }

    public MuseValueSource getName()
        {
        return _name;
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String name = getValue(_name, context, false, String.class);
        for (SystemVariableProvider provider : getProject().getSystemVariableProviders().getProviders())
            {
            if (provider.provides(name))
                {
                Object value = provider.resolve(name, context);
                context.raiseEvent(new ValueSourceResolvedEvent(getDescription(), value));
                return value;
                }
            }
        throw new ValueSourceResolutionError("Unable to find provider for system variable: " + name);
        }

    private MuseValueSource _name;

    public final static String TYPE_ID = SystemVariableSource.class.getAnnotation(MuseTypeId.class).value();
    }
