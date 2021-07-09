package org.museautomation.builtins.condition;

import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("string-contains")
@MuseValueSourceName("String Contains")
@MuseValueSourceTypeGroup("Text.Search & Match")
@MuseValueSourceShortDescription("returns true if the String contains the specified value")
@MuseValueSourceLongDescription("Evaluates the 'string' and 'target' parameters. If the former is a string and it contains the latter, this source returns true.")
@MuseStringExpressionSupportImplementation(StringContainsSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "string", description = "String to search", type = SubsourceDescriptor.Type.Named, name = "string")
@MuseSubsourceDescriptor(displayName = "target", description = "Value to look for", type = SubsourceDescriptor.Type.Named, name = "target")
public class StringContainsSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public StringContainsSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _string = getValueSource(config, STRING_PARAM, true, project);
        _target = getValueSource(config, TARGET_PARAM, true, project);
        }

    @Override
    public Boolean resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String target = getValue(_target, context, false, String.class);
        String string = getValue(_string, context, true, String.class);
        boolean contains = false;
        if (string != null)
            contains = string.contains(target);
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), contains));
        return contains;
        }

    private MuseValueSource _string;
    private MuseValueSource _target;

    public final static String STRING_PARAM = "string";
    public final static String TARGET_PARAM = "target";

    public final static String TYPE_ID = StringContainsSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "stringContains";
            }

        @Override
        protected int getNumberArguments()
            {
            return 2;
            }

        @Override
        protected String[] getArgumentNames()
            {
            return new String[] {STRING_PARAM, TARGET_PARAM};
            }

        @Override
        protected boolean storeArgumentsNamed()
            {
            return true;
            }

        @Override
        protected String getTypeId()
            {
            return StringContainsSource.TYPE_ID;
            }
        }
    }
