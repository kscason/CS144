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
        </form>
    </body>
</html>
