package org.musetest.builtins.value.sysvar;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // methods are called via reflection.
public interface EnvironmentProviderInterface
    {
    Map<String,String> getVars();
    Dictionary getProps();
    String getUsername();
    String getHostname();

    String VARS_NAME = "vars";
    String PROPS_NAME = "props";
    String USERNAME_NAME = "username";
    String HOSTNAME_NAME = "hostname";
    }

