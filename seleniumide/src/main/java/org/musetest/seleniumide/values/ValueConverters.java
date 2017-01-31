package org.musetest.seleniumide.values;

import org.musetest.core.values.*;
import org.musetest.seleniumide.*;
import org.reflections.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueConverters implements ValueConverter
    {
    public static ValueConverters get()
        {
        if (INSTANCE == null)
            INSTANCE = new ValueConverters();
        return INSTANCE;
        }

    @Override
    public ValueSourceConfiguration convertValue(String parameter)
        {
        for (ValueConverter converter : _converters)
            {
            ValueSourceConfiguration source = converter.convertValue(parameter);
            if (source != null)
                return source;
            }
        return null;
        }

    private ValueConverters()
        {
        Reflections reflections = new Reflections("org.musetest.seleniumide.values");
        Set<Class<? extends ValueConverter>> converter_classes = reflections.getSubTypesOf(ValueConverter.class);
        for (Class<? extends ValueConverter> converter_class : converter_classes)
            try
                {
                if (converter_class.equals(DefaultValueConverter.class)
                    || converter_class.equals(getClass()))
                    continue;
                _converters.add(converter_class.newInstance());
                }
            catch (Exception e)
                {
                LOG.error(String.format("Unable to instantiate %s. Does it have a public no-args constructor?", converter_class.getSimpleName()));
                }
        _converters.add(new DefaultValueConverter());
        }

    private List<ValueConverter> _converters = new ArrayList<>();

    private static ValueConverters INSTANCE;
    private final static Logger LOG = LoggerFactory.getLogger(ValueConverters.class);
    }


