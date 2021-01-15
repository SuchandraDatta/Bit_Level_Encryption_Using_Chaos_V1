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
class DecryptFinal
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
        System.out.println("Enter Decryption Key : ");
        int pass=sc.nextInt();
        System.out.println("Enter Encrypted File name : ");
        String  file1=sc.nextLine();
        file1=sc.nextLine();
        System.out.println("Enter Output File name : ");
        String  file2=sc.nextLine();
        FileOutputStream fout=new FileOutputStream(file2);
        FileInputStream fin=new FileInputStream(file1);
        String plaintext, totalCiphertext="", outputToScreen="";
        int readValue=0;
        while((readValue=fin.read())!=-1)
        {
            //System.out.println("Plaintext read value: " + getBits(readValue));
        

            totalCiphertext=totalCiphertext+getBits(readValue);
            //System.out.println("Bits got: " + getBits(readValue));

        }
        //System.out.println("totalCiphertext is: " + totalCiphertext);
        String cipherbits=totalCiphertext.substring(0,totalCiphertext.length()-8);
        //System.out.println("cipherbits is: " + cipherbits);

        int getTerm=getByte(totalCiphertext.substring(totalCiphertext.length()-8));
        System.out.println("Hopefully the getterm is: " + getTerm);

        mainDecryption(cipherbits, pass, fout, getTerm);
		fout.close();
        
    }
    public static void mainDecryption(String cipherbits, int pass, FileOutputStream fout, int getTerm)throws IOException
    { 
    	String partialxor="";
    	String temp="";
    	String xorWithThis="";
    	String tempXor="";
    	xorWithThis=cipherbits.substring(cipherbits.length()-4);
        partialxor="";
        int ii;
        for( ii=1;ii<=cipherbits.length()-8;ii+=4)
        {
            temp="";
            tempXor=cipherbits.substring(ii,ii+4);
            for(int j=0;j<4;j++)
            {
                temp=temp+Integer.toString(Integer.parseInt(tempXor.substring(j,j+1))^Integer.parseInt(xorWithThis.substring(j,j+1)));
            }
            partialxor=partialxor+temp;
        }
        if(ii!=partialxor.length()-8)
        partialxor=xorWithThis+cipherbits.substring(0,1)+partialxor+cipherbits.substring(ii, cipherbits.length()-4);
        else
        partialxor=xorWithThis+cipherbits.substring(0,1)+partialxor;
        System.out.println("\nInverse of x4: " + partialxor);

    	String secondxor="";
    	xorWithThis=partialxor.substring(partialxor.length()-4);
    	for(int i=0;i<=partialxor.length()-8;i+=4)
        {
            temp="";
            tempXor=partialxor.substring(i,i+4);
            for(int j=0;j<4;j++)
            {
                temp=temp+Integer.toString(Integer.parseInt(tempXor.substring(j,j+1))^Integer.parseInt(xorWithThis.substring(j,j+1)));
            }
            secondxor=secondxor+temp;
        }
        secondxor=secondxor+xorWithThis;
        System.out.println("\nInverse of x3: " + secondxor);

        xorWithThis=secondxor.substring(0,8);
        String thirdxor="";
        for(int i=8;i<=secondxor.length()-8;i+=8)
        {
            temp="";
            tempXor=secondxor.substring(i,i+8);
            for(int j=0;j<8;j++)
            {

                temp=temp+Integer.toString(Integer.parseInt(tempXor.substring(j,j+1))^Integer.parseInt(xorWithThis.substring(j,j+1)));
            }
            thirdxor=thirdxor+temp;

        }
        thirdxor=thirdxor+xorWithThis;
        System.out.println("\nInverse of x2: " + thirdxor);
        String fourthxor="";
        xorWithThis=thirdxor.substring(0,8);
        for(int i=8;i<=thirdxor.length()-8;i+=8)
        {
            temp="";
            tempXor=thirdxor.substring(i,i+8);
            for(int j=0;j<8;j++)
            {

                temp=temp+Integer.toString(Integer.parseInt(tempXor.substring(j,j+1))^Integer.parseInt(xorWithThis.substring(j,j+1)));
            }
            fourthxor=fourthxor+temp;
        }
        fourthxor=xorWithThis+fourthxor;
        System.out.println("\nInverse of x1: " + fourthxor);
        int count=0,limit=(int)Math.sqrt(fourthxor.length());//maximum value of the indexes ! depending upon square matrix !

        String unmatrixxorred="";
        
        unmatrixxorred=fourthxor;
        for(int i=1;i<=getTerm;i++)
        { //unmatrixxorred=functionForMatrixRandom(fourthxor,limit)+fourthxor.substring(limit*limit);
            unmatrixxorred=functionForMatrixRandom(unmatrixxorred, limit);
        }
        if(getTerm>0)
        unmatrixxorred=unmatrixxorred+fourthxor.substring(limit*limit);
        System.out.println("Prior to mtrndom: " + unmatrixxorred);



        RandomIndexes obj[]=new RandomIndexes[unmatrixxorred.length()];
		for(int i=0;i<unmatrixxorred.length();i++)
		{
			obj[i]=new RandomIndexes();
		}
		Random sr=new Random((long)pass);
		int ri,rj;
		int div; //div is the value which will decide the number of alive cells by making a fraction of cells 1.
		div=1+sr.nextInt(4);//1 to 5 er moddhe hobe.
		count=0;//limit=(int)Math.sqrt(plainbits.length());//maximum value of the indexes ! depending upon square matrix !
		for(int i=0;i<(int)(unmatrixxorred.length())/div;i++) //random value ! : plainbits.length()/2; initial number of 1s (initial number of live cells)
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
		}
		int grid[][]=new int[limit][limit];
		gameOfLife(obj,limit,(int)unmatrixxorred.length()/div,grid); //new parameter needed !!
        String plainbits="";
        int c=0;
        for(int i=0;i<limit;i++)
        {
        	for(int j=0;j<limit;j++)
        	{
        		String pbit=unmatrixxorred.substring(c,c+1);
        		String rbit=Integer.toString(grid[i][j]);
        		int xorred=Integer.parseInt(pbit)^Integer.parseInt(rbit);
        		plainbits=plainbits+Integer.toString(xorred);
        		c++;
        	}
        }
        plainbits=plainbits+unmatrixxorred.substring(c);

        String plaintextTrial="";
        for(int i=0;i<=plainbits.length()-8;i+=8)
        {
            System.out.println("Each byte is: " + getByte(plainbits.substring(i,i+8)));
            plaintextTrial=plaintextTrial+(char)getByte(plainbits.substring(i,i+8));
            fout.write(getByte(plainbits.substring(i,i+8)));
        }
        
        System.out.println("\nDeciphered Text=(my version)"+plaintextTrial);

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
        System.out.println("\nInput matrix is: \n");
        for(i=0;i<limit;i++)
        {
            for(j=0;j<limit;j++)
            {
                //System.out.print(gridForRandom[i][j]);
            }
           // System.out.println();
        }

        String t;
        
       /* //leftshift
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
      }*/
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

      //upshift decrypt
      for(i=0;i<limit;i++)
      {
        t=gridForRandom[0][i];
        for(j=0;j<limit-1;j++)
            gridForRandom[j][i]=gridForRandom[j+1][i];
        gridForRandom[limit-1][i]=t;
      }
     // System.out.println("\nInput matrix is fter upshift: \n");
        for(i=0;i<limit;i++)
        {
            for(j=0;j<limit;j++)
            {
                //System.out.print(gridForRandom[i][j]);
            }
            //System.out.println();
        }

      //cycle decrypt
      int row=0, col=limit-1;
      while(count<=limit/2)
      {
        if(count%2==1)
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
       //System.out.println("\nInput matrix is fter cycle shift: \n");
        for(i=0;i<limit;i++)
        {
            for(j=0;j<limit;j++)
            {
               // System.out.print(gridForRandom[i][j]);
            }
            //System.out.println();
        }
       //leftshift decrypt
      for(i=0;i<limit;i++)
      { t=gridForRandom[i][0];
        for(j=0;j<limit-1;j++)
        {
            gridForRandom[i][j]=gridForRandom[i][j+1];
        }
        gridForRandom[i][limit-1]=t;
      }
       // System.out.println("\nInput matrix is fter leftshift: \n");
        for(i=0;i<limit;i++)
        {
            for(j=0;j<limit;j++)
            {
                //System.out.print(gridForRandom[i][j]);
            }
            //System.out.println();
        }
       //downshift decrypt
      for(i=0;i<limit;i++)
      {
        t=gridForRandom[limit-1][i];
        for(j=limit-1;j>0;j--)
            gridForRandom[j][i]=gridForRandom[j-1][i];
        gridForRandom[0][i]=t;
      }
        //System.out.println("\nInput matrix is fter downshift: \n");
        for(i=0;i<limit;i++)
        {
            for(j=0;j<limit;j++)
            {
                //System.out.print(gridForRandom[i][j]);
            }
            //System.out.println();
        }

       //rightshift decrypt
      for(i=0;i<limit;i++)
      {
        t=gridForRandom[i][limit-1];
        for(j=limit-1;j>0;j--)
            gridForRandom[i][j]=gridForRandom[i][j-1];
        gridForRandom[i][0]=t;
      }
         //System.out.println("\nInput matrix is fter rightshift: \n");
        for(i=0;i<limit;i++)
        {
            for(j=0;j<limit;j++)
            {
                //System.out.print(gridForRandom[i][j]);
            }
            //System.out.println();
        }







      String ans="";
      for( i=0;i<limit;i++)
      {
        for( j=0;j<limit;j++)
        {   
            ans=ans+gridForRandom[i][j];
        }
      }
      //System.out.println("Rndm is: " + ans);
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
	public static void gameOfLife(RandomIndexes obj[],int arraysize,int sizesquare,int grid[][])
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
  		for(int i=0;i<arraysize;i++)
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
  		}
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
        } */
        System.out.println();
        try{ 
        nextGeneration(grid, M, N,0);
        }
        catch(Exception e)
        {
        	System.out.println(e.getMessage());
        } 
    } 
    //this code is from geeks_for_geeks
  	public static void nextGeneration(int grid[][], int M, int N,int checkInfinite) 
    { 
        int[][] future = new int[M][N]; 
  
        // Loop through every cell 
        for (int l = 1; l < M - 1; l++) 
        { 
            for (int m = 1; m < N - 1; m++) 
            { 
                // finding no Of Neighbours that are alive 
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
        if(live_count==0)
        {
        	//System.out.println("EXTERMINATED");
        	return;
        }
        //if(live_count<10)
        	//nextGeneration(future,M,
    }
}