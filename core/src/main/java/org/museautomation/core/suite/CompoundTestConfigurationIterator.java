package org.museautomation.core.suite;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class CompoundTestConfigurationIterator<TestConfiguration> implements Iterator<TestConfiguration>
	{
	public void add(Iterator<TestConfiguration> iterator)
		{
		_iterators.add(iterator);
		}

	@Override
	public boolean hasNext()
		{
		while (_iterators.peek() != null)
			{
			if (_iterators.peek().hasNext())
				return true;
			else
				_iterators.remove();
			}
		return false;
		}

	@Override
	public TestConfiguration next()
		{
		Iterator<TestConfiguration> next = _iterators.peek();
		while (next != null)
			{
			if (next.hasNext())
				return next.next();
			else
				_iterators.remove();
			next = _iterators.peek();
			}
		throw new NoSuchElementException("No more iterators!  You should check the result of hasNext() before calling next().");
		}

	private Queue<Iterator<TestConfiguration>> _iterators = new LinkedList<>();
	}


