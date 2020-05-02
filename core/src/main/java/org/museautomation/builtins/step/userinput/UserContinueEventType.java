package org.museautomation.builtins.step.userinput;

import org.museautomation.core.*;
import org.museautomation.core.events.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class UserContinueEventType extends EventType
    {
    public UserContinueEventType()
        {
        super(TYPE_ID, "User Continue");
        }

    public static MuseEvent create(String message)
        {
        MuseEvent event = new MuseEvent(TYPE_ID);
        event.setAttribute(MuseEvent.DESCRIPTION, message);
        return event;
        }

    public static MuseEvent createAbort(String message)
        {
        MuseEvent event = new MuseEvent(TYPE_ID);
        event.setAttribute(MuseEvent.DESCRIPTION, message);
        event.addTag(MuseEvent.TERMINATE);
        return event;
        }

    public final static String TYPE_ID = "user-continue";
    }