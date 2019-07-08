package org.musetest.seleniumide.tests;

import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import org.musetest.seleniumide.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SideConverageTests
    {
    @Test
       void convertTest() throws IOException
   		{
   	    ObjectMapper mapper = new ObjectMapper();
   	    SideProject project = mapper.readValue(getClass().getResourceAsStream("/the-internet.side"), SideProject.class);

   	    SideTestConverter converter = new SideTestConverter();
   	    ConversionResult result = converter.convert(project.getTests()[0], project);

   	    Assertions.assertEquals(5, result._errors.size());
   	    }
    }