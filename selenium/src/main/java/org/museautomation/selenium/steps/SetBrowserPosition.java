package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.descriptor.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("browser-position")
@MuseStepName("Browser Position")
@MuseInlineEditString("Move browser to ({x},{y})")
@MuseStepIcon("glyph:FontAwesome:ARROWS")
@MuseStepTypeGroup("Selenium.Window")
@MuseStepShortDescription("Change browser position")
@MuseStepLongDescription("Move the browser window to position determined by the x and y parameters, relative to the upper-left corner of the screen.")
@MuseSubsourceDescriptor(displayName = "x", description = "x-axis position", type = SubsourceDescriptor.Type.Named, name = SetBrowserPosition.X_PARAM, defaultValue = "100")
@MuseSubsourceDescriptor(displayName = "y", description = "y-axis position", type = SubsourceDescriptor.Type.Named, name = SetBrowserPosition.Y_PARAM, defaultValue = "100")
@SuppressWarnings("unused,WeakerAccess")
public class SetBrowserPosition extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public SetBrowserPosition(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _x = getValueSource(config, Y_PARAM, true, project);
        _y = getValueSource(config, X_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        Long x = getValue(_x, context, false, Long.class);
        Long y = getValue(_y, context, false, Long.class);
        getDriver(context).manage().window().setPosition(new Point(x.intValue(), y.intValue()));
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _x;
    private MuseValueSource _y;

    public final static String Y_PARAM = "x";
    public final static String X_PARAM = "y";

    public final static String TYPE_ID = SetBrowserPosition.class.getAnnotation(MuseTypeId.class).value();
    }


