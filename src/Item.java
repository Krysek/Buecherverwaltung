/*
 * Created on 4-jun-04
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

/**
 * @author Training.XP34
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Item
{
	private String strValues[];
	private String strColumNames[];
	int numberOfInt;
	int numberOfStr;
	
	Item(int numberOfInt, int numberOfStr, String strColumNames[])
	{
		this.numberOfInt = numberOfInt;
		this.numberOfStr = numberOfStr;
		this.strColumNames = strColumNames;
	}

	
}