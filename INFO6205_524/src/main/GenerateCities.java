package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

public class GenerateCities {
	
	public static void main(String[] args)  
    {  
        String filepath = System.getProperty("user.dir");     
        filepath +="\\data.txt";  
        System.out.println(filepath);  
          
        try   
        {  
            File file = new File(filepath);           
            if(!file.exists())  
            {   //if data.txt not exist, then create it 
                file.createNewFile();  
                System.out.println("data.txt created");               
            }  
            FileWriter fw = new FileWriter(file);       //create file writer  
            BufferedWriter bw = new BufferedWriter(fw);  
              
            //generate random data and then insert to file  
            Random random = new Random();  
            for(int i=0;i<1000;i++)  
            {     
                int randint1 =(int)Math.floor((random.nextDouble()*10000.0));   //generate numbers from 0-10000          
                bw.write(String.valueOf(randint1));      //write file
                bw.write(",");
                int randint2 =(int)Math.floor((random.nextDouble()*10000.0));
                bw.write(String.valueOf(randint2));
                bw.newLine();       //new line 
            }  
            bw.close();  
            fw.close();  
              
        }   
        catch (Exception e)   
        {  
            e.printStackTrace();  
        }         
    } 

}
