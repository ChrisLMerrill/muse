package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("click-element")
@MuseStepName("Click")
@MuseInlineEditString("click {element}")
@MuseStepIcon("glyph:FontAwesome:HAND")
@MuseStepTypeGroup("Selenium")
@MuseStepShortDescription("Click an element")
@MuseStepLongDescription("Resolves the 'element' source to a WebElement and then calls the click() method.")
public class ClickElement extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public ClickElement(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(config);
        _element_source = getValueSource(config, ELEMENT_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws StepExecutionError
        {
        getElement(_element_source, context).click();
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _element_source;

    public final static String ELEMENT_PARAM = "element";

    public final static String TYPE_ID = ClickElement.class.getAnnotation(MuseTypeId.class).value();
    }


