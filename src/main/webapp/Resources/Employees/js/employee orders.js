async function getOrders()
{
	//make_request(URL, method, headers, body_content)
	var URL = "./orders";
	
	var response = await make_request(URL, "GET", JSON_headers, null);
	
	if(!response.ok)
	{
		alert("Error, cannot obtain the list of orders.");
		return;
	}
	
	var responseJSON = await response.json();
	
	console.log(responseJSON);
}



getOrders();