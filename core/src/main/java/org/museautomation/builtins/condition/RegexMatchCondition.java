package org.museautomation.builtins.condition;

import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

import java.util.regex.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("regex-match")
@MuseValueSourceName("RegEx match")
@MuseValueSourceTypeGroup("Matching")
@MuseValueSourceShortDescription("Matches a string against a RegEx pattern")
@MuseValueSourceLongDescription("Accepts a Regular Expression pattern and matches it against the target string. Returns true on match. Supports Java-style RegEx format.")
@MuseStringExpressionSupportImplementation(RegexMatchCondition.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Pattern", description = "The pattern to use", type = SubsourceDescriptor.Type.Named, name = RegexMatchCondition.PATTERN_PARAM, defaultValue = "pattern")
@MuseSubsourceDescriptor(displayName = "Target", description = "The string to check", type = SubsourceDescriptor.Type.Named, name = RegexMatchCondition.TARGET_PARAM, defaultValue = "target string")
@MuseSubsourceDescriptor(displayName = "Case insensitive", description = "Should case be ignored when matching? (default is false)", type = SubsourceDescriptor.Type.Named, name = RegexMatchCondition.CASE_PARAM, optional = true, defaultValue = "true")
public class RegexMatchCondition extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public RegexMatchCondition(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _pattern_source = getValueSource(config, PATTERN_PARAM, true, project);
        _target_source = getValueSource(config, TARGET_PARAM, true, project);
        _case_source = getValueSource(config, CASE_PARAM, false, project);
        }

    @Override
    public Boolean resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String pattern = getValue(_pattern_source, context, false, String.class);
        String target = getValue(_target_source, context, false, String.class);
        Boolean case_insensitive = getValue(_case_source, context, Boolean.class, Boolean.FALSE);

        try
            {
            Pattern regex;
            if (case_insensitive)
                regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            else
                regex = Pattern.compile(pattern);
            boolean matches = regex.matcher(target).matches();
            context.raiseEvent(ConditionEvaluatedEventType.create(String.format("RegEx pattern '%s' matches '%s' is %s", pattern, target, matches)));
            return matches;
            }
        catch (Exception e)
            {
            throw new ValueSourceResolutionError(String.format("Unable to execute RegEx pattern match '%s' on '%s': %s", pattern, target, e.getMessage()), e);
            }
        }

    @Override
    public String getDescription()
        {
        return "regexMatch(" + _pattern_source.getDescription() + "," + _target_source.getDescription() + ")";
        }

    private MuseValueSource _pattern_source;
    private MuseValueSource _target_source;
    private MuseValueSource _case_source;

    public final static String PATTERN_PARAM = "pattern";
    public final static String TARGET_PARAM = "target";
    public final static String CASE_PARAM = "insensitive";
    public final static String TYPE_ID = RegexMatchCondition.class.getAnnotation(MuseTypeId.class).value();

    // for supporting the text expressions
    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "regexMatch";
            }

        @Override
        protected int getNumberArguments()
            {
            return 2;
            }

        @Override
        protected boolean storeArgumentsNamed()
            {
            return true;
            }

        @Override
        protected String[] getArgumentNames()
            {
            return new String[] {PATTERN_PARAM, TARGET_PARAM};
            }

        @Override
        protected String getTypeId()
            {
            return RegexMatchCondition.TYPE_ID;
            }
        }
    }