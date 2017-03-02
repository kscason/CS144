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
    </body>
</html>
