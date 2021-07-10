async function getProducts()
{
	//make_request(URL, method, headers, body_content)
	var URL = "./products";
	
	var response = await make_request(URL, "GET", JSON_headers, null);
	
	if(!response.ok)
	{
		alert("Error, cannot obtain the list of products.");
		return;
	}
	
	var responseJSON = await response.json();
	
	console.log(responseJSON);
	
	var select = document.getElementById("products_select");
	
	for(var i = 0; i < responseJSON.length; i++)
	{
		var newOption = '<option value="' + responseJSON[i].product_ID + '">' + responseJSON[i].product_ID + '</option>';
		select.innerHTML += newOption;
	}
	//var order_received = document.getElementById("");
	var thumbnail = document.getElementById("thumbnail");
	var name = document.getElementById("name");
	var manufacturer = document.getElementById("manufacturer");
	var price = document.getElementById("price");
	var category_ID = document.getElementById("category_ID");
	var mass = document.getElementById("mass");
	var warranty = document.getElementById("warranty");
	var description = document.getElementById("description");
	
	
	select.addEventListener("change", async function(event)
	{
		var selectedID = event.target.value;
		
		var URL = "./products/" + selectedID;
		var products_data_response = await make_request(URL, "GET", JSON_headers, null);
		
		if(!products_data_response.ok)
		{
			alert("Error encountered while fetching the data.");
			return;
		}
		
		var products_data = await products_data_response.json();
		
		thumbnail.src = products_data.thumbnail;
		name.value = products_data.name;
		manufacturer.value = products_data.manufacturer;
		price.value = products_data.price;
		category_ID.value = products_data.category_ID;
		mass.value = products_data.mass;
		warranty.value = products_data.warranty;
		description.value = products_data.description;
	});
}



getProducts();