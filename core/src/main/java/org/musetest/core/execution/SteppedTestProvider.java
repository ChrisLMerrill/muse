package org.musetest.core.execution;

import org.musetest.core.*;
import org.musetest.core.steptest.*;

/**
 * Convenience wrapper, also allows deferred lookups.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface SteppedTestProvider
    {
    SteppedTest getTest();
    MuseProject getProject();
    }

