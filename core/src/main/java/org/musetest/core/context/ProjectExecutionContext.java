package org.musetest.core.context;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ProjectExecutionContext extends BaseExecutionContext
	{
	public ProjectExecutionContext(MuseProject project)
		{
		super(project, ContextVariableScope.Project);
		setVariable(EXECUTION_ID_VARNAME, Long.toString(System.currentTimeMillis()), ContextVariableScope.Project);
		}
	}
