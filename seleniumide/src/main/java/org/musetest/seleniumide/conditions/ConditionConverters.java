package org.musetest.seleniumide.conditions;

import org.reflections.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ConditionConverters
    {
    public ConditionConverter find(String id)
        {
        for (ConditionConverter converter : _converters)
            if (converter.getId().equals(id))
                return converter;
        return null;
        }

    private ConditionConverters()
        {
        Reflections reflections = new Reflections("org.musetest.seleniumide.conditions");
        Set<Class<? extends ConditionConverter>> converter_classes = reflections.getSubTypesOf(ConditionConverter.class);
        for (Class<? extends ConditionConverter> converter_class : converter_classes)
            try
                {
                _converters.add(converter_class.newInstance());
                }
            catch (Exception e)
                {
                LOG.error(String.format("Unable to instantiate %s. Does it have a public no-args constructor?", converter_class.getSimpleName()));
                }
        }

    public static ConditionConverters getInstance()
        {
        if (INSTANCE == null)
            INSTANCE = new ConditionConverters();
        return INSTANCE;
        }

    private List<ConditionConverter> _converters = new ArrayList<>();

    private static ConditionConverters INSTANCE;

    private final static Logger LOG = LoggerFactory.getLogger(ConditionConverters.class);
    }


