package org.musetest.builtins.value.sysvar;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface EnvironmentProviderInterface
    {
    Map<String,String> getVars();
    Dictionary getProps();

    String VARS_NAME = "vars";
    String PROPS_NAME = "props";
    }

