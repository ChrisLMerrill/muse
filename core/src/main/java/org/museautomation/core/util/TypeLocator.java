package org.museautomation.core.util;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TypeLocator
    {
    public TypeLocator(MuseProject project)
        {
        if (project == null)
            _locator = DefaultClassLocator.get();
        else
            _locator = project.getClassLocator();
        }

    public TypeLocator(ClassLocator locator)
        {
        if (locator == null)
            throw new IllegalArgumentException("locator cannot be null");
        else
            _locator = locator;
        }

    public Class getClassForTypeId(String type_id)
        {
        return _locator.getAnnotationValueToClassMap(MuseTypeId.class).get(type_id);
        }

    @SuppressWarnings("unchecked")
    public <T> List<Class<? extends T>> getImplementors(Class superclass)
        {
        Map<String, Class> type_map = _locator.getAnnotationValueToClassMap(MuseTypeId.class);
        List<Class<? extends T>> types = new ArrayList<>();
        for (Class type : type_map.values())
            {
            if (superclass.isAssignableFrom(type))
                types.add(type);
            }
        return types;
        }

    public Set<String> getTypes()
        {
        return _locator.getAnnotationValueToClassMap(MuseTypeId.class).keySet();
        }

    private final ClassLocator _locator;
    }


