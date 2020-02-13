package org.museautomation.core.context;

import org.museautomation.core.*;

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
