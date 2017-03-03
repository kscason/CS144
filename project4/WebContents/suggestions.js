
/**
 * Provides suggestions for state names (USA).
 * @class
 * @scope public
 */
function XMLSuggestions() {
}

/**
 * Request suggestions for the given autosuggest control. 
 * @scope protected
 * @param oAutoSuggestControl The autosuggest control to provide suggestions for.
 */
XMLSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl /*:AutoSuggestControl*/,
                                                          bTypeAhead /*:boolean*/) {
    var aSuggestions = [];
    var sTextboxValue = encodeURIComponent(oAutoSuggestControl.textbox.value);
    
    if (sTextboxValue.length > 0){
    
        //Create a request with the query to send to /eBay/suggest so ProxyServlet can process it
        var request = "/eBay/suggest?q=" + sTextboxValue;
        var xhr = new XMLHttpRequest();

        //Whenever the readyState attribute changes
        xhr.onreadystatechange = function() {
            //If client is done, get the XML suggestions
            if (xhr.readyState == XMLHttpRequest.DONE) {
                //Get suggestion items and stick it in the array to pass to the control
                var xmlSuggestions = xhr.responseXML.getElementsByTagName("suggestion");
                
                for (var i=0; i < xmlSuggestions.length; i++) { 
                    var item = xmlSuggestions[i].getAttribute("data");
                    aSuggestions.push(item);
                }
                //We are not typing ahead because it's annoying... and suggests autocomplete (ew)
                oAutoSuggestControl.autosuggest(aSuggestions, false);
            }
        };
        //Send http request with query to /suggest
        xhr.open('GET', request, true);
        xhr.send(null);
    }

    //provide suggestions to the control
    oAutoSuggestControl.autosuggest(aSuggestions, bTypeAhead);
};