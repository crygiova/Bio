package ukkonen;

public class StrInt {
	private String stringa;
	private int value=-1;
	
	public StrInt(String stringa,int value)
	{
		this.value=value;
		this.stringa=stringa;
	}
	
	public StrInt(String stringa)
	{

		this.stringa=stringa;
		this.value=-1;
	}
	
	public StrInt(int value)
	{
		this.value=value;
		this.stringa="";
	}
	
	public void setString(String s)
	{
		this.stringa=s;
	}

	public String getString()
	{
		return this.stringa;
	}

	public int getValue()
	{
		return this.value;
	}

	public void setValue(int v)
	{
		this.value=v;
	}
	
	public boolean hasValue()
	{
		if(value<0)
		{
			return false;
		}
		return true;
	}
	
	public boolean hasStringa()
	{
		if(stringa.compareTo("")==0)
		{
			return false;
		}
		return true;
	}

}
