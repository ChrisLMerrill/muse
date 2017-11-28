package org.musetest.core.util;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface Taggable
	{
	/**
	 * Tags allow the end user to group and select resources. E.g. Add an "administrator" tag to
	 * tests that required administrator credentials.
	 */
	Set<String> getTags();

	/**
	 * This is required for Jackson serialization. It is not recommended for general use, as the project will not
	 * see these changes...so queries based on the new tags will malfunction.
	 *
	 * @see #addTag
	 * @see #removeTag
	 * @see #hasTag
	 */
	@SuppressWarnings("unused")
	// used in UI
	void setTags(Set<String> tags);

	boolean addTag(String tag);

	@SuppressWarnings("unused")
	// used in UI
	boolean removeTag(String tag);

	boolean hasTag(String tag);
	}

