package org.musetest.core.resource.types;

import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.javascript.factory.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ResourceTypes
    {
    public ResourceTypes(ClassLocator locator)
        {
        List<Class> implementors = locator.getImplementors(ResourceType.class);
        for (Class the_class : implementors)
            {
            try
                {
                Object obj = the_class.newInstance();
                if (obj instanceof ResourceType)
                    {
                    ResourceType type = (ResourceType) obj;
                    if (_types.get(type.getTypeId()) != null)
                        LOG.warn("Duplicate ResourceTypes found for id: " + type.getTypeId());
                    else
                        _types.put(type.getTypeId().toLowerCase(), type);
                    }
                }
            catch (Exception e)
                {
                // no need to deal with abstract implementations, etc
                }
            }
        }

    public ResourceType forImplementationInterface(Class implementation)
        {
        for (ResourceType type : _types.values())
            if (type.getInterfaceClass().equals(implementation))
                return type;
        return null;
        }

    @SuppressWarnings("unchecked")
    public ResourceType forObject(Object obj)
        {
        for (ResourceType type : _types.values())
            if (type.getInterfaceClass().isAssignableFrom(obj.getClass()))
                return type;
        return null;
        }

    public ResourceType forIdIgnoreCase(String value)
        {
        return _types.get(value.toLowerCase());
        }

    private Map<String, ResourceType> _types = new HashMap<>();

    static class TestResourceType extends ResourceType
        {
        public TestResourceType()
            {
            super("test", "Test", MuseTest.class);
            }

        @Override
        public MuseResource create()
            {
            return new SteppedTest(createStarterStep());
            }
        }
    static class MacroResourceType extends ResourceType
        {
        public MacroResourceType()
            {
            super("macro", "Macro", org.musetest.core.step.Macro.class);
            }

        @Override
        public MuseResource create()
            {
            Macro macro = new Macro();
            macro.setStep(createStarterStep());
            return macro;
            }

        }
    static class FunctionResourceType extends ResourceType
        {
        public FunctionResourceType()
            {
            super("function", "Function", org.musetest.core.step.Function.class);
            }

        @Override
        public MuseResource create()
            {
            org.musetest.core.step.Function function = new org.musetest.core.step.Function();
            function.setStep(createStarterStep());
            return function;
            }
        }
    static class SuiteResourceType extends ResourceType
        {
        public SuiteResourceType()
            {
            super("suite", "Test Suite", MuseTestSuite.class);
            }
        }
    static class jsStepResourceType extends ResourceType
        {
        public jsStepResourceType()
            {
            super("jsstep", "Javascript Step", JavascriptStepResource.class);
            }
        }

    private static StepConfiguration createStarterStep()
        {
        StepConfiguration step = new StepConfiguration();
        step.setType(ScopedGroup.TYPE_ID);
        StepConfiguration step1 = new StepConfiguration();
        step1.setType(LogMessage.TYPE_ID);
        step1.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue("replace this with some useful steps"));
        step.addChild(step1);
        return step;
        }

    public final static ResourceType Test = new TestResourceType();
    public final static ResourceType Macro = new MacroResourceType();
    public final static ResourceType Suite = new SuiteResourceType();
    public final static ResourceType jsStep = new jsStepResourceType();
    public final static ResourceType Function = new FunctionResourceType();

    final static Logger LOG = LoggerFactory.getLogger(ResourceTypes.class);
    }


