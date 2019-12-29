package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.values.descriptor.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("browser-size")
@MuseStepName("Browser Size")
@MuseInlineEditString("Resize browser to {width}x{height}")
@MuseStepIcon("glyph:FontAwesome:EXPAND")
@MuseStepTypeGroup("Selenium.Window")
@MuseStepShortDescription("Change browser window size")
@MuseStepLongDescription("Change the size of the browser window as defined by the width and height parameters")
@MuseSubsourceDescriptor(displayName = "width", description = "width of window", type = SubsourceDescriptor.Type.Named, name = SetBrowserSize.WIDTH_PARAM, defaultValue = "400")
@MuseSubsourceDescriptor(displayName = "height", description = "height of window", type = SubsourceDescriptor.Type.Named, name = SetBrowserSize.HEIGHT_PARAM, defaultValue = "400")
@SuppressWarnings("unused,WeakerAccess")
public class SetBrowserSize extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public SetBrowserSize(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _height = getValueSource(config, HEIGHT_PARAM, true, project);
        _width = getValueSource(config, WIDTH_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        Long height = getValue(_height, context, false, Long.class);
        Long width = getValue(_width, context, false, Long.class);
        getDriver(context).manage().window().setSize(new Dimension(width.intValue(), height.intValue()));
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _height;
    private MuseValueSource _width;

    public final static String HEIGHT_PARAM = "height";
    public final static String WIDTH_PARAM = "width";

    public final static String TYPE_ID = SetBrowserSize.class.getAnnotation(MuseTypeId.class).value();
    }


