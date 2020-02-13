package org.museautomation.core.resource;

import org.reflections.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class CustomClassLocator extends DefaultClassLocator
    {
    public CustomClassLocator(ClassLoader classloader, Reflections reflections)
        {
        _reflections = reflections;
        _classloader = classloader;
        }
    }


