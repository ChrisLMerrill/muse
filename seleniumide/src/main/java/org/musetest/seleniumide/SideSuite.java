package org.musetest.seleniumide;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SideSuite
	{
	public String getId()
		{
		return id;
		}

	public void setId(String id)
		{
		this.id = id;
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public String[] getTests()
		{
		return tests;
		}

	public void setTests(String[] tests)
		{
		this.tests = tests;
		}

	String id;
	String name;
	String[] tests;
	}