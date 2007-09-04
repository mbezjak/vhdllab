<?xml version="1.0" encoding="utf-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.io.InputStream"%>
<%@page import="hr.fer.zemris.vhdllab.utilities.FileUtil"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.Enumeration"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="application/xhtml+xml;charset=utf-8"/>
  <link rel="stylesheet" href="stylesheet.css" type="text/css" />
  <script type="text/javascript" src="functions.js"></script>
  <title>VHDL Lab</title>
</head>
<body onunload="javascript:exitApplication();">
  <div>
  	<%
	Enumeration e = request.getHeaderNames();
	while(e.hasMoreElements()) {
		String h = (String) e.nextElement();
		System.out.println("header " + h + "=" +request.getHeader(h));
	}
	System.out.println(request.getRequestedSessionId());
	System.out.println(session.getId());
	System.out.println(request.getRequestedSessionId());
	Cookie[] cs = request.getCookies();
	if(cs != null) {
	for(Cookie c : cs) {
		System.out.println(c.getName() + "/" + c.getValue());
	}
	}


  	
		InputStream is = getServletContext().getResourceAsStream("/WEB-INF/parameters.properties");
  		Properties p = FileUtil.getProperties(is);
  	%>
  	<jsp:plugin code="hr.fer.zemris.vhdllab.applets.main.MainApplet"
  				archive="vhdllab-web-onClient.jar,commons-logging.jar,commons-collections.jar,commons-beanutils-1.7.0.jar,commons-digester-1.7.jar,commons-codec-1.3.jar,commons-httpclient-3.0.1.jar"
  				type="applet" width="100%" height="100%" jreversion="1.6" name="vhdllab">
  		<jsp:params>
  		  <jsp:param name="mayscript" value="true"></jsp:param>
  		  <jsp:param name="id" value="vhdllab"></jsp:param>
  		  <jsp:param name="class" value="vhdllab"></jsp:param>
	      <jsp:param name="vhdllab.path" value="<%=p.getProperty("vhdllab.path")%>"></jsp:param>
	      <jsp:param name="authentication.path" value="<%=p.getProperty("authentication.path")%>"></jsp:param>
	      <jsp:param name="cookie.host" value="<%=p.getProperty("cookie.host")%>"></jsp:param>
	      <jsp:param name="cookie.path" value="<%=p.getProperty("cookie.path")%>"></jsp:param>
	      <jsp:param name="session.length" value="<%=session.getMaxInactiveInterval()%>"></jsp:param>
	      <jsp:param name="sessionId" value="<%=session.getId()%>"></jsp:param>
	      <jsp:param name="userId" value="<%=request.getRemoteUser()%>"></jsp:param>
  		</jsp:params>
  		<jsp:fallback>Your brower can't display java applets!</jsp:fallback>
  	</jsp:plugin>
  </div>
</body>
</html>