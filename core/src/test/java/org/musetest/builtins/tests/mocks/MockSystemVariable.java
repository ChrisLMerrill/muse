package org.musetest.builtins.tests.mocks;

import org.musetest.builtins.value.sysvar.*;
import org.musetest.core.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockSystemVariable implements SystemVariableProvider
    {
    @Override
    public boolean provides(String name)
        {
        return NAME.equals(name);
        }

    @Override
    public Object resolve(String name, MuseExecutionContext context) throws ValueSourceResolutionError
        {
        if (NAME.equals(name))
            return VALUE;
        return null;
        }

    public final static String NAME = "mock-sysvar";
    public final static String VALUE = "mock-sysvar-value";
    }


