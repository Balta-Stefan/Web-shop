/*
	All buttons in the side panel will receive event listeners.They will determine whether a new request will be sent
	(in the case when the side panel holds categories) or whether filter values will drop down (if the side panel holds filters).
	These elements will be inserted as HTML code where buttons will contain onaction attribute.
*/


/*
	Categories history object format:
	{
		type: pageType.CATEGORIES,
		content:
		{
			// copy of the DOM subtree that contains categories
		}
	}
	
	Filter values history object format:
	{
		type: pageType.FILTERS,
		content:
		{
			DOM_copy: xxx, 
			selected_filters: // list of IDs that correspond to selected checkboxes, this will have to be checked and manually selected in the interface
		}
	}
	
	Products list history object format:
	{
		type: pageType.PRODUCTS_LIST,
		content:
		{
			products_list_DOM_copy: xxx, // copy of the DOM tree that contains all the products
			filters_DOM_copy, 
			selected_filters,
		}
	}
	
	Product info history object format:
	{
		type: pageType.PRODUCT_INFO,
		content:
		{
			// copy of the DOM tree that contains the product info
		}
	}
*/

const pageType =
{
	CATEGORIES: 1,
	FILTERS: 2,
	PRODUCTS_LIST: 3,
	PRODUCT_INFO: 4,
	SHOPPING_CART: 5
};
Object.freeze(pageType);
var currentPageType = null;

const filter_wrapper_data_attribute_name = "data-paragraph_ID";
const category_data_ID = "data-category_ID";
const category_data_name = "data-category_name";
const product_data_ID = "data-product-ID";
const shopping_cart_data_remove_ID = "data-product-cart-removal-button-ID";

const currencySymbol = " KM";
const massUnit = " kg";
const URLprefix = "v1/";
const chosenCategoryHeader = document.getElementById("chosen_category_name");
const JSON_headers = 
{
	"Content-Type": "application/json",
	"Accept": "application/json"
};

var my_email = null;
var ownID = null;

//var main_content = document.getElementById("sidenav_and_main_content_wrapper");

var sidePanel = document.getElementById("sidenav");
var products_list = document.getElementById("product_list");
var login_button = document.getElementById("login_button");
var shopping_cart_button = document.getElementById("shopping_cart_button");
var articles_panel = document.getElementById("products_list_wrapper");
var other_content_panel = document.getElementById("other_content");
var main_content_div = document.getElementById("main_content");


shopping_cart_button.addEventListener("click", activate_shopping_cart_panel);
login_button.addEventListener("click", activate_login_panel);

// handler for the back button in the browser
window.onpopstate = function(event)
{
	switch_to_last_page(event.state);
}
function check_filter_checkboxes(list_of_checked_IDs)
{
	for(var i = 0; i < list_of_checked_IDs.length; i++)
	{
		var tempCheckbox = document.getElementById(list_of_checked_IDs[i]);
		tempCheckbox.checked = true;
	}
}


function reset_categories(content)
{
	sidePanel.innerHTML = content;
}

function reset_filters(content)
{
	sidePanel.innerHTML = content.DOM_copy;
	check_filter_checkboxes(content.selected_filters);
}

function reset_products_list(content)
{
	console.log("am resetting products list");
	products_list.innerHTML = content.products_list_DOM_copy;
	sidePanel.innerHTML = content.filters_DOM_copy;
	check_filter_checkboxes(content.selected_filters);
}

function reset_product_info(content)
{
	products_list.innerHTML = content;
}

function reset_shopping_cart(content)
{
	other_content_panel.innerHTML = content;
}

function switch_to_last_page(stateObject)
{
	if(stateObject == null)
		return;
	
	// clear wrappers
	removeChildren(sidePanel);
	removeChildren(other_content_panel);
	removeChildren(products_list);
	
	console.log("going back, the object is: ");
	console.log(stateObject);
	
	switch(stateObject.type)
	{
		case pageType.CATEGORIES:
			reset_categories(stateObject.contents);
			break;
		case pageType.FILTERS:
			reset_filters(stateObject.contents);
			break;
		case pageType.PRODUCTS_LIST:
			reset_products_list(stateObject.contents);
			break;
		case pageType.PRODUCT_INFO:
			reset_product_info(stateObject.contents)
			break;
		case pageType.SHOPPING_CART:
			reset_shopping_cart(stateObject.contents);
			break;
	}
	currentPageType = stateObject.type;
}

