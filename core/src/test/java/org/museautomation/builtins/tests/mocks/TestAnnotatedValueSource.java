package org.museautomation.builtins.tests.mocks;

import org.museautomation.core.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.descriptor.*;

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
    public Object resolveValue(MuseExecutionContext context)
        {
        return null;
        }

    @Override
    public String getDescription()
        {
        return null;
        }
    }


