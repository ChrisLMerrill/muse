package org.musetest.builtins.condition;

import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("glob-match")
@MuseValueSourceName("Glob match")
@MuseValueSourceTypeGroup("Matching")
@MuseValueSourceShortDescription("Matches a string against a glob pattern")
@MuseValueSourceLongDescription("Accepts a Posix-style glob pattern and matches it against the target string. Returns true on match. Supports these wildcards: *, ? and [a-zA-Z0-9]")
@MuseStringExpressionSupportImplementation(GlobMatchCondition.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Pattern", description = "The pattern to use", type = SubsourceDescriptor.Type.Named, name = GlobMatchCondition.PATTERN_PARAM)
@MuseSubsourceDescriptor(displayName = "Target", description = "The string to check", type = SubsourceDescriptor.Type.Named, name = GlobMatchCondition.TARGET_PARAM)
public class GlobMatchCondition extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public GlobMatchCondition(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _pattern_source = getValueSource(config, PATTERN_PARAM, true, project);
        _target_source = getValueSource(config, TARGET_PARAM, true, project);
        }

    @Override
    public Boolean resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String pattern = getValue(_pattern_source, context, false, String.class);
        String target = getValue(_target_source, context, false, String.class);

        try
            {
            GlobPattern matcher = new GlobPattern(pattern);
            boolean matches = matcher.matches(target);
            context.raiseEvent(ConditionEvaluatedEventType.create(String.format("Glob pattern %s matches %s is %s", pattern, target, matches)));
            return matches;
            }
        catch (Exception e)
            {
            throw new ValueSourceResolutionError(String.format("Unable to execute glob pattern match %s on %s: %s", pattern, target, e.getMessage()), e);
            }
        }

    @Override
    public String getDescription()
        {
        return "globMatch(" + _pattern_source.getDescription() + "," + _target_source.getDescription() + ")";
        }

    private MuseValueSource _pattern_source;
    private MuseValueSource _target_source;

    public final static String PATTERN_PARAM = "pattern";
    public final static String TARGET_PARAM = "target";
    public final static String TYPE_ID = GlobMatchCondition.class.getAnnotation(MuseTypeId.class).value();

    // for supporting the text expressions
    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "globMatch";
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
            return GlobMatchCondition.TYPE_ID;
            }
        }
    }
