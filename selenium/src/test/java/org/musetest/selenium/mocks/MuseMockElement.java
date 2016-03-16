package org.musetest.selenium.mocks;

import org.openqa.selenium.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MuseMockElement implements WebElement
    {
    @Override
    public void click()
        {
        _clicked = true;
        }

    @Override
    public void submit()
        {

        }

    @Override
    public void sendKeys(CharSequence... keysToSend)
        {

        }

    @Override
    public void clear()
        {

        }

    @Override
    public String getTagName()
        {
        return null;
        }

    @Override
    public String getAttribute(String name)
        {
        return null;
        }

    @Override
    public boolean isSelected()
        {
        return false;
        }

    @Override
    public boolean isEnabled()
        {
        return _enabled;
        }

    @Override
    public String getText()
        {
        return null;
        }

    @Override
    public List<WebElement> findElements(By by)
        {
        return null;
        }

    @Override
    public WebElement findElement(By by)
        {
        return null;
        }

    @Override
    public boolean isDisplayed()
        {
        return _displayed;
        }

    @Override
    public Point getLocation()
        {
        return null;
        }

    @Override
    public Dimension getSize()
        {
        return null;
        }

    @Override
    public String getCssValue(String propertyName)
        {
        return null;
        }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException
        {
        return null;
        }

    public boolean isClicked()
        {
        return _clicked;
        }

    public void setDisplayed(boolean displayed)
        {
        this._displayed = displayed;
        }

    public void setEnabled(boolean enabled)
        {
        _enabled = enabled;
        }

    private boolean _clicked = false;
    private boolean _displayed = false;
    private boolean _enabled = false;
    }


