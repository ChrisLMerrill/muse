package org.musetest.core.values;

import org.musetest.core.*;

/**
 * Interface for classes that support quick-editing of a value source. Quick-editing allows
 * the user to enter a text string that is parsed to create the value source.
 * ValueSource implementations are not required to implement this.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ValueSourceStringExpressionSupport
    {
    ValueSourceConfiguration parse(String string, MuseProject project);
    String asString(ValueSourceConfiguration config, MuseProject project);
    int getPriority();
    }

