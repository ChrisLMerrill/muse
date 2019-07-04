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
@MuseTypeId("find-nvp-regex")
@MuseValueSourceName("Find NVP by RegEx")
@MuseValueSourceShortDescription("Uses a RegularExpression to find an NVP within a String")
@MuseValueSourceLongDescription("Resolves the RegEx parameter to a string and uses it to search the Target parameter, which is also resolved as a string. Selects 2 capture groups and constructs an NVP (Name Value Pair) from the values. Returns null if not found")
@MuseStringExpressionSupportImplementation(FindNvpByRegexSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "RegEx", description = "Regular Expression to evaluate", type = SubsourceDescriptor.Type.Named, name = FindNvpByRegexSource.REGEX_PARAM)
@MuseSubsourceDescriptor(displayName = "Target", description = "String to search", type = SubsourceDescriptor.Type.Named, name = FindNvpByRegexSource.TARGET_PARAM)
@MuseSubsourceDescriptor(displayName = "Name Group", description = "Index of the capture group containing the name. Default is 1.", type = SubsourceDescriptor.Type.Named, name = FindNvpByRegexSource.NAME_GROUP_PARAM, defaultValue = "1")
@MuseSubsourceDescriptor(displayName = "Value Group", description = "Index of the capture group containing the value. Default is 2.", type = SubsourceDescriptor.Type.Named, name = FindNvpByRegexSource.NAME_GROUP_PARAM, defaultValue = "2")
public class FindNvpByRegexSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public FindNvpByRegexSource(ValueSourceConfiguration config, MuseProject project) throws MuseExecutionError
        {
        super(config, project);
        _regex = getValueSource(config, REGEX_PARAM, true, project);
        _target = getValueSource(config, TARGET_PARAM, true, project);
        _match_index = getValueSource(config, MATCH_INDEX_PARAM, false, project);
        _name_group = getValueSource(config, NAME_GROUP_PARAM, false, project);
        _value_group = getValueSource(config, VALUE_GROUP_PARAM, false, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String regex = getValue(_regex, context, false, String.class);
        String target = getValue(_target, context, false, String.class);
        long match_index = getValue(_match_index, context, Long.class, 0L);
        long name_group = getValue(_name_group, context, Long.class, 1L);
        long value_group = getValue(_value_group, context, Long.class, 2L);

        Matcher matcher = Pattern.compile(regex).matcher(target);
        int found_index = -1;
        while (found_index < match_index)
            {
            if (matcher.find())
                found_index++;
            else
                {
                context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), null));
                return null;
                }

            }
        String name = matcher.group((int) name_group);
        String value = matcher.group((int) value_group);
        NameValuePair nvp = new NameValuePair(name, value);
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), nvp));
        return nvp;
        }

    private MuseValueSource _regex;
    private MuseValueSource _target;
    private MuseValueSource _match_index;
    private MuseValueSource _name_group;
    private MuseValueSource _value_group;

    public final static String REGEX_PARAM = "regex";
    public final static String TARGET_PARAM = "string";
    public final static String NAME_GROUP_PARAM = "name-group";
    public final static String VALUE_GROUP_PARAM = "value-group";
    public final static String MATCH_INDEX_PARAM = "match-index";

    public final static String TYPE_ID = FindNvpByRegexSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "findNvpByRegex";
            }

        @Override
        protected int getNumberArguments()
            {
            return 4;
            }

        @Override
        protected String getTypeId()
            {
            return FindNvpByRegexSource.TYPE_ID;
            }

        @Override
        protected boolean storeArgumentsNamed()
            {
            return true;
            }

        @Override
        protected String[] getArgumentNames()
            {
            return new String[] {FindNvpByRegexSource.REGEX_PARAM, FindNvpByRegexSource.TARGET_PARAM, FindNvpByRegexSource.NAME_GROUP_PARAM, FindNvpByRegexSource.VALUE_GROUP_PARAM};
            }
        }
    }
