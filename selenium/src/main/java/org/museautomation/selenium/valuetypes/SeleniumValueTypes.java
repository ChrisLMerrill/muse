package org.museautomation.selenium.valuetypes;

import org.museautomation.*;
import org.museautomation.builtins.valuetypes.*;
import org.museautomation.core.valuetypes.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SeleniumValueTypes implements ValueTypeProvider
    {
    @Override
    public List<MuseValueType> getValueTypes()
        {
        return TYPES;
        }

    private static List<MuseValueType> TYPES;
    static
        {
        TYPES = List.of(
            new WebdriverValueType());
        }
    }