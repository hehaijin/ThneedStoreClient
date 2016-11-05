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
  private int thneed = 0;
  private int balance;
  private Scanner sc = null;
  private PrintStream ps = null;

  private boolean SocketListenerFlag = true;

  public Client() throws Exception
  {
    s = new Socket("127.0.0.1", 6666);
   // System.out.println("the socket"+ s);
    
    sc = new Scanner(s.getInputStream());
   // System.out.println("scanner "+sc);
    ps = new PrintStream(s.getOutputStream());
    cm.start();
    sl.start();

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
      Scanner sc = new Scanner(System.in);

      while (CommandLineFlag)
      {

        String command = sc.next();
        if (command.equals("buy:"))
        {
          String amountInString = sc.next();
          
          String upInString = sc.next();
          int amount = Integer.parseInt(amountInString);
          int up = Integer.parseInt(upInString);
          sc.nextLine();
          if (amount > thneed)
          {
            System.out.println("There is not enough thneed in the store! Please enter again!");
          } else
          {
            ps.println(command + " " + amount + " " + up);
        //    System.out.println(command + " " + amount + " " + up);
          }

        } else if (command.equals("sell:"))
        {

        }

        else if (command.equals("quit:"))
        {
          end();
        } else if (command.equals("inventory:"))
        {

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
          System.out.println(s);
          String[] me = s.split(" ");
          if (me[0].equals("status:"))
          {
            thneed = Integer.parseInt(me[1]);
            balance = Integer.parseInt(me[2]);
            System.out.println("Thneed store information updated: thneed "+thneed+" balance "+balance);
          }
        }

      }
    }
  }

  public static void main(String[] args) throws Exception
  {
    // TODO Auto-generated method stub
    Client client = new Client();
   

  }

}
