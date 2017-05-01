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
        String response;
        int number = 0;
        
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
        
        whileLoop: while(true) {
            System.out.println("");
            System.out.println("Press 1 for number of clients in DB");
            System.out.println("Press 2 for client JSON");
            System.out.println("Press 3 for orders JSON");
            System.out.println("Press 4 for number of partners in DB");
            System.out.println("Press 5 for number of completed orders in DB");
            System.out.println("Press 6 to terminate");
            if (scan.hasNextInt()){
                number=scan.nextInt();
                scan.nextLine();
            } else {
                System.out.println("Input skal være et tal");
                scan.nextLine();
                continue;}
            switch(number) {
                case 1:
                    number = logik.getClientsA(username, password);
                    System.out.println("Antal klienter i DB: " + number);
                    break;
                case 2:
                    response = logik.getClients(username, password);
                    System.out.println("Klienter JSON: " + response);
                    break;
                case 3:
                    response = logik.getOrders(username, password);
                    System.out.println("Orders JSON: " + response);
                    break;
                case 4:
                    number = logik.getPartnersA(username, password);
                    System.out.println("Antal partnere i DB: " + number);
                    break;
                case 5:
                    number = logik.getOrdersA(username, password);
                    System.out.println("Antal ordere i DB: " + number);
                    break;
                case 6: break whileLoop;
                default: System.out.println("Ugyldigt tal, prøv igen");
            }
        }
    }
}