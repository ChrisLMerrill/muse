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

    @Override
    public String getDescription(MuseEvent event)
        {
        if (event.hasTag(MuseEvent.ERROR))
            return "Unsatisified inputs: " + event.getAttributeAsString(MISSING_INPUTS);
        return "Inputs resolved successfully";
        }

    public static MuseEvent create()
        {
        return new MuseEvent(TYPE_ID);
        }

    public static MuseEvent createFailure(String missing)
        {
        MuseEvent event = new MuseEvent(TYPE_ID);
        event.setAttribute(MISSING_INPUTS, missing);
        event.addTag(MuseEvent.ERROR);
        return event;
        }

    public final static String TYPE_ID = "st-end-inputs";
    public final static String MISSING_INPUTS = "st-end-inputs";
    }