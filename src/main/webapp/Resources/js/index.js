/*
	All buttons in the side panel will receive event listeners.They will determine whether a new request will be sent
	(in the case when the side panel holds categories) or whether filter values will drop down (if the side panel holds filters).
	These elements will be inserted as HTML code where buttons will contain onaction attribute.
*/

const currencySymbol = "KM";
const URLprefix = "v1/";
const chosenCategoryHeader = document.getElementById("chosen_category_name");
const JSON_headers = 
{
	"Content-Type": "application/json",
	"Accept": "application/json"
};

var sidePanel = document.getElementById("sidenav");
var product_list = document.getElementById("product_list");
var login_button = document.getElementById("login_button");
var shopping_cart_button = document.getElementById("shopping_cart_button");
var articles_panel = document.getElementById("products_list");
var other_content_panel = document.getElementById("other_content");


shopping_cart_button.addEventListener("click", activate_shopping_cart_panel);
login_button.addEventListener("click", activate_login_panel);

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

function FormData_to_JSON(formData_object)
{
	// returns a string
	
	const plainFormData = Object.fromEntries(formData_object.entries());
	const formDataJSON = JSON.stringify(plainFormData);
	return formDataJSON;
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

// not tested
async function examineProduct(productID)
{
	console.log("inside examine product");
	var URL = URLprefix + "product/" + productID;
	console.log(URL);
	var response = await make_request(URL, "GET", JSON_headers, null);
	if(!response.ok)
	{
		alert("Error");
		return;
	}
	
	var responseJSON = await response.json();
	console.log(JSON.stringify(responseJSON));
	
	// to do
	
}
// not tested
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
		var productLink = document.getElementById("product_link");
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

// not tested
async function sendFilters()
{
	var selectedFilters = getSelectedCheckboxes();
	
	// response contains an array of products
	
	
	var URL = URLprefix + "filter-product";
	var body = [];
	
	// convert array of integers into array of JSON objects of the form {ID: filter_ID}
	for(var i = 0; i < selectedFilters.length; i++)
	{
		var tempObject = {ID: selectedFilters[i]}
		body.push(tempObject);
	}
	console.log(JSON.stringify(body));
	var response = await make_request(URL, "PUT", JSON_headers, JSON.stringify(body));
	
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
			type: responseType, // can be "categories" or "filters"
			contents: {...}
		}
	*/
	var URL = URLprefix + "category/" + categoryID;
	console.log(URL);
	var response = await make_request(URL, "GET", JSON_headers, null);
	if(!response.ok)
	{
		alert("Error");
		return;
	}
	
	var response_JSON = await response.json();
	console.log(response_JSON);
	
	chosenCategoryHeader.innerHTML = categoryName;
	
	if(response_JSON.type === "categories")
		addCategories(response_JSON.contents);
	else if(response_JSON.type === "filters")
		addFilters(response_JSON.contents);
	else
		alert("Error");
}

