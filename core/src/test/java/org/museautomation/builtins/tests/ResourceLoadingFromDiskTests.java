package org.museautomation.builtins.tests;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.step.*;
import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.origin.*;
import org.museautomation.core.step.*;
import org.museautomation.utils.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ResourceLoadingFromDiskTests
    {
    @Test
    void testLoadingMacroStep() throws IOException
        {
        File file = TestResources.getFile("test_files/MacroStep.json", this.getClass());
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(file));
        Assertions.assertEquals(1, resources.size());
        MuseResource resource = resources.get(0);
        Assertions.assertEquals(new Macro.MacroResourceType(), resource.getType());
        Assertions.assertEquals("MacroStep", resource.getId());

        Assertions.assertTrue(resource instanceof Macro);
        Macro macro = (Macro) resource;

        StepConfiguration main_step = macro.getStep();
        Assertions.assertEquals(BasicCompoundStep.TYPE_ID, main_step.getType());
        Assertions.assertEquals(2, main_step.getChildren().size());
        Assertions.assertEquals(LogMessage.TYPE_ID, main_step.getChildren().get(0).getType());
        Assertions.assertEquals(StoreVariable.TYPE_ID, main_step.getChildren().get(1).getType());
        }
    }


