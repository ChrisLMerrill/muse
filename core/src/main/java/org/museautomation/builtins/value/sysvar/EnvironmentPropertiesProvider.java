package org.museautomation.builtins.value.sysvar;

import java.util.*;

/**
 * Implementors provide specific properties on the Environment system variable.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // methods are called via reflection.
public interface EnvironmentPropertiesProvider
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

