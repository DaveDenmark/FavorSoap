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
        int number;
        Object returned;
        
        while (!loggedIn){
            System.out.println("Indtast brugernavn");
            username = scan.next();
            System.out.println("\n"+"Indtast password");
            password = scan.next();
            
            boolean login = logik.login(username, password);
            
            if(!login) {
                System.out.println("\n"+"Forkert brugernavn eller password. Prøv igen.");
            }
            else {
                loggedIn = true;
            }
        }
        
        whileLoop: while(true) {
            System.out.println("\n"+"Press 1 for number of clients in DB");
            System.out.println("Press 2 for client JSON");
            System.out.println("Press 3 for orders JSON");
            System.out.println("Press 4 for number of partners in DB");
            System.out.println("Press 5 for number of completed orders in DB");
            System.out.println("Press 6 to terminate");
            if (scan.hasNextInt()){
                number=scan.nextInt();
                scan.nextLine();
            } else {
                System.out.println("\n"+"Input skal være et tal");
                scan.nextLine();
                continue;}
            switch(number) {
                case 1:
                    returned = logik.getClientsA(username, password);
                    System.out.println("\n"+"Antal klienter i DB: " + returned);
                    break;
                case 2:
                    returned = logik.getClients(username, password);
                    System.out.println("\n"+"Klienter JSON: " + returned);
                    break;
                case 3:
                    returned = logik.getOrders(username, password);
                    System.out.println("\n"+"Orders JSON: " + returned);
                    break;
                case 4:
                    returned = logik.getPartnersA(username, password);
                    System.out.println("\n"+"Antal partnere i DB: " + returned);
                    break;
                case 5:
                    returned = logik.getOrdersA(username, password);
                    System.out.println("\n"+"Antal ordre i DB: " + returned);
                    break;
                case 6: 
                    scan.close();
                    break whileLoop;
                default: System.out.println("\n"+"Ugyldigt tal, prøv igen");
            }
        }
    }
}