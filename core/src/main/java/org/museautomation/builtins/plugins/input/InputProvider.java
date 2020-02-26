package org.museautomation.builtins.plugins.input;

import org.museautomation.core.*;
import org.museautomation.core.task.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface InputProvider
    {
    boolean isLastChanceProvider();

    /*
     * Returns a map of NVPs to be provided as required input values. It should only contain entries
     * for values that are not in set of values provided (values param).
     * @param inputs The inputs defined for the task.
     * @param values The context the values will be used in. Note that the values should NOT be injected
     * into the context in this method. This will be handled by the caller (possibly after further intervention).
     */
    Map<String, Object> gatherInputValues(TaskInputSet inputs, MuseExecutionContext context);
    }