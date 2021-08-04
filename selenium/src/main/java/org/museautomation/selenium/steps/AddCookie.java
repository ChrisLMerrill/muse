package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.descriptor.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("add-cookie")
@MuseStepName("Add Cookie")
@MuseInlineEditString("Add cookie {name}={value}")
@MuseStepIcon("glyph:FontAwesome:PLUS")
@MuseStepTypeGroup("Selenium.Other")
@MuseStepShortDescription("Add a cookie to the browser cookie store")
@MuseStepLongDescription("Creates a cookie and adds it to the browser cookie store.")
@MuseSubsourceDescriptor(displayName = "Name", description = "Name of the cookie", type = SubsourceDescriptor.Type.Named, name = AddCookie.NAME_PARAM)
@MuseSubsourceDescriptor(displayName = "Value", description = "Value of the cookie", type = SubsourceDescriptor.Type.Named, name = AddCookie.VALUE_PARAM)
@MuseSubsourceDescriptor(displayName = "Domain", description = "Domain the cookie will be sent to. If not set, it will apply only to the current domain. Cookies may only be added to the domain of the current page, or subdomains.", type = SubsourceDescriptor.Type.Named, name = AddCookie.DOMAIN_PARAM, optional = true)
@MuseSubsourceDescriptor(displayName = "Path", description = "Path of URLs the cookie will be sent to. It not specified, \"/\" will be used.", type = SubsourceDescriptor.Type.Named, name = AddCookie.PATH_PARAM, optional = true)
@MuseSubsourceDescriptor(displayName = "Secure", description = "Should the cookie be sent only to secure URLs? If not specified, this will be considered false.", type = SubsourceDescriptor.Type.Named, name = AddCookie.SECURE_PARAM, optional = true, defaultValue = "true")
@SuppressWarnings("unused")  // instantiated via reflection
public class AddCookie extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public AddCookie(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _name_source = getValueSource(config, NAME_PARAM, true, project);
        _value_source = getValueSource(config, VALUE_PARAM, true, project);
        _domain_source = getValueSource(config, DOMAIN_PARAM, false, project);
        _path_source = getValueSource(config, PATH_PARAM, false, project);
        _secure_source = getValueSource(config, SECURE_PARAM, false, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        String name = getValue(_name_source, context, false, String.class);
        String value = getValue(_value_source, context, false, String.class);
        String domain = getValue(_domain_source, context, true, String.class);
        String path = getValue(_path_source, context, true, String.class);
        Boolean secure = getValue(_secure_source, context, Boolean.class, Boolean.FALSE);

        try
            {
            Cookie cookie = new Cookie(name, value, domain, path, null, secure);
            getDriver(context).manage().addCookie(cookie);
            context.raiseEvent(MessageEventType.create(String.format("Added cookie %s = %s", name, value)));
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
            }
        catch (Exception e)
            {
            context.raiseEvent(MessageEventType.createError(String.format("Unable to add cookie: %s", e.getMessage())));
            return new BasicStepExecutionResult(StepExecutionStatus.ERROR);
            }
        }

    private final MuseValueSource _name_source;
    private final MuseValueSource _value_source;
    private final MuseValueSource _domain_source;
    private final MuseValueSource _path_source;
    private final MuseValueSource _secure_source;

    public final static String NAME_PARAM = "name";
    public final static String VALUE_PARAM = "value";
    public final static String DOMAIN_PARAM = "domain";
    public final static String PATH_PARAM = "path";
    public final static String SECURE_PARAM = "secure";

    public final static String TYPE_ID = AddCookie.class.getAnnotation(MuseTypeId.class).value();
    }


