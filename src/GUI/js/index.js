function activate_template(destination, template_id)
{
	var template = document.getElementById(template_id);
	var clone = document.importNode(template.content, true);
	destination.appendChild(clone);
}

var list = document.getElementById("lista");

for(var i = 0; i < 20; i++)
{
	activate_template(list, "product_wrapper_template");
}