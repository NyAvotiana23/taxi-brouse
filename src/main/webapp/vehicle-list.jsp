<%@ page import="java.util.List" %>
<%@ page import="com.mdgtaxi.entity.Vehicle" %>
<html>
<body>
<h2>Vehicles List</h2>
<table border="1">
    <tr><th>ID</th><th>Model</th><th>Plate</th></tr>
    <%
        List<Vehicle> vehicles = (List<Vehicle>) request.getAttribute("vehicles");
        for (Vehicle vehicle : vehicles) {
    %>
    <tr><td><%= vehicle.getId() %></td><td><%= vehicle.getModel() %></td><td><%= vehicle.getPlateNumber() %></td></tr>
    <%
        }
    %>
</table>
<a href="vehicle-form.jsp">Add New Vehicle</a>
</body>
</html>