package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("goto-url")
@MuseStepName("Go to URL")
@MuseInlineEditString("Go to {URL}")
@MuseStepIcon("glyph:FontAwesome:PLANE")
@MuseStepTypeGroup("Selenium")
@MuseStepShortDescription("Goto a URL in the browser")
@MuseStepLongDescription("Resolve the URL source to a string. Then go to that URL in the browser by calling driver.navigate.to(URL).")
@MuseSubsourceDescriptor(displayName = "URL", description = "URL to navigate to", type = SubsourceDescriptor.Type.Named, name = GotoUrl.URL_PARAM)
public class GotoUrl extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public GotoUrl(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(config);
        _url = getValueSource(config, URL_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws StepExecutionError
        {
        String url = getValue(_url, context, false, String.class);
        getDriver(context).navigate().to(url);

        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _url;

    public final static String URL_PARAM = "URL";

    public final static String TYPE_ID = GotoUrl.class.getAnnotation(MuseTypeId.class).value();
    }


