package org.museautomation.core.output;

import org.junit.jupiter.api.*;
import org.museautomation.core.context.*;
import org.museautomation.core.project.*;
import org.museautomation.core.task.*;
import org.museautomation.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class OutputTests
    {
    @Test
    public void storeAndRetrieveOutput()
        {
        SimpleProject project = new SimpleProject();
        TestTask task = new TestTask();
        DefaultTaskExecutionContext context = new DefaultTaskExecutionContext(project, task);

        task.execute(context);

        Assertions.assertEquals(123L, context.outputs().getOutput("abc"));
        Assertions.assertEquals("AND ME", context.outputs().getOutput("xyz"));
        Assertions.assertEquals(2, context.outputs().getOutputNames().size());
        Assertions.assertTrue(context.outputs().getOutputNames().contains("abc"));
        Assertions.assertTrue(context.outputs().getOutputNames().contains("xyz"));
        }

    static class TestTask extends BaseMuseTask
        {
        @Override
        protected boolean executeImplementation(TaskExecutionContext context)
            {
            context.outputs().storeOutput("abc", 123L);
            context.outputs().storeOutput("xyz", "AND ME");
            return true;
            }

        @Override
        public Map<String, ValueSourceConfiguration> getDefaultVariables()
            {
            return null;
            }

        @Override
        public void setDefaultVariables(Map<String, ValueSourceConfiguration> default_variables)
            {

            }

        @Override
        public void setDefaultVariable(String name, ValueSourceConfiguration source)
            {

            }
        }
    }