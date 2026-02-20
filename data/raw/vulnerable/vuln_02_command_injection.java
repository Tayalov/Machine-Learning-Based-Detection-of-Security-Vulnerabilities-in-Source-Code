String host = request.getParameter("host");
String cmd = "ping -c 1 " + host;
Runtime.getRuntime().exec(cmd);
