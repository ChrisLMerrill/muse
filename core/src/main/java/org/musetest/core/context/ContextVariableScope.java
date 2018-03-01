package org.musetest.core.context;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public enum ContextVariableScope
    {
    Project,
    Suite,
    Execution,
    Local,      // the closest scope
    }
