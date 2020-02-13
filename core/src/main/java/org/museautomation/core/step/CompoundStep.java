package org.museautomation.core.step;

import org.museautomation.core.*;

/**
 * A CompoundStep consists of other steps to be run each time it runs.
 *
 * The only method returns a provider for child steps to be executed.
 *
 * This level of abstraction accounts for situations where the number of child steps is not known until runtime...and
 * may change as execution proceeds. This would be useful, for example, with a step that does something with all
 * images on a page - the number of images is not known until the test reaches this step (or even later).
 *
 * See BasicCompoundStep for the more common case, where the tester explicitly configures each child step in the parent.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface CompoundStep extends MuseStep
    {
    }

