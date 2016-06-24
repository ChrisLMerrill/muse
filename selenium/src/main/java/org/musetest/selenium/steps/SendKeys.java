package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.descriptor.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("send-keys")
@MuseStepName("Send keys")
@MuseInlineEditString("send {keys} to {element}")
@MuseStepIcon("glyph:FontAwesome:KEYBOARD_ALT")
@MuseStepTypeGroup("Selenium")
@MuseStepShortDescription("Send keystrokes to an element")
@MuseStepLongDescription("Resolves the 'keys' source to a String and the 'element' source to a WebElement. If both succeed, then sendKeys() method of WebElement is called with the keys string. Control keys are not yet supported.")
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to send the keys to", type = SubsourceDescriptor.Type.Named, name = SendKeys.ELEMENT_PARAM)
@MuseSubsourceDescriptor(displayName = "Keys", description = "Text string containing the keys to send to the element", type = SubsourceDescriptor.Type.Named, name = SendKeys.KEYS_PARAM)
public class SendKeys extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public SendKeys(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(config);
        _keys_source = getValueSource(config, KEYS_PARAM, true, project);
        _element_source = getValueSource(config, ELEMENT_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws StepExecutionError
        {
        WebElement element = getElement(_element_source, context);

        CharSequence keys = getValue(_keys_source, context, false, CharSequence.class);
        element.sendKeys(keys);

        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _keys_source;
    private MuseValueSource _element_source;

    public final static String KEYS_PARAM = "keys";
    public final static String ELEMENT_PARAM = "element";

    public final static String TYPE_ID = SendKeys.class.getAnnotation(MuseTypeId.class).value();
    }


