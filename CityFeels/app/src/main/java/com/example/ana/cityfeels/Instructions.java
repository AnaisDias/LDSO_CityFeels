package com.example.ana.cityfeels;

public class Instructions
{

	private static String[] INFO_ORIENTATION;
	private static String INFO_ORIENTATION_DEFAULT;

	private String text;
	private int orientation;

	static
	{
		INFO_ORIENTATION_DEFAULT = "Perto de si";
		INFO_ORIENTATION = new String[4];
		INFO_ORIENTATION[0] = "À sua frente";
		INFO_ORIENTATION[1] = "À sua direita";
		INFO_ORIENTATION[2] = "Atrás de si";
		INFO_ORIENTATION[3] = "À sua esquerda";
	}

	public Instructions(String text, int orientation)
	{
		this.text = text;
		this.orientation = orientation;
	}

	public String applyOrientation(Float currentOrientation)
	{
		if(this.text == null)
			return null;

		if(currentOrientation != null)
		{
			int index = (int) (((this.orientation - currentOrientation + 360) % 360) / 90);
			return this.text.replace("[ori]", INFO_ORIENTATION[index]);
		}
		else
			return this.text.replace("[ori]", INFO_ORIENTATION_DEFAULT);
	}

}
