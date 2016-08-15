package org.musetest.core;

import com.fasterxml.jackson.annotation.*;
import org.musetest.core.context.*;
import org.musetest.core.values.*;

import java.util.*;

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

    Map<String, ValueSourceConfiguration> getDefaultVariables();
    void setDefaultVariables(Map<String, ValueSourceConfiguration> default_variables);
    void setDefaultVariable(String name, ValueSourceConfiguration source);
    }

