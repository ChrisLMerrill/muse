package org.museautomation.core.events;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public enum EventStatus
	{
	/**
	 * Nothing wrong with this event.
	 */
	Normal,

	/**
	 * Indicates an failure that is expected (such as a verification step that failed to satisfy the desired condition).
     */
	Failure,

	/**
	 * Indicates that something went unexpectedly wrong, such as a configuration or coding error, that prevented execution or may prevent future execution.
	 * This status is fatal - it implies the test should stop execution.
	 */
	Error
	}
