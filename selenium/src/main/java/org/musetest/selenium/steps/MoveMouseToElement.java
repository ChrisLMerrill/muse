package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.values.descriptor.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("move-mouse-to-element")
@MuseStepName("Move Mouse to...")
@MuseInlineEditString("Move mouse to {element}")
@MuseStepIcon("glyph:FontAwesome:HAND_ALT_UP")
@MuseStepTypeGroup("Selenium")
@MuseStepShortDescription("Move the mouse to an element")
@MuseStepLongDescription("Resolves the 'element' source to a WebElement and then moves the mouse to hat element.")
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to move to", type = SubsourceDescriptor.Type.Named, name = MoveMouseToElement.ELEMENT_PARAM)
public class MoveMouseToElement extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public MoveMouseToElement(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _element_source = getValueSource(config, ELEMENT_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        WebElement element = getElement(_element_source, context);
        new Actions(getDriver(context)).moveToElement(element).click(element).perform();
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _element_source;

    public final static String ELEMENT_PARAM = "element";

    public final static String TYPE_ID = MoveMouseToElement.class.getAnnotation(MuseTypeId.class).value();
    }


