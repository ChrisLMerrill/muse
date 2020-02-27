package org.museautomation.builtins.plugins.state;

import org.museautomation.core.task.state.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StateStore
    {
    List<InterTaskState> findStates(String type_id);
    void addState(InterTaskState state);
    }