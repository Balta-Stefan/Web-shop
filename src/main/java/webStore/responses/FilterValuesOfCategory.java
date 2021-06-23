package webStore.responses;

import java.util.ArrayList;
import java.util.List;


public class FilterValuesOfCategory
{
	// holds different filters
	
	public class ConcreteFilter
	{
		// holds values of a filter
		
		public int filter_ID;
		public String filter_type_name;
		public List<ID_string_pair> filter_values = new ArrayList<>();
		public ConcreteFilter(int filter_ID, String filter_type_name)
		{
			this.filter_ID = filter_ID;
			this.filter_type_name = filter_type_name;
		}
	}
	
	public List<ConcreteFilter> categoryFilters = new ArrayList<>();
	
	// determines whether a new filter needs to be added or if it already exists
	public void addFilterOrValue(int filter_value_ID, int filter_ID, String filter_name, String filter_value)
	{
		ID_string_pair newFilterValue = new ID_string_pair(filter_value_ID, filter_value);
		for(ConcreteFilter cf : categoryFilters)
		{
			if(cf.filter_ID == filter_ID)
			{
				// filter already exists, add the passed value to it
				cf.filter_values.add(newFilterValue);
				return;
			}
		}
		
		// given filter doesn't exist, add it and the given value
		ConcreteFilter newFilter = new ConcreteFilter(filter_ID, filter_name);
		newFilter.filter_values.add(newFilterValue);
		categoryFilters.add(newFilter);
	}
}
