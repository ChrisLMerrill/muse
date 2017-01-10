package org.musetest.seleniumide.steps;

import org.musetest.core.step.*;
import org.musetest.seleniumide.*;
import org.reflections.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepConverters implements StepConverter
    {
    public static StepConverters get()
        {
        if (INSTANCE == null)
            INSTANCE = new StepConverters();
        return INSTANCE;
        }

    @Override
    public StepConfiguration convertStep(TestConverter test_converter, String command, String param1, String param2) throws UnsupportedError
        {
        for (StepConverter converter : _converters)
            {
            StepConfiguration step = converter.convertStep(test_converter, command, param1, param2);
            if (step != null)
                return step;
            }
        return null;
        }

    private StepConverters()
        {
        Reflections reflections = new Reflections("org.musetest.seleniumide.steps");
        Set<Class<? extends StepConverter>> converter_classes = reflections.getSubTypesOf(StepConverter.class);
        for (Class<? extends StepConverter> converter_class : converter_classes)
            try
                {
                if (converter_class != getClass())
                    _converters.add(converter_class.newInstance());
                }
            catch (Exception e)
                {
                LOG.error(String.format("Unable to instantiate %s. Does it have a public no-args constructor?", converter_class.getSimpleName()));
                }
        }

    private List<StepConverter> _converters = new ArrayList<>();

    private static StepConverters INSTANCE;
    final static Logger LOG = LoggerFactory.getLogger(StepConverters.class);
    }


