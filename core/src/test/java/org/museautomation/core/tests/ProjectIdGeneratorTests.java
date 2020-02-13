package org.museautomation.core.tests;

import org.junit.jupiter.api.*;
import org.museautomation.core.project.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ProjectIdGeneratorTests
	{
	@Test
    void generateUniqueIds()
	    {
	    SimpleProject project = new SimpleProject();

	    long id1 = IdGenerator.get(project).generateLongId();
	    long id2 = IdGenerator.get(project).generateLongId();

	    Assertions.assertTrue(id1 != 0);
	    Assertions.assertTrue(id2 != 0);
	    Assertions.assertTrue(id1 != id2);
	    }
	}


