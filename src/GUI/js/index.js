/*
	All buttons in the side panel will receive event listeners.They will determine whether a new request will be sent
	(in the case when the side panel holds categories) or whether filter values will drop down (if the side panel holds filters).
	These elements will be inserted as HTML code where buttons will contain onaction attribute.
*/

const currencySymbol = "KM";
const URLprefix = "webshop/v1/";
const chosenCategoryHeader = document.getElementById("chosen_category_name");
const JSON_headers = 
{
	"Content-Type": "application/json",
	"Accept": "application/json"
};

var sidePanel = document.getElementById("sidenav");
var product_list = document.getElementById("product_list");



async function make_request(URL, method, headers, body_content)
{
	// body_content has to be a string
	
	// returns response as an object that has to be converted to JSON as await response.json();
	// check response status with response.ok (a boolean value)
	
	var response = await fetch(URL,
	{
		method: method,
		headers: headers,
		body: body_content
	});
	
	//const json_response = await response.json(); // converts JSON object to JSON string
	return response;
}


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

async function examineProduct(productID)
{
	var URL = URLprefix + "products/" + productID;
	var response = await make_request(URL, "GET", JSON_headers, null);
	if(!response.ok)
	{
		alert("Error");
		return;
	}
	
	// to do
	
}

function addProducts(products)
{
	/*
		a product is of the form
		{
			ID: product_ID,
			name: product_name,
			thumbnail: picture_URL,
			price: product_price_as_integer,
		}
		
		-function argument is an array of products
	*/
	removeChildren(product_list);
	var product_template = document.getElementById("product_wrapper_template");
	
	for(var i = 0; i < products.length; i++)
	{
		var template_clone = document.importNode(product_template.content, true);
		product_list.appendChild(template_clone);
		
		// populate the template instance with info
		var productLink = document.getElementById("product_image_link");
		var productThumbnail = document.getElementById("product_thumbnail");
		var productName = document.getElementById("product_name");
		var productPrice = document.getElementById("product_price");
		
		let temp = products[i].ID;
		productLink.addEventListener("click", async function()
		{
			examineProduct(temp);
		});
		
		productThumbnail.src = products[i].thumbnail;
		productName.innerHTML = products[i].name;
		productPrice.innerHTML = products[i].price + currencySymbol;
		
		productLink.id = products[i].ID + "-link";
		productThumbnail.id = products[i].ID + "-thumbnail";
		productName.id = products[i].ID + "-name";
		productPrice.id = products[i].ID + "-price";
	}
}

async function sendFilters()
{
	var selectedFilters = getSelectedCheckboxes();
	
	// to do
	
	// response contains an array of products
	
	
	var URL = URLprefix + "filter-product";
	var body = [];
	
	// convert array of integers into array of JSON objects of the form {ID: filter_ID}
	for(var i = 0; i < selectedFilters.length; i++)
	{
		var tempObject = {ID: selectedFilters[i]}
		body.push(tempObject);
	}
	
	var response = await make_request(URL, "GET", JSON_headers, JSON.stringify(body));
	
	if(!response.ok)
	{
		alert("Error");
		return;
	}
	
	var responseJSON = await response.json();
	addProducts(responseJSON);
}

async function selectCategory(categoryID, categoryName)
{
	// called when a category is clicked.The argument is the ID of the chosen category.
	
	// send a request to obtain subcategories/filters of the chosen category
	// response contains either subcategories or filters
	/* response is of the format:
		{
			type: responseType // can be "categories" or "filters"
			contents:
			{
				{
					
				},
				{
					...
				}
			}
		}
	*/
	
	var URL = URLprefix + "category/" + categoryID;
	var response = await make_request(URL, "GET", JSON_headers, null);
	if(!response.ok)
	{
		alert("Error");
		return;
	}
	
	var response_JSON = await response.json();
	
	if(response_JSON.type === "categories")
		addCategories(response_JSON.contents);
	else if(response_JSON.type === "filters")
	{
		// change chosen category header
		chosenCategoryHeader.innerHTML = categoryName;
		addFilters(response_JSON.contents);
	}
	else
		alert("Error");
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
		var tempStr = '<a href="#" onclick="selectCategory(' + categories[i].ID +  ', ' + categories[i].name + ')">' + categories[i].name + '</a>';
		sidePanel.innerHTML += tempStr;
	}
}

function hide_or_display_category(categoryID)
{
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
			var tempStr = '<div class="input-paragraph-row"><input type="checkbox" value="' + valuesArray[j].ID + '" id="' + valuesArray[j].ID + '">';
			tempStr += '<label for="' + valuesArray[j].ID + '">' + valuesArray[j].value + '</label></div>';
			
			filter_values_wrapper.innerHTML += tempStr;
		}
		filter_values_wrapper.id = filters[i].name + "-values";
		let tmp = filter_values_wrapper.id; // without using "let", all categories will point to the last one due to scoping
		link.addEventListener("click", function(){hide_or_display_category(tmp);}, false);
	}
	
	// register the button to submit chosen filters
	var filterButton = document.getElementById("send_filters_button");
	filterButton.addEventListener("click", sendFilters);
}

function getSelectedCheckboxes()
{
	var array = [];
	var checkboxes = document.querySelectorAll('input[type=checkbox]:checked');

	for (var i = 0; i < checkboxes.length; i++)
		array.push(checkboxes[i].value);
	
	return array;
}



for(var i = 0; i < 20; i++)
{
	activate_template(product_list, "product_wrapper_template");
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



