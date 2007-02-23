<html>
<head>
  <meta http-equiv="Content-Type" content="application/xhtml+xml;charset=utf-8"/>
  <link rel="stylesheet" href="stylesheet.css" type="text/css" />
  <script type="text/javascript" src="functions.js"></script>
  <title>VHDL Lab</title>
</head>
<body>
  <div>
    <applet archive="vhdllab-web-onClient.jar,commons-logging.jar,commons-collections.jar,commons-beanutils-1.7.0.jar,commons-digester-1.7.jar"
      code="hr.fer.zemris.vhdllab.applets.main.MainApplet"
      mayscript="mayscript"
      height="100%"
      width="100%">
      <%
        String remUser = request.getRemoteUser();
        if(remUser != null && !remUser.equals("")) { 
      %>
      <param name="userId" value="<%=remUser%>">
      <%}%>
    </applet>
  </div>
</body>
</html>