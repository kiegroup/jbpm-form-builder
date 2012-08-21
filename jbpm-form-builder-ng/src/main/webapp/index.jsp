<%
	String queryString = request.getQueryString();
    String redirectURL = "org.jbpm.form.builder.ng.jBPMShowcase/jBPM.html?"+(queryString==null?"":queryString);
    response.sendRedirect(redirectURL);
%>
