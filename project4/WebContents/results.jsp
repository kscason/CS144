<%@ page import="edu.ucla.cs.cs144.*" %>

<%
    int skipped = Integer.parseInt(request.getParameter("skip").toString());
    int returned = Integer.parseInt(request.getParameter("howMany").toString());
    String query = request.getParameter("q").toString();
%>

<html>
    <head>
        <title> <%= request.getAttribute("title") %> </title>
    </head>
    <body>
        <h1> <%= request.getAttribute("title") %> </h1>
        <form method = get action="search" >
            Search for items: <input type="text" name="q">
            <input type="hidden" name="skip" value = "0">
            <input type="hidden" name="howMany" value = "20">
            <input type="submit" /> <br /> 
        </form>
        <% SearchResult[] rs = (SearchResult[])request.getAttribute("result"); %>
        <h2> Results for "<%= request.getAttribute("search") %>" (<%= skipped+1 %> - <%= skipped+rs.length %>) </h2>
            <ul>
                <%  
                for(SearchResult r : rs) { %>
                    <li><a href="/eBay/item?id=<%= r.getItemId() %>">Item <%= r.getItemId() %></a>: <%=  r.getName() %> </li>
                <% } %>
            </ul>

        <br />
        <% if(skipped > 0){ %>
            <a href= "/eBay/search?q=<%= query %>&skip=<%= skipped-returned >= 0 ? skipped-returned : 0 %>&howMany=<%= returned%>">Previous Page</a>
        <%  } %>

        <% if(rs.length == returned){ %>
            <a href="/eBay/search?q=<%= query %>&skip=<%= skipped+returned %>&howMany=<%= returned%>">Next Page </a>
        <%  } %>
    </body>
</html>
