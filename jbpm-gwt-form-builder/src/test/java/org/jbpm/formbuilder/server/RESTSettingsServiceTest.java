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
import java.security.Principal;
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
import org.jbpm.model.formapi.client.Settings;
import org.jbpm.model.formapi.client.SettingsEntry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

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
        
        ApplicationContext ctx = new FileSystemXmlApplicationContext("src/main/webapp/WEB-INF/springComponents.xml");
        ServiceFactory.getInstance().setBeanFactory(ctx);
        
        RESTUserService restService = new RESTUserService();
        List<Object> requestMocks = createRequestMocks();
        
        Settings settings = new Settings("admin");
        settings.setId(1L);
        SettingsEntry settingsEntry = new SettingsEntry("storage", "fs");
        settingsEntry.setId(1L);
        settings.addEntry(settingsEntry);
        SettingsEntry settingsEntry2 = new SettingsEntry("anotherprop", "othervalue");
        settingsEntry2.setId(2L);
        settings.addEntry(settingsEntry2);
        
        Object[] mocks = requestMocks.toArray();
        EasyMock.replay(mocks);
        assertNotNull(((HttpServletRequest)mocks[0]).getSession());
        assertNotNull(((HttpServletRequest)mocks[0]).getUserPrincipal());
        assertEquals("admin",((HttpServletRequest)mocks[0]).getUserPrincipal().getName());
        Response resp = restService.applySettings(settings,(HttpServletRequest)mocks[0]);
        EasyMock.verify(mocks);
        assertNotNull("resp shouldn't be null", resp);
        assertStatus(resp.getStatus(), Status.OK);
        
        Response resp2 = restService.getUserSettings((HttpServletRequest)mocks[0]);
        assertNotNull("resp shouldn't be null", resp2);
        assertStatus(resp2.getStatus(), Status.OK);
        assertTrue(resp2.getEntity() instanceof Settings);
        
    }
    
    
    private List<Object> createRequestMocks() {
        List<Object> requestMocks = new ArrayList<Object>();
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        Principal principal = EasyMock.createMock(Principal.class);
        HttpSession session = EasyMock.createMock(HttpSession.class);
        ServletContext context = EasyMock.createMock(ServletContext.class);
        EasyMock.expect(request.getUserPrincipal()).andReturn(principal).anyTimes();
        EasyMock.expect(principal.getName()).andReturn("admin").anyTimes();
        EasyMock.expect(request.getSession()).andReturn(session).once();
        
        requestMocks.add(request);
        requestMocks.add(session);
        requestMocks.add(context);
        requestMocks.add(principal);
        return requestMocks;
    }
}
