package org.musetest.seleniumide.tests;

import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.selenium.steps.*;
import org.musetest.seleniumide.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class SideFormatTests
	{
	@Test
    void parseSideFile() throws IOException
		{
	    ObjectMapper mapper = new ObjectMapper();
	    SideProject project = mapper.readValue(getClass().getResourceAsStream("/LoginLogout.side"), SideProject.class);

	    Assertions.assertEquals("6423bde0-f882-4e1b-bd88-230f17b39987", project.getId());
	    Assertions.assertEquals("TestProject", project.getName());
	    Assertions.assertEquals("https://the-internet.herokuapp.com", project.getUrl());

	    Assertions.assertEquals(1, project.getTests().length);
	    SideTest test = project.getTests()[0];
		Assertions.assertEquals("Login-Logout", test.getName());
		Assertions.assertEquals("7e6bb643-7452-4e34-a5f9-178a3e7572b9", test.getId());
		Assertions.assertEquals(8, test.getCommands().length);

		SideCommand command = test.getCommands()[3];
		Assertions.assertEquals("a002dae2-a9cd-4b10-839a-571fc69e368a", command.getId());
		Assertions.assertEquals("type", command.getCommand());
		Assertions.assertEquals("enter username", command.getComment());
		Assertions.assertEquals("id=username", command.getTarget());
		Assertions.assertEquals("tomsmith", command.getValue());
	    }

	@Test
    void convertTests() throws IOException
		{
	    ObjectMapper mapper = new ObjectMapper();
	    SideProject project = mapper.readValue(getClass().getResourceAsStream("/LoginLogout.side"), SideProject.class);

	    SideTestConverter converter = new SideTestConverter();
	    ConversionResult result = converter.convert(project.getTests()[0], project);

	    Assertions.assertEquals(8, result._total_steps);
	    Assertions.assertEquals(0, result._errors.size());

		SteppedTest test = result._test;
		Assertions.assertEquals(BasicCompoundStep.TYPE_ID, test.getStep().getType());
		Assertions.assertEquals(8, test.getStep().getChildren().size());

		StepConfiguration step1 = test.getStep().getChildren().get(0);
		Assertions.assertEquals(GotoUrl.TYPE_ID, step1.getType());
		Assertions.assertEquals("https://the-internet.herokuapp.com", step1.getSource(GotoUrl.URL_PARAM).getValue());
	    }
	}