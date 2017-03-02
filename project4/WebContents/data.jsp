<%@ page import="edu.ucla.cs.cs144.*" %>
<html>
    <head>
        <title> <%= request.getAttribute("title") %> </title>
    </head>
    <body>
        <h1> <%= request.getAttribute("title") %> </h1>
        <form method = get action="item" >
            Search for Item Info: <input type="text" name="id" value="">
            <input type="submit" /> <br /> 
        </form>

        <% String id = request.getParameter("id").toString(); 
        String data = request.getParameter("data").toString(); %>
            <h2>I /said/ i wasn't going to XML dump but here we are</h2>
            <h3><%= data %></h3>
    </body>
</html>
