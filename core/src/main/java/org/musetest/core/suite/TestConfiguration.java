package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.context.*;

import java.util.*;

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
        }

    public TestConfiguration(MuseTest test, ContextInitializer initializer)
        {
        _test = test;
        _initializers.add(initializer);
        }

    public MuseTest getTest()
        {
        return _test;
        }

    public List<ContextInitializer> getInitializers()
        {
        return Collections.unmodifiableList(_initializers);
        }

    private final MuseTest _test;
    private final List<ContextInitializer> _initializers = new ArrayList<>();
    }


