package org.musetest.builtins.tests;


import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.helpers.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.origin.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.step.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ResourceLoadingFromDiskTests
    {
    @Test
    public void testLoadingMacroStep() throws IOException
        {
        File file = TestUtils.getTestResource("test_files/MacroStep.json", this.getClass());
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(file));
        Assert.assertEquals(1, resources.size());
        MuseResource resource = resources.get(0);
        Assert.assertEquals(ResourceTypes.Macro, resource.getMetadata().getType());
        Assert.assertEquals("MacroStep", resource.getMetadata().getId());

        Assert.assertTrue(resource instanceof Macro);
        Macro macro = (Macro) resource;

        StepConfiguration main_step = macro.getStep();
        Assert.assertEquals(BasicCompoundStep.TYPE_ID, main_step.getType());
        Assert.assertEquals(2, main_step.getChildren().size());
        Assert.assertEquals(LogMessage.TYPE_ID, main_step.getChildren().get(0).getType());
        Assert.assertEquals(StoreVariable.TYPE_ID, main_step.getChildren().get(1).getType());
        }
    }


