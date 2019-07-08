package org.musetest.seleniumide.conditions;

/**
 * In SeleniumIDE there are separate commands for checking the text of an Alert, Prompt or Confirmation.
 * But in Selenium, they are all treated the same, so this just uses the Alert behavior.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // instantiated via reflection
public class PromptConditionConverter extends AlertConditionConverter
    {
    public PromptConditionConverter()
        {
        super("Prompt");
        }
    }