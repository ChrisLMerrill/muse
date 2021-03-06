package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.descriptor.*;
import org.openqa.selenium.support.ui.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("select-index")
@MuseStepName("Select by index")
@MuseInlineEditString("select choice #{index} in {element}")
@MuseStepIcon("glyph:FontAwesome:HAND_ALT_UP")
@MuseStepTypeGroup("Selenium.Select")
@MuseStepShortDescription("Select an option by index")
@MuseStepLongDescription("Resolves the 'element' source to a Select WebElement and then selects the option with index = 'index' (zero-based).")
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to select from", type = SubsourceDescriptor.Type.Named, name = SelectOptionByIndex.ELEMENT_PARAM)
@MuseSubsourceDescriptor(displayName = "Index", description = "Index of the entry to select (expects an integer)", type = SubsourceDescriptor.Type.Named, name = SelectOptionByIndex.INDEX_PARAM)
public class SelectOptionByIndex extends SelectOptionBaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public SelectOptionByIndex(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project, INDEX_PARAM);
        }

    protected void executeSelection(Select select, String option, StepExecutionContext context)
        {
        select.selectByIndex(Integer.parseInt(option));
        }

    public final static String INDEX_PARAM = "index";

    public final static String TYPE_ID = SelectOptionByIndex.class.getAnnotation(MuseTypeId.class).value();
    }