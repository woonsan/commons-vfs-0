/* ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.apache.commons.vfs.provider.jar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileSystemException;

/**
 * A default URL connection that will work for most file systems.
 *
 * @author <a href="mailto:brian@mmmanager.org">Brian Olsen</a>
 * @version $Revision: 1.6 $ $Date: 2002/10/27 08:16:20 $
 */
public class JarURLConnectionImpl
    extends JarURLConnection
{
    // This is because JarURLConnection SUCKS
    private static final String HACK_URL = "jar:http://somehost/somejar.jar!/";

    private FileContent content;
    private URL parentURL;
    private JarFileObject file;
    private String entryName;

    public JarURLConnectionImpl( JarFileObject file, FileContent content )
        throws MalformedURLException, FileSystemException
    {
        //This is because JarURLConnection SUCKS!!
        super( new URL( HACK_URL ) );

        this.url = file.getURL();
        this.content = content;
        this.parentURL = file.getURL();
        this.entryName = file.getName().getPath();
        this.file = file;
    }


    public URL getJarFileURL()
    {
        return parentURL;
    }


    public String getEntryName()
    {
        return entryName;
    }


    public JarFile getJarFile() throws IOException
    {
        throw new FileSystemException( "vfs.provider.jar/jar-file-no-access.error" );
    }


    public Manifest getManifest() throws IOException
    {
        return file.getManifest();
    }


    public JarEntry getJarEntry() throws IOException
    {
        throw new FileSystemException( "vfs.provider.jar/jar-entry-no-access.error" );
    }


    public Attributes getAttributes() throws IOException
    {
        return file.getAttributes();
    }


    public Certificate[] getCertificates()
    {
        return file.doGetCertificates();
    }


    public void connect()
    {
        connected = true;
    }

    public InputStream getInputStream()
        throws IOException
    {
        return content.getInputStream();
    }

    public OutputStream getOutputStream()
        throws IOException
    {
        return content.getOutputStream();
    }

    public int getContentLength()
    {
        try
        {
            return (int)content.getSize();
        }
        catch ( FileSystemException fse )
        {
        }

        return -1;
    }

}