package org.musetest.core.test.plugins;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.test.plugin.*;
import org.musetest.core.values.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class CompoundPluginTests
	{
	@Test
	public void apply1Plugin() throws MuseExecutionError
		{
	    CompoundPluginConfiguration config = new CompoundPluginConfiguration();
	    config.parameters().addSource(CompoundPluginConfiguration.LISTS_PARAM, ValueSourceConfiguration.forValue("pl1"));
	    config.parameters().addSource(BaseTestPlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
	    config.parameters().addSource(BaseTestPlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));

	    BaseExecutionContext context = new DefaultTestExecutionContext(_project, new MockTest());
	    config.createPlugin().addToContext(context, true);

	    Assert.assertEquals(1, context.getPlugins().size());
	    Assert.assertTrue(context.getPlugins().get(0) instanceof MockTestPlugin);
	    }

	@Test
	public void ignoreAutoApply() throws MuseExecutionError
		{
	    CompoundPluginConfiguration config = new CompoundPluginConfiguration();
	    config.parameters().addSource(CompoundPluginConfiguration.LISTS_PARAM, ValueSourceConfiguration.forValue("pl2"));
	    config.parameters().addSource(BaseTestPlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
	    config.parameters().addSource(BaseTestPlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));

	    BaseExecutionContext context = new DefaultTestExecutionContext(_project, new MockTest());
	    config.createPlugin().addToContext(context, true);

	    Assert.assertEquals(1, context.getPlugins().size());
		Assert.assertTrue(context.getPlugins().get(0) instanceof MockTestPlugin);
	    }

	@Test
	public void apply2Plugins() throws MuseExecutionError
		{
	    CompoundPluginConfiguration config = new CompoundPluginConfiguration();
	    config.parameters().addSource(CompoundPluginConfiguration.LISTS_PARAM, ValueSourceConfiguration.forValue("pl1,pl2"));
	    config.parameters().addSource(BaseTestPlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
	    config.parameters().addSource(BaseTestPlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));

	    BaseExecutionContext context = new DefaultTestExecutionContext(_project, new MockTest());
	    config.createPlugin().addToContext(context, true);

	    Assert.assertEquals(2, context.getPlugins().size());
		Assert.assertTrue(context.getPlugins().get(0) instanceof MockTestPlugin);
		Assert.assertTrue(context.getPlugins().get(1) instanceof MockTestPlugin);
	    }

	@Before
	public void setup() throws IOException
		{
		_plugin1 = new MockPluginConfiguration(true, true);
		_plugin1.setId("pl1");
		_plugin2 = new MockPluginConfiguration(true, true);
		_plugin2.setId("pl2");

		_project = new SimpleProject();
		_project.getResourceStorage().addResource(_plugin1);
		_project.getResourceStorage().addResource(_plugin2);
		}

	private SimpleProject _project;
	private MockPluginConfiguration _plugin1;
	private MockPluginConfiguration _plugin2;
	}
