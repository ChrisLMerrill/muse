package org.museautomation.selenium.valuetypes;

import org.museautomation.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class WebdriverValueType implements MuseValueType
    {
    @Override
    public String getId()
        {
        return "browser";
        }

    @Override
    public String getName()
        {
        return "Browser";
        }

    @Override
    public boolean isInstance(Object obj)
        {
        return obj instanceof WebDriver;
        }

    @Override
    public boolean equals(Object obj)
        {
        return obj instanceof WebdriverValueType && getId().equals(((WebdriverValueType) obj).getId());
        }
    }