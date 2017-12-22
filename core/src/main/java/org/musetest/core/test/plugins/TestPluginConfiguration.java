package org.musetest.core.test.plugins;

import com.fasterxml.jackson.annotation.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;
import org.slf4j.*;

import java.util.*;

/**
 * Represents configuration data for a TestPlugin.  It has a type, which is used to
 * lookup the implementation of the plugin. It also has an ApplyCondition, which should
 * be evaluated before applying to the context. It contains named ValueSourceConfigurations which
 * should be supplied to the TestPlugin.configure() method after construction (or anytime prior
 * to use).
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess") // publicly extensible UI
public class TestPluginConfiguration implements ContainsNamedSources
    {
    // required for de/serialization
   	public String getTypeId()
   		{
   		return _plugin_type;
   		}

   	// required for de/serialization
   	public void setTypeId(String plugin_type)
   		{
   		String old_type = _plugin_type;
	    _plugin_type = plugin_type;
	    for (ChangeEventListener listener : _listeners)
	    	listener.changeEventRaised(new TypeChangeEvent(this, old_type, plugin_type));
   		}

   	// required for de/serialization
   	public Map<String, ValueSourceConfiguration> getParameters()
   		{
   		return _named_sources.getSourceMap();
   		}

   	// required for de/serialization
   	public void setParameters(Map<String, ValueSourceConfiguration> parameters)
   		{
   		_named_sources.setSourceMap(parameters);
   		}

    public void addParameter(String name, ValueSourceConfiguration source)
	    {
	    _named_sources.addSource(name, source);
	    }

   	public ValueSourceConfiguration getApplyCondition()
   		{
   		return _apply_condition;
   		}

   	public void setApplyCondition(ValueSourceConfiguration apply_condition)
   		{
	    ValueSourceConfiguration old_condition = _apply_condition;
	    if (old_condition != null)
	    	old_condition.removeChangeListener(getRelayListener());
   		_apply_condition = apply_condition;
   		_apply_condition.addChangeListener(getRelayListener());
	    for (ChangeEventListener listener : _listeners)
	    	listener.changeEventRaised(new ApplyConditionChangeEvent(this, old_condition, apply_condition));
   		}

   	@Override
   	public ValueSourceConfiguration getSource(String name)
   		{
   		return _named_sources.getSource(name);
   		}

   	@Override
   	public void addSource(String name, ValueSourceConfiguration source)
   		{
   		_named_sources.addSource(name, source);
   		}

   	@Override
   	public ValueSourceConfiguration removeSource(String name)
   		{
   		return _named_sources.removeSource(name);
   		}

   	@Override
   	public boolean renameSource(String old_name, String new_name)
   		{
   		return _named_sources.renameSource(old_name, new_name);
   		}

   	@Override
   	public ValueSourceConfiguration replaceSource(String name, ValueSourceConfiguration new_source)
   		{
   		return _named_sources.replaceSource(name, new_source);
   		}

   	@JsonIgnore
   	@Override
   	public Set<String> getSourceNames()
   		{
   		return _named_sources.getSourceNames();
   		}

   	@Override
   	public void addChangeListener(ChangeEventListener listener)
   		{
   		_listeners.add(listener);
   		_named_sources.addChangeListener(listener);
   		}

   	@Override
   	public boolean removeChangeListener(ChangeEventListener listener)
   		{
	    _listeners.remove(listener);
   		return _named_sources.removeChangeListener(listener);
   		}

    public TestPlugin createPlugin(MuseProject project)
	    {
	    final List<Class> plugins = project.getClassLocator().getImplementors(TestPlugin.class);
	    for (Class plugin_class : plugins)
		    {
		    try
			    {
			    TestPlugin plugin = (TestPlugin) plugin_class.newInstance();
			    if (getTypeId().equals(plugin.getType()))
				    {
				    plugin.configure(this);
				    return plugin;
				    }
			    }
		    catch (Exception e)
			    {
			    LOG.warn(String.format("WARNING: %s implements TestPlugin but does not have a default constructor. Thus it cannot be instantiated automatically (which may or may not be a problem).", plugin_class.getSimpleName()));
			    }
		    }
	    // TODO lookup the implementation by the type
	    // TODO apply the configuration
	    return new EventLogger();
	    }

    public boolean shouldApply(MuseExecutionContext context) throws ValueSourceResolutionError, MuseInstantiationException
	    {
	    return BaseValueSource.getValue(_apply_condition.createSource(context.getProject()), context, false, Boolean.class);
	    }

    private ChangeEventListener getRelayListener()
	    {
	    if (_listener == null)
		    _listener = event ->
			    {
			    for (ChangeEventListener listener : _listeners)
				    listener.changeEventRaised(event);
			    };
	    return _listener;
	    }

    private NamedSourcesContainer _named_sources = new NamedSourcesContainer();
   	private String _plugin_type;
   	private ValueSourceConfiguration _apply_condition;
   	private Set<ChangeEventListener> _listeners = new HashSet<>();

   	private transient ChangeEventListener _listener;

   	private final static Logger LOG = LoggerFactory.getLogger(TestPluginConfiguration.class);
    }


