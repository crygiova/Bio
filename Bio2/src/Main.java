import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Hashtable;


/**
 * 
 */

/**
 * @author christiangiovanelli
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String P = null;
		String PP = "TGGAATTCTCGGGTGCCAAGGAACTCCAGTCACACAGTGATCTCGTATGCCGTCTTCTGCTTG";
		//P = ;
		//P = "aaaa";
		String T;
		//T = "afcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcd";
		//T = "TGGAATTCTCGGGTGCCAAGGAACTCCAGTCACACAGTGATCTCGTATGCCG";
		//T = "baaa";
		
		int trovate =0;
		
		double percent = 1;
		PrintWriter writer =IOClass.getPrinter("stat"+Double.toString(percent)+".txt"); //print all timing on a file 
		String fname = "./s_3_sequence_1M.txt";
		int numStrTot=0;//total number of strings
		int count=0;//counter contator
		int i = 0;
		int numStrNoRip=0;//number of string without the repetitions
		Hashtable<String, Integer> hashCounter = new Hashtable<String, Integer>();
		Hashtable<Integer,String> hashIndexes = new Hashtable<Integer,String>();
		Long timeIn;
		int statistics[] = new int[50];
		
		
		

		/******************** INPUT PART ********************/
		
		System.out.println("START");
		timeIn = System.nanoTime();
		try{
			FileInputStream fstream = new FileInputStream(fname);
	         DataInputStream in = new DataInputStream(fstream);
	         BufferedReader br = new BufferedReader(new InputStreamReader(in));
	         String strLine;
	         while ((strLine = br.readLine()) != null)
	         {
	        	T = strLine;
	        	/*if(hashCounter.get(strLine)!=null)
	        	{
	        		numStrTot++;
	        		hashCounter.put(strLine,hashCounter.get(strLine)+1);
	        	}
	        	else*/
	        	{ //if the first time in the hash
	        		numStrTot++;
	        		hashCounter.put(strLine, 1);//hash string -> number of istances
	        		hashIndexes.put(numStrNoRip++, strLine);//hash index ->string
	        		/*vector[count]= strLine;
	        		count++;*/
	        		////////////////////////////////////////////////////////////////////////////////////////
	        		boolean found = false, exit= false;
	    			int q = 0;
	    			count = 0;
	    			int j = 0;
	    			int jj = 0;
	    			double hhhh = percent*(strLine.length());
	    			int k = (int) hhhh;
	    			
	    			int  m = strLine.length();//
	    			int mm = strLine.length();
	    			P = PP.substring(0, mm);// cut pattern oversize
	    			int n = P.length();//length of the pattern
	    			int l = -1;
	    				while(!found && j<mm)//probably -2 with $
	    				{
	    					//System.out.println("K:"+k);
	    					jj=0;
	    					exit= false;
	    					while(!exit)
	    					{
	    						l = lce(strLine,jj,P,q);
	    						if(jj+l>=n)
	    						{
	    							exit = true;
	    							found = true;
	    						}
	    						else
	    						{	if(count<=k)
	    							{
	    								count++;
	    								jj= jj+l+1;
	    								q=q+l+1;
	    							}
	    							if(count==k+1)
	    							{
	    								j++;
	    								P = P.substring(0, P.length()-1);
	    								n = P.length();
	    								strLine = strLine.substring(1, strLine.length());
	    								m = strLine.length();
	    								q=0;
	    								count=0;
	    								double buffer = percent*(m);//cambiato mm con m
	    								k=(int)buffer;
	    								exit = true;
	    							}
	    						}
	    					}	
	    				}
	    			if(j!=mm)
	    			{
	    				//System.out.println("["+i+"]->>"+j+";Errors:"+count+"\t"+T.substring(0, j));
	    				writer.println("->>"+j+";Errors:"+count+"\t"+T.substring(0, j));
	    				trovate++;
	    				statistics[j]++;
	    			}
	    			else
	    			{
	    				//System.out.println("["+i+"]->>Not found-> 0"+"\t"+T);
	    				writer.println("->>NF-> 0"+"\t"+T);
	    				statistics[0]++;
	    			}
	        		////////////////////////////////////////////////////////////////////////////////////////
	        	}
	         }    
			 in.close();
		}
			catch (Exception e )
			{
			 System.out.println("ERROR :" + e.toString());
			}
		timeIn = System.nanoTime()-timeIn;
		writer.close();
		writer = IOClass.getPrinter("distribution"+Double.toString(percent)+".txt");
		writer.println("Percentage: "+percent);
		writer.println("Fragments founded: "+trovate);
		writer.println("DISTRIBUTION: "+trovate);
		int jj=0;
		for (i=0;i<statistics.length;i++)
		{
			writer.println("Length:"+i+"\tNum of fragments->\t"+statistics[i]);
			//System.out.println(statistics[i]+"\n");
			jj+=statistics[i];
		}
		writer.close();
		System.out.println("END--"+jj+"--");
	}
	
	public static int lce(String P,int j,String T,int q)
	{
		int count=0;
		while(j<P.length() && q<T.length() && P.charAt(j)==T.charAt(q))
		{
			count++;
			j++;
			q++;
		}
		return count;
	}

}
