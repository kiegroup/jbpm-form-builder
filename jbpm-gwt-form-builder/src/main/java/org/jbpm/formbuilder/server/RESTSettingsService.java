package org.jbpm.formbuilder.server;

import java.io.ByteArrayInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.jbpm.formapi.client.CommonGlobals;
import org.jbpm.formapi.client.Settings;
import org.jbpm.formbuilder.server.settings.SettingsService;

@Path("/settings")
public class RESTSettingsService extends RESTBaseService {
    private SettingsService settingsService;

    
    public RESTSettingsService() {
        

    }
    
    private void init(){
        if(this.settingsService == null){
            this.settingsService = ServiceFactory.getInstance().getSettingsService();
        }
    }

    @GET
    @Path("/{userName}")
    public Response getUserSettings(@PathParam("userName") String userName, @Context HttpServletRequest request) {
        init();
        Settings settings = settingsService.getSettingsByUserId(userName);
        return Response.ok(settings, MediaType.APPLICATION_XML).build();
    }

   

    @POST
    @Consumes("text/plain")
    @Path("/{userName}")
    public Response applySettings(String settingsXml, @PathParam("userName") String userName, @Context HttpServletRequest request) {
        init();
        try {
            JAXBContext context = JAXBContext.newInstance(Settings.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Settings settings = (Settings) unmarshaller.unmarshal(new ByteArrayInputStream(settingsXml.getBytes()));
            settingsService.applySettings(settings, userName);
            CommonGlobals.getInstance().setSettings(settings);
        } catch (JAXBException ex) {
            Logger.getLogger(RESTSettingsService.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
        return Response.ok().build();
    }
 
}
