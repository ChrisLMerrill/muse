package org.musetest.core;

import com.fasterxml.jackson.annotation.*;
import org.musetest.core.context.*;

/**
 * A Test is executed to return a result (pass or fail).
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseTest extends MuseResource
    {
    MuseTestResult execute(TestExecutionContext context);

    @JsonIgnore
    String getDescription();
    }

