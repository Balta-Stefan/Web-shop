const JSON_headers = 
{
	"Content-Type": "application/json",
	"Accept": "application/json"
};


function FormData_to_JSON(formData_object)
{
	const plainFormData = Object.fromEntries(formData_object.entries());
	const formDataJSON = JSON.stringify(plainFormData);
	return formDataJSON;
}

// returns response as an object that should be converted to JSON
async function make_request(URL, method, headers, body_content)
{
	var response = await fetch(URL,
	{
		"method": method,
		"headers": headers,
		"body": body_content
	});
	
	//const json_response = await response.json();
	return response;
}

function FormData_to_JSON(formData_object)
{
	const plainFormData = Object.fromEntries(formData_object.entries());
	return plainFormData;
}