package org.musetest.core.tests;

import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.musetest.core.project.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ProjectIdGeneratorTests
	{
	@Test
	public void generateUniqueIds()
	    {
	    SimpleProject project = new SimpleProject();

	    long id1 = IdGenerator.get(project).generateLongId();
	    long id2 = IdGenerator.get(project).generateLongId();

	    Assertions.assertTrue(id1 != 0);
	    Assertions.assertTrue(id2 != 0);
	    Assertions.assertTrue(id1 != id2);
	    }
	}


