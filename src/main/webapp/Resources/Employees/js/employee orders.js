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
	
	var select = document.getElementById("orders_select");
	
	for(var i = 0; i < responseJSON.length; i++)
	{
		var newOption = '<option value="' + responseJSON[i].order_ID + '">' + responseJSON[i].order_ID + '</option>';
		//'<option value="' + answer_JSON[i].id + '">' + "ID zahtjeva: " + answer_JSON[i].id + '</option>';
		select.innerHTML += newOption;
	}
	//var order_received = document.getElementById("");
	var inventory_ID = document.getElementById("inventory_ID_input");
	var order_received = document.getElementById("order_received_timestamp_input");
	var order_delivered_at = document.getElementById("order_delivered_timestamp_input");
	var status_ID = document.getElementById("status_ID_input");
	var ordered_by = document.getElementById("ordered_by_input");
	var reason_for_returning = document.getElementById("reason_for_returning");
	var amount = document.getElementById("amount_input");
	
	select.addEventListener("change", async function(event)
	{
		var selectedID = event.target.value;
		console.log("clicked on: " + selectedID);
		
		var URL = "./orders/" + selectedID;
		var order_data_response = await make_request(URL, "GET", JSON_headers, null);
		
		if(!order_data_response.ok)
		{
			alert("Error encountered while fetching the data.");
			return;
		}
		
		var order_data = await order_data_response.json();
		
		inventory_ID.value = order_data.inventory_ID;
		order_received.value = order_data.order_received_at;
		order_delivered_at.value = order_data.order_delivered_at;
		status_ID.value = order_data.status_ID;
		ordered_by.value = order_data.ordered_by;
		reason_for_returning.value = order_data.returned_reason;
		amount.value = order_data.amount;
	});
}



getOrders();