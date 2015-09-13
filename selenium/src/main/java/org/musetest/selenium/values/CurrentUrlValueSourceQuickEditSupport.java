package org.musetest.selenium.values;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class CurrentUrlValueSourceQuickEditSupport extends ContantValueSourceQuickEditSupport
    {
    public CurrentUrlValueSourceQuickEditSupport()
        {
        super(CurrentUrlValueSource.NAME, CurrentUrlValueSource.TYPE_ID);
        }
    }


