package org.jbpm.formbuilder.server;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.persistence.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.jboss.resteasy.annotations.providers.jaxb.DoNotUseJAXBProvider;
import org.jbpm.formapi.client.CommonGlobals;
import org.jbpm.formapi.client.SettingsEntry;
import org.jbpm.formapi.client.Settings;

@Path("/settings")
public class RESTSettingsService extends RESTBaseService {

    private EntityManager em;

    public RESTSettingsService() {
        if (em == null) {
            EntityManagerFactory emf = null;
            try {
                InitialContext ctx = new InitialContext();
                // For JBOSS 7.x
                // see: https://docs.jboss.org/author/display/AS71/JPA+Reference+Guide#JPAReferenceGuide-BindingEntityManagerFactorytoJNDI
                System.out.println("before Context lookup...");
                emf = (EntityManagerFactory) ctx.lookup("java:jboss/myEntityManagerFactory");
                System.out.println("After Context lookup...");
            } catch (NamingException ex) {
                System.out.println(" MY EX: " + ex.getMessage());
                ex.printStackTrace();
            }
            if (emf == null) {
                // For Tomcat / Jetty
                System.out.println("Before Direct creation");
                emf = Persistence.createEntityManagerFactory("form-builder");
                System.out.println("After Direct creation");
            }
            em = emf.createEntityManager();
            System.out.println("EM = " + em + " - EMF" + emf);

        }

    }

    @GET
    @Path("/{userName}")
    public Response getUserSettings(@PathParam("userName") String userName, @Context HttpServletRequest request) {
        System.out.println("UserId " + userName);
        Settings settings = getSettingsByUserId(userName);
        return Response.ok(settings, MediaType.APPLICATION_XML).build();
    }

    private Settings getSettingsByUserId(String userName) {
        Query query = em.createNamedQuery("GetSettingsByUser");
        query.setParameter("userId", userName);
        Settings settings = null;
        try {
            settings = (Settings) query.getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("Creating Settings for user =" + userName);
            settings = new Settings(userName);

            em.persist(settings);

        }
        return settings;
    }

    @POST
    @Consumes("text/plain")
    @Path("/{userName}")
    public Response applySettings(String settings, @PathParam("userName") String userName, @Context HttpServletRequest request) {
        try {
            System.out.println("Persisting Settings -> " + settings);
            JAXBContext context = JAXBContext.newInstance(Settings.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Settings object = (Settings) unmarshaller.unmarshal(new ByteArrayInputStream(settings.getBytes()));
            System.out.println("Persisting Settings unmarshalled-> " + object);
            em.persist(object);
            CommonGlobals.getInstance().setSettings(object);
        } catch (JAXBException ex) {
            Logger.getLogger(RESTSettingsService.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
        return Response.ok().build();
    }
}