function save_page()
{
	var historyObject = {type: currentPageType, contents: null};
	
	switch(historyObject.type)
	{
		case pageType.FILTERS:
			historyObject.contents = {};
			historyObject.contents.selected_filters = getSelectedCheckboxes();
			historyObject.contents.DOM_copy = sidePanel.innerHTML;
			break;
		
		case pageType.CATEGORIES:
			historyObject.contents = sidePanel.innerHTML;
			break;
		case pageType.PRODUCTS_LIST:
			historyObject.contents = {};
			
			historyObject.contents.products_list_DOM_copy = products_list.innerHTML;
			historyObject.contents.filters_DOM_copy = sidePanel.innerHTML;
			historyObject.contents.selected_filters = getSelectedCheckboxes();
			break;
		case pageType.PRODUCT_INFO:
			historyObject.contents = products_list.innerHTML;
			break;
		case pageType.SHOPPING_CART:
			historyObject.contents = other_content_panel.innerHTML
			break;
	}
	console.log(historyObject);
	history.pushState(historyObject, null, null);
	history.pushState(null, null, null); // the last element is ignored
}

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

async function examineProduct(productID)
{
	save_page();
	currentPageType = pageType.PRODUCT_INFO;
	
	// clear the side panel
	removeChildren(sidePanel);
	
	
	var URL = URLprefix + "product/" + productID;
	
	var response = await make_request(URL, "GET", JSON_headers, null);
	if(!response.ok)
	{
		alert("Error");
		return;
	}
	
	var responseJSON = await response.json();
	
	
	
	removeChildren(products_list);
	activate_template(products_list, "examine_product_template");
	
	var thumbnail = document.getElementById("examine_product_thumbnail");
	thumbnail.src = responseJSON.thumbnail;
	var product_name = document.getElementById("other_product_info_wrapper-product_name");
	product_name.innerHTML = responseJSON.name;
	var product_description = document.getElementById("other_product_info_wrapper-product_description");
	product_description.innerHTML = responseJSON.description;
	var product_manufacturer = document.getElementById("other_product_info_wrapper-product_manufacturer");
	product_manufacturer.innerHTML += responseJSON.manufacturer;
	var product_mass = document.getElementById("other_product_info_wrapper-product_mass");
	product_mass.innerHTML += responseJSON.mass + massUnit;
	var product_price = document.getElementById("other_product_info_wrapper-product_price");
	product_price.innerHTML += responseJSON.price + currencySymbol;
	var product_warranty = document.getElementById("other_product_info_wrapper-product_warranty");
	product_warranty.innerHTML += responseJSON.warranty_months + " months";
	
	var buy_button = document.getElementById("buy_product_button");
	
	
	var product_ID = responseJSON.product_ID;
	buy_button.addEventListener("click", async function()
	{
		var quantity = document.getElementById("buy_product_amount_input").value;
		// add the product to the customer's cart
		var URL = URLprefix + "buy/" + product_ID;
		var body = {"ID": product_ID, "quantity": quantity, "customer_email": my_email}
	
		var response = await make_request(URL, "PUT", JSON_headers, JSON.stringify(body));
		
		if(!response.ok)
		{
			alert("Error");
			return;
		}
		
		alert("Product added to cart");
	});
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
	save_page();
	currentPageType = pageType.PRODUCTS_LIST;
	
	
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
		/*productLink.addEventListener("click", async function()
		{
			examineProduct(temp);
		});*/
		
		productThumbnail.src = products[i].thumbnail;
		productName.innerHTML = products[i].name;
		productPrice.innerHTML = products[i].price + currencySymbol;
		
		productLink.id = products[i].ID + "-link";
		productThumbnail.id = products[i].ID + "-thumbnail";
		productName.id = products[i].ID + "-name";
		productPrice.id = products[i].ID + "-price";
		
		productThumbnail.setAttribute(product_data_ID, temp);
	}
}

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
	
	var response = await make_request(URL, "GET", JSON_headers, null);
	if(!response.ok)
	{
		alert("Error");
		return;
	}
	
	var response_JSON = await response.json();
	
	
	chosenCategoryHeader.innerHTML = categoryName;
	
	save_page();
	if(response_JSON.type === "categories")
	{
		currentPageType = pageType.CATEGORIES;
		
		addCategories(response_JSON.contents);
	}
	else if(response_JSON.type === "filters")
	{
		currentPageType = pageType.FILTERS;
		
		addFilters(response_JSON.contents);
	}
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
		var element = document.createElement('p'); 
		  
		// Create the text node for anchor element.
		var link = document.createTextNode(tempName);
		  
		// Append the text node to anchor element.
		element.appendChild(link); 
		  
		// Set the title.
		element.title = tempName; 
		
		element.setAttribute(category_data_ID, tempID);
		element.setAttribute(category_data_name, tempName);
		
		element.className = "category_class"
		  
		// Set the href property.
		//a.href = "#"; 
		  
		// Append the anchor element to the body.
		sidePanel.appendChild(element); 
		
		
		/*element.addEventListener("click", function()
		{
			selectCategory(tempID, tempName);
		});*/
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
	
	const filter_values_wrapper_IDprefix = "filter-values-wrapper-";
	const filter_link_ID_prefix = "filter-link-";
	const filter_label_ID_prefix = "filter-label-";
	
	
	var receivedFilters = filters.categoryFilters;
	
	// remove all the elements from the side panel
	removeChildren(sidePanel);
	activate_template(sidePanel, "filters_template");
	var categoryButtonWrapper = document.getElementById("category_button_wrapper");
	
	// when selecting a filter paragraph, event will bubble to this event listener
	/*categoryButtonWrapper.addEventListener("click", function(e)
	{
		if(e.target && e.target.matches("p.filter_paragraph_class"))
		{
			console.log(e.target);
			var selectedID = e.target.getAttribute(filter_wrapper_data_attribute_name);
			console.log("selected ID: " + selectedID);
			hide_or_display_category(selectedID);
			
			e.stopPropagation();
		}
	});*/
	
	
	var single_filter_template = document.getElementById("single_filter_template");
	var filter_value_template = document.getElementById("filter_value_template");
	
	
	for(var i = 0; i < receivedFilters.length; i++)
	{
		var single_filter_template_clone = document.importNode(single_filter_template.content, true);
		categoryButtonWrapper.appendChild(single_filter_template_clone);
		var filter_link = document.getElementById("filter_link");
		filter_link.id = filter_link_ID_prefix + receivedFilters[i].filter_ID;
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
			filterLabel.id = filter_label_ID_prefix + filter_values[j].ID;
			filterLabel.innerHTML = filter_values[j].name;
		}
		
		filter_values_div.id =  filter_values_wrapper_IDprefix + receivedFilters[i].filter_ID;
		let tempID = filter_values_div.id;
		
		filter_link.setAttribute(filter_wrapper_data_attribute_name, tempID);
		/*filter_link.addEventListener("click", function()
		{
			hide_or_display_category(tempID);
		});*/
		
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
	
	
	return array;
}

