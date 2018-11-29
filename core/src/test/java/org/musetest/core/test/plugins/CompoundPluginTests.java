package org.musetest.core.test.plugins;

import org.junit.jupiter.api.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.mocks.*;
import org.musetest.core.plugins.*;
import org.musetest.core.project.*;
import org.musetest.core.values.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class CompoundPluginTests
	{
	@Test
    void apply1Plugin() throws MuseExecutionError
		{
	    CompoundPluginConfiguration config = new CompoundPluginConfiguration();
	    config.parameters().addSource(CompoundPluginConfiguration.LISTS_PARAM, ValueSourceConfiguration.forValue("pl1"));
	    config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
	    config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));

		DefaultTestExecutionContext context = new DefaultTestExecutionContext(_project, new MockTest());
	    config.createPlugin().conditionallyAddToContext(context, true);

	    Assertions.assertEquals(1, context.getPlugins().size());
	    Assertions.assertTrue(context.getPlugins().get(0) instanceof MockTestPlugin);
	    }

	@Test
    void ignoreAutoApply() throws MuseExecutionError
		{
	    CompoundPluginConfiguration config = new CompoundPluginConfiguration();
	    config.parameters().addSource(CompoundPluginConfiguration.LISTS_PARAM, ValueSourceConfiguration.forValue("pl2"));
	    config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
	    config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));

		DefaultTestExecutionContext context = new DefaultTestExecutionContext(_project, new MockTest());
	    config.createPlugin().conditionallyAddToContext(context, true);

	    Assertions.assertEquals(1, context.getPlugins().size());
		Assertions.assertTrue(context.getPlugins().get(0) instanceof MockTestPlugin);
	    }

	@Test
    void apply2Plugins() throws MuseExecutionError
		{
	    CompoundPluginConfiguration config = new CompoundPluginConfiguration();
	    config.parameters().addSource(CompoundPluginConfiguration.LISTS_PARAM, ValueSourceConfiguration.forValue("pl1,pl2"));
	    config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
	    config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));

		DefaultTestExecutionContext context = new DefaultTestExecutionContext(_project, new MockTest());
	    config.createPlugin().conditionallyAddToContext(context, true);

	    Assertions.assertEquals(2, context.getPlugins().size());
		Assertions.assertTrue(context.getPlugins().get(0) instanceof MockTestPlugin);
		Assertions.assertTrue(context.getPlugins().get(1) instanceof MockTestPlugin);
	    }

	@BeforeEach
    void setup() throws IOException
		{
		MockPluginConfiguration plugin1 = new MockPluginConfiguration(true, true);
		plugin1.setId("pl1");
		MockPluginConfiguration plugin2 = new MockPluginConfiguration(true, true);
		plugin2.setId("pl2");

		_project = new SimpleProject();
		_project.getResourceStorage().addResource(plugin1);
		_project.getResourceStorage().addResource(plugin2);
		}

	private SimpleProject _project;
	}
