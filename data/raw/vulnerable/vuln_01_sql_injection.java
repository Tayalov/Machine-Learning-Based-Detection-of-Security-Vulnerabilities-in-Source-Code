String id = request.getParameter("id");
String sql = "SELECT * FROM users WHERE id = " + id;
Statement stmt = connection.createStatement();
ResultSet rs = stmt.executeQuery(sql);
