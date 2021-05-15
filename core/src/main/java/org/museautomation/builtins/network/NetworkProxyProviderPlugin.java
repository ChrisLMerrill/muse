package org.museautomation.builtins.network;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.suite.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NetworkProxyProviderPlugin extends GenericConfigurableTaskPlugin implements NetworkProxyProvider
    {
    NetworkProxyProviderPlugin(NetworkProxyProviderPluginConfiguration configuration)
        {
        super(configuration);
        }

    @Override
    public void initialize(MuseExecutionContext context) throws MuseInstantiationException, ValueSourceResolutionError
        {
        if (_initialized)
            return;

        _initialized = true;
        MuseValueSource config_source = BaseValueSource.getValueSource(_configuration.parameters(), NetworkProxyProviderPluginConfiguration.PROXY_CONFIG_PARAM_NAME, false, context.getProject());
        Object config_value = BaseValueSource.getValue(config_source, context, true);
        if (config_value == null)
            {
            context.raiseEvent(MessageEventType.create("Proxy Configuration parameter is null. No proxy will be used."));
            return;
            }
        if (config_value instanceof NetworkProxyConfiguration)
            _proxy = (NetworkProxyConfiguration) config_value;
        else if (config_value instanceof String)
            {
            MuseResource resource = context.getProject().getResourceStorage().getResource((String) config_value);
            if (resource == null)
                context.raiseEvent(MessageEventType.createError("Unable to find a Proxy Configuration resource with id = " + config_value));
            else if (resource instanceof NetworkProxyConfiguration)
                _proxy = (NetworkProxyConfiguration) resource;
            else
                context.raiseEvent(MessageEventType.createError(String.format("Configured project resource (%s) is a %s. Expected a Proxy Configuration", config_value, resource.getType().getName())));
            }
        else
            context.raiseEvent(MessageEventType.createError(String.format("Proxy Configuration parameter is a %s. Expected to be configured with a Proxy Configuration project resource or the id of one.", config_value.getClass().getSimpleName())));
        }

    @Override
    protected boolean applyToContextType(MuseExecutionContext context)
        {
        if (Plugins.findType(this.getClass(), context) != null)
            return false;

        return context instanceof TaskSuiteExecutionContext || context instanceof TaskExecutionContext;
        }

    @Override
    public NetworkProxyConfiguration getProxy()
        {
        return _proxy;
        }

    private NetworkProxyConfiguration _proxy = null;
    private boolean _initialized = false;
    }