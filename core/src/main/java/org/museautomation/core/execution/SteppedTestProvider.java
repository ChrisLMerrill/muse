package org.museautomation.core.execution;

import org.museautomation.core.*;
import org.museautomation.core.steptest.*;

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

