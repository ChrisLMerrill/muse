package org.museautomation.selenium.mocks;

import org.openqa.selenium.*;
import org.openqa.selenium.logging.*;
import org.slf4j.*;
import org.slf4j.Logger;

import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MuseMockDriver implements WebDriver, TakesScreenshot
    {
    @Override
    public void get(String url)
        {
        _current_url = url;
        _title = "MuseMock - " + url + " title";
        }

    @Override
    public String getCurrentUrl()
        {
        return _current_url;
        }

    @Override
    public String getTitle()
        {
        return _title;
        }

    @Override
    public List<WebElement> findElements(By by)
        {
        return null;
        }

    @Override
    public WebElement findElement(By by)
        {
        if (by instanceof By.ByXPath)
            return _xpath_elements.get(extractPrivateMember("xpathExpression", by));
        else if (by instanceof By.ById)
            return _id_elements.get(extractPrivateMember("id", by));
        return null;
        }

    @Override
    public String getPageSource()
        {
        return new String(_html);
        }

    @Override
    public void close()
        {

        }

    @Override
    public void quit()
        {
        _is_quitted = true;
        }

    @Override
    public Set<String> getWindowHandles()
        {
        return null;
        }

    @Override
    public String getWindowHandle()
        {
        return null;
        }

    @Override
    public TargetLocator switchTo()
        {
        return new TargetLocator()
            {
            @Override
            public WebDriver frame(int index)
                {
                return null;
                }

            @Override
            public WebDriver frame(String nameOrId)
                {
                return null;
                }

            @Override
            public WebDriver frame(WebElement frameElement)
                {
                _target = frameElement;
                return MuseMockDriver.this;
                }

            @Override
            public WebDriver parentFrame()
                {
                return null;
                }

            @Override
            public WebDriver window(String nameOrHandle)
                {
                return null;
                }

            @Override
            public WebDriver defaultContent()
                {
                return null;
                }

            @Override
            public WebElement activeElement()
                {
                return null;
                }

            @Override
            public Alert alert()
                {
                return null;
                }
            };
        }

    @Override
    public Navigation navigate()
        {
        return new Navigation()
            {
            @Override
            public void back()
                {

                }

            @Override
            public void forward()
                {

                }

            @Override
            public void to(String url)
                {
                get(url);
                }

            @Override
            public void to(URL url)
                {
                get(url.toString());
                }

            @Override
            public void refresh()
                {

                }
            };
        }

    @Override
    public Options manage()
        {
        return new Options()
	        {
	        @Override
	        public void addCookie(Cookie cookie)
		        {

		        }

	        @Override
	        public void deleteCookieNamed(String name)
		        {

		        }

	        @Override
	        public void deleteCookie(Cookie cookie)
		        {

		        }

	        @Override
	        public void deleteAllCookies()
		        {

		        }

	        @Override
	        public Set<Cookie> getCookies()
		        {
		        return null;
		        }

	        @Override
	        public Cookie getCookieNamed(String name)
		        {
		        return null;
		        }

	        @Override
	        public Timeouts timeouts()
		        {
		        return null;
		        }

	        @Override
	        public ImeHandler ime()
		        {
		        return null;
		        }

	        @Override
	        public Window window()
		        {
		        return null;
		        }

	        @Override
	        public Logs logs()
		        {
		        return new Logs()
			        {
			        @Override
			        public LogEntries get(String logType)
				        {
				        LogEntry entry = new LogEntry(Level.ALL, System.currentTimeMillis(), new String(_log));
				        return new LogEntries(Collections.singleton(entry));
				        }

			        @Override
			        public Set<String> getAvailableLogTypes()
				        {
				        return Collections.singleton("only-one");
				        }
			        };
		        }
	        };
        }

    public void addXpathElement(String xpath, WebElement element)
        {
        _xpath_elements.put(xpath, element);
        }

    public void addIdElement(String id, WebElement element)
        {
        _id_elements.put(id, element);
        }

    public Object getTarget()
        {
        return _target;
        }

    private String extractPrivateMember(String name, Object target)
        {
        try
            {
            Class the_class = target.getClass();
            for (Field field : the_class.getDeclaredFields())
                if (name.equals(field.getName()))
                    {
                    field.setAccessible(true);
                    return field.get(target).toString();
                    }
            return null;
            }
        catch (IllegalAccessException e)
            {
            LOG.error("Unable to access private member of the By object.");
            return null;
            }
        }

    public void putScreenshot(byte[] bytes)
	    {
	    _screenshot = bytes;
	    }

    public void putHtml(byte[] bytes)
	    {
	    _html = bytes;
	    }

    public void putLog(byte[] bytes)
	    {
	    _log = bytes;
	    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException
	    {
	    if (target.equals(OutputType.BYTES))
	    	return (X) _screenshot;
	    return null;
	    }

    public boolean _is_quitted = false;
    private String _current_url = null;
    private String _title = null;
    private Object _target = null;

    private Map<String, WebElement> _xpath_elements = new HashMap<>();
    private Map<String, WebElement> _id_elements = new HashMap<>();

    private byte[] _screenshot;
    private byte[] _html;
    private byte[] _log = "".getBytes();

    private final static Logger LOG = LoggerFactory.getLogger(MuseMockDriver.class);
    }