async function get_main_categories()
{
	var URL =  URLprefix + "category";
	var response = await make_request(URL, "GET", JSON_headers, null);
	
	if(!response.ok)
		return null;
	
	return await response.json();
}

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
		
		
		var response = await make_request(URL, "POST", JSON_headers, JSON.stringify(tmp));
		
		if(!response.ok)
		{
			alert("Registration unsuccessful");
			return;
		}

		
		alert("Registration successful");
	});
}

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
		
		my_email = JSON.parse(formData_JSON).email;

		
		var URL =  URLprefix + "customers/login";
		var response = await make_request(URL, "POST", JSON_headers, formData_JSON);
		
		
		if(!response.ok)
		{
			alert("Error");
			return;
		}
		
		var responseJSON = await response.json();
		
		ownID = responseJSON.ID;
		
		// login successful
		other_content_panel.style.display = "none";
		articles_panel.style.display = "block"; // this might be incorrect, try flex if block doesn't work
		
		var login_panel = document.getElementById("login_panel");
		login_panel.style.display = "none";
		
		var main_categories = await get_main_categories();
		if(main_categories == null)
		{
			alert("Error");
			return;
		}
		
		addCategories(main_categories);
		currentPageType = pageType.CATEGORIES;
		//save_page();
		
		login_button.style.display = "none";
		shopping_cart_button.style.display = "block";
		
		// register event listeners (all the events will bubble up to them and they will determine if they should handle the event)
		
		// for showing/hiding filter values
		sidePanel.addEventListener("click", function(e)
		{
			if(e.target && e.target.matches("p.filter_paragraph_class"))
			{
				var selectedID = e.target.getAttribute(filter_wrapper_data_attribute_name);
				hide_or_display_category(selectedID);
				
				e.stopPropagation();
			}
		});
		
		// for obtaining subcategories or filters of a clicked category
		sidePanel.addEventListener("click", function(e)
		{
			if(e.target && e.target.matches("p.category_class"))
			{
				var categoryID = e.target.getAttribute(category_data_ID);
				var categoryName = e.target.getAttribute(category_data_name);
				
				selectCategory(categoryID, categoryName);
				
				e.stopPropagation();
			}
		});
		
		// for examining a product
		main_content_div.addEventListener("click", function(e)
		{
			if(e.target && e.target.matches("img.product_thumbnail_class"))
			{
				var productID = e.target.getAttribute(product_data_ID);
				
				examineProduct(productID);
				
				e.stopPropagation();
			}
		});
		
		// for buying the shopping cart
		main_content_div.addEventListener("click", function(e)
		{
			if(e.target && e.target.matches("button#buy_shopping_cart"))
			{
				buy_shopping_cart();
				
				e.stopPropagation();
			}
		});
		
		// for removing items from the shopping cart
		main_content_div.addEventListener("click", function(e)
		{
			if(e.target && e.target.matches("button.remove_item_from_shopping_cart_button"))
			{
				console.log("In remove from cart handler");
				var productID = e.target.getAttribute(shopping_cart_data_remove_ID);
				remove_item_from_shopping_cart(productID);
				
				e.stopPropagation();
			}
		});
		
	});

	var register_button = document.getElementById("register_button");
	register_button.addEventListener("click", activate_registration_panel);
}

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
	
	var URL =  URLprefix + "customers/" + ownID + "/shopping-cart";
	var response = await make_request(URL, "GET", JSON_headers, null);
	if(!response.ok)
		return null;
	
	return await response.json();
}

