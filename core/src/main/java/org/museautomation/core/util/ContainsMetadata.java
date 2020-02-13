package org.museautomation.core.util;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ContainsMetadata
	{
	@JsonIgnore
	void setMetadataField(String name, Object value);

	@JsonIgnore
	void removeMetadataField(String name);

	@JsonIgnore
	Object getMetadataField(String name);

	@SuppressWarnings("unused")  // used in UI
	@JsonIgnore
	Set<String> getMetadataFieldNames();

	void addChangeListener(ChangeEventListener listener);
	boolean removeChangeListener(ChangeEventListener listener);
	}

