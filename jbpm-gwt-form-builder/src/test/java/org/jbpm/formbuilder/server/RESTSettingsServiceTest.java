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
package org.jbpm.formbuilder.server;


import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.easymock.EasyMock;
import org.jbpm.formapi.client.Settings;
import org.jbpm.formapi.client.SettingsEntry;

public class RESTSettingsServiceTest extends RESTAbstractTest {

    public void testNothing() throws JAXBException{
        Settings settings = new Settings("salaboy");
        settings.setId(1L);
        SettingsEntry settingsEntry = new SettingsEntry("storage", "fs");
        settingsEntry.setId(1L);
        settings.addEntry(settingsEntry);
        SettingsEntry settingsEntry2 = new SettingsEntry("anotherprop", "othervalue");
        settingsEntry2.setId(2L);
        settings.addEntry(settingsEntry2);
        
        JAXBContext context = JAXBContext.newInstance(Settings.class);
        Marshaller createMarshaller = context.createMarshaller();
        StringWriter writer = new StringWriter();
        createMarshaller.marshal(settings, writer);
        System.out.println("Result = "+ writer.toString());
        
    }
    //test happy path of RESTaddUserSettings
    public void testaddUserSettingsEntryOK() throws Exception {
        RESTSettingsService restService = new RESTSettingsService();
        List<Object> requestMocks = createRequestMocks();
        
        Object[] mocks = requestMocks.toArray();
        
        Response resp = restService.applySettings("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><settings><entries><key>storage</key><value>fs</value></entries><userId>salaboy</userId></settings>", 
                                                    "salaboy", (HttpServletRequest)mocks[0]);

        assertNotNull("resp shouldn't be null", resp);
        assertStatus(resp.getStatus(), Status.OK);
        
        Response resp2 = restService.getUserSettings("salaboy", (HttpServletRequest)mocks[0]);
        assertNotNull("resp shouldn't be null", resp2);
        assertStatus(resp2.getStatus(), Status.OK);
        assertTrue(resp2.getEntity() instanceof Settings);
        
    }
    
    
    private List<Object> createRequestMocks() {
        List<Object> requestMocks = new ArrayList<Object>();
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        HttpSession session = EasyMock.createMock(HttpSession.class);
        ServletContext context = EasyMock.createMock(ServletContext.class);
        EasyMock.expect(request.getSession()).andReturn(session).once();
        EasyMock.expect(session.getServletContext()).andReturn(context).once();
        requestMocks.add(request);
        requestMocks.add(session);
        requestMocks.add(context);
        return requestMocks;
    }
}
