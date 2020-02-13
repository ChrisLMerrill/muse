package org.museautomation.core.variables;

import org.museautomation.core.context.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public enum VariableQueryScope
	{
	Project,
	Suite,
	Execution,
	Local,
	Any,       // Look in any of the scopes (limited - see NotLocal)
	AnyButLocal;  // Any turns into AnyButLocal after looking in the first local scope (because local scopes are not visible to child scopes).

	public boolean appliesToScope(ContextVariableScope context_scope)
		{
		if (context_scope == null)
			return false;

		switch (this)
			{
			case Project:
				return context_scope.equals(ContextVariableScope.Project);
			case Suite:
				return context_scope.equals(ContextVariableScope.Suite);
			case Execution:
				return context_scope.equals(ContextVariableScope.Execution);
			case Local:
				return context_scope.equals(ContextVariableScope.Local);
			case Any:
				return true;
			case AnyButLocal:
				return !context_scope.equals(ContextVariableScope.Local);
			default:
				LOG.error("VariableQueryScope.appliesToScope() encountered an unexpected VariableQueryScope: " + name());
				return false;
			}
		}

	private final static Logger LOG = LoggerFactory.getLogger(VariableQueryScope.class);
	}
