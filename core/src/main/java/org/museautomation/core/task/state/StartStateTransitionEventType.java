package org.museautomation.core.task.state;

import org.museautomation.core.*;
import org.museautomation.core.events.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StartStateTransitionEventType extends EventType
    {
    public StartStateTransitionEventType()
   		{
   		super(TYPE_ID, "Start State Transition");
   		}

    public static MuseEvent create()
        {
        return new MuseEvent(TYPE_ID);
        }

    public final static String TYPE_ID = "st-start";
    }