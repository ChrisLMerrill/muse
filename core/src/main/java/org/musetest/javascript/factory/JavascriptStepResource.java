package org.musetest.javascript.factory;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.step.*;
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

    private String _script;
    private ResourceOrigin _origin;
    private ResourceMetadata _metadata = new ResourceMetadata(ResourceTypes.jsStep);
    }


