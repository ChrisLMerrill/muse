package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.context.*;

/**
 * Everything needed to run a test.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestConfiguration
    {
    public TestConfiguration(MuseTest test)
        {
        _test = test;
        _context = null;
        }

    public TestConfiguration(MuseTest test, TestExecutionContext context)
        {
        _test = test;
        _context = context;
        }

    public final MuseTest _test;
    public final TestExecutionContext _context;
    }


