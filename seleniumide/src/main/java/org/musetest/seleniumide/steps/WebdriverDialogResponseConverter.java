package org.musetest.seleniumide.steps;

import org.musetest.core.step.*;
import org.musetest.selenium.steps.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // invoked via reflection from StepConverters
public class WebdriverDialogResponseConverter implements StepConverter
    {
    @Override
    public StepConfiguration convertStep(String base_url, String command, String param1, String param2)
        {
        if (command.equals(OK))
            return new StepConfiguration(AcceptDialog.TYPE_ID);
        if (command.equals(CANCEL_CONFIRM) || command.equals(CANCEL_PROMPT))
            return new StepConfiguration(CancelDialog.TYPE_ID);
        if (command.equals(ANSWER))
            {
            StepConfiguration respond = new StepConfiguration(SendKeysToDialog.TYPE_ID);
            respond.addSource(SendKeysToDialog.KEYS_PARAM, ValueConverters.get().convert(param1));

            StepConfiguration step = new StepConfiguration(BasicCompoundStep.TYPE_ID);
            step.setMetadataField(StepConfiguration.META_DESCRIPTION, "respond to prompt");
            step.addChild(respond);
            step.addChild(new StepConfiguration(AcceptDialog.TYPE_ID));

            return step;
            }
        return null;
        }

    public static final String OK = "webdriverChooseOkOnVisibleConfirmation";
    public static final String CANCEL_CONFIRM = "webdriverChooseCancelOnVisibleConfirmation";
    public static final String ANSWER = "webdriverAnswerOnVisiblePrompt";
    public static final String CANCEL_PROMPT = "webdriverChooseCancelOnVisiblePrompt";
    }


