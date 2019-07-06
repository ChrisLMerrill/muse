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

    public boolean isPersistSession()
        {
        return persistSession;
        }

    public void setPersistSession(boolean persistSession)
        {
        this.persistSession = persistSession;
        }

    public boolean isParallel()
        {
        return parallel;
        }

    public void setParallel(boolean parallel)
        {
        this.parallel = parallel;
        }

    public long getTimeout()
        {
        return timeout;
        }

    public void setTimeout(long timeout)
        {
        this.timeout = timeout;
        }

    String id;
	String name;
	boolean persistSession;
	boolean parallel;
	long timeout;
	String[] tests;
	}