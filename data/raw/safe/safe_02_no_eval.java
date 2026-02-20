int a = Integer.parseInt(request.getParameter("a"));
int b = Integer.parseInt(request.getParameter("b"));
int result = a + b;
response.getWriter().write(String.valueOf(result));
