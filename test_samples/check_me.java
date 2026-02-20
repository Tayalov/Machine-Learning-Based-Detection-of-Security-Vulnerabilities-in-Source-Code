String userId = request.getParameter("id");
String sql = "SELECT * FROM accounts WHERE id = " + userId;
Statement st = connection.createStatement();
ResultSet rs = st.executeQuery(sql);
