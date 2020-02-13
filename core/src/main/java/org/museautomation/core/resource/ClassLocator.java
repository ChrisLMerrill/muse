package org.museautomation.core.resource;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ClassLocator
    {
    Map<String, Class> getAnnotationValueToClassMap(Class annotation_class);
    <T> List<T> getInstances(Class T);
    <T> List<Class> getImplementors(Class T);
    }


