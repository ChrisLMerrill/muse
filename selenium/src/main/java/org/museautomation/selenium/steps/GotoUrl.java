package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.values.descriptor.*;

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
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        String url = getValue(_url, context, false, String.class);
        getDriver(context).navigate().to(url);

        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _url;

    public final static String URL_PARAM = "URL";

    public final static String TYPE_ID = GotoUrl.class.getAnnotation(MuseTypeId.class).value();
    }


