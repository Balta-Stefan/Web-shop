var submitButton = document.getElementById("add_inventory_button");
submitButton.addEventListener("click", submitForm);

var products_select = document.getElementById("product_ID");
var suppliers_select = document.getElementById("supplier_ID");
var warehouse_select = document.getElementById("stored_at");

const amount_input = document.getElementById("amount");
const available_amount_input = document.getElementById("available_amount");
const price_input = document.getElementById("price");
const suppliers_price_input = document.getElementById("suppliers_price");



getProducts();
getSuppliers();
getWarehouses();


async function getProducts()
{
	var URL = "./product";
	var response = await make_request(URL, "GET", JSON_headers, null);

	if(!response.ok)
	{
		alert("Error while obtaining products.");
		return;
	}
	
	var data = await response.json();
	
	for(var i = 0; i < data.length; i++)
	{
		products_select.innerHTML += '<option value="' + data[i].product_ID + '">' + data[i].name + '</option';
	}
}

async function getSuppliers()
{
	var URL = "../supplier";
	var response = await make_request(URL, "GET", JSON_headers, null);

	if(!response.ok)
	{
		alert("Error while obtaining suppliers.");
		return;
	}
	
	var data = await response.json();
	
	for(var i = 0; i < data.length; i++)
	{
		suppliers_select.innerHTML += '<option value="' + data[i].ID + '">' + data[i].name + '</option';
	}
}

async function getWarehouses()
{
	var URL = "./warehouse";
	var response = await make_request(URL, "GET", JSON_headers, null);

	if(!response.ok)
	{
		alert("Error while obtaining warehouses.");
		return;
	}
	
	var data = await response.json();
	
	for(var i = 0; i < data.length; i++)
	{
		warehouse_select.innerHTML += '<option value="' + data[i].warehouse_ID + '">' + data[i].warehouse_ID + '</option';
	}
}

async function submitForm()
{
	event.preventDefault();
	
	const formData = new FormData(document.getElementById("inventory_form"));
	const formData_JSON = FormData_to_JSON(formData);
	
	
	var URL = "./inventory";
	var response = await make_request(URL, "PUT", JSON_headers, JSON.stringify(formData_JSON));
	
	if(!response.ok)
	{
		alert("Error.");
		return;
	}
	alert("Success");
	
	amount_input.value = "";
	available_amount_input.value = "";
	price_input.value = "";
	suppliers_price_input.value = "";
}
