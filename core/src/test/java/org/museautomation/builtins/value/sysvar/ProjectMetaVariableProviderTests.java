package org.museautomation.builtins.value.sysvar;

import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.project.*;
import org.museautomation.core.values.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ProjectMetaVariableProviderTests
    {
    @Test
    void testProjectLocation() throws ValueSourceResolutionError
        {
        ProjectMetaVariableProvider provider = new ProjectMetaVariableProvider();
        Assertions.assertTrue(provider.provides(ProjectMetaVariableProvider.PROJECT_VARNAME));

        File project_location = new File("src/test/other_resources/projects/settings");
        MuseProject project = ProjectFactory.create(project_location, Collections.emptyMap());
        Assertions.assertEquals(project_location.getAbsolutePath(), provider.resolve(ProjectMetaVariableProvider.PROJECT_VARNAME, new ProjectExecutionContext(project)));
        }
    }