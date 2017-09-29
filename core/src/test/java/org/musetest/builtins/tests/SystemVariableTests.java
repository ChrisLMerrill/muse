package org.musetest.builtins.tests;

import org.junit.*;
import org.musetest.builtins.tests.mocks.*;
import org.musetest.builtins.value.sysvar.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.context.initializers.*;
import org.musetest.core.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SystemVariableTests
    {
    @Test
    public void environmentVariable() throws ValueSourceResolutionError
        {
        MuseProject project = new SimpleProject();
        MockEnvironmentProperties environment = new MockEnvironmentProperties();
        final String var_name = "env-var1";
        final String value = UUID.randomUUID().toString();
        environment.setVariable(var_name, value);
        EnvironmentSysvarProvider.overrideImplementation(project, environment);

        Object env = project.getSystemVariableProviders().resolve(EnvironmentSysvarProvider.VARNAME1, new BaseExecutionContext(project));
        Assert.assertTrue(env instanceof EnvironmentPropertiesProvider);
        Assert.assertEquals(value, ((EnvironmentPropertiesProvider)env).getVars().get(var_name));
        }

    @Test
    public void environmentJavaProperties() throws ValueSourceResolutionError
        {
        MuseProject project = new SimpleProject();
        MockEnvironmentProperties environment = new MockEnvironmentProperties();
        final String var_name = "prop1";
        final String value = UUID.randomUUID().toString();
        environment.setProperty(var_name, value);
        EnvironmentSysvarProvider.overrideImplementation(project, environment);

        Object env = project.getSystemVariableProviders().resolve(EnvironmentSysvarProvider.VARNAME1, new BaseExecutionContext(project));
        Assert.assertTrue(env instanceof EnvironmentPropertiesProvider);
        Assert.assertEquals(value, ((EnvironmentPropertiesProvider)env).getProps().get(var_name));
        }

    @Test
    public void environmentUsername() throws ValueSourceResolutionError
        {
        MuseProject project = new SimpleProject();
        MockEnvironmentProperties environment = new MockEnvironmentProperties();
        final String value = UUID.randomUUID().toString();
        environment.setUsername(value);
        EnvironmentSysvarProvider.overrideImplementation(project, environment);

        Object env = project.getSystemVariableProviders().resolve(EnvironmentSysvarProvider.VARNAME1, new BaseExecutionContext(project));
        Assert.assertTrue(env instanceof EnvironmentPropertiesProvider);
        Assert.assertEquals(value, ((EnvironmentPropertiesProvider)env).getUsername());
        }

    @Test
    public void testTags() throws ValueSourceResolutionError
        {
        MuseTest test = new MockTest();
        final String tag = UUID.randomUUID().toString();
        test.addTag(tag);

        MuseProject project = new SimpleProject();
        Object test_var = project.getSystemVariableProviders().resolve(TestVariableProvider.SYSVAR_NAME, new DefaultTestExecutionContext(project, test));
        Assert.assertTrue(test_var instanceof TestVariableProvider.TestVariableProxy);
        Assert.assertTrue(((TestVariableProvider.TestVariableProxy)test_var).getTags().contains(tag));
        }

    @Test
    public void varlistInitializer() throws ValueSourceResolutionError
        {
        final String id = UUID.randomUUID().toString();
        MuseExecutionContext context = new BaseExecutionContext(new SimpleProject());
        context.setVariable(ProjectVarsInitializerSysvarProvider.VARIABLE_LIST_ID_VARNAME, id);
        ProjectVarsInitializerSysvarProvider provider = new ProjectVarsInitializerSysvarProvider();

        Assert.assertEquals(id, provider.resolve(ProjectVarsInitializerSysvarProvider.SYSVAR_NAME, context));
        }

    @Test
    public void commandLineOptions() throws ValueSourceResolutionError
        {
        final String name = "command1";
        final String value = "option1";
        Map<String, String> options = new HashMap<>();
        options.put(name, value);
        MuseProject project = new SimpleProject();
        project.setCommandLineOptions(options);

        CommandLineOptionSysvarProvider provider = new CommandLineOptionSysvarProvider();
        Object resolved = provider.resolve("clo", new DefaultTestExecutionContext(project, new MockTest()));
        Assert.assertTrue(resolved instanceof Map);
        Assert.assertEquals(((Map) resolved).get(name), value);
        }
    }


