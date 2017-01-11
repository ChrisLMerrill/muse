package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

import java.util.concurrent.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("random")
@MuseValueSourceName("Random Number")
@MuseValueSourceTypeGroup("Math")
@MuseValueSourceShortDescription("Generates a random integer from {min} to {max}")
@MuseValueSourceLongDescription("Resolves the 'min' and 'max' value sources as integers. Returns a random integer from min to max, inclusive.")
@MuseStringExpressionSupportImplementation(RandomNumberValueSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Minimum", description = "The minimum value", type = SubsourceDescriptor.Type.Named, name = RandomNumberValueSource.MIN_PARAM)
@MuseSubsourceDescriptor(displayName = "Maximum", description = "The maximum value", type = SubsourceDescriptor.Type.Named, name = RandomNumberValueSource.MAX_PARAM)
public class RandomNumberValueSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public RandomNumberValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _min_source = getValueSource(config, MIN_PARAM, true, project);
        _max_source = getValueSource(config, MAX_PARAM, true, project);
        }

    @Override
    public Long resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        long min = getValue(_min_source, context, false, Number.class).longValue();
        long max = getValue(_max_source, context, false, Number.class).longValue();
        return ThreadLocalRandom.current().nextLong(min, max + 1);
        }

    @Override
    public String getDescription()
        {
        return "random(" + _min_source.getDescription() + "," + _max_source.getDescription() + ")";
        }

    private MuseValueSource _min_source;
    private MuseValueSource _max_source;

    public final static String MIN_PARAM = "min";
    public final static String MAX_PARAM = "max";
    public final static String TYPE_ID = RandomNumberValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "random";
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
            return new String[] { RandomNumberValueSource.MIN_PARAM, RandomNumberValueSource.MAX_PARAM };
            }

        @Override
        protected String getTypeId()
            {
            return RandomNumberValueSource.TYPE_ID;
            }

        @Override
        protected boolean storeSingleArgumentAsSingleSubsource()
            {
            return true;
            }
        }
    }
