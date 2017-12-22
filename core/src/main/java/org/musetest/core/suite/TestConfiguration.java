package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.test.plugins.*;

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

    public TestConfiguration(MuseTest test, TestPlugin plugin)
        {
        _test = test;
        _plugins.add(plugin);
        }

    public MuseTest getTest()
        {
        return _test;
        }

    public String getName()
        {
        if (_name != null)
            return _name;
        return _test.getDescription();
        }

    public void setName(String name)
        {
        _name = name;
        }

    public List<TestPlugin> getPlugins()
        {
        return Collections.unmodifiableList(_plugins);
        }

    private final MuseTest _test;
    private final List<TestPlugin> _plugins = new ArrayList<>();
    private String _name;
    }


