package org.museautomation.core.values.descriptor;

import org.museautomation.core.values.*;

/**
 * Provides information about a MuseValueSource that may be useful in the UI.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ValueSourceDescriptor
    {
    String getType();
    String getName();
    String getShortDescription();
    String getInstanceDescription(ValueSourceConfiguration source, StringExpressionContext context);
    String getGroupName();
    String getLongDescription();
    String getDocumentationDescription();
    boolean hideFromUI();
    SubsourceDescriptor[] getSubsourceDescriptors();
    }


