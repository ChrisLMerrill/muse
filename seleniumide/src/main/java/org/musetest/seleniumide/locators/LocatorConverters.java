package org.musetest.seleniumide.locators;

import org.musetest.core.values.*;
import org.musetest.seleniumide.*;
import org.reflections.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class LocatorConverters
    {
    public static LocatorConverters get()
        {
        if (INSTANCE == null)
            INSTANCE = new LocatorConverters();
        return INSTANCE;
        }

    public ValueSourceConfiguration convert(String param) throws UnsupportedError
        {
        for (LocatorConverter converter : _converters)
            {
            ValueSourceConfiguration source = converter.createLocator(param);
            if (source != null)
                return source;
            }
        throw new UnsupportedError(String.format("Unable to produce value source for element locator parameter '%s'", param));
        }

    private LocatorConverters()
        {
        Reflections reflections = new Reflections("org.musetest.seleniumide.locators");
        Set<Class<? extends LocatorConverter>> converter_classes = reflections.getSubTypesOf(LocatorConverter.class);
        for (Class<? extends LocatorConverter> converter_class : converter_classes)
            try
                {
                _converters.add(converter_class.newInstance());
                }
            catch (Exception e)
                {
                LOG.error(String.format("Unable to instantiate %s. Does it have a public no-args constructor?", converter_class.getSimpleName()));
                }
        }

    private List<LocatorConverter> _converters = new ArrayList<>();

    private static LocatorConverters INSTANCE;
    final static Logger LOG = LoggerFactory.getLogger(LocatorConverters.class);
    }


