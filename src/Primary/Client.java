package Primary;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
  private CommandLine cm = new CommandLine();
  private SocketListener sl = new SocketListener();
  private Socket s;
  private int thneed=10;
  private int balance;
 
  private boolean SocketListenerFlag = true;

  public Client() throws Exception
  {
    s = new Socket("127.0.0.1", 6666);

  }

  private class CommandLine extends Thread
  {
    private boolean CommandLineFlag = true;
    
    @Override
    public void run() 
    {
      // TODO Auto-generated method stub
     System.out.println("Please type in your commands:");
     System.out.println("buy: quantity unitPrice | sell: quantity unitPrice | quit: | inventory:");
     Scanner sc=new Scanner(System.in);
     
     while(CommandLineFlag)
     {  
       
     String command=sc.next();
     if(command.equals("buy:"))
     {
       String amountInString=sc.next();
       String upInString=sc.next();
       int amount=Integer.parseInt(amountInString);
       int up=Integer.parseInt(upInString);
       sc.nextLine();
       if(amount > thneed )
       {
         System.out.println("There is not enough thneed in the store! Please enter again!");
       }
       else
       {
        OutputStream out=null;
        try
        {
          out = s.getOutputStream();
        } catch (IOException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        PrintStream ps=new PrintStream(out);
        ps.println(command+" "+amountInString+" "+upInString);
       }
       
     }
     else if(command.equals("sell:"))
     {
       
     }
      
     else if(command.equals("quit:"))
    {
      end();
    }
     else if(command.equals("inventory:"))
    {

    }
    else
    {
      System.out.println("Wrong input for command! Please type again!");
    }
     
     
   }
  }
    
    
    public void end()
    {
      CommandLineFlag=false;
    }
  }

 
    private class SocketListener extends Thread
  {

    @Override
    public void run()
    {
      // TODO Auto-generated method stub

    }

  }

  public static void main(String[] args) throws Exception
  {
    // TODO Auto-generated method stub
    Client client = new Client();
    client.cm.start();
    client.sl.start();

  }

}
