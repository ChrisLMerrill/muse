package org.museautomation.selenium.values;

import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.museautomation.selenium.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("dialog-text")
@MuseValueSourceName("Dialog Text")
@MuseValueSourceTypeGroup("Selenium.Dialog")
@MuseValueSourceShortDescription("Returns the dialog text.")
@MuseValueSourceLongDescription("Returns the text content of the currently-active modal dialog (a.k.a. Javascript Alert)")
@MuseStringExpressionSupportImplementation(DialogTextSource.StringExpressionSupport.class)
@SuppressWarnings("unused")  // instantiated via reflection
public class DialogTextSource extends BaseSeleniumValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public DialogTextSource(ValueSourceConfiguration config, MuseProject project)
        {
        super(config, project);
        }

    @Override
    public String resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        try
            {
            Alert alert = getDriver(context).switchTo().alert();
            return alert.getText();
            }
        catch (NoAlertPresentException e)
            {
            throw new ValueSourceResolutionError("No dialog is present");
            }
        }

    @Override
    public String getDescription()
        {
        return "dialogText()";
        }

    public final static String TYPE_ID = DialogTextSource.class.getAnnotation(MuseTypeId.class).value();

    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "dialogText";
            }

        @Override
        protected int getNumberArguments()
            {
            return 0;
            }

        @Override
        protected String getTypeId()
            {
            return DialogTextSource.TYPE_ID;
            }

        @Override
        protected boolean storeSingleArgumentAsSingleSubsource()
            {
            return true;
            }
        }
    }
