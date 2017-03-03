<html>
    <head>
        <title> <%= request.getAttribute("title") %> </title>
        <script type="text/javascript" src="autosuggest.js"></script>
        <script type="text/javascript" src="suggestions.js"></script>
        <link rel="stylesheet" type="text/css" src="autosuggest.css" />
        <script type="text/javascript">
            window.onload = function () {
                var oTextbox = new AutoSuggestControl(document.getElementById("searchbox"), new XMLSuggestions()); 
            }
        </script>
    </head>
    <body>
        <h1> <%= request.getAttribute("title") %> </h1>
        <form method = get action="search" >
            Search for items: <input type="text" name="q" size="75px" id="searchbox">
            <input type="hidden" name="skip" value = "0">
            <input type="hidden" name="howMany" value = "20">
            <input type="submit" /> <br /> 
        </form>
    </body>
</html>
