var submitButton = document.getElementById("add_supplier_button");
submitButton.addEventListener("click", submitForm);

var nameInput = document.getElementById("name");
var phoneInput = document.getElementById("phone");
var emailInput = document.getElementById("email");
var websiteInput = document.getElementById("website");

const suppliersSelect = document.getElementById("suppliers_select");
suppliersSelect.addEventListener("change", selectSupplier);

getSuppliersList();

async function getSupplier(supplier_ID)
{
	var URL = "../supplier/" + supplier_ID;
	var response = await make_request(URL, "GET", JSON_headers, null);
	
	if(!response.ok)
	{
		alert("Error.");
		return;
	}
	
	var data = await response.json();
	
	nameInput.value = data.name;
	phoneInput.value = data.phone;
	emailInput.value = data.email;
	websiteInput.value = data.website;
}

async function selectSupplier()
{
	var selectedSupplierID = suppliersSelect.value;
	
	if(selectedSupplierID == -1)
	{
		nameInput.disabled = false;
		phoneInput.disabled = false;
		emailInput.disabled = false;
		websiteInput.disabled = false;
		submitButton.disabled = false;
	}
	else
	{
		nameInput.disabled = true;
		phoneInput.disabled = true;
		emailInput.disabled = true;
		websiteInput.disabled = true;
		submitButton.disabled = true;
		
		getSupplier(selectedSupplierID);
	}
}

async function getSuppliersList()
{
	var URL = "../supplier";
	var response = await make_request(URL, "GET", JSON_headers, null);

	if(!response.ok)
	{
		alert("Error.");
		return;
	}
	
	var data = await response.json();
	
	for(var i = 0; i < data.length; i++)
	{
		suppliersSelect.innerHTML += '<option value="' + data[i].ID + '">(' + data[i].ID + ')' + data[i].name + '</option>';
	}
}

async function submitForm()
{
	event.preventDefault();
	
	const formData = new FormData(document.getElementById("suppliers_form"));
	const formData_JSON = FormData_to_JSON(formData);
	
	
	var URL = "../supplier";
	var response = await make_request(URL, "PUT", JSON_headers, JSON.stringify(formData_JSON));
	
	if(!response.ok)
	{
		alert("Error.");
		return;
	}
	alert("Success");
	
	nameInput.value = "";
	phoneInput.value = "";
	emailInput.value = "";
	websiteInput.value = "";
}
