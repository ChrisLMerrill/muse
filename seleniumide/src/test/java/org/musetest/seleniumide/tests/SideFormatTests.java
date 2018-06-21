package org.musetest.seleniumide.tests;

import com.fasterxml.jackson.databind.*;
import org.junit.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.selenium.steps.*;
import org.musetest.seleniumide.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SideFormatTests
	{
	@Test
	public void parseSideFile() throws IOException
		{
	    ObjectMapper mapper = new ObjectMapper();
	    SideProject project = mapper.readValue(getClass().getResourceAsStream("/LoginLogout.side"), SideProject.class);

	    Assert.assertEquals("6423bde0-f882-4e1b-bd88-230f17b39987", project.getId());
	    Assert.assertEquals("TestProject", project.getName());
	    Assert.assertEquals("https://the-internet.herokuapp.com", project.getUrl());

	    Assert.assertEquals(1, project.getTests().length);
	    SideTest test = project.getTests()[0];
		Assert.assertEquals("Login-Logout", test.getName());
		Assert.assertEquals("7e6bb643-7452-4e34-a5f9-178a3e7572b9", test.getId());
		Assert.assertEquals(8, test.getCommands().length);

		SideCommand command = test.getCommands()[3];
		Assert.assertEquals("a002dae2-a9cd-4b10-839a-571fc69e368a", command.getId());
		Assert.assertEquals("type", command.getCommand());
		Assert.assertEquals("enter username", command.getComment());
		Assert.assertEquals("id=username", command.getTarget());
		Assert.assertEquals("tomsmith", command.getValue());
	    }

	@Test
	public void convertTests() throws IOException, UnsupportedError
		{
	    ObjectMapper mapper = new ObjectMapper();
	    SideProject project = mapper.readValue(getClass().getResourceAsStream("/LoginLogout.side"), SideProject.class);

	    SideTestConverter converter = new SideTestConverter();
	    ConversionResult result = converter.convert(project.getTests()[0], project);

	    Assert.assertEquals(8, result._total_steps);
	    Assert.assertEquals(0, result._errors.size());

		SteppedTest test = result._test;
		Assert.assertEquals(BasicCompoundStep.TYPE_ID, test.getStep().getType());
		Assert.assertEquals(8, test.getStep().getChildren().size());

		StepConfiguration step1 = test.getStep().getChildren().get(0);
		Assert.assertEquals(GotoUrl.TYPE_ID, step1.getType());
		Assert.assertEquals("https://the-internet.herokuapp.com", step1.getSource(GotoUrl.URL_PARAM).getValue());
	    }
	}