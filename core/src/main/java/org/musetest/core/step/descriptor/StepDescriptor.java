package org.musetest.core.step.descriptor;

import org.musetest.core.step.*;
import org.slf4j.*;

/**
 * Provides metadata about steps. The intended uses are for the UI to provide context-relevant text and graphics
 * to the end user to help them understand what this step type or step instance will do.
 *
 * A StepDescriptor could apply to either a step type (e.g. a class implementing MuseStep) or
 * to a specific instance of that step, or to a StepConfiguration (which describes how a MuseStep
 * will be created and configured at runtime). To find StepDescriptor for a step, see the methods
 * on StepDescriptors.
 *
 * @see StepDescriptors
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StepDescriptor
    {
    String getName();
    String getShortDescription(StepConfiguration step);
    String getShortDescription();
    String getLongDescription();
    String getIconDescriptor();
    boolean isCompound();
    String getInlineEditString();
    String getType();
    String getGroupName();
    String getDocumentationDescription();

    Logger LOG = LoggerFactory.getLogger(StepDescriptor.class);
    }


