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
            TestExecutionContext test_context = MuseExecutionContext.findAncestor(context, TestExecutionContext.class);
            if (test_context == null)
	            throw new ValueSourceResolutionError("Cannot get the test variable - not executed within the context of a test.");
            return new TestVariableProxy(test_context.getTest());
            }

        throw new ValueSourceResolutionError("Cannot provide this value. Did you check provides() first?");
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

        public Set<String> getTags()
            {
            return _test.getTags();
            }

        public String getId()
            {
            return _test.getId();
            }

        private MuseTest _test;
        }
    }


