async function getWarehouses()
{
	//make_request(URL, method, headers, body_content)
	var URL = "./warehouse";
	
	var response = await make_request(URL, "GET", JSON_headers, null);
	
	if(!response.ok)
	{
		alert("Error, cannot obtain the list of warehouses.");
		return;
	}
	
	var responseJSON = await response.json();
	
	console.log(responseJSON);
	
	var select = document.getElementById("warehouses_select");
	
	for(var i = 0; i < responseJSON.length; i++)
	{
		var newOption = '<option value="' + responseJSON[i].warehouse_ID + '">' + responseJSON[i].warehouse_ID + '</option>';
		select.innerHTML += newOption;
	}
	//var order_received = document.getElementById("");
	var capacity_input = document.getElementById("capacity_input");
	var free_capacity_input = document.getElementById("free_capacity_input");
	var address_input = document.getElementById("address_input");
	var country_input = document.getElementById("country_input");
	var state_input = document.getElementById("state_input");
	
	
	select.addEventListener("change", async function(event)
	{
		var selectedID = event.target.value;
		
		var URL = "./warehouse/" + selectedID;
		var warehouse_data_response = await make_request(URL, "GET", JSON_headers, null);
		
		if(!warehouse_data_response.ok)
		{
			alert("Error encountered while fetching the data.");
			return;
		}
		
		var warehouse_data = await warehouse_data_response.json();
		
		capacity_input.value = warehouse_data.capacity;
		free_capacity_input.value = warehouse_data.free_capacity;
		address_input.value = warehouse_data.address;
		country_input.value = warehouse_data.country;
		state_input.value = warehouse_data.state;
	});
}



getWarehouses();