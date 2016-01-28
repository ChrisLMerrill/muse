package org.musetest.javascript.factory;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.javascript.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JavascriptStepResource implements MuseResource
    {
    public JavascriptStepResource(ResourceOrigin origin, String script)
        {
        _origin = origin;
        _metadata.setId(origin.suggestId());
        _script = script;
        }

    @Override
    public ResourceMetadata getMetadata()
        {
        return _metadata;
        }

    public String getScript()
        {
        return _script;
        }

    public MuseStep createStep(StepConfiguration configuration)
        {
        return new JavascriptStep(configuration, _origin, _script);
        }

    public StepDescriptor getStepDescriptor()
        {
        return new DefaultStepDescriptor(JavascriptStep.class, null)
            {
            @Override
            public String getName()
                {
                return "Javascript step";
                }

            @Override
            public String getShortDescription(StepConfiguration step)
                {
                return "Javascript step, type=" + step.getType();
                }

            @Override
            public String getShortDescription()
                {
                return "A custom step developed in Javascript";
                }

            @Override
            public String getLongDescription()
                {
                return "see the source code at: " + getMetadata().getOrigin().getDescription();
                }

            @Override
            public String getIconDescriptor()
                {
                return null;
                }

            @Override
            public boolean isCompound()
                {
                return false;
                }

            @Override
            public String getInlineEditString()
                {
                return null;
                }

            @Override
            public String getType()
                {
                return getMetadata().getId();
                }

            @Override
            public String getGroupName()
                {
                return "javascript";
                }
            };
        }

    private String _script;
    private ResourceOrigin _origin;
    private ResourceMetadata _metadata = new ResourceMetadata(ResourceTypes.jsStep);
    }


