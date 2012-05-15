/*
 * Copyright 2011 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.formbuilder.server.file;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

public class FSFileServiceTest extends TestCase {

    private String fileSeparator = System.getProperty("file.separator");
    private String baseUrl = "/tmp";
    
    protected void setUp() throws Exception {
        FileUtils.deleteDirectory(new File(baseUrl+fileSeparator+"somePackage"));
    }
    
    protected void tearDown() throws Exception {
        FileUtils.deleteDirectory(new File(baseUrl+fileSeparator+"somePackage"));
    }
    
    public void testStoreFileOK() throws Exception {
        FSFileService service = createService(baseUrl);

        String url = service.storeFile("somePackage", "fileName.txt", "This is the file content".getBytes());
        String readFileToString = FileUtils.readFileToString(new File(url));
        assertEquals("This is the file content", readFileToString);

        service.deleteFile(url);

    }

    public void testDeleteFileOK() throws Exception {
        FSFileService service = createService(baseUrl);

        String url = service.storeFile("somePackage", "fileName.txt", "This is the file content".getBytes());

        service.deleteFile(url);

        assertFalse(new File(url).exists());



    }

    public void testLoadFilesByTypeOK() throws Exception {
        FSFileService service = createService(baseUrl);

        String url = service.storeFile("somePackage", "fileName1.txt", "This is the file content 1".getBytes());
        String url2 = service.storeFile("somePackage", "fileName2.txt", "This is the file content 2".getBytes());

        List<String> files = service.loadFilesByType("somePackage", "txt");

        assertEquals(2, files.size());
        boolean urlOk = false;
        boolean url2Ok = false;
        if(url.equals(files.get(0)) || url.equals(files.get(1))){
            urlOk = true;
        }
        if(url2.equals(files.get(0)) || url2.equals(files.get(1))){
            url2Ok = true;
        }
        
        assertTrue(urlOk);
        assertTrue(url2Ok);

        service.deleteFile(url);

        service.deleteFile(url2);

    }

    public void testLoadFilesByTypeNoTypeSpecified() throws Exception {
        FSFileService service = createService(baseUrl);

        List<String> files = service.loadFilesByType("somePackage", "");

        assertEquals(0, files.size());


    }

    public void testLoadFileOK() throws Exception {
        FSFileService service = createService(baseUrl);

        String url = service.storeFile("somePackage", "someFile.txt", "This is the file content".getBytes());

        byte[] retval = service.loadFile("somePackage", "someFile.txt");

        assertEquals("This is the file content", new String(retval));


        service.deleteFile(url);


    }

    private FSFileService createService(String baseUrl) {
        FSFileService service = new FSFileService();
        service.setBaseUrl(baseUrl);
        return service;
    }
}
