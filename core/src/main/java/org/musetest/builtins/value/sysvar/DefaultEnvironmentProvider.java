package org.musetest.builtins.value.sysvar;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultEnvironmentProvider implements EnvironmentProviderInterface
    {
    @Override
    public Map<String,String> getVars()
        {
        return System.getenv();
        }

    @Override
    public Dictionary getProps()
        {
        return System.getProperties();
        }

    @Override
    public String getUsername()
        {
        return System.getProperties().getProperty("user.name");
        }

    @Override
    public String toString()
        {
        return getClass().getSimpleName();
        }
    }



