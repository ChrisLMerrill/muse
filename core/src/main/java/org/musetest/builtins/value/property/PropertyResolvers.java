package org.musetest.builtins.value.property;

import org.musetest.core.*;
import org.musetest.core.util.*;

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

    public List<PropertyResolver> getPropertyResolvers()
        {
        if (_resolvers == null)
            _resolvers = new FactoryLocator(_project.getClassLocator()).findFactories(PropertyResolver.class);
        return _resolvers;
        }

    private final MuseProject _project;
    private List<PropertyResolver> _resolvers;
    }


