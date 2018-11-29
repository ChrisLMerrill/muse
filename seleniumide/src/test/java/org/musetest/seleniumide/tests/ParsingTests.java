package org.musetest.seleniumide.tests;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.junit.jupiter.api.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ParsingTests
    {
    @Test
    void parseExample() throws IOException
        {
        Document doc = Jsoup.parse(getClass().getResourceAsStream("/example.html"), "UTF-8", "http://example.com/");
        Assertions.assertNotNull(doc);

        Elements links = doc.getElementsByTag("link");
        Assertions.assertEquals(1, links.size());
        Assertions.assertEquals("selenium.base", links.get(0).attr("rel"));
        Assertions.assertEquals("http://www.example.com/", links.get(0).attr("href"));

        Elements rows = doc.getElementsByTag("tr");
        Assertions.assertEquals(6, rows.size());  // +1 for the header

        Element row1 = rows.get(1);  // first data row
        Assertions.assertEquals(3, row1.children().size());

        Assertions.assertEquals("open", row1.children().get(0).text());
        Assertions.assertEquals("/login/", row1.children().get(1).text());
        Assertions.assertEquals("", row1.children().get(2).text());
        }
    }


