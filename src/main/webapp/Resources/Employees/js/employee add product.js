const categorySelect = document.getElementById("category");
const manufacturerSelect = document.getElementById("manufacturer");

const nameInput = document.getElementById("name");
const priceInput = document.getElementById("price");
const filter_value_IDsInput = document.getElementById("filter_value_IDs");
const massInput = document.getElementById("mass");
const thumbnailInput = document.getElementById("thumbnail");
const other_picturesInput = document.getElementById("other_pictures");
const warrantyInput = document.getElementById("warranty");
const descriptionTextArea = document.getElementById("description");



var submitButton = document.getElementById("add_product_button");
submitButton.addEventListener("click", submitForm);

getManufacturers();
getCategories();

async function submitForm()
{
	event.preventDefault();
	
	const formData = new FormData(document.getElementById("add_product_form"));
	const formData_JSON = FormData_to_JSON(formData);
	
	
	
	formData_JSON.otherPictureURIs = formData_JSON.otherPictureURIs.split(",");
	for(var i = 0; i < formData_JSON.otherPictureURIs.length; i++)
		formData_JSON.otherPictureURIs[i] = formData_JSON.otherPictureURIs[i].trimStart();
	
	formData_JSON.filter_value_IDs = formData_JSON.filter_value_IDs.split(",");
	for(var i = 0; i < formData_JSON.filter_value_IDs.length; i++)
		formData_JSON.filter_value_IDs[i] = formData_JSON.filter_value_IDs[i].trimStart();
	
	if(formData_JSON.filter_value_IDs == "")
		formData_JSON.filter_value_IDs = null;
	
	if(formData_JSON.price == "")
		formData_JSON.price = null;
	
	var URL = "../product";
	var response = await make_request(URL, "PUT", JSON_headers, JSON.stringify(formData_JSON));
	
	if(!response.ok)
	{
		alert("Error.");
		return;
	}
	alert("Success");
	
	nameInput.value = "";
	priceInput.value = "";
	filter_value_IDsInput.value = "";
	massInput.value = "";
	thumbnailInput.value = "";
	other_picturesInput.value = "";
	warrantyInput.value = "";
	descriptionTextArea.value = "";
}

async function getManufacturers()
{
	var URL = "../manufacturer";
	var response = await make_request(URL, "GET", JSON_headers, null);
	
	if(!response.ok)
	{
		alert("Error occured while obtaining manufacturers.");
		return;
	}
	
	var manufacturers = await response.json();
	
	for(var i = 0; i < manufacturers.length; i++)
	{
		manufacturerSelect.innerHTML += '<option value="' + manufacturers[i].manufacturer_ID + '">' + manufacturers[i].name + '</option>';
	}
}

async function getCategories()
{
	var URL = "../category";
	var response = await make_request(URL, "GET", JSON_headers, null);
	
	if(!response.ok)
	{
		alert("Error occured while obtaining categories.");
		return;
	}
	
	var categories = await response.json();
	
	for(var i = 0; i < categories.length; i++)
	{
		categorySelect.innerHTML += '<option value="' + categories[i].ID + '">' + categories[i].name + '</option>';
	}
}