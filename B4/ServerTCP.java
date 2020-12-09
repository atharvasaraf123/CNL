import java.io.*;
import java.net.*;


public class ServerTCP
{
    public static void main(String[] args) throws IOException
    {
        ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]));
        Socket cs = ss.accept();

        BufferedReader fromserver = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader fromclient = new BufferedReader(new InputStreamReader(cs.getInputStream()));
        PrintWriter toclient = new PrintWriter(cs.getOutputStream(), true);


        toclient.println("Connection with server established");

        while(true)
        {
            String c = fromclient.readLine();
            System.out.println("From client:- " + c);
            if(c.equalsIgnoreCase("bye"))
                break;

            System.out.print("Server:- ");
            String s = fromserver.readLine();
            toclient.println(s);
            if(s.equalsIgnoreCase("bye"))
                break;
        }
        toclient.close();
        fromclient.close();
        fromserver.close();
        cs.close();
        ss.close();
    }
}