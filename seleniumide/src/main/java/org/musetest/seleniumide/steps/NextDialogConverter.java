package org.musetest.seleniumide.steps;

import org.musetest.core.step.*;
import org.musetest.selenium.steps.*;
import org.musetest.seleniumide.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // invoked via reflection from StepConverters
public class NextDialogConverter implements StepConverter
    {
    @Override
    public StepConfiguration convertStep(String base_url, String command, String param1, String param2) throws UnsupportedError
        {
        if (command.equals(OK_NEXT_CONFIRM)
            || command.equals(CANCEL_NEXT_CONFIRM)
            || command.equals(ANSWER_NEXT_PROMPT)
            || command.equals(CANCEL_NEXT_PROMPT))
            throw new UnsupportedError(command + " is not necessary in Selenium tests, because it can respond to Javascript dialogs on-demand. Use the Accept Dialog, Cancel Dialog and Send Keys to Dialog steps, instead.");
        return null;
        }

    public static final String OK_NEXT_CONFIRM = "chooseOkOnNextConfirmation";
    public static final String CANCEL_NEXT_CONFIRM = "chooseCancelOnNextConfirmation";
    public static final String ANSWER_NEXT_PROMPT = "answerOnNextPrompt";
    public static final String CANCEL_NEXT_PROMPT = "chooseCancelOnNextPrompt";
    }


