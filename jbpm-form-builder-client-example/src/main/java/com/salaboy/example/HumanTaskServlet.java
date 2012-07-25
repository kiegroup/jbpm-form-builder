/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jbpm.task.Group;
import org.jbpm.task.User;
import org.jbpm.task.service.TaskService;
import org.jbpm.task.service.TaskServiceSession;
import org.jbpm.task.service.local.LocalTaskService;
import org.drools.SystemEventListenerFactory;
import org.jbpm.task.Task;
import org.jbpm.task.service.ContentData;
import org.jbpm.task.utils.ContentMarshallerHelper;

/**
 *
 * @author salaboy
 */
public class HumanTaskServlet extends HttpServlet {

    // Create a local instance of the TaskService
    private LocalTaskService localTaskService;
    private EntityManagerFactory emf;
    private TaskService taskService;
    private TaskServiceSession taskSession;
    private Map<String, User> users = new HashMap<String, User>();
    private Map<String, Group> groups = new HashMap<String, Group>();
    private long taskId;
    public HumanTaskServlet() {
        // Create an EntityManagerFactory based on the PU configuration
        
        try {
                InitialContext ctx = new InitialContext();
                // For JBOSS 7.x
                // see: https://docs.jboss.org/author/display/AS71/JPA+Reference+Guide#JPAReferenceGuide-BindingEntityManagerFactorytoJNDI
                emf = (EntityManagerFactory) ctx.lookup("java:jboss/myTaskEntityManagerFactory");
            } catch (NamingException ex) {
                Logger.getLogger(HumanTaskServlet.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        System.out.println("EMF = "+emf);
        // The Task Service will use the EMF to store our Task Status
        taskService = new TaskService(emf, SystemEventListenerFactory.getSystemEventListener());
        // We can uset the Task Service to get an instance of the Task Session which
        // allows us to introduce to our database the users and groups information before 
        // running our tests
        taskSession = taskService.createSession();
        // Adds 1 Administrator, 2 users and 1 Group
        addUsersAndGroups(taskSession);

        //We need to set up an user to represent the user that is making the requests
        MockUserInfo userInfo = new MockUserInfo();
        taskService.setUserinfo(userInfo);
        localTaskService = new LocalTaskService(taskService);
        
        String str = "(with (new Task()) { priority = 55, taskData = (with( new TaskData()) { } ), ";
        str += "peopleAssignments = (with ( new PeopleAssignments() ) { potentialOwners = [users['salaboy' ], users['bobba'] ], }),";                        
        str += "names = [ new I18NText( 'en-UK', 'This is my task name')] })";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("users", users);
        params.put("groups", groups);
        params.put( "now", new Date() );
        Task task = (Task)TaskService.eval(str, params);
        
        localTaskService.addTask(task, new ContentData());
        
        taskId = task.getId();
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileUploadException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);

        out.println("Hi There! ");
        
        out.println("Task Id = "+taskId);
        // Parse the request
        Map<String, String> params = new HashMap<String, String>();
        List<FileItem> items = upload.parseRequest(request); 
        Iterator iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            if (item.isFormField()) {
                String name = item.getFieldName();
                String value = item.getString();
                params.put(name, value);
            }
        }
        if(params.get("action").equals("start")){
            localTaskService.start(taskId, "salaboy");
            out.println("Task Started!");
        }
        
        if(params.get("action").equals("complete")){
            ContentData data = ContentMarshallerHelper.marshal(params, null);
            localTaskService.complete(taskId, "salaboy", data);
            out.println("Task Completed!");
        }
        out.close();
    }
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (FileUploadException ex) {
            Logger.getLogger(HumanTaskServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (FileUploadException ex) {
            Logger.getLogger(HumanTaskServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void addUsersAndGroups(TaskServiceSession taskSession) {
        User user = new User("salaboy");
        User watman = new User("watman");
        taskSession.addUser(user);
        taskSession.addUser(watman);
        User administrator = new User("Administrator");
        taskSession.addUser(administrator);
        users.put("salaboy", user);
        users.put("watman", watman);
        users.put("administrator", administrator);
        Group myGroup = new Group("group1");
        taskSession.addGroup(myGroup);
        groups.put("group1", myGroup);

    }
}
