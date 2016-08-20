package org.musetest.builtins.value.sysvar;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestVariableProvider implements SystemVariableProvider
    {
    @Override
    public boolean provides(String name)
        {
        return SYSVAR_NAME.equals(name);
        }

    @Override
    public Object resolve(String name, MuseExecutionContext context) throws ValueSourceResolutionError
        {
        if (SYSVAR_NAME.equals(name))
            {
            MuseExecutionContext top_context = context;
            while (top_context.getParent() != null)
                top_context = top_context.getParent();

            if (top_context instanceof TestExecutionContext)
                return new TestVariableProxy(((TestExecutionContext)top_context).getTest());
            }

        throw new ValueSourceResolutionError("Cannot get the test variable - not executed within the context of a test.");
        }

    public final static String SYSVAR_NAME = "test";

    /**
     * Provides controlled access to the test from a context SystemVariable.
     */
    @SuppressWarnings("WeakerAccess")  // can be accessed via reflection
    public class TestVariableProxy
        {
        public TestVariableProxy(MuseTest test)
            {
            _test = test;
            }

        public List<String> getTags()
            {
            return _test.getMetadata().getTags();
            }

        public String getId()
            {
            return _test.getMetadata().getId();
            }

        private MuseTest _test;
        }
    }


