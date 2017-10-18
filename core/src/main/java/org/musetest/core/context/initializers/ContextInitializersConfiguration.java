package org.musetest.core.context.initializers;

import com.fasterxml.jackson.annotation.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
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
		return _apply_to_test_condition;
		}

	public void setApplyToTestCondition(ValueSourceConfiguration apply_to_test_condition)
		{
		_apply_to_test_condition = apply_to_test_condition;
		}

	public List<ContextInitializerConfiguration> getInitializers()
		{
		return _initializer_configs;
		}

	public void setInitializers(List<ContextInitializerConfiguration> initializer_configs)
		{
		_initializer_configs = initializer_configs;
		}

	public void addConfiguration(ContextInitializerConfiguration config)
		{
		if (_initializer_configs == null)
			_initializer_configs = new ArrayList<>();
		_initializer_configs.add(config);
		// TODO throw a change event for the UI
		}

	ValueSourceConfiguration _apply_to_test_condition;
	List<ContextInitializerConfiguration> _initializer_configs;

	public static class ContextInitializersConfigurationResourceType extends ResourceType
		{
		public ContextInitializersConfigurationResourceType()
			{
			super("context-initializer-configurations", "Context Initializer", ContextInitializersConfiguration.class);
			}
		}
	}