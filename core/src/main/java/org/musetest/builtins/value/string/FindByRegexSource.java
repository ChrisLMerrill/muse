package org.musetest.builtins.value.string;

import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

import java.util.regex.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("find-regex")
@MuseValueSourceName("Find by RegEx")
@MuseValueSourceShortDescription("Uses a RegularExpression to find a value within a String")
@MuseValueSourceLongDescription("Resolves the RegEx parameter to a string and uses it to search the Target parameter, which is also resolved as a string. Returns null if not found")
@MuseStringExpressionSupportImplementation(FindByRegexSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "RegEx", description = "Regular Expression to evaluate", type = SubsourceDescriptor.Type.Named, name = FindByRegexSource.REGEX_PARAM)
@MuseSubsourceDescriptor(displayName = "Target", description = "String to search", type = SubsourceDescriptor.Type.Named, name = FindByRegexSource.TARGET_PARAM)
public class FindByRegexSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public FindByRegexSource(ValueSourceConfiguration config, MuseProject project) throws MuseExecutionError
        {
        super(config, project);
        _regex = getValueSource(config, REGEX_PARAM, false, project);
        _target = getValueSource(config, TARGET_PARAM, false, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String regex = getValue(_regex, context, false, String.class);
        String target = getValue(_target, context, false, String.class);

        Matcher matcher = Pattern.compile(regex).matcher(target);
        if (matcher.find())
            {
            String found = matcher.group(1);
            context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), found));
            return found;
            }
        else
            {
            context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), null));
            return null;
            }
        }

    private MuseValueSource _regex;
    private MuseValueSource _target;

    public final static String REGEX_PARAM = "regex";
    public final static String TARGET_PARAM = "string";

    public final static String TYPE_ID = FindByRegexSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "findByRegex";
            }

        @Override
        protected int getNumberArguments()
            {
            return 2;
            }

        @Override
        protected String getTypeId()
            {
            return FindByRegexSource.TYPE_ID;
            }

        @Override
        protected boolean storeArgumentsNamed()
            {
            return true;
            }

        @Override
        protected String[] getArgumentNames()
            {
            return new String[] {FindByRegexSource.REGEX_PARAM, FindByRegexSource.TARGET_PARAM};
            }
        }
    }
