package org.musetest.seleniumide.steps;

import org.musetest.seleniumide.*;
import org.reflections.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepConverters
    {
    public static StepConverters get()
        {
        if (INSTANCE == null)
            INSTANCE = new StepConverters();
        return INSTANCE;
        }

    public StepConverter getConverter(String command)
        {
        return _converters.get(command);
        }

    private StepConverters()
        {
        Reflections reflections = new Reflections("org.musetest.seleniumide.steps");
        Set<Class<? extends StepConverter>> converter_classes = reflections.getSubTypesOf(StepConverter.class);
        for (Class<? extends StepConverter> converter_class : converter_classes)
            try
                {
                addConverter(converter_class.newInstance());
                }
            catch (Exception e)
                {
                LOG.error(String.format("Unable to instantiate %s. Does it have a public no-args constructor?", converter_class.getSimpleName()));
                }
        }

    private void addConverter(StepConverter converter)
        {
        _converters.put(converter.getCommand(), converter);
        }

    private Map<String, StepConverter> _converters = new HashMap<>();

    private static StepConverters INSTANCE;
    final static Logger LOG = LoggerFactory.getLogger(StepConverters.class);
    }


