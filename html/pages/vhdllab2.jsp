<?xml version="1.0" encoding="utf-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="application/xhtml+xml;charset=utf-8"/>
  <link rel="stylesheet" href="stylesheet.css" type="text/css" />
  <script type="text/javascript" src="functions.js"></script>
  <title>VHDL Lab</title>
</head>
<body onunload="javascript:exitApplication();">
  <div>
  	<jsp:plugin code="hr.fer.zemris.vhdllab.applets.main.MainApplet"
  				archive="vhdllab-web-onClient.jar,commons-logging.jar,commons-collections.jar,commons-beanutils-1.7.0.jar,commons-digester-1.7.jar"
  				type="applet" width="100%" height="100%" jreversion="1.6" name="vhdllab">
  		<jsp:params>
  		  <jsp:param name="mayscript" value="true"></jsp:param>
  		  <jsp:param name="id" value="vhdllab"></jsp:param>
  		  <jsp:param name="class" value="vhdllab"></jsp:param>
  		</jsp:params>
  		<jsp:fallback>Ne valja!</jsp:fallback>
  	</jsp:plugin>
  </div>
</body>
</html>