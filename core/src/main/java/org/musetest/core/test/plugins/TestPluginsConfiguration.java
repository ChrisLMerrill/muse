package org.musetest.core.test.plugins;

import com.fasterxml.jackson.annotation.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * Represents a set of TestPluginConfigurations. This should be asked if it should
 * be applied to a given test by calling isAppliedToTest(context). Then each of the plugins
 * should be applied.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("test-plugins")
public class TestPluginsConfiguration extends BaseMuseResource
	{
	@JsonIgnore
	@Override
	public ResourceType getType()
		{
		return new TestPluginsConfigurationResourceType();
		}

	boolean shouldApplyToTest(MuseExecutionContext context) throws MuseInstantiationException, ValueSourceResolutionError
		{
		if (getApplyToTestCondition() == null)
			return false;
		final Object value = getApplyToTestCondition().createSource(context.getProject()).resolveValue(context);
		if (value instanceof Boolean)
			return (Boolean) value;

		boolean guess;
		if (value instanceof Number)
			guess = ((Number) value).doubleValue() != 0;
		else
			guess = value != null;
		String type = "<unknown>";
		if (value != null)
			type = value.getClass().getSimpleName();
		context.raiseEvent(MessageEventType.create(String.format("WARNING: TestPluginsConfiguration expects isAppliedToTest condition to resolve to a boolean. Instead it is a %s with value '%s'. Treating this as %s.", type, value, guess)));
		return guess;
		}

	public ValueSourceConfiguration getApplyToTestCondition()
		{
		if (_apply_to_test_condition == null)
			_apply_to_test_condition = ValueSourceConfiguration.forValue(true);
		return _apply_to_test_condition;
		}

	public void setApplyToTestCondition(ValueSourceConfiguration apply_to_test_condition)
		{
		_apply_to_test_condition = apply_to_test_condition;
		}

	public List<TestPluginConfiguration> getPlugins()
		{
		if (_configs == null)
			return Collections.emptyList();
		return _configs;
		}

	public synchronized void setPlugins(List<TestPluginConfiguration> plugin_configs)
		{
		_configs = plugin_configs;
		}

	public synchronized void addPlugin(TestPluginConfiguration config, int index)
		{
		if (_configs == null)
			_configs = new ArrayList<>();
		if (index < 0 || index > _configs.size())
			throw new IllegalArgumentException(String.format("Can't add at index %d in a list of length %d", index, _configs.size()));
		_configs.add(index, config);

		if (_listeners != null)
			{
			ConfigAddedEvent event = new ConfigAddedEvent(this, config, index);
			for (ChangeEventListener listener : _listeners)
				listener.changeEventRaised(event);
			}
		}

	public synchronized void addPlugin(TestPluginConfiguration config)
		{
		if (_configs == null)
			_configs = new ArrayList<>();
		addPlugin(config, _configs.size());
		}

	public synchronized void removeConfiguration(TestPluginConfiguration config)
		{
		if (_configs == null)
			return;
		int index = _configs.indexOf(config);
		if (index < 0)
			return;  // not in list

		_configs.remove(config);
		if (_listeners != null)
			{
			ConfigRemovedEvent event = new ConfigRemovedEvent(this, config, index);
			for (ChangeEventListener listener : _listeners)
				listener.changeEventRaised(event);
			}
		}

	private ValueSourceConfiguration _apply_to_test_condition = ValueSourceConfiguration.forValue(true);
	private List<TestPluginConfiguration> _configs;

	public void addChangeListener(ChangeEventListener listener)
		{
		if (_listeners == null)
			_listeners = new HashSet<>();
		_listeners.add(listener);
		}

	public void removeChangeListener(ChangeEventListener listener)
		{
		if (_listeners == null)
			return;
		_listeners.remove(listener);
		}

	private transient Set<ChangeEventListener> _listeners;

	public static class TestPluginsConfigurationResourceType extends ResourceType
		{
		public TestPluginsConfigurationResourceType()
			{
			super("test-plugins", "Test Plugin", TestPluginsConfiguration.class);
			}
		}

	public static class ConfigAddedEvent extends ChangeEvent
		{
		ConfigAddedEvent(TestPluginsConfiguration target, TestPluginConfiguration added, int index)
			{
			super(target);
			_added = added;
			_index = index;
			}

		public TestPluginConfiguration getAddedConfig()
			{
			return _added;
			}

		public int getIndex()
			{
			return _index;
			}

		private TestPluginConfiguration _added;
		private int _index;
		}

	public static class ConfigRemovedEvent extends ChangeEvent
		{
		ConfigRemovedEvent(TestPluginsConfiguration target, TestPluginConfiguration removed, int index)
			{
			super(target);
			_removed = removed;
			_index = index;
			}

		public int getIndex()
			{
			return _index;
			}

		public TestPluginConfiguration getRemovedConfig()
			{
			return _removed;
			}

		private TestPluginConfiguration _removed;
		private int _index;
		}
	}