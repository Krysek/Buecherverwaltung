/*
 * Autor: Christian Frei
 * Datum: 09.06.2004
 */

import javax.swing.text.*;

public class JTextFieldLimit extends PlainDocument
{
	private int limit;
	private boolean digitsOnly;
	
	public JTextFieldLimit(int newLimit)
	{
		super();
		if (limit < 0){
			limit = 0;
		} else {
			limit = newLimit;
		}
		digitsOnly = false;
	}
	
	public JTextFieldLimit(int newLimit, boolean digitsOnly)
	{
		super();
		
		this.digitsOnly = digitsOnly;
		
		if (limit < 0){
			limit = 0;
		} else {
			limit = newLimit;
		}
	}
	
	public void insertString (int offset, String str, AttributeSet attr)
			throws BadLocationException
		{
			int i;
			boolean insert = true;

			if (str == null) return;
			
			if ((getLength() + str.length()) <= limit)
			{
				if(digitsOnly == true)
					for(i = 0; i < str.length(); i++)
						if(str.charAt(i) < 48 || str.charAt(i) > 57)
							insert = false;

				if (insert == true)
					super.insertString(offset, str, attr);
			}
	}
}
