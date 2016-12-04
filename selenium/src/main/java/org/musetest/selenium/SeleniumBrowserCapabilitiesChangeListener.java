package org.musetest.selenium;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface SeleniumBrowserCapabilitiesChangeListener
    {
    void capabilityChanged(String name, Object old_value, Object new_value);
    }


