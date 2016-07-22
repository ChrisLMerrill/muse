package org.musetest.builtins.tests.mocks;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("test-annotated-value-source")
@MuseValueSourceName("test-name")
@MuseValueSourceLongDescription("long-description")
@MuseValueSourceShortDescription("short-description")
@MuseValueSourceTypeGroup("test-group")
@MuseInlineEditString("edit-string")
@MuseSubsourceDescriptor(displayName = "display-name1", description = "sub-description1", type = SubsourceDescriptor.Type.Named, name = "name1")
@MuseSubsourceDescriptor(displayName = "display-name2", description = "sub-description2", type = SubsourceDescriptor.Type.Value, optional = true, resolutionType = String.class)
public class TestAnnotatedValueSource implements MuseValueSource
    {
    @Override
    public Object resolveValue(StepExecutionContext context) throws ValueSourceResolutionError
        {
        return null;
        }

    @Override
    public String getDescription()
        {
        return null;
        }
    }


