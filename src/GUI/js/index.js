/*
	All buttons in the side panel will receive event listeners.They will determine whether a new request will be sent
	(in the case when the side panel holds categories) or whether filter values will drop down (if the side panel holds filters).
	These elements will be inserted as HTML code where buttons will contain onaction attribute.
*/



function getTemplate(template_id)
{
	var template = document.getElementById(template_id);
	var clone = document.importNode(template.content, true);
	
	return clone;
}

function activate_template(destination, template_id)
{
	destination.appendChild(getTemplate(template_id));
}
function removeChildren(element)
{
	while(element.firstChild)
		element.firstChild.remove();
}

function selectCategory(categoryID)
{
	// called when a category is clicked.The argument is the ID of the chosen category.
	
	alert("i am " + categoryID);
}

function sendFilters(filters)
{
	var selectedFilters = getSelectedCheckboxes();
	
	// to do
	
	
}

function addCategories(categories)
{
	// adds new categories into the side panel.The argument is a JSON array.
	// format of a JSON object in the array: {ID: categoryID, name: categoryName}
	
	// remove all the elements from the side panel
	removeChildren(sidePanel);
	
	// add the new categories
	for(var i = 0; i < categories.length; i++)
	{
		var tempStr = '<a href="#" onclick="selectCategory(' + categories[i].ID +  ')">' + categories[i].name + '</a>';
		sidePanel.innerHTML += tempStr;
	}
}

function hide_or_display_category(categoryID)
{
	console.log(categoryID);
	var element = document.getElementById(categoryID);
	
	if (element.style.display === "none")
		element.style.display = "flex";
	else
		element.style.display = "none";
} 

function addFilters(filters)
{
	// adds new filters into the side panel.The argument is a JSON array.
	/* format of a JSON object in the array: 
		{
			name: filterName,
			values:
				{
					{
						ID: filter1_ID,
						value: filter1_value
					},
					{
						ID: filter2_ID,
						value: filter2_value
					}...
				}
		}
	*/
	
	// remove all the elements from the side panel
	removeChildren(sidePanel);
	activate_template(sidePanel, "filter_template");
	var categoryButtonWrapper = document.getElementById("category_button_wrapper");
	
	// filter_template template will be used
	for(var i = 0; i < filters.length; i++)
	{
		var valuesArray = filters[i].values;
		activate_template(categoryButtonWrapper, "filter_row_template");
		
		// add the filter name and change its HTML id
		var link = document.getElementById("filter_link");
		link.innerHTML = filters[i].name;
		link.id = filters[i].name; // when clicked, show the hidden filter values (change from "display:none" to whatever)
		
		
		var filter_values_wrapper = document.getElementById("filter_values");
		// add all the values of the filter
		for(var j = 0; j < valuesArray.length; j++)
		{
			// <input type="checkbox" value="some value">
			var tempStr = '<div class="input-paragraph-row"><input type="checkbox" value="' + valuesArray[j].ID + '">';
			tempStr += '<p>' + valuesArray[j].value + '</p></div>';
			
			filter_values_wrapper.innerHTML += tempStr;
		}
		filter_values_wrapper.id = filters[i].name + "-values";
		console.log(filter_values_wrapper.id);
		let tmp = filter_values_wrapper.id; // without using "let", all categories will point to the last one due to scoping
		link.addEventListener("click", function(){hide_or_display_category(tmp);}, false);
	}
}

function getSelectedCheckboxes()
{
	var array = [];
	var checkboxes = document.querySelectorAll('input[type=checkbox]:checked');

	for (var i = 0; i < checkboxes.length; i++)
		array.push(checkboxes[i].value);
	
	return array;
}


var sidePanel = document.getElementById("sidenav");
var list = document.getElementById("lista");

for(var i = 0; i < 20; i++)
{
	activate_template(list, "product_wrapper_template");
}

var obj1 = 
{
	name: "First filter",
	values:
		[
			{
				ID: "filter1_ID",
				value: "this is the first value"
			},
			{
				ID: "filter2_ID",
				value: "this is the second value"
			}
		]
		
}

var obj2 = 
{
	name: "Second filter",
	values:
		[
			{
				ID: "filter3_ID",
				value: "this is the third value"
			}
		]
		
}

var obj3 = 
{
	name: "Third filter",
	values:
		[
			{
				ID: "filter4_ID",
				value: "this is the fourth value"
			},
			{
				ID: "filter5_ID",
				value: "this is the fifth value"
			},
			{
				ID: "filter6_ID",
				value: "this is the sixth value"
			},
			{
				ID: "filter7_ID",
				value: "this is the seventh value"
			}
		]
}



var array = [obj1, obj2, obj3];
addFilters(array);


