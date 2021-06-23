package webStore.responses;

public abstract class SubcategoriesOrFilters<T>
{
	public static enum types{categories, filters}
	public T contents;
}
