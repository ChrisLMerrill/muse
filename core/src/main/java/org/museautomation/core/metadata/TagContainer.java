package org.museautomation.core.metadata;

import java.util.*;

/**
 * A tag is simply a descriptive string applied to an entity. They are unique metadata configured by
 * the user (or other code) to be acted upon later by another entity.
 *
 * For example, a user may tag items in collection and use a filter to select those matching the tag
 * for future operations
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TagContainer
	{
	/**
	 * Access the entire list of tags. The set returned is not modifiable.
	 */
	Set<String> getTags();

	boolean addTag(String tag);

	@SuppressWarnings("unused")
	// used in UI
	boolean removeTag(String tag);

	boolean hasTag(String tag);

	@SuppressWarnings("unused")  // used UI
	void addListener(TagChangeListener listener);
    @SuppressWarnings("unused")  // used UI
	void removeListener(TagChangeListener listener);

    interface TagChangeListener
        {
        void tagAdded(String tag);
        void tagRemoved(String tag);
        }
	}

