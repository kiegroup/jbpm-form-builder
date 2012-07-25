/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.jbpm.formapi.server.render.RendererException;
import org.jbpm.formbuilder.server.render.gwt.Renderer;

/**
 *
 * @author salaboy
 */
public class FormHostServlet extends HttpServlet {

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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Renderer renderer = new Renderer();
        Map<String, Object> params = new HashMap<String, Object>();
        String formName = (String)request.getParameter("formName");
        Object result = null;
        URL url = new URL("http://localhost:8080/jbpm-form-builder/rest/form/definitions/package/defaultPackage/id/"+formName);

        try {
            params.put(Renderer.BASE_CONTEXT_PATH, "http://localhost:8080/jbpm-form-builder");
            result = renderer.render(url, params);
        } catch (RendererException ex) {
            Logger.getLogger(FormHostServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println(result.toString());
            
//             Enumeration paramNames = request.getParameterNames();
//        while (paramNames.hasMoreElements()) {
//            String paramName = (String) paramNames.nextElement();
//            out.println("<TR><TD>" + paramName + "\n<TD>");
//            String[] paramValues = request.getParameterValues(paramName);
//            if (paramValues.length == 1) {
//                String paramValue = paramValues[0];
//                if (paramValue.length() == 0) {
//                    out.print("<I>No Value</I>");
//                } else {
//                    out.print(paramValue);
//                }
//            } else {
//                out.println("<UL>");
//                for (int i = 0; i < paramValues.length; i++) {
//                    out.println("<LI>" + paramValues[i]);
//                }
//                out.println("</UL>");
//            }
//        }
//        out.println("</TABLE>\n");
        } finally {
            out.close();
        }
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
        processRequest(request, response);
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
        processRequest(request, response);
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
}
