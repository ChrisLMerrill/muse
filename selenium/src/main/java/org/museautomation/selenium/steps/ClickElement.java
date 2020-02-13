package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("click-element")
@MuseStepName("Click")
@MuseInlineEditString("click {element}")
@MuseStepIcon("glyph:FontAwesome:HAND_ALT_UP")
@MuseStepTypeGroup("Selenium")
@MuseStepShortDescription("Click an element")
@MuseStepLongDescription("Resolves the 'element' source to a WebElement and then calls the click() method.")
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to click", type = SubsourceDescriptor.Type.Named, name = ClickElement.ELEMENT_PARAM)
public class ClickElement extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public ClickElement(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _element_source = getValueSource(config, ELEMENT_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        getElement(_element_source, context).click();
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _element_source;

    public final static String ELEMENT_PARAM = "element";

    public final static String TYPE_ID = ClickElement.class.getAnnotation(MuseTypeId.class).value();
    }


