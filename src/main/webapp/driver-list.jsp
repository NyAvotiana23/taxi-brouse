<%@ page import="java.util.List" %>
<%@ page import="com.mdgtaxi.entity.Driver" %>
<html>
<body>
<h2>Drivers List</h2>
<table border="1">
  <tr><th>ID</th><th>Name</th><th>License</th></tr>
  <%
    List<Driver> drivers = (List<Driver>) request.getAttribute("drivers");
    for (Driver driver : drivers) {
  %>
  <tr><td><%= driver.getId() %></td><td><%= driver.getName() %></td><td><%= driver.getLicenseNumber() %></td></tr>
  <%
    }
  %>
</table>
<a href="driver-form.jsp">Add New Driver</a>
</body>
</html>