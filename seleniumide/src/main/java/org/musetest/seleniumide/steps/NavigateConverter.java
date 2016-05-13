package org.musetest.seleniumide.steps;

import org.musetest.core.step.*;
import org.musetest.selenium.steps.*;
import org.musetest.seleniumide.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // invoked via reflection from StepConverters
public class NavigateConverter implements StepConverter
    {
    @Override
    public StepConfiguration convertStep(TestConverter converter, String command, String param1, String param2) throws UnsupportedError
        {
        if (command.equals(BACK) || command.equals(BACK_AND_WAIT))
            return new StepConfiguration(NavigateBack.TYPE_ID);
        if (command.equals(REFRESH) || command.equals(REFRESH_AND_WAIT))
            return new StepConfiguration(RefreshPage.TYPE_ID);
        return null;
        }

    @Override
    public String[] getCommands()
        {
        return new String[] { BACK, BACK_AND_WAIT, REFRESH, REFRESH_AND_WAIT };
        }     // not calling this, anyway

    public static final String BACK = "goBack";
    public static final String BACK_AND_WAIT = "goBackAndWait";
    public static final String REFRESH = "refresh";
    public static final String REFRESH_AND_WAIT = "refreshAndWait";
    // note: there is apparently no forward implemented in the IDE
    }


