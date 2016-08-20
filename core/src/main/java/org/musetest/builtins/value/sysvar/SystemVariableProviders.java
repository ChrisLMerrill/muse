package org.musetest.builtins.value.sysvar;

import org.musetest.core.*;
import org.musetest.core.util.*;

import java.util.*;

/**
 * Looks up the implementations of SystemVariableProvider.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SystemVariableProviders
    {
    public SystemVariableProviders(MuseProject project)
        {
        _project = project;
        }

    public List<SystemVariableProvider> getProviders()
        {
        if (_resolvers == null)
            _resolvers = new FactoryLocator(_project.getClassLocator()).findFactories(SystemVariableProvider.class);
        return _resolvers;
        }

    private final MuseProject _project;
    private List<SystemVariableProvider> _resolvers;
    }


