package org.museautomation.core.task.state;

import org.museautomation.core.*;
import org.museautomation.core.events.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EndResolvingTransitionInputsEventType extends EventType
    {
    public EndResolvingTransitionInputsEventType()
   		{
   		super(TYPE_ID, "End Resolving Transition Inputs");
   		}

    public static MuseEvent create()
        {
        return new MuseEvent(TYPE_ID);
        }

    public final static String TYPE_ID = "st-end-inputs";
    }