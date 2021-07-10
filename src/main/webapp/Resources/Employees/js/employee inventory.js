async function getInventories()
{
	//make_request(URL, method, headers, body_content)
	var URL = "./inventory";
	
	var response = await make_request(URL, "GET", JSON_headers, null);
	
	if(!response.ok)
	{
		alert("Error, cannot obtain the list of inventories.");
		return;
	}
	
	var responseJSON = await response.json();
	
	console.log(responseJSON);
	
	var select = document.getElementById("inventories_select");
	
	for(var i = 0; i < responseJSON.length; i++)
	{
		var newOption = '<option value="' + responseJSON[i].inventory_ID + '">' + responseJSON[i].inventory_ID + '</option>';
		select.innerHTML += newOption;
	}
	
					
					
					
	//var order_received = document.getElementById("");
	var ordered_amount = document.getElementById("ordered_amount");
	var available_amount = document.getElementById("available_amount");
	var delivery_timestamp = document.getElementById("delivery_timestamp");
	var price = document.getElementById("price");
	var warehouse_ID = document.getElementById("warehouse_ID");
	var suppliers_price = document.getElementById("suppliers_price");
	var product_ID = document.getElementById("product_ID");
	var supplier_ID = document.getElementById("supplier_ID");
	var expiration_date = document.getElementById("expiration_date");
	
	select.addEventListener("change", async function(event)
	{
		var selectedID = event.target.value;
		
		var URL = "./inventory/" + selectedID;
		var products_data_response = await make_request(URL, "GET", JSON_headers, null);
		
		if(!products_data_response.ok)
		{
			alert("Error encountered while fetching the data.");
			return;
		}
		
		var inventory_data = await products_data_response.json();
		
		ordered_amount.value = inventory_data.amount;
		price.value = inventory_data.price;
		delivery_timestamp.value = inventory_data.delivered_at;
		available_amount.value = inventory_data.available_amount;
		warehouse_ID.value = inventory_data.stored_at;
		suppliers_price.value = inventory_data.suppliers_price;
		product_ID.value = inventory_data.product_ID;
		supplier_ID.value = inventory_data.supplier_ID;
		expiration_date.value = inventory_data.expiration_date;
	});
}



getInventories();