package org.musetest.selenium.values;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public class CurrentUrlValueSourceStringExpressionSupport extends ContantValueSourceStringExpressionSupport
    {
    public CurrentUrlValueSourceStringExpressionSupport()
        {
        super(CurrentUrlValueSource.NAME, CurrentUrlValueSource.TYPE_ID);
        }
    }


