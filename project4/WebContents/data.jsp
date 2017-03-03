<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ page import="edu.ucla.cs.cs144.*" %>
<html>
    <head>
        <title> <%= request.getAttribute("title") %> </title>
    </head>
    <body onLoad="initializeMap()">
        <h1> <%= request.getAttribute("title") %> </h1>

        <x:parse xml="${data}" var="Data"/>

        <h1> Name: <x:out select="$Data/Item/Name" /> </h1>
        <h2> Seller: <x:out select="$Data/Item/Seller/@UserID" /> (<x:out select="$Data/Item/Seller/@Rating" />) </h2>
        <p>
          Currently: <x:out select="$Data/Item/Currently" /><br>
          <x:if select="$Data/Item/Buy_Price">
            Buy Price: <x:out select="$Data/Item/Buy_Price" /><br>
          </x:if>
          Description: <x:out select="$Data/Item/Description" /><br>
          Categories: <x:forEach select="$Data/Item/Category" var="c">
              <x:out select="$c" />. 
              </x:forEach>
              <br><br>
          Location: <x:out select="$Data/Item/Location" /> (<x:out select="$Data/Item/Country" />)<br>
          <x:choose>
          <x:when select="$Data/Item/Location/@Latitude">            
             (<x:out select="$Data/Item/Location/@Latitude" />, <x:out select="$Data/Item/Location/@Longitude" />)<br>
            <script type="text/javascript" 
              src="http://maps.google.com/maps/api/js?sensor=false"> 
            </script> 
            <script type="text/javascript"> 
              function initializeMap() {
                var lat = parseFloat(`<x:out select="$Data/Item/Location/@Latitude" />`);
                var lng = parseFloat(`<x:out select="$Data/Item/Location/@Longitude" />`);
                var latlng = new google.maps.LatLng(lat,lng); 
                  var myOptions = { 
                      zoom: 14, // default is 8  
                      center: latlng, 
                      mapTypeId: google.maps.MapTypeId.ROADMAP 
                  }; 
                  var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions); 
              } 
            </script>
            <div id="map_canvas" style="width:50%; height:50%"></div>
          </x:when>
          <x:otherwise>
          (No map data to display)<br>
          </x:otherwise>
          </x:choose>
          <br>
          Auction Start: <x:out select="$Data/Item/Started" /><br>
          Auction End: <x:out select="$Data/Item/Ends" /><br>
          First Bid Amount: <x:out select="$Data/Item/First_Bid" /><br>

          Number of Bids: <x:out select="$Data/Item/Number_of_Bids" /><br>
          <ul>
          <x:forEach select="$Data/Item/Bids/Bid" var="b">
            <li>
              Bidder: <x:out select="$b/Bidder/@UserID" /> (<x:out select="$b/Bidder/@Rating" />)<br>
              <x:if select="$b/Bidder/Location">
                Location: <x:out select="$b/Bidder/Location" /> <br>
              </x:if>
              <x:if select="$b/Bidder/Country">
                Country: <x:out select="$b/Bidder/Country" /> <br>
              </x:if>
              Bid at: <x:out select="$b/Time" /> <br>
              Bid amount: <x:out select="$b/Amount" /> <br><br>
            </li>
          </x:forEach>
          </ul>
          
        </p>
    </body>
</html>
