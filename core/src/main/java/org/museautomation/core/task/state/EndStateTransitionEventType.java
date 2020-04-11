package org.museautomation.core.task.state;

import org.museautomation.core.*;
import org.museautomation.core.events.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EndStateTransitionEventType extends EventType
    {
    public EndStateTransitionEventType()
   		{
   		super(TYPE_ID, "End State Transition");
   		}

    public static MuseEvent create()
        {
        return new MuseEvent(TYPE_ID);
        }


    public final static String TYPE_ID = "st-end";
    }