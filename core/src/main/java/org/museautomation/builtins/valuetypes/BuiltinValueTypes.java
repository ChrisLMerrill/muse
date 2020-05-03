package org.museautomation.builtins.valuetypes;

import org.museautomation.*;
import org.museautomation.core.valuetypes.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class BuiltinValueTypes implements ValueTypeProvider
    {
    @Override
    public List<MuseValueType> getValueTypes()
        {
        return TYPES;
        }

    private static final List<MuseValueType> TYPES;
    static
        {
        TYPES = List.of(
            new AnyValueType(),
            new StringValueType(),
            new IntegerValueType(),
            new BooleanValueType());
        }
    }