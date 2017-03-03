<%@ page import="edu.ucla.cs.cs144.*" %>

<%
    int skipped = Integer.parseInt(request.getParameter("skip").toString());
    int returned = Integer.parseInt(request.getParameter("howMany").toString());
    String query = request.getParameter("q").toString();
%>

<html>
    <head>
        <title> <%= request.getAttribute("title") %> </title>
        <script type="text/javascript" src="autosuggest.js"></script>
        <script type="text/javascript" src="suggestions.js"></script>
        <link rel="stylesheet" type="text/css" href="autosuggest.css" />
        <script type="text/javascript">
            window.onload = function () {
                var oTextbox = new AutoSuggestControl(document.getElementById("searchbox"), new XMLSuggestions()); 
            }
        </script>
    </head>
    <body>
        <h1> <%= request.getAttribute("title") %> </h1>
        <form method = get action="search" >
            Search for items: <input type="text" name="q" size="75px"id="searchbox">
            <input type="hidden" name="skip" value = "0">
            <input type="hidden" name="howMany" value = "20">
            <input type="submit" /> <br /> 
        </form>
        <% SearchResult[] rs = (SearchResult[])request.getAttribute("result"); %>
        <h2> Results for "<%= request.getAttribute("search") %>" (<%= rs.length > 0 ? skipped+1 : 0%> - <%= skipped+rs.length %>) </h2>
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
