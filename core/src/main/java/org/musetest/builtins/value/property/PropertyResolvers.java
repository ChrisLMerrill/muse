package org.musetest.builtins.value.property;

import org.musetest.core.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class PropertyResolvers
    {
    public PropertyResolvers(MuseProject project)
        {
        _project = project;
        }

    List<PropertyResolver> getPropertyResolvers()
        {
        if (_resolvers == null)
            _resolvers = _project.getClassLocator().getInstances(PropertyResolver.class);
        return _resolvers;
        }

    private final MuseProject _project;
    private List<PropertyResolver> _resolvers;
    }


