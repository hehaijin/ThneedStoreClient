package Primary;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 
 * Client class for the Thneed Store
 * @author Haijin He
 *
 */
public class Client
{
  private CommandLine cm = new CommandLine();
  private SocketListener sl = new SocketListener();
  private Socket s;
  private int thneed = 0;
  private double balance;
  private Scanner sc = null;
  private PrintStream ps = null;

  private boolean SocketListenerFlag = true;
/**
 * Constructor
 * @throws Exception
 */
  public Client(String hostName,int port) throws Exception
  {
    s = new Socket(hostName, port);
    // System.out.println("the socket"+ s);
    System.out.println("Connected to Thneed Store.");

    sc = new Scanner(s.getInputStream());
    // System.out.println("scanner "+sc);
    ps = new PrintStream(s.getOutputStream());
    cm.start();
    sl.start();

  }

  /**
   * the class responsible for parsing use commands
   * @author Haijin He
   *
   */
  private class CommandLine extends Thread
  {
    private boolean CommandLineFlag = true;

    @Override
    public void run()
    {
      // TODO Auto-generated method stub
      System.out.println("Please type in your commands: buy: quantity unitPrice | sell: quantity unitPrice | quit: | inventory:");
     
      Scanner sc = new Scanner(System.in);

      while (CommandLineFlag)
      {

        String command = sc.next();
        if (command.equals("buy:"))
        {
          String amountInString = sc.next();

          String upInString = sc.next();
          int amount = Integer.parseInt(amountInString);
         // int up = Integer.parseInt(upInString);
          double up=Double.parseDouble(upInString);
          sc.nextLine();
          if (amount*up > balance)
          {
            System.out.println("There is not balance in the store! Please enter again!");
          } else
          {
            ps.println(command + " " + amount + " " + up);
            // System.out.println(command + " " + amount + " " + up);
          }

        } else if (command.equals("sell:"))
        {
          String amountInString = sc.next();

          String upInString = sc.next();
          int amount = Integer.parseInt(amountInString);
         // int up = Integer.parseInt(upInString);
          double up=Double.parseDouble(upInString);
          sc.nextLine();
          if (amount > thneed)
          {
            System.out.println("There is not thneed in the store! Please enter again!");
          } else
          {
            ps.println(command + " " + amount + " " + up);
            // System.out.println(command + " " + amount + " " + up);
          }

        }

        else if (command.equals("quit:"))
        {
          end();
          sl.end();
          sc.close();
          ps.close();
          try
          {
            s.close();
          } catch (IOException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
      
        } else if (command.equals("inventory:"))
        {
           System.out.println("Current Inventory: "+thneed + " Current Balance: "+balance);  
        } else
        {
          System.out.println("Wrong input for command! Please type again!");
        }

      }
    }

    public void end()
    {
      CommandLineFlag = false;
    }
  }
  /**
   * the class for socket communication from client to serverworker
   * @author Haijin He
   *
   */

  private class SocketListener extends Thread
  {
    private boolean flag = true;

    @Override
    public void run()
    {
      // TODO Auto-generated method stub
      while (flag)
      {
        while (sc.hasNextLine())
        {
          
          String s = sc.nextLine();
          System.out.println("Received message: "+s);
          String[] me = s.split(" ");
          if (me[0].equals("status:"))
          {
            thneed = Integer.parseInt(me[2].substring(10));
            balance = Double.parseDouble(me[3].substring(9));
           // System.out.println("Received Thneed store information update: thneed " + thneed + " balance " + balance);
          }
         
        }

      }
    }
    
    public void end()
    {
      flag=false;
    }
  }

  public static void main(String[] args) throws Exception
  {
    // TODO Auto-generated method stub
    String hostName=args[0];
    int port=Integer.parseInt(args[1]);
    Client client = new Client(hostName,port);

  }

}
