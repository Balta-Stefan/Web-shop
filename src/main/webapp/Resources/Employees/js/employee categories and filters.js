const filters_type = "filters";
const subcategories_type = "categories";

const categories_and_filters_path = document.getElementById("categories_and_filters_path");

var currentlySelected = null;

async function getCategories()
{
	//make_request(URL, method, headers, body_content)
	var URL = "../category";
	
	var response = await make_request(URL, "GET", JSON_headers, null);
	
	if(!response.ok)
	{
		alert("Error, cannot obtain the list of categories.");
		return;
	}
	
	var responseJSON = await response.json();
	
	console.log(responseJSON);
	
	var select = document.getElementById("categories_select");
	var subcategories_or_filters_select = document.getElementById("subcategories_or_filters_select");
	
	for(var i = 0; i < responseJSON.length; i++)
	{
		var newOption = '<option value="' + responseJSON[i].ID + '">' + responseJSON[i].name + '</option>';
		select.innerHTML += newOption;
	}

	var type_header = document.getElementById("type_header");
	
	select.addEventListener("change", async function(event)
	{
		var options = this.getElementsByTagName("option");
		var optionHTML = options[this.selectedIndex].innerHTML;  
		
		categories_and_filters_path.innerHTML = optionHTML;
		var selectedID = event.target.value;
		modifySelect(select, selectedID);
	});
	
	subcategories_or_filters_select.addEventListener("change", async function (event)
	{
		if(currentlySelected === filters_type)
			return; // filters don't have subfilters
		
		var options = this.getElementsByTagName("option");
		var optionHTML = options[this.selectedIndex].innerHTML;  
		
		categories_and_filters_path.innerHTML += " > " + optionHTML;
		
		var selectedID = event.target.value;
		modifySelect(subcategories_or_filters_select, selectedID);
	});
}

async function modifySelect(selectElement, selectedID)
{
	var URL = "../category/" + selectedID;
	var subcategories_or_filters = await make_request(URL, "GET", JSON_headers, null);
	
	if(!subcategories_or_filters.ok)
	{
		alert("Error encountered while fetching the data.");
		return;
	}
	
	var subcategories_or_filters_data = await subcategories_or_filters.json();
	
	
	type_header.innerHTML = subcategories_or_filters_data.type;
	
	if(subcategories_or_filters_data.type == filters_type)
	{
		currentlySelected = filters_type;
		insertFiltersIntoSelect(subcategories_or_filters_select, subcategories_or_filters_data.contents);
	}
	else 
	{
		currentlySelected = subcategories_type;
		insertSubcategoriesIntoSelect(subcategories_or_filters_select, subcategories_or_filters_data.contents);
	}
}

function insertSubcategoriesIntoSelect(selectElement, data)
{
	selectElement.innerHTML = "";
	
	for(var i = 0; i < data.length; i++)
	{
		var subcategory_ID = data[i].ID;
		var subcategory_name = data[i].name;
		
		selectElement.innerHTML += '<option value="' + subcategory_ID + '">' + subcategory_name + '</option>';
	}
}

function insertFiltersIntoSelect(selectElement, data)
{
	selectElement.innerHTML = "";
	
	var contents = data.categoryFilters;
	
	for(var i = 0; i < contents.length; i++)
	{
		var filter_ID = contents[i].filter_ID;
		var filter_type_name = contents[i].filter_type_name;
		var filter_values = contents[i].filter_values;
		
		selectElement.innerHTML += '<option value="' + filter_ID + '">' + filter_type_name + '</option>';
		
		for(var j = 0; j < filter_values.length; j++)
		{
			var value_ID = filter_values[j].ID;
			var filter_name = filter_values[j].name;
			
			selectElement.innerHTML += '<option value="' + value_ID + '">' + "&nbsp&nbsp&nbsp" + filter_name + '</option>';
		}
	}
}

getCategories();