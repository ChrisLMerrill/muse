package org.musetest.builtins.value.sysvar;

import org.musetest.core.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EnvironmentProvider implements SystemVariableProvider
    {
    @Override
    public boolean provides(String name)
        {
        return VARNAME1.equals(name) || VARNAME2.equals(name);
        }

    @Override
    public Object resolve(String name, MuseExecutionContext context) throws ValueSourceResolutionError
        {
        if (provides(name))
            {
            // for unit test
            EnvironmentProviderInterface override = EPI_OVERRIDES.get(context.getProject());
            if (override != null)
                return override;

            return new DefaultEnvironmentProvider();
            }
        return null;
        }

    public final static String VARNAME1 = "env";
    public final static String VARNAME2 = "environment";

    /**
     * For unit testing - install an override for a specific project.
     */
    private final static Map<MuseProject, EnvironmentProviderInterface> EPI_OVERRIDES = new HashMap<>();
    public static void overrideImplementation(MuseProject project, EnvironmentProviderInterface implementation)
        {
        EPI_OVERRIDES.put(project, implementation);
        }
    }


