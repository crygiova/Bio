package ukkonen;

import java.io.*;
import java.util.*;

public class IOClass
{

	private final static String PRTNFILE = "File not found";
	private final static String PRTNREAD = "File not read";
	private final static String PRTNWRITE = "Writing nt done";
	private final static String PRTNEND ="Il file non stato chiuso correttamente";
	
	public static int []  readStr (String fname)
	 {
		int count=5;
		int i = 0;
		Hashtable<String, Integer> h = new Hashtable<String, Integer>();
		int vector[] = new int [4432461];
		try{
			FileInputStream fstream = new FileInputStream(fname);
	         DataInputStream in = new DataInputStream(fstream);
	         BufferedReader br = new BufferedReader(new InputStreamReader(in));
	         String strLine;
	         while ((strLine = br.readLine()) != null)
	         {
	        	
	        	if(h.get(strLine)!=null)
	        	{
	        		h.put(strLine,h.get(strLine)+1);
	        	}
	        	else
	        	{ //if the first time in the hash
	        		h.put(strLine, 1);
	        		int j=0;
	        		int number=0;
	        		for (j=0;j<strLine.length();j++)
	        		{
	        			switch(strLine.charAt(j)){
	        				case 'A':
	        					number= 1;
	        					break;
	        				case 'C':
        						number= 2;
	        					break;
	        				case 'G':
        						number= 3;
	        					break;
	        				case 'T':
        						number= 4;
	        					break;
	        			
	        			}
	        			vector[j+i]=number;
	        		}
	        		vector[j+i]= count;
	        		count++;
	        		i+=strLine.length()+1;
	        	}
	         }    
			 in.close();
		}
			catch (FileNotFoundException excNotFound)
					{
					 System.out.println(PRTNFILE + fname);
					}
				 catch (IOException excLettura)
					{
					 System.out.println(PRTNREAD + fname);
					}
			//saveObj("./ciao.txt",h.toString());
			 return vector;
		//return k;
	 } 
	public static void writeOn(String fname,int result[]) throws IOException
	{
		BufferedWriter wrt = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname)));
		for (int i=0;i<result.length;i++)
		{
			
			/* VECTOR value*/
			wrt.newLine();
			wrt.append((Integer.toString(i)));
			wrt.append(' ');	
			wrt.append('-');
			wrt.append('>');
			wrt.append(' ');
			wrt.append(Integer.toString(result[i]));
		
			/* */
			
		}
		wrt.close();
	}
	public static void generaFile(String char_speciale, String char_sostituto, String url_scheletro_file, String url_nuovo_file){
		try{
		 BufferedReader rdr = new BufferedReader(new InputStreamReader(new FileInputStream(url_scheletro_file)));
	     BufferedWriter wrt = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(url_nuovo_file)));
	     String line=rdr.readLine();
	     while(line!= null){
	    	 line=line.replaceAll(char_speciale, char_sostituto);
	    	 wrt.append(line);
	    	 wrt.newLine();
	    	 line = rdr.readLine();
	     }
	     wrt.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static PrintWriter getPrinter (String f)
	 {
		 PrintWriter write = null;
		 try
			{
			 write = new PrintWriter(new FileOutputStream(f));
			
			}
		 catch (IOException excScrittura)
			{
			 System.out.println(PRTNWRITE + f );
			}
		 return write;
	 } 

	public static void saveStr (String f, String str)
	 {
		 PrintWriter write = null;
		 try
			{
			 write = new PrintWriter(new FileOutputStream(f));
			// write.;
			}
		 catch (IOException excScrittura)
			{
			 System.out.println(PRTNWRITE + f );
			}
 	     finally
			{
			 if (write != null)
				{
				 write.close();
				}
			}
		 } 
	
	
	public static void saveObj (String f, Object obj)

	 {
		 ObjectOutputStream write = null;
		 try
			{
			 write = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
			 write.writeObject(obj);
			}
		 catch (IOException excScrittura)
			{
			 System.out.println(PRTNWRITE + f );
			}
 	     finally
			{
			 if (write != null)
				{
				 try 
				  {
				   write.close();
				  }
				 catch (IOException excChiusura)
					{
			 			System.out.println(PRTNEND + f);
					}
				}
			}
		 } 
		
	public static void delete(File f)
	{
		f.delete();
	}
}