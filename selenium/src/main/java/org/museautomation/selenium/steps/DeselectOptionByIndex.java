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
@MuseTypeId("deselect-index")
@MuseStepName("Deselect by index")
@MuseInlineEditString("deselect choice #{index} in {element}")
@MuseStepIcon("glyph:FontAwesome:HAND_ALT_UP")
@MuseStepTypeGroup("Selenium.Select")
@MuseStepShortDescription("Deselect an option by index")
@MuseStepLongDescription("Resolves the 'element' source to a Select WebElement and then deselects the option with index = 'index' (zero-based).")
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to deselect from", type = SubsourceDescriptor.Type.Named, name = DeselectOptionByIndex.ELEMENT_PARAM)
@MuseSubsourceDescriptor(displayName = "Index", description = "Index of the entry to deselect (expects an integer)", type = SubsourceDescriptor.Type.Named, name = DeselectOptionByIndex.INDEX_PARAM)
public class DeselectOptionByIndex extends SelectOptionBaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public DeselectOptionByIndex(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project, INDEX_PARAM);
        }

    protected void executeSelection(Select select, String option, StepExecutionContext context)
        {
        select.deselectByIndex(Integer.parseInt(option));
        }

    public final static String INDEX_PARAM = "index";

    public final static String TYPE_ID = DeselectOptionByIndex.class.getAnnotation(MuseTypeId.class).value();
    }