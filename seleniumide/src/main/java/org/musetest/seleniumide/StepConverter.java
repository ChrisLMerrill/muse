package org.musetest.seleniumide;

import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StepConverter
    {
    StepConfiguration convertStep(String base_url, String command, String param1, String param2) throws UnsupportedError;
    }

