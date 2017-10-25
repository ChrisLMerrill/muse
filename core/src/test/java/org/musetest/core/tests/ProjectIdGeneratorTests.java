package org.musetest.core.tests;

import org.junit.*;
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

	    Assert.assertTrue(id1 != 0);
	    Assert.assertTrue(id2 != 0);
	    Assert.assertTrue(id1 != id2);
	    }
	}


