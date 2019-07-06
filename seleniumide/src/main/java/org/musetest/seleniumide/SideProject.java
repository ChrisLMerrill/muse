package org.musetest.seleniumide;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SideProject
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

	public String getUrl()
		{
		return url;
		}

	public void setUrl(String url)
		{
		this.url = url;
		}

	public String[] getUrls()
		{
		return urls;
		}

	public void setUrls(String[] urls)
		{
		this.urls = urls;
		}

	public SideTest[] getTests()
		{
		return tests;
		}

	public void setTests(SideTest[] tests)
		{
		this.tests = tests;
		}

	public SideSuite[] getSuites()
		{
		return suites;
		}

	public void setSuites(SideSuite[] suites)
		{
		this.suites = suites;
		}

    public String getVersion()
        {
        return version;
        }

    public void setVersion(String version)
        {
        this.version = version;
        }

    public String[] getPlugins()
        {
        return plugins;
        }

    public void setPlugins(String[] plugins)
        {
        this.plugins = plugins;
        }

    String id;
	String name;
	String url;
	String version;
	String[] urls;
	String[] plugins;
	SideTest[] tests;
	SideSuite[] suites;
	}
