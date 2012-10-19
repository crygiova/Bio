package ukkonen;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.System;
import java.util.concurrent.TimeUnit;

public class Tree 
{
  public static int stacktrack;
	public int TERMINATORS_RANGE = 5;
	public static int count=0;
	public static void dfsd(Node c){
		if (c.isLeaf()){
			//System.out.println("\nbasecase");
			//count++;
			return;
		}
		Node a;
		System.out.println(c.sons.keySet());
		
		Iterator it = c.sons.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			a = (Node)pairs.getValue();
			;
			for(int i=0;i<stacktrack;i++)System.out.print("\t");
			System.out.println(stacktrack+" br>>>>>>> ="+count+"= "+pairs.getKey() + " = " + a.edgeStart + " : " + a.edgeEnd );
			stacktrack++;
			count++;
			dfsd(c.sons.get(pairs.getKey()));
		
			stacktrack--;
			for(int i=0;i<stacktrack;i++)System.out.print("\t");
			System.out.println(stacktrack+" bt<<<<<<< ="+count+"= "+pairs.getKey() + " = " + a.edgeStart + " : " + a.edgeEnd );
		}
	}
	public static boolean isMatching(int a [],int str [],int i,int length,int delta)
	{
		for(int j=i;j<length+i;j++)
		{
			if(j>=a.length || j+delta>=str.length)
			{
				return false;
			}
			else
			{
				if(a[j]!=str[delta+j])
				{
					return false;
				}
			}
		}
		
		return true;
	}
	/*
	 * a = pattern to find
	 * t = tree of the text
	 * textSize = size of the text with inserted $ but without repetitions
	 * strSize = size of each string + $
	 * str = entire text with inserted $
	 * result = length of the longest suffix/prefix match for each string
	 * 
	 * */
	public static int [] findPattern(int[]a, SuffixTree t, int textSize,int strSize,int [] str){
		int result[] = new int [textSize/strSize]; //(textSize/strSize) -> every string(strSize) gets one slot of the result 
		int patternIndex = 0; // letter of the pattern to analyze
		int j = 0; // local counter when we have to jump more than one letter (related to patternIndex)
		int mainCount=0; // aggregate depth until current node
		int newMain=0; // // local counter when we have to jump more than one letter (related to mainCount)
		Node nextNode = new Node(); //nextNode to visit
		Node currentNode = new Node(); //Node that is currently being computed
		boolean endloop = false; //flag that becomes true if the algorithm should stop early(no possibilities)
		int lengthEdge=0; //length of the edge of the current node
		while (patternIndex<=a.length && !endloop){ //loops through the whole pattern or stops when no matching strings are found		
			if(patternIndex == 0) { //base case : ROOT NODE
				currentNode = t.root;
				Iterator it = (t.root).sons.entrySet().iterator();
				int fin = 0; // fin = 1 if we find the next node to compute
				while (it.hasNext() && fin==0) { //iterate through all sons or when we find the path
					Map.Entry pairs = (Map.Entry)it.next();
					currentNode = (Node)pairs.getValue();
					if((Integer)pairs.getKey() == a[0]){ // probable path (first letter is OK)
						lengthEdge=currentNode.edgeEnd-currentNode.edgeStart;
						if(lengthEdge==1) // Only one char in the edge
						{
							patternIndex++;
							fin=1;
							mainCount+=1;
						}
						else // More than one char in the edge
						{
							if(lengthEdge-1<=a.length)
							{
								if(isMatching(a,str,patternIndex,lengthEdge,currentNode.edgeStart-patternIndex)) //if is matching follow that node
								{
									patternIndex+=lengthEdge;
									fin=1;
									mainCount+=lengthEdge;
								}
							}
						}
					}
				}
				
			}
			else { // ITERATIVE CASE
				Iterator it = (currentNode).sons.entrySet().iterator();				
				while (it.hasNext()) { //iterate through sons
					Map.Entry pairs = (Map.Entry)it.next();
					currentNode = (Node)pairs.getValue();
					if(currentNode.edgeEnd == textSize){ // We find a dollar 
						int index,length;
						index=currentNode.edgeStart/strSize;
						length=((textSize-currentNode.edgeStart)%strSize)-1;
						if(length>0) // edge > 1
						{
							if(isMatching(a,str,patternIndex,length,currentNode.edgeStart-patternIndex)) //match the rest of the string until the dollar with the pattern
							{
								result[index]=length+mainCount;
							}
						}
						else //edge == 1
						{
							result[index]=length+mainCount;
						}
					}
					else if(patternIndex<a.length){ //seg fault barrier
						if((Integer)pairs.getKey()==a[patternIndex]){ //we find the next letter of the pattern
							lengthEdge=currentNode.edgeEnd-currentNode.edgeStart;
							if(lengthEdge==1) //only one char
							{
								j=patternIndex+1;
								nextNode=currentNode;
								newMain=mainCount+1;
							}
							else // More than one char in the edge
							{
								if(lengthEdge-1<=a.length)
								{
									if(isMatching(a,str,patternIndex,lengthEdge,currentNode.edgeStart-patternIndex)) //if is matching follow that node
									{
										j+=patternIndex+lengthEdge;
										nextNode=currentNode;							
										newMain=mainCount+lengthEdge;
									}
								}
							}
						}
					}
					else endloop=true; //we are outside the pattern a, end loop
				}
				if (j<=patternIndex) endloop=  true; //we have not found suitable sons, end loop
				patternIndex=j; //update next index
				mainCount=newMain; //update current depth
				currentNode=nextNode; //update next node
			}
		}
		return result; //return array with result
	}
					
	public static void main(String[] args) throws IOException {
		/*
		 *  adenine (A) = 1 
		 *  cytosine (C) = 2 
		 *  guanine (G) = 3
		 *  thymine (T) = 4
		 *  
		 *  any number > 4 means a separator
		 */

		String fname = "./s_3_sequence_1M.txt";
		int numStrTot=0;//total number of strings
		int count=5;//counter of the differents $
		int i = 0;
		int numStrNoRip=0;//number of string without the repetitions
		Hashtable<String, Integer> hashCounter = new Hashtable<String, Integer>();
		Hashtable<Integer,String> hashIndexes = new Hashtable<Integer,String>();
		int vector[] = new int [4432461];//TODO FIND THIS VALUE 
		Long timeIn;
		Long timeOut;
		Long timeCompFindPattern;
		Long timeStat;
		Long timeCompMkTree;
		Long timeTot;
		
		/******************** INPUT PART ********************/
		
		System.out.println("START");
		timeTot = System.nanoTime();
		timeIn = System.nanoTime();
		try{
			FileInputStream fstream = new FileInputStream(fname);
	         DataInputStream in = new DataInputStream(fstream);
	         BufferedReader br = new BufferedReader(new InputStreamReader(in));
	         String strLine;
	         while ((strLine = br.readLine()) != null)
	         {
	        	
	        	if(hashCounter.get(strLine)!=null)
	        	{
	        		numStrTot++;
	        		hashCounter.put(strLine,hashCounter.get(strLine)+1);
	        	}
	        	else
	        	{ //if the first time in the hash
	        		numStrTot++;
	        		hashCounter.put(strLine, 1);//hash string -> number of istances
	        		hashIndexes.put(numStrNoRip++, strLine);//hash index ->string
	        		int j=0;
	        		int number=0;
	        		for (j=0;j<strLine.length();j++) //decoding the string
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
			catch (Exception e )
					{
					 System.out.println("ERROR :" + e.toString());
					}
		timeIn= System.nanoTime()-timeIn;
		/********************END OF THE READING PART********************/
		
		/********************COMPUTATIONAL PART		********************/
		
		System.out.println("COMPUTATIONAL");
		
		int size = 51; //size of every word + $ (50+1)
		int nMatching=0;//number of string in S that matches with a prefix of pattern
		int distribuition []= new int [size]; // number of stringgs that has a prefix suffix of that length
		
		String pattern= "TGGAATTCTCGGGTGCCAAGGAACTCCAGTCACACAGTGATCTCGTATGCCGTCTTCTGCTTG";
		int decodedPattern[]=decodeString(pattern); //changes pattern from chars to ints
		
		int result[] = new int [hashCounter.size()]; //array with the resulting data
		
		timeCompMkTree =System.nanoTime();
		SuffixTree tree = new SuffixTree(vector);// build tree from text
		timeCompMkTree= System.nanoTime()-timeCompMkTree;
		
		timeCompFindPattern =System.nanoTime();
		result = findPattern(decodedPattern,tree,vector.length,size,vector); //analyze tree with pattern and compute result
		timeCompFindPattern= System.nanoTime()-timeCompFindPattern;
		
		/********************STAT PART********************/
		
		timeStat = System.nanoTime();
		for(int q= 0;q<result.length;q++)
		{
			if(result[q]>=0)
			{
				if(result[q]>0)
				{
					//number of strings that matches
					nMatching+=hashCounter.get(hashIndexes.get(q));
				}
				//distribuitions of matches
				distribuition[result[q]]+=hashCounter.get(hashIndexes.get(q));
			}
		}
		timeStat = System.nanoTime()-timeStat;
		
		/********************OUTPUT  part********************/
		
		System.out.println("OUTPUT");
		int width = 20;
		char filler = ' ';
		timeOut =System.nanoTime();
		
		//NOT WORKING FOR NOW -> IOClass.saveObj("treeObject.txt", tree); //print the Object SuffixTRee in a file -> we don't have to calculate it every time
		
		PrintWriter writer = IOClass.getPrinter("stat.txt"); 
		writer.println(Output.incornicia("Strings that matches \t"+nMatching));
		writer.println(Output.incornicia("\nDISTRIBUITIONS:\n"+printVector(distribuition)));
		writer.close();
		
		writer = IOClass.getPrinter("result.txt");//write result on a file
		for(int q= 0;q<result.length;q++)
		{
			writer.print("\n"+q+"\t>\t"+result[q]+" \n");
		}
		writer.close();
		
		writer =IOClass.getPrinter("hashCounter.txt");
		writer.println(hashCounter.toString());//write the hash counter on a file
		writer.close();

		writer =IOClass.getPrinter("hashIndexes.txt");//write the indexes hash on a file
		writer.println(hashIndexes.toString());
		writer.close();
		timeOut = System.nanoTime()-timeOut;
		
		/********************** TIMING *******************************/
		 
		writer =IOClass.getPrinter("compTimes"); //print all timing on a file 
		
		writer.println(Output.incornicia("Computational Times"));
		Long temp=TimeUnit.SECONDS.convert(timeIn, TimeUnit.NANOSECONDS); 
		writer.println("Time for Input :\t"+Output.rightAlign(timeIn.toString(), width, filler)+" ns\t"+Output.rightAlign(temp.toString(), width/2, filler)+" s");
		
		temp=TimeUnit.SECONDS.convert(timeCompMkTree, TimeUnit.NANOSECONDS); 
		writer.println("Time Make Tree :\t"+Output.rightAlign(timeCompMkTree.toString(), width, filler)+" ns\t"+Output.rightAlign(temp.toString(), width/2, filler)+" s");
		
		temp=TimeUnit.SECONDS.convert(timeCompFindPattern, TimeUnit.NANOSECONDS);
		writer.println("Time Find Patt :\t"+Output.rightAlign(timeCompFindPattern.toString(), width, filler)+" ns\t"+Output.rightAlign(temp.toString(), width/2, filler)+" s");
		
		temp=TimeUnit.SECONDS.convert(timeStat, TimeUnit.NANOSECONDS);
		writer.println("Time Statistic :\t"+Output.rightAlign(timeStat.toString(), width, filler)+" ns\t"+Output.rightAlign(temp.toString(), width/2, filler)+" s");
		
		temp=TimeUnit.SECONDS.convert(timeOut, TimeUnit.NANOSECONDS);
		writer.println("Time for Output:\t"+Output.rightAlign(timeOut.toString(), width, filler)+" ns\t"+Output.rightAlign(temp.toString(), width/2, filler)+" s");
		
		timeTot = System.nanoTime()-timeTot;
		writer.println("Time Total Comp :\t"+Output.rightAlign(timeTot.toString(), width, filler)+" ns\t"+TimeUnit.SECONDS.convert(timeTot, TimeUnit.NANOSECONDS)+" s");
		writer.close();
		
		System.out.println("FIN");		
	}

	public static String printVector(int [] vector)
	{
		String str="";
		for (int i=0;i<vector.length;i++)
		{
			str+="\n"+i+"\t>\t"+vector[i]+" \n";
		}
		return str;
	}

	public static int [] decodeString (String strLine)//decode a string from char to numbers
	{
		int[] result = new int [strLine.length()];
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
			result[j]=number;
		}
		return result;
	}
}