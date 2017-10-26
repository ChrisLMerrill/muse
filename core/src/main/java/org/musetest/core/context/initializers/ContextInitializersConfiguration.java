package org.musetest.core.context.initializers;

import com.fasterxml.jackson.annotation.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * Represents a set of ContextInitializerConfiguration. This should be asked if it should
 * be applied to a give test by calling isAppliedToTest(context). Then each of the initalizers
 * should be applied.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("context-initializers")
public class ContextInitializersConfiguration extends BaseMuseResource
	{
	@JsonIgnore
	@Override
	public ResourceType getType()
		{
		return new ContextInitializersConfigurationResourceType();
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
		context.raiseEvent(new MessageEvent(String.format("WARNING: ContextInitializersConfiguration expects isAppliedToTest condition to resolve to a boolean. Instead it is a %s with value '%s'. Treating this as %s.", type, value, guess)));
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

	public List<ContextInitializerConfiguration> getInitializers()
		{
		if (_initializer_configs == null)
			return Collections.emptyList();
		return _initializer_configs;
		}

	public synchronized void setInitializers(List<ContextInitializerConfiguration> initializer_configs)
		{
		_initializer_configs = initializer_configs;
		}

	public synchronized void addConfiguration(ContextInitializerConfiguration config, int index)
		{
		if (_initializer_configs == null)
			_initializer_configs = new ArrayList<>();
		if (index < 0 || index > _initializer_configs.size())
			throw new IllegalArgumentException(String.format("Can't add at index %d in a list of length %d", index, _initializer_configs.size()));
		_initializer_configs.add(index, config);

		if (_listeners != null)
			{
			ConfigAddedEvent event = new ConfigAddedEvent(this, config, index);
			for (ChangeEventListener listener : _listeners)
				listener.changeEventRaised(event);
			}
		}

	public synchronized void addConfiguration(ContextInitializerConfiguration config)
		{
		if (_initializer_configs == null)
			_initializer_configs = new ArrayList<>();
		addConfiguration(config, _initializer_configs.size());
		}

	public synchronized void removeConfiguration(ContextInitializerConfiguration config)
		{
		if (_initializer_configs == null)
			return;
		int index = _initializer_configs.indexOf(config);
		if (index < 0)
			return;  // not in list

		_initializer_configs.remove(config);
		if (_listeners != null)
			{
			ConfigRemovedEvent event = new ConfigRemovedEvent(this, config, index);
			for (ChangeEventListener listener : _listeners)
				listener.changeEventRaised(event);
			}
		}

	private ValueSourceConfiguration _apply_to_test_condition = ValueSourceConfiguration.forValue(true);
	private List<ContextInitializerConfiguration> _initializer_configs;

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

	public static class ContextInitializersConfigurationResourceType extends ResourceType
		{
		public ContextInitializersConfigurationResourceType()
			{
			super("context-initializer-configurations", "Context Initializer", ContextInitializersConfiguration.class);
			}
		}

	public static class ConfigAddedEvent extends ChangeEvent
		{
		ConfigAddedEvent(ContextInitializersConfiguration target, ContextInitializerConfiguration added, int index)
			{
			super(target);
			_added = added;
			_index = index;
			}

		public ContextInitializerConfiguration getAddedConfig()
			{
			return _added;
			}

		public int getIndex()
			{
			return _index;
			}

		private ContextInitializerConfiguration _added;
		private int _index;
		}

	public static class ConfigRemovedEvent extends ChangeEvent
		{
		ConfigRemovedEvent(ContextInitializersConfiguration target, ContextInitializerConfiguration removed, int index)
			{
			super(target);
			_removed = removed;
			_index = index;
			}

		public int getIndex()
			{
			return _index;
			}

		public ContextInitializerConfiguration getRemovedConfig()
			{
			return _removed;
			}

		private ContextInitializerConfiguration _removed;
		private int _index;
		}
	}