package org.musetest.core;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // public API for extensions
public enum ExtensionSelectionPriority
	{
	NEVER,      // don't use this extensions
	FALLBACK,   // for an extensions that can cover any operation but isn't the first choice
	BUILTIN,    // for the default choice / default is a reserved word :(
	OVERRIDE    // for overriding the default (i.e. for extensions)
	}