function addCategories(categories)
{
	/*
		passed parameter is a JSON array whose elements have the format:
		{
			ID: xxx,
			name: xxx
		}
	*/
	
	console.log("adding categories: " + categories);
	if(categories == null)
		return;
	// adds new categories into the side panel.The argument is a JSON array.
	// format of a JSON object in the array: {ID: categoryID, name: categoryName}
	
	// remove all the elements from the side panel
	removeChildren(sidePanel);
	
	// add the new categories
	for(var i = 0; i < categories.length; i++)
	{
		//var tempStr = '<a href="#" onclick="selectCategory(' + categories[i].ID + ',"' + categories[i].name + '"' + ')">' + categories[i].name + '</a>';
		//var tempStr = '<a href="#" id="' + categories[i].ID + '">' + categories[i].name + '</a>'
		//sidePanel.innerHTML += tempStr;
		
		let tempID = categories[i].ID;
		let tempName = categories[i].name
		
		// Create anchor element.
		var a = document.createElement('p'); 
		  
		// Create the text node for anchor element.
		var link = document.createTextNode(tempName);
		  
		// Append the text node to anchor element.
		a.appendChild(link); 
		  
		// Set the title.
		a.title = tempName; 
		  
		// Set the href property.
		//a.href = "#"; 
		  
		// Append the anchor element to the body.
		sidePanel.appendChild(a); 
		
		
		a.addEventListener("click", function()
		{
			selectCategory(tempID, tempName);
		});
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
	/* 
		received parameter is of the following format:
		{
			categoryFilters:
			[
				{
					filter_ID: xxx,
					filter_type_name: xxx,
					filter_values:
					[
						{
							ID: xxx,
							name: xxx
						},...
					]
				},...
			]
		}
	*/
	
	/*
		procedure:
			1)activate filters_template
			2)activate single_filter_template for every filter type
			3)for every filter type, activate as many filter_value_template templates as needed (they hold checkbox and filter value)
	*/
	
	var receivedFilters = filters.categoryFilters;
	
	// remove all the elements from the side panel
	removeChildren(sidePanel);
	activate_template(sidePanel, "filters_template");
	var categoryButtonWrapper = document.getElementById("category_button_wrapper");
	
	
	var single_filter_template = document.getElementById("single_filter_template");
	var filter_value_template = document.getElementById("filter_value_template");
	
	
	for(var i = 0; i < receivedFilters.length; i++)
	{
		var single_filter_template_clone = document.importNode(single_filter_template.content, true);
		categoryButtonWrapper.appendChild(single_filter_template_clone);
		var filter_link = document.getElementById("filter_link");
		filter_link.id = "filter-link-" + receivedFilters[i].filter_ID;
		filter_link.innerHTML = receivedFilters[i].filter_type_name;
		
		var filter_values_div = document.getElementById("filter_values");

		
		
		var filter_values = receivedFilters[i].filter_values;
		for(var j = 0; j < filter_values.length; j++)
		{
			var filter_value_template_clone = document.importNode(filter_value_template.content, true);
			filter_values_div.appendChild(filter_value_template_clone);
			
			var checkbox = document.getElementById("filter_checkbox");
			checkbox.id = filter_values[j].ID;
			checkbox.value = filter_values[j].ID;
			
			var filterLabel = document.getElementById("filter_checkbox_label");
			filterLabel.htmlFor  = checkbox.id;
			filterLabel.id = "filter-label-" + filter_values[j].ID;
			filterLabel.innerHTML = filter_values[j].name;
		}
		
		filter_values_div.id =  "filter-values-wrapper-" + receivedFilters[i].filter_ID;
		let tempID = filter_values_div.id;
		filter_link.addEventListener("click", function()
		{
			hide_or_display_category(tempID);
		});
		
		//link.addEventListener("click", function(){hide_or_display_category(tmp);}, false);
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
	
	console.log("selected checkboxes:");
	console.log(array);
	return array;
}

// not tested
async function get_main_categories()
{
	var URL =  URLprefix + "category";
	var response = await make_request(URL, "GET", JSON_headers, null);
	
	if(!response.ok)
		return null;
	
	return await response.json();
}

// not tested
function activate_registration_panel()
{
	removeChildren(other_content_panel);
	
	activate_template(other_content_panel, "register_panel");
	
	const registerForm = document.getElementById("register_form");
	registerForm.addEventListener("submit", async function(event)
	{
		event.preventDefault();
		
		var formData = new FormData(this);
		var formData_JSON = FormData_to_JSON(formData);
		
		var tmp = JSON.parse(formData_JSON);
		if(tmp.password != tmp.password_confirm)
		{
			alert("Passwords don't match!");
			return;
		}
		
		// remove the password_confirm field
		delete tmp.password_confirm;
		
		//make_request(URL, method, headers, body_content)
		var URL = URLprefix + "customers";
		console.log(URL);
		console.log(JSON.stringify(tmp));
		
		var response = await make_request(URL, "POST", JSON_headers, JSON.stringify(tmp));
		
		if(!response.ok)
		{
			alert("Registration unsuccessful");
			return;
		}
		alert("Registration successful");
	});
}

// not tested
function activate_login_panel()
{
	removeChildren(other_content_panel);
	activate_template(other_content_panel, "login_and_register_template");
	other_content_panel.style.display = "block";
	
	var login_form = document.getElementById("login_form");
	login_form.addEventListener("submit", async function(event)
	{
		event.preventDefault();
		
		var formData = new FormData(this);
		var formData_JSON = FormData_to_JSON(formData);
		
		var URL =  URLprefix + "customers/login";
		var response = await make_request(URL, "POST", JSON_headers, formData_JSON);
		
		if(!response.ok)
		{
			alert("Error");
			return;
		}
		
		// login successful
		other_content_panel.style.display = "none";
		articles_panel.style.display = "block"; // this might be incorrect, try flex if block doesn't work
		var main_categories = await get_main_categories();
		if(main_categories == null)
		{
			alert("Error");
			return;
		}
		addCategories(main_categories);
		
		login_button.style.display = "none";
		shopping_cart_button.style.display = "block";
	});

	var register_button = document.getElementById("register_button");
	register_button.addEventListener("click", activate_registration_panel);
}

// not tested
async function get_shopping_cart_items()
{
	// returns a JSON array of shopping cart items
	/*
		format of an item in the shopping cart is:
		{
			ID: someID,
			name: productName,
			thumbnail: someURL,
			price: integerPrice
		}
	*/
	
	var URL =  URLprefix + "customers/shopping-cart";
	var response = await make_request(URL, "GET", JSON_headers, null);
	if(!response.ok)
		return null;
	
	return await response.json();
}

// not tested
async function remove_item_from_shopping_cart(product_ID)
{
	var URL =  URLprefix + "customers/shopping-cart";
	var tempObj = {product_ID};
	var response = await make_request(URL, "DELETE", JSON_headers, JSON.stringify(tempObj));
	
	if(!response.ok)
	{
		alert("Error");
		return;
	}
	
	// remove the element from the list
	var item_wrapper = document.getElementById(product_ID);
	item_wrapper.remove();
}

// not tested
async function activate_shopping_cart_panel()
{
	removeChildren(other_content_panel);
	activate_template(other_content_panel, "shopping_cart_panel");
	
	var shopping_cart_items = await get_shopping_cart_items();
	if(shopping_cart_items == null)
	{
		alert("Error");
		return;
	}
	
	// populate the shopping cart panel
	var articles_holder = document.getElementById("articles_in_cart_holder");
	var shopping_cart_item_template = document.getElementById("shopping_cart_item_template");
	
	for(var i = 0; i < shopping_cart_items.length; i++)
	{
		var clone = document.importNode(shopping_cart_item_template.content, true);
		articles_holder.appendChild(clone);
		
		var item_thumbnail = document.getElementById("cart_item_thumbnail");
		var item_name = document.getElementById("cart_item_name");
		var item_price = document.getElementById("cart_item_price");
		var item_remove_button = document.getElementById("cart_item_remove_button");
		var cart_item_wrapper = document.getElementById("cart_item_wrapper");
		
		item_thumbnail.src = shopping_cart_items[i].thumbnail;
		item_name.innerHTML += shopping_cart_items[i].name;
		item_price.innerHTML += shopping_cart_items[i].price + currencySymbol;
		let tmp = shopping_cart_items[i].ID;
		item_remove_button.addEventListener("click", function()
		{
			remove_item_from_shopping_cart(tmp);
		});
		
		item_thumbnail.id = shopping_cart_items[i].ID + "-thumbnail";
		item_name.id = shopping_cart_items[i].ID + "-name";
		item_price.id = shopping_cart_items[i].ID + "-price";
		item_remove_button.id = shopping_cart_items[i].ID + "-remove-button";
		cart_item_wrapper.id = shopping_cart_items[i].ID;
	}
}

/*for(var i = 0; i < 20; i++)
{
	activate_template(product_list, "product_wrapper_template");
}*/

/*var obj1 = 
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
addFilters(array);*/

