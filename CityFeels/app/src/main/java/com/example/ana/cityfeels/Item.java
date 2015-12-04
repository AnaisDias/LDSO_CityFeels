package com.example.ana.cityfeels;

public class Item<T, R>
{

	private R key;
	private T value;

	public Item(T value, R key)
	{
		this.key = key;
		this.value = value;
	}

	public R getKey()
	{
		return this.key;
	}

	public T getValue()
	{
		return this.value;
	}

	@Override
	public String toString()
	{
		return this.key.toString();
	}

}
