package org.musetest.core.util;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FileUtils
    {
	public static String getNameWithoutExtension(File file)
		{
		String name = file.getName();
		if (name.contains("."))
			{
			int index = name.lastIndexOf('.');
			if (index > 0)
				name = name.substring(0, index - 1);
			}
		return name;
		}

    public static String loadTextFile(File f) throws IOException
    	{
		try (Reader r = openText(f))
			{
			StringWriter writer = new StringWriter((int) f.length());
			copyAndCloseInput(r, writer);
			return writer.toString();
			}
    	}

    public static void copyAndCloseInput(Reader in, Writer out) throws IOException
    	{
    	char[] buffer=new char[2048];
    	int rd;
    	while ((rd=in.read(buffer))>=0)
    		out.write(buffer, 0, rd);
    	in.close();
    	}


    private static final short UTF8=003, UTF16BE=012, UTF16LE=002, UTF32BE=014, UTF32LE=004, DEFAULT=0;
    /**
     * Windows (and possibly other scenarios) will write text files potentially
     * using some variant of Unicode encoding, and distinguishes these files with
     * a BOM at the beginning of the file. Since FileReader regretably will not
     * check for a BOM, we must do it ourselves.
     * <P>
     * See http://www.unicode.org/faq/utf_bom.html#BOM
     *
     * @param f The file to read
     * @return A reader for the file
     * @throws IOException If the file cannot be opened
     */
    public static Reader openText(File f) throws IOException
	    {
	    return openText(new FileInputStreamProvider(f));
	    }
    public static Reader openText(InputStreamProvider provider) throws IOException
	    {
	    InputStream f_in=provider.getInputStream();
	    f_in.mark(8);
	    InputStream bom_in=new BufferedInputStream(f_in, 4);
	    int rd=bom_in.read();
	    short c_set=DEFAULT;
	    switch (rd) {
	        case 0x00:	if ((bom_in.read()==0x00) && (bom_in.read()==0xFE) && (bom_in.read()==0xFF)) c_set=UTF32BE;
				        break;
		    case 0xEF:	if ((bom_in.read()==0xBB) && (bom_in.read()==0xBF)) c_set=UTF8;
					    break;
		    case 0xFE:	if (bom_in.read()==0xFF) c_set=UTF16BE;
					    break;
		    case 0xFF:	rd=bom_in.read();
					    if (rd==0xFE) c_set=((bom_in.read()==0) && (bom_in.read()==0)) ? UTF32LE : UTF16LE;
					    rd=0;	//in case rd < 0
					    //break;
	    }
	    if (rd >= 0) {
		    if (f_in.markSupported()) f_in.reset();
		    else {
			    f_in.close();
			    f_in=provider.getInputStream();}}
        long skip=c_set & 007L;
	    while (skip > 0) skip-=f_in.skip(skip);
	    String enc=null;
	    switch (c_set) {
		    case UTF32BE:	enc="UTF-32BE";	break;
		    case UTF32LE:	enc="UTF-32LE";	break;
		    case UTF16BE:	enc="UTF-16BE";	break;
		    case UTF16LE:	enc="UTF-16LE";	break;
		    case UTF8:		enc="UTF-8";	break;
		    case DEFAULT:	return new InputStreamReader(f_in);
	    }
	    return new InputStreamReader(f_in, enc);
    }

    }


