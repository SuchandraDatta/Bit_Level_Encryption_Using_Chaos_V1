import java.util.Scanner;
import java.util.Random;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.*;
class RandomIndexes
{
	int r,c;
}
public class EncryptFinal
{
    

    public static int createPassCode()throws IOException
    {   
        
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String snum=dateFormat.format(date);
        String timeAsString="";
        for(int i=0;i<snum.length();i++)
        {
            if(snum.substring(i,i+1).equals(":")) continue;
            timeAsString=timeAsString+snum.substring(i,i+1);
        }
        return 10;
        //return (Integer.parseInt(timeAsString)/10);
       
    }
	public static void main(String s[])throws IOException
	{
        //do the passcode generation only once
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Encryption Key : ");
	    int pass=sc.nextInt();
	    System.out.println("Enter Input File name : ");
	    String  file1=sc.nextLine();
	    file1=sc.nextLine();
	    System.out.println("Enter Encrypted File name : ");
	    String  file2=sc.nextLine();
        FileOutputStream fout=new FileOutputStream(file2);
        FileInputStream fin=new FileInputStream(file1);
        String plaintext, totalPlaintext="", outputToScreen="";
        int readValue=0;
        while((readValue=fin.read())!=-1)
        {
            //System.out.println("Plaintext read value: " + getBits(readValue));
            outputToScreen=outputToScreen+(char)readValue;
            totalPlaintext=totalPlaintext+getBits(readValue);
            //System.out.println("GET BYTES: " + getBits(readValue));
        }
        //System.out.println("Total plaintext is: " + outputToScreen);
        mainEncryption(totalPlaintext, pass, fout);
		fout.close();
        
    }
    public static void mainEncryption(String plainbits, int pass, FileOutputStream fout)throws IOException
       { //CONVERTING THE PLAINTEXT TO BITS
        
		
        //System.out.println("Plainbits length: " + plainbits.length());
        int totCount=0;
        System.out.println("Plainbits length is: " + plainbits.length());
       
		//CREATING THE RANDOM INDICES AS STARTING POINT
        RandomIndexes obj[]=new RandomIndexes[plainbits.length()];
		for(int i=0;i<plainbits.length();i++)
		{
			obj[i]=new RandomIndexes();
		}
		Random sr=new Random((long)pass);
		int ri,rj;
		int div; //div is the value which will decide the number of alive cells by making a fraction of cells 1.
		div=1+sr.nextInt(4);//1 to 5 er moddhe hobe.
		int count=0,limit=(int)Math.sqrt(plainbits.length());//maximum value of the indexes ! depending upon square matrix !
		for(int i=0;i<(int)(plainbits.length())/div;i++) //random value ! : plainbits.length()/2; initial number of 1s (initial number of live cells)
		{ 
			ri=sr.nextInt(limit);
			rj=sr.nextInt(limit);
			if(indexNotEqual(ri,rj,obj,count))
			{
				obj[count].r=ri;
				obj[count].c=rj;
				count++;
			}
			else
			{
				--i;
			}
           // System.out.print("\nIn loop to assign indices.");
			totCount++;
		}
		int grid[][]=new int[limit][limit];
		int sizesquare=(int)plainbits.length()/div;

		for(int i=0;i<limit;i++)
  		{
  			for(int j=0;j<limit;j++)
  			{
  				if(!(indexNotEqual(i,j,obj,sizesquare)))
  				{
  					
  					grid[i][j]=1;
  				}
  				else
  					grid[i][j]=0;
  				totCount++;
  			}

  		}
  		

		
		totCount=gameOfLife(obj,limit,(int)plainbits.length()/div,grid, totCount); //new parameter needed !!}
        String xorredbits="";
        int c=0;
        for(int i=0;i<limit;i++)
        {
        	for(int j=0;j<limit;j++)
        	{
        		String pbit=plainbits.substring(c,c+1);
        		String rbit=Integer.toString(grid[i][j]);
        		int xorred=Integer.parseInt(pbit)^Integer.parseInt(rbit);
        		xorredbits=xorredbits+Integer.toString(xorred);
        		c++;
        		totCount++;
        	}
        }
        System.out.println("C is: " + c);
         
        //System.out.print("\nOriginal xorred bits: " + xorredbits);
        //System.out.println("\nAfter XOR: " + xorredbits);
        
        //System.out.println("\nXorred before randomization: " + xorredbits);
        //xorredbits=functionForMatrixRandom(xorredbits,limit)+plainbits.substring(c);
        
       // xorredbits=functionForMatrixRandom(xorredbits,limit)+plainbits.substring(c);
        
        //System.out.println("getTerm is: " + getTerm);
        xorredbits=xorredbits+plainbits.substring(c);
        System.out.println("xorredbits length is " + xorredbits.length());
        int sum=0, term=1;
        for(int i=xorredbits.length()-1;i>=0;i--)
        {
             if(xorredbits.substring(i,i+1).equals("1")) 
             {
               sum=sum+term; System.out.println("\nYes its 1 sum is: " + sum);


             }
             term=term+2;
             totCount++;
        }
        
        System.out.println("\nPower of 1's is: " + sum);
        //System.out.print("\nOriginal xorred bits: " + xorredbits);
        //System.out.println("\nAfter XOR: " + xorredbits);
        
        //System.out.println("\nXorred before randomization: " + xorredbits);
        //xorredbits=functionForMatrixRandom(xorredbits,limit)+plainbits.substring(c);
        
       // xorredbits=functionForMatrixRandom(xorredbits,limit)+plainbits.substring(c);
        String numToStr=Integer.toString(sum);
        int getTerm;
        if(numToStr.length()>=2)
        getTerm=Integer.parseInt(numToStr.substring(numToStr.length()-2));
        else
        getTerm=Integer.parseInt(numToStr);
        
        System.out.println("getTerm is: " + getTerm);
        System.out.println("\nBefore matRandom: " + xorredbits);
        
        for(int i=1;i<=getTerm;i++)
        {
        	xorredbits=functionForMatrixRandom(xorredbits, limit);
        	totCount++;
        }
        //The problem is if getTerm is zero, then the value of xorredbits doesnt change and hence the substring
        //increases the length of xorredbits unintentionally
        
        if(getTerm>0)
        xorredbits=xorredbits+plainbits.substring(c);
         
        
        
        
        System.out.print("Limit is : " + limit);
        System.out.print("After randomizing, final XORred bits: " + xorredbits);
        //
        //System.out.println("After matRandom full string for 2nd xor: " + xorredbits);
        String xorWithThis=xorredbits.substring(0,8), temp="", tempXor="";
        String finalXorredBits="";
        //System.out.println("\nXor with this: " + xorWithThis);
        for(int i=8;i<=xorredbits.length()-8;i+=8)
        {
            temp="";
            tempXor=xorredbits.substring(i,i+8);
            for(int j=0;j<8;j++)
            {

                temp=temp+Integer.toString(Integer.parseInt(tempXor.substring(j,j+1))^Integer.parseInt(xorWithThis.substring(j,j+1)));
                totCount++;
            }
            finalXorredBits=finalXorredBits+temp;

        }
        finalXorredBits=xorWithThis+finalXorredBits;
        System.out.println("\nAFter xorring attempt one: " + finalXorredBits);
        xorWithThis=finalXorredBits.substring(finalXorredBits.length()-8);
        xorredbits="";
        //System.out.println("\nSecond Xor with this: " + xorWithThis);
        for(int i=0;i<=finalXorredBits.length()-16;i+=8)
        {
            temp="";
            tempXor=finalXorredBits.substring(i,i+8);
            for(int j=0;j<8;j++)
            {
                temp=temp+Integer.toString(Integer.parseInt(tempXor.substring(j,j+1))^Integer.parseInt(xorWithThis.substring(j,j+1)));
                 totCount++;
            }
            xorredbits=xorredbits+temp;
        }
        xorredbits=xorWithThis+xorredbits;
        System.out.println("\nAFter 2nd xor: " + xorredbits);
        //attempt 3
        xorWithThis=xorredbits.substring(xorredbits.length()-4);
        finalXorredBits="";
        for(int i=0;i<=xorredbits.length()-8;i+=4)
        {
            temp="";
            tempXor=xorredbits.substring(i,i+4);
            for(int j=0;j<4;j++)
            {
                temp=temp+Integer.toString(Integer.parseInt(tempXor.substring(j,j+1))^Integer.parseInt(xorWithThis.substring(j,j+1)));
                 totCount++;
            }
            finalXorredBits=finalXorredBits+temp;
        }
        finalXorredBits=finalXorredBits+xorWithThis;
        System.out.println("\nAFter 3rd XOR: " + finalXorredBits);
        xorredbits=finalXorredBits;
        //attempt 4
        xorWithThis=xorredbits.substring(0,4);
        finalXorredBits="";
        int ii;
        for( ii=5;ii<=xorredbits.length()-4;ii+=4)
        {
            temp="";
            tempXor=xorredbits.substring(ii,ii+4);
            for(int j=0;j<4;j++)
            {
                temp=temp+Integer.toString(Integer.parseInt(tempXor.substring(j,j+1))^Integer.parseInt(xorWithThis.substring(j,j+1)));
                 totCount++;
            }
            finalXorredBits=finalXorredBits+temp;
        }
        if(ii!=xorredbits.length()-4)
        finalXorredBits=xorredbits.substring(4,5)+finalXorredBits+xorredbits.substring(ii)+xorWithThis;
        else
        finalXorredBits=xorredbits.substring(4,5)+finalXorredBits+xorWithThis;
        xorredbits=finalXorredBits;

        System.out.println("\nAfter final XOR: " + xorredbits);
        System.out.println("\nAfter final XOR length: " + xorredbits.length());


        String ciphertextTrial="";
        //System.out.println("Final encrypted string(attempt 3); " + xorredbits);
        
       
        for(int i=0;i<=xorredbits.length()-8;i+=8)
        {
            System.out.print("\nEach byte is: " + getByte(xorredbits.substring(i,i+8)));
            ciphertextTrial=ciphertextTrial+(char)getByte(xorredbits.substring(i,i+8));
            fout.write(getByte(xorredbits.substring(i,i+8)));
        }
        fout.write(getTerm);
        System.out.println("Getterm on file: " + getTerm);  
        System.out.println("Roughly estimated total number of ops: " + totCount);
        System.out.println("\nCipher Text=(my version)"+ciphertextTrial);
        
       
	}
	public static String xorFunc(String xorWithThis, String s)
	{
		String re="", temp="", tempXor="", finalXorredBits="";
		for(int i=4;i<=s.length()-4;i+=4)
		{
			temp="";
            tempXor=s.substring(i,i+4);
            for(int j=0;j<4;j++)
            {
                temp=temp+Integer.toString(Integer.parseInt(tempXor.substring(j,j+1))^Integer.parseInt(xorWithThis.substring(j,j+1)));
            }
            finalXorredBits=finalXorredBits+temp;
		}
		re=finalXorredBits+xorWithThis;
		return re;
	}
    public static String reverseString(String s)
    {
        String rev="";
        for(int i=0;i<s.length();i++)
        {
            rev=s.substring(i,i+1)+rev;
        }
        return rev;
    }
    public static String functionForMatrixRandom(String xorredbits, int limit)
    {
        String gridForRandom[][]=new String[limit][limit];
        int c=0;
        int count=1,i,j;
        for(i=0;i<limit;i++)
        {
            for(j=0;j<limit;j++)
            {
                gridForRandom[i][j]=xorredbits.substring(c,c+1);
                c++;
            }
        }
        /*System.out.println("\nInput matrix is: \n");
        for(i=0;i<limit;i++)
        {
            for(j=0;j<limit;j++)
            {
                System.out.print(gridForRandom[i][j]);
            }
            System.out.println();
        }*/

        String t;
        
        //leftshift
      for(i=0;i<limit;i++)
      { t=gridForRandom[i][0];
        for(j=0;j<limit-1;j++)
        {
            gridForRandom[i][j]=gridForRandom[i][j+1];
        }
        gridForRandom[i][limit-1]=t;
      }
      //upshift
      for(i=0;i<limit;i++)
      {
        t=gridForRandom[0][i];
        for(j=0;j<limit-1;j++)
            gridForRandom[j][i]=gridForRandom[j+1][i];
        gridForRandom[limit-1][i]=t;
      }
       //rightshift
      for(i=0;i<limit;i++)
      {
        t=gridForRandom[i][limit-1];
        for(j=limit-1;j>0;j--)
            gridForRandom[i][j]=gridForRandom[i][j-1];
        gridForRandom[i][0]=t;
      }
      int row=0, col=limit-1;
      while(count<=limit/2)
      {
        if(count%2==0)
        {
            t=gridForRandom[row][col];
            rightshift(gridForRandom,row,col,0);
            upshift(gridForRandom, row,col,0);
            leftshift(gridForRandom,row,col,0);
            downshift(gridForRandom,row,col,0);
            gridForRandom[row+1][col]=t;
        }
        else
        {
            t=gridForRandom[row][row];
            leftshift(gridForRandom,row,col,1);
            upshift(gridForRandom,row,col,1);
            rightshift(gridForRandom,row,col,1);
            downshift(gridForRandom,row,col,1);
            gridForRandom[row+1][row]=t;
        }
        row++; col--; count++;
      }

     
      //downshift
      for(i=0;i<limit;i++)
      {
        t=gridForRandom[limit-1][i];
        for(j=limit-1;j>0;j--)
            gridForRandom[j][i]=gridForRandom[j-1][i];
        gridForRandom[0][i]=t;
      }
      //diagonal_shift
      /*for(i=0;i<n/2;i++)
      {
        t=a[i][i];
        a[i][i]=a[n-1-i][n-1-i];
        a[n-1-i][n-1-i]=t;
      }
      for(i=0;i<n/2;i++)
      {
        t=a[i][n-1-i];
        a[i][n-1-i]=a[n-1-i][i];
        a[n-1-i][i]=t;
      }*/
      String ans="";
      for( i=0;i<limit;i++)
      {
        for( j=0;j<limit;j++)
        {   
            ans=ans+gridForRandom[i][j];
        }
      }
      System.out.println("Rndm is: " + ans);
      /*System.out.println("\nOutput matrix is: \n");
        for(i=0;i<limit;i++)
        {
            for(j=0;j<limit;j++)
            {
                System.out.print(gridForRandom[i][j]);
            }
            System.out.println();
        }*/
      return ans;
   }
     public static void rightshift(String a[][], int row, int col, int c)
      {
        if(c==0)
        {
            for(int j=col;j>row;j--)
                a[row][j]=a[row][j-1];
        }
        else
        {
            for(int j=col;j>row;j--)
                a[col][j]=a[col][j-1];
         }
      }
    public static  void upshift(String a[][], int row, int col, int c)
      {
        if(c==0)
        {
            for(int j=row;j<col;j++)
                a[j][row]=a[j+1][row];
        }
        else
        {
            for(int j=row;j<col;j++)
                a[j][col]=a[j+1][col];
        }

      }
    public static  void leftshift(String a[][], int row, int col, int c)
      {
        if(c==0)
        {
            for(int j=row;j<col;j++)
                a[col][j]=a[col][j+1];
        }
        else
        {
            for(int j=row;j<col;j++)
            a[row][j]=a[row][j+1];
        }

      }
    public static void downshift(String a[][], int row, int col, int c)
      {
         if(c==0)
         {
            for(int j=col;j>row;j--)
                a[j][col]=a[j-1][col];
         }
         else
         {
            for(int j=col;j>row;j--)
                a[j][row]=a[j-1][row];
         }
      }

  
    
    
	public static int getByte(String x)
	{
		int ans=0;
		for(int i=7;i>=0;i--)
		{
			if(x.charAt(i)=='1')
				ans=ans+(int)Math.pow(2,7-i);
		}
		return ans;
	}
	public static String getBits(int x)
	{
		String ans="";
		int mask=128;
		while(mask>0)
		{
			int k=x&mask;
			if(k!=0)
				ans=ans+"1";
			else
				ans=ans+"0";

			mask=mask/2;
		}
		return ans;
	}
	public static boolean indexNotEqual(int ri,int rj,RandomIndexes obj[],int count)
	{
		for(int i=0;i<count;i++)
		{
			if(obj[i].r==ri && obj[i].c==rj)
				return false;
		}	
		return true; 
	}
	public static int gameOfLife(RandomIndexes obj[],int arraysize,int sizesquare,int grid[][], int totCount)
	{
		//int grid[][]=new int[arraysize][arraysize];
		int M = arraysize, N = arraysize; 
  		int count=0;
        // Intiliazing the grid. 
        /*int[][] grid = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
            { 0, 0, 0, 1, 1, 0, 0, 0, 0, 0 }, 
            { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 }, 
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
            { 0, 0, 0, 1, 1, 0, 0, 0, 0, 0 }, 
            { 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 }, 
            { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 }, 
            { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 }, 
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } 
        }; */
  		/*for(int i=0;i<arraysize;i++)
  		{
  			for(int j=0;j<arraysize;j++)
  			{
  				if(!(indexNotEqual(i,j,obj,sizesquare)))
  				{
  					count++;
  					grid[i][j]=1;
  				}
  				else
  					grid[i][j]=0;
  			}
  		}*/
  		//System.out.println("No. of 1s at the beginning :"+count);
        // Displaying the grid 
        /*System.out.println("Original Generation"); 
        for (int i = 0; i < M; i++) 
        { 
            for (int j = 0; j < N; j++) 
            { 
                if (grid[i][j] == 0) 
                    System.out.print("."); 
                else
                    System.out.print("*"); 
            } 
            System.out.println(); 
        } 
        System.out.println();*/
        try{ 
        totCount=nextGeneration(grid, M, N,0, totCount);
        
        }
        catch(Exception e)
        {
        	System.out.println(e.getMessage());
        } 
        return totCount;
    } 
    //this code is from geeks_for_geeks
  	public static int nextGeneration(int grid[][], int M, int N,int checkInfinite, int totCount) 
    { 
        int[][] future = new int[M][N]; 
  
        // Loop through every cell 
        for (int l = 1; l < M - 1; l++) 
        { 
        	
            for (int m = 1; m < N - 1; m++) 
            { 
                // finding no Of Neighbours that are alive 
                totCount++;
                int aliveNeighbours = 0; 
                for (int i = -1; i <= 1; i++) 
                    for (int j = -1; j <= 1; j++) 
                        aliveNeighbours += grid[l + i][m + j]; 
  
                // The cell needs to be subtracted from 
                // its neighbours as it was counted before 
                aliveNeighbours -= grid[l][m]; 
  
                // Implementing the Rules of Life 
  
                // Cell is lonely and dies 
                if ((grid[l][m] == 1) && (aliveNeighbours < 2)) 
                    future[l][m] = 0; 
  
                // Cell dies due to over population 
                else if ((grid[l][m] == 1) && (aliveNeighbours > 3)) 
                    future[l][m] = 0; 
  
                // A new cell is born 
                else if ((grid[l][m] == 0) && (aliveNeighbours == 3)) 
                    future[l][m] = 1; 
  
                // Remains the same 
                else
                    future[l][m] = grid[l][m]; 
            } 
        } 
  		int live_count=0;
        //System.out.println("Next Generation"); 
        for (int i = 0; i < M; i++) 
        { 
            for (int j = 0; j < N; j++) 
            { 
            	grid[i][j]=future[i][j];
                if (future[i][j] == 0) 
                {
                    //System.out.print("."); 
                }
                else
                {
                    //System.out.print("*"); 
                    live_count++;
                }
            } 
            //System.out.println(); 
        }
        /*if(live_count==0)
        {
        	//System.out.println("EXTERMINATED");
        	return 0;
        }*/
        return totCount;
        //if(live_count<10)
        	//nextGeneration(future,M,
    }
}