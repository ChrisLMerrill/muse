package org.musetest.seleniumide.tests.mocks;

import org.musetest.seleniumide.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockTestConverter extends TestConverter
    {
    public MockTestConverter()
        {
        super(new ByteArrayInputStream("unused".getBytes()));
        }
    }


