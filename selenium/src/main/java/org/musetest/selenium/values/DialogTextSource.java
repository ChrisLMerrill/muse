package org.musetest.selenium.values;

import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.selenium.*;
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
    public DialogTextSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
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
            throw new ValueSourceResolutionError("No alert is present");
            }
        }

    @Override
    public String getDescription()
        {
        return "alertText()";
        }

    public final static String TYPE_ID = DialogTextSource.class.getAnnotation(MuseTypeId.class).value();

    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "alertText";
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
