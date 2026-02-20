String id = request.getParameter("id");
PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
ps.setInt(1, Integer.parseInt(id));
ResultSet rs = ps.executeQuery();
