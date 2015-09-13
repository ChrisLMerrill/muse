package org.musetest.seleniumide;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface LocatorConverter
    {
    ValueSourceConfiguration createLocator(String parameter);
    }


