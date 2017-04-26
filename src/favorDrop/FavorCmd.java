/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package favorDrop;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;


/**
 *
 * @author Henrik
 */
public class FavorCmd {
    
    public static void main(String[] args) throws MalformedURLException, Exception{
        run();
    }
    
    private static void run() throws MalformedURLException, Exception {
        //   URL url = new URL("http://ubuntu4.javabog.dk:18371/FavorDropSoap?wsdl");
        URL url = new URL("http://localhost:18372/FavorDropSoap?wsdl");
        QName qname = new QName("http://favorDrop/", "LogikService");
        Service service = Service.create(url, qname);
        LogikI logik = service.getPort(LogikI.class);
        boolean loggedIn = false;
        Scanner scan = new Scanner(System.in);
        String username = "", password = "";
        
        while (!loggedIn){
            System.out.println("Indtast brugernavn");
            username = scan.next();
            System.out.println("Indtast password");
            password = scan.next();
            
            boolean login = logik.login(username, password);
            
            if(!login) {
                System.out.println("Forkert brugernavn eller password. Prøv igen.");
            }
            else {
                loggedIn = true;
            } 
        }
      //  String orders = logik.getClients(username, password);
        int orders = logik.getClientsA(username, password);
        System.out.println("Ordre: " + orders);
        
    }
}