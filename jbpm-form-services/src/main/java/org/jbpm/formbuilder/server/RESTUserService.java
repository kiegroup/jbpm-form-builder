package org.jbpm.formbuilder.server;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.jboss.resteasy.annotations.providers.jaxb.DoNotUseJAXBProvider;
import org.jbpm.formapi.client.CommonGlobals;
import org.jbpm.formapi.client.Settings;
import org.jbpm.formbuilder.server.settings.SettingsService;

@Path("/user")
public class RESTUserService extends RESTBaseService {
    private SettingsService settingsService;
    
    private static final String[] AVAILABLE_ROLES = new String[] { 
        "admin", "webdesigner", "functionalanalyst" 
    };
    
    private void init(){
        if(this.settingsService == null){
            this.settingsService = ServiceFactory.getInstance().getSettingsService();
        }
    }
    
    @GET @Path("/current/roles")
    @Consumes("*/*")
    @Produces("text/plain")
    @DoNotUseJAXBProvider
    public Response getCurrentRoles(@Context HttpServletRequest request) {
        List<String> roles = getRoles(request);
        StringBuilder txtRoles = new StringBuilder();
        for (Iterator<String> iter = roles.iterator(); iter.hasNext(); ) {
            txtRoles.append(iter.next());
            if (iter.hasNext()) {
                txtRoles.append(",");
            }
        }
        return Response.ok(txtRoles.toString()).build();
    }

    @POST @Path("/current/logout")
    public Response logout(@Context HttpServletRequest request) {
        request.getSession().invalidate();
        return Response.ok().build();
    }
    
    public static List<String> getRoles(HttpServletRequest request) {
        List<String> roles = new ArrayList<String>();
        for (String role : AVAILABLE_ROLES) {
            if (request.isUserInRole(role)) {
                roles.add(role);
            }
        }
        return roles;
    }

    public static boolean hasDesignerPrivileges(HttpServletRequest request) {
        List<String> roles = getRoles(request);
        return roles.contains("admin") || roles.contains("webdesigner");
    }
    
    @GET
    @Path("/current/settings")
    public Response getUserSettings(@Context HttpServletRequest request) {
        init();
        String userName = request.getUserPrincipal().getName(); 
        Settings settings = settingsService.getSettingsByUserId(userName);
        return Response.ok(settings, MediaType.APPLICATION_XML).build();
    }

   

    @POST
    @Consumes("application/xml")
    @Path("/current/settings")
    public Response applySettings(Settings settings, @Context HttpServletRequest request) {
        String userName = request.getUserPrincipal().getName(); 
        init();
 //       try {
//            JAXBContext context = JAXBContext.newInstance(Settings.class);
//            Unmarshaller unmarshaller = context.createUnmarshaller();
//            Settings settings = (Settings) unmarshaller.unmarshal(new ByteArrayInputStream(settingsXml.getBytes()));
            settingsService.applySettings(settings, userName);
            CommonGlobals.getInstance().setSettings(settings);
//        } catch (JAXBException ex) {
//            Logger.getLogger(RESTSettingsService.class.getName()).log(Level.SEVERE, null, ex);
//            return Response.serverError().build();
//        }
        return Response.ok().build();
    }
}
