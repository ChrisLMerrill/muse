package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.descriptor.*;
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
@MuseStepLongDescription("Resolves the 'element' source to a WebElement and then moves the mouse to that element.")
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
        new Actions(getDriver(context)).moveToElement(element).perform();
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _element_source;

    public final static String ELEMENT_PARAM = "element";

    public final static String TYPE_ID = MoveMouseToElement.class.getAnnotation(MuseTypeId.class).value();
    }


