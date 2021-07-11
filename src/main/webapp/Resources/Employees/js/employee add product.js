var submitButton = document.getElementById("add_product_button");
submitButton.addEventListener("click", submitForm);


async function submitForm()
{
	event.preventDefault();
	
	const formData = new FormData(document.getElementById("add_product_form"));
	const formData_JSON = FormData_to_JSON(formData);
	
	
	
	formData_JSON.otherPictureURIs = formData_JSON.otherPictureURIs.split(",");
	for(var i = 0; i < formData_JSON.otherPictureURIs.length; i++)
		formData_JSON.otherPictureURIs[i] = formData_JSON.otherPictureURIs[i].trimStart();
	
	
	var URL = "../product";
	var response = await make_request(URL, "PUT", JSON_headers, JSON.stringify(formData_JSON));
	
	if(!response.ok)
	{
		alert("Error.");
		return;
	}
	alert("Success");
}
