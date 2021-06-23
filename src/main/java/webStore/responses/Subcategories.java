package webStore.responses;

import java.util.List;
import java.util.ArrayList;

public class Subcategories extends SubcategoriesOrFilters<List<ID_string_pair>>
{
	public SubcategoriesOrFilters.types type = SubcategoriesOrFilters.types.categories;
	
	{
		contents = new ArrayList<>();
	}
}
