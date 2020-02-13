package org.museautomation.builtins.value.sysvar;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.storage.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // discovered via reflection
public class ProjectMetaVariableProvider implements  SystemVariableProvider
    {
    @Override
    public boolean provides(String name)
        {
        return PROJECT_VARNAME.equals(name);
        }

    @Override
    public Object resolve(String name, MuseExecutionContext context) throws ValueSourceResolutionError
        {
        ResourceStorage storage = context.getProject().getResourceStorage();
        if (storage instanceof FolderIntoMemoryResourceStorage)
            return ((FolderIntoMemoryResourceStorage)storage).getBaseLocation().getAbsolutePath();
        else
            throw new ValueSourceResolutionError("'project-location' SystemVariable for in-memory projects is undefined.");
        }

    public final static String PROJECT_VARNAME = "project-location";
    }


