package org.museautomation.builtins.plugins.state;

import org.museautomation.core.task.state.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StateProvider
    {
    List<InterTaskState> getStates(String type_id);
    }