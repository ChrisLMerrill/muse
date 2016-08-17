package org.musetest.builtins.tests.mocks;

import com.sun.javafx.runtime.*;
import org.musetest.builtins.value.sysvar.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockEnvironment implements EnvironmentProviderInterface
    {
    public void setVariable(String name, String value)
        {
        _vars.put(name, value);
        }

    public void setProperty(String key, String value)
        {
        _props.setProperty(key, value);
        }

    @Override
    public Map<String, String> getVars()
        {
        return _vars;
        }

    @Override
    public Dictionary getProps()
        {
        return _props;
        }

    private Map<String, String> _vars = new HashMap<>();
    private Properties _props = new Properties();
    }