async function remove_item_from_shopping_cart(product_ID)
{
	var URL =  URLprefix + "customers/" + ownID + "/shopping-cart/" + product_ID;
	var tempObj = {product_ID};
	var response = await make_request(URL, "DELETE", JSON_headers, null);
	
	if(!response.ok)
	{
		alert("Error");
		return;
	}
	
	// remove the element from the list
	var item_wrapper = document.getElementById(product_ID);
	item_wrapper.remove();
}

async function buy_shopping_cart()
{
	/*
		response is an array of JSON objects of the format
		{
			ID: ordered_product_ID,
			name: true/false - when true, item ordering was successful
		}
	*/
	var URL =  URLprefix + "customers/" + ownID + "/shopping-cart/buy";
	var response = await make_request(URL, "PUT", JSON_headers, null);
	if(!response.ok)
	{
		alert("Error");
		return null;
	}
	
	var responseJSON = await response.json();
	console.log(responseJSON);
	var item_wrappers = document.getElementsByClassName("shopping_cart_item");
	
	
	var itemsToRemove = [];
	for(var j = 0; j < item_wrappers.length; j++)
	{
		for(var i = 0; i < responseJSON.length; i++)
		{
			if(item_wrappers[j].id == responseJSON[i].ID)
			{
				if(responseJSON[i].name == "true")
					itemsToRemove.push(responseJSON[i].ID);
				else
					item_wrappers[j].style.color = "red";
				
				break;
			}
		}
	}

	for(var i = 0; i < itemsToRemove.length; i++)
	{
		var item = document.getElementById(itemsToRemove[i]);
		item.parentNode.removeChild(item);
	}
}

async function activate_shopping_cart_panel()
{
	save_page();
	currentPageType = pageType.SHOPPING_CART;
	
	
	removeChildren(sidePanel);
	removeChildren(other_content);
	removeChildren(products_list);
	//product_list.style.display = "none";
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
		var item_quantity = document.getElementById("cart_item_quantity");
		
		item_thumbnail.src = shopping_cart_items[i].thumbnail;
		item_name.innerHTML += shopping_cart_items[i].name;
		item_price.innerHTML += shopping_cart_items[i].price*shopping_cart_items[i].quantity + currencySymbol;
		item_quantity.innerHTML += shopping_cart_items[i].quantity;
		let tmp = shopping_cart_items[i].product_ID;
		item_remove_button.setAttribute(shopping_cart_data_remove_ID, tmp);
		/*item_remove_button.addEventListener("click", function()
		{
			remove_item_from_shopping_cart(tmp);
		});*/
		
		item_thumbnail.id = shopping_cart_items[i].product_ID + "-thumbnail";
		item_name.id = shopping_cart_items[i].product_ID + "-name";
		item_price.id = shopping_cart_items[i].product_ID + "-price";
		item_quantity.id = shopping_cart_items[i].product_ID + "-quantity";
		item_remove_button.id = shopping_cart_items[i].product_ID + "-remove-button";
		cart_item_wrapper.id = shopping_cart_items[i].product_ID;
	}
	
	other_content_panel.style.display = "block";
	
	var buy_cart_button = document.getElementById("buy_shopping_cart");
	/*buy_cart_button.addEventListener("click", async function() 
	{
		buy_shopping_cart();
	});*/
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

