package org.museautomation.builtins.tests;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.tests.mocks.*;
import org.museautomation.builtins.value.sysvar.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.project.*;
import org.museautomation.core.test.plugins.*;
import org.museautomation.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class SystemVariableTests
    {
    @Test
    void environmentVariable() throws ValueSourceResolutionError
        {
        MuseProject project = new SimpleProject();
        MockEnvironmentProperties environment = new MockEnvironmentProperties();
        final String var_name = "env-var1";
        final String value = UUID.randomUUID().toString();
        environment.setVariable(var_name, value);
        EnvironmentSysvarProvider.overrideImplementation(project, environment);

        Object env = project.getSystemVariableProviders().resolve(EnvironmentSysvarProvider.VARNAME1, new ProjectExecutionContext(project));
        Assertions.assertTrue(env instanceof EnvironmentPropertiesProvider);
        Assertions.assertEquals(value, ((EnvironmentPropertiesProvider)env).getVars().get(var_name));
        }

    @Test
    void environmentJavaProperties() throws ValueSourceResolutionError
        {
        MuseProject project = new SimpleProject();
        MockEnvironmentProperties environment = new MockEnvironmentProperties();
        final String var_name = "prop1";
        final String value = UUID.randomUUID().toString();
        environment.setProperty(var_name, value);
        EnvironmentSysvarProvider.overrideImplementation(project, environment);

        Object env = project.getSystemVariableProviders().resolve(EnvironmentSysvarProvider.VARNAME1, new ProjectExecutionContext(project));
        Assertions.assertTrue(env instanceof EnvironmentPropertiesProvider);
        Assertions.assertEquals(value, ((EnvironmentPropertiesProvider)env).getProps().get(var_name));
        }

    @Test
    void environmentUsername() throws ValueSourceResolutionError
        {
        MuseProject project = new SimpleProject();
        MockEnvironmentProperties environment = new MockEnvironmentProperties();
        final String value = UUID.randomUUID().toString();
        environment.setUsername(value);
        EnvironmentSysvarProvider.overrideImplementation(project, environment);

        Object env = project.getSystemVariableProviders().resolve(EnvironmentSysvarProvider.VARNAME1, new ProjectExecutionContext(project));
        Assertions.assertTrue(env instanceof EnvironmentPropertiesProvider);
        Assertions.assertEquals(value, ((EnvironmentPropertiesProvider)env).getUsername());
        }

    @Test
    void testTags() throws ValueSourceResolutionError
        {
        MuseTest test = new MockTest();
        final String tag = UUID.randomUUID().toString();
        test.addTag(tag);

        MuseProject project = new SimpleProject();
        Object test_var = project.getSystemVariableProviders().resolve(TestVariableProvider.SYSVAR_NAME, new DefaultTestExecutionContext(project, test));
        Assertions.assertTrue(test_var instanceof TestVariableProvider.TestVariableProxy);
        Assertions.assertTrue(((TestVariableProvider.TestVariableProxy)test_var).getTags().contains(tag));
        }

    @Test
    void varlistInitializer()
        {
        final String id = UUID.randomUUID().toString();
        MuseExecutionContext context = new ProjectExecutionContext(new SimpleProject());
        context.setVariable(ProjectVarsInitializerSysvarProvider.VARIABLE_LIST_ID_VARNAME, id);
        ProjectVarsInitializerSysvarProvider provider = new ProjectVarsInitializerSysvarProvider();

        Assertions.assertEquals(id, provider.resolve(ProjectVarsInitializerSysvarProvider.SYSVAR_NAME, context));
        }

    @Test
    void commandLineOptions()
        {
        final String name = "command1";
        final String value = "option1";
        Map<String, String> options = new HashMap<>();
        options.put(name, value);
        MuseProject project = new SimpleProject();
        project.setCommandLineOptions(options);

        CommandLineOptionSysvarProvider provider = new CommandLineOptionSysvarProvider();
        Object resolved = provider.resolve("clo", new DefaultTestExecutionContext(project, new MockTest()));
        Assertions.assertTrue(resolved instanceof Map);
        Assertions.assertEquals(((Map) resolved).get(name), value);
        }

    @Test
    void executionId() throws ValueSourceResolutionError
	    {
        final MuseProject project = new SimpleProject();
	    final BaseExecutionContext context = new ProjectExecutionContext(project);
	    String id = (String) project.getSystemVariableProviders().resolve(ExecutionIdProvider.VARNAME, context);
        Assertions.assertNotNull(id);
        }
    }
