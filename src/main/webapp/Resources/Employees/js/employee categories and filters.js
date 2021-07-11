/*
	Handling adding of new filters or categories:
		1)Adding new subcategories
		2)Adding new filter values
*/

const filters_type = "filters";
const subcategories_type = "categories";

const categories_and_filters_path = document.getElementById("categories_and_filters_path");

var currentlySelected = null;
var path_string_to_append = null;
var selected_item_ID = null;
var parent_category_ID = null; // used for adding new categories or filters
var appendToPath = false;
var chosenSelect = null;
var selectedNewCategory = false;

const addNewCategoryButton = document.getElementById("add_new_category_button");
addNewCategoryButton.addEventListener("click", addNewCategory);


const fetchButton = document.getElementById("fetch_subcategories_or_filers_button");
fetchButton.addEventListener("click", fetch_categories_or_filters);

const addCategoryInput = document.getElementById("add_category_or_filter_input");
const parent_ID_input = document.getElementById("parent_id_input");
const add_type_select = document.getElementById("add_type_select");



// related to removal
const remove_type_select = document.getElementById("remove_type_select");
const id_to_remove_input = document.getElementById("id_to_remove_input");
const remove_selected_button = document.getElementById("remove_selected_button");
remove_selected_button.addEventListener("click", remove);

async function remove()
{
	var selectedType = remove_type_select.value;
	var chosenID = id_to_remove_input.value;
	
	if(chosenID == "")
		return;
	
	var URL = "../" + selectedType + "/" + chosenID;
	var response = await make_request(URL, "DELETE", JSON_headers, null);
	if(!response.ok)
	{
		alert("Error");
		return;
	}
	alert("Success");
	
}

async function fetch_categories_or_filters()
{
	if(selectedNewCategory == false && currentlySelected === filters_type)
		return; // filters don't have subfilters
	
	if(chosenSelect == null || selected_item_ID == null)
	{
		alert("Choose an item first.");
		return;
	}
	modifySelect(chosenSelect, selected_item_ID)
}



async function addNewCategory()
{
	var selection = add_type_select.value;
	var URL = "../" + selection;
	
	var parent_ID = parent_ID_input.value;
	if(parent_ID_input.value == "")
		parent_ID = -1;
		
	var body = {ID: parent_ID, name: addCategoryInput.value}
	
	var response = await make_request(URL, "PUT", JSON_headers, JSON.stringify(body));
	if(!response.ok)
	{
		alert("Error");
		return;
	}
	alert("Success");
}


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
	
	var main_categories_select = document.getElementById("categories_select");
	var subcategories_or_filters_select = document.getElementById("subcategories_or_filters_select");
	
	for(var i = 0; i < responseJSON.length; i++)
	{
		var newOption = '<option value="' + responseJSON[i].ID + '">' + "(" + responseJSON[i].ID + ")" + responseJSON[i].name + '</option>';
		main_categories_select.innerHTML += newOption;
	}

	var type_header = document.getElementById("type_header");
	
	main_categories_select.addEventListener("change", async function(event)
	{
		selectedNewCategory = true;
		var options = this.getElementsByTagName("option");
		var optionHTML = options[this.selectedIndex].innerHTML;  
		
		//categories_and_filters_path.innerHTML = optionHTML;
		path_string_to_append = optionHTML;
		appendToPath = false;
		chosenSelect = main_categories_select;
		
		selected_item_ID = event.target.value;
		//modifySelect(main_categories_select, selected_item_ID);
	});
	
	subcategories_or_filters_select.addEventListener("change", async function (event)
	{
		selectedNewCategory = false;
		
		selected_item_ID = event.target.value;
		if(currentlySelected === filters_type)
			return; // filters don't have subfilters
		
		var options = this.getElementsByTagName("option");
		var optionHTML = options[this.selectedIndex].innerHTML;  
		
		//categories_and_filters_path.innerHTML += " > " + optionHTML;
		path_string_to_append = " > " + optionHTML;
		appendToPath = true;
		chosenSelect = subcategories_or_filters_select;
		
		//modifySelect(subcategories_or_filters_select, selected_item_ID);
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
	
	if(appendToPath == true)
		categories_and_filters_path.innerHTML += path_string_to_append;
	else
		categories_and_filters_path.innerHTML = path_string_to_append;
	
	parent_category_ID = selected_item_ID;
	selected_item_ID = null;
	
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
		
		selectElement.innerHTML += '<option value="' + subcategory_ID + '">' + "(" + subcategory_ID + ")" + subcategory_name + '</option>';
	}
}

function insertFiltersIntoSelect(selectElement, data)
{
	selectElement.innerHTML = "";
	
	var contents = data.categoryFilters;
	console.log(data);
	for(var i = 0; i < contents.length; i++)
	{
		var filter_ID = contents[i].filter_ID;
		var filter_type_name = contents[i].filter_type_name;
		var filter_values = contents[i].filter_values;
		
		selectElement.innerHTML += '<option value="' + filter_ID + '">' + "(" + filter_ID + ")" + filter_type_name + '</option>';
		
		for(var j = 0; j < filter_values.length; j++)
		{
			if(filter_values[j] == null)
				continue;
			var value_ID = filter_values[j].ID;
			var filter_name = filter_values[j].name;
			
			selectElement.innerHTML += '<option value="' + value_ID + '">' + "&nbsp&nbsp&nbsp" + "(" + value_ID + ")" + filter_name + '</option>';
		}
	}
}



getCategories();