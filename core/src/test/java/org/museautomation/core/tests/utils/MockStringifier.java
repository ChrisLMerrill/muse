package org.museautomation.core.tests.utils;

import org.museautomation.core.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess,unused") // located via reflection
public class MockStringifier extends Stringifier
    {
    @Override
    public Object create(Object target)
        {
        if (target instanceof MockStringifier)
            return new Object()
                {
                @Override
                public String toString()
                    {
                    return OUTPUT;
                    }
                };
        else
            return null;
        }

    @Override
    public String toString()
        {
        return "FAIL";
        }

    public final static String OUTPUT = "o.m.c.t.u.ms.output";
    }


