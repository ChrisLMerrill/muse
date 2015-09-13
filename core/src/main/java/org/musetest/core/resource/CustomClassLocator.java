package org.musetest.core.resource;

import org.reflections.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class CustomClassLocator extends DefaultClassLocator
    {
    public CustomClassLocator(Reflections reflections)
        {
        _reflections = reflections;
        }
    }


