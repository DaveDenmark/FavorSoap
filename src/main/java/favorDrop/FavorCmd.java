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
        URL url = new URL("http://52.213.91.0:18372/FavorDropSoap?wsdl");
        QName qname = new QName("http://favorDrop/", "LogikService");
        Service service = Service.create(url, qname);
        LogikI logik = service.getPort(LogikI.class);
        boolean loggedIn = false;
        Scanner scan = new Scanner(System.in);
        String username = "", password = "", login = "";
        int number;
        Object returned;
        
        while (!loggedIn){
            System.out.println("Indtast brugernavn");
            username = scan.next();
            System.out.println("\n"+"Indtast password");
            password = scan.next();
            
            login = logik.login(username, password);
            if("Not authorized".equals(login)) {
                System.out.println("\n"+"Forkert brugernavn eller password. Prøv igen.");
            }
            else {
                loggedIn = true;
            }
        }
        
        whileLoop: while(true) {
            String OID;
            System.out.println("\n"+"Tryk 1 for at se antal bruger i DB");
            System.out.println("Tryk 2 for at se antal partnere i DB");
            System.out.println("Tryk 3 for at se antal færdige ordre");
            System.out.println("Tryk 4 for at hente en liste af nye ordre ID'er");
            System.out.println("Tryk 5 for at slette en ny ordre");
            System.out.println("Tryk 6 for at hente en liste af igangværende ordre ID'er");
            System.out.println("Tryk 7 for at slette en igangværende ordre");
            System.out.println("Tryk 8 for at afslutte");
            if (scan.hasNextInt()){
                number=scan.nextInt();
                scan.nextLine();
            } else {
                System.out.println("\n"+"Input skal være et tal");
                scan.nextLine();
                continue;}
            switch(number) {
                case 1:
                    returned = logik.getClientsA(login);
                    System.out.println("\n"+"Antal klienter i DB: " + returned);
                    break;
                case 2:
                    returned = logik.getPartnersA(login);
                    System.out.println("\n"+"Antal partnere i DB: " + returned);
                    break;
                case 3:
                    returned = logik.getCompletedOrdersLength(login);
                    System.out.println("\n"+"Antal gennemførte ordre i DB: " + returned);
                    break;
                case 4:
                    System.out.println("\n"+"Nye ordrer:");
                    returned = logik.getOrdersNew(login);
                    System.out.println(returned);
                    break;
                case 5:
                    System.out.println("\n"+"Indtast new OrdreID som du vil slette");
                    OID = scan.nextLine();
                    returned = logik.deleteOrderNew(login, OID);
                    System.out.println(returned);
                    break;
                case 6:
                    System.out.println("\n"+"Igangværende ordrer:");
                    returned = logik.getOrdersInService(login);
                    System.out.println(returned);
                    break;
                case 7:
                    System.out.println("\n"+"Indtast in service OrdreID som du vil slette");
                    OID = scan.nextLine();
                    returned = logik.deleteOrderInService(login, OID);
                    System.out.println(returned);
                    break;
                case 8:
                    scan.close();
                    break whileLoop;
                default: System.out.println("\n"+"Ugyldigt tal, prøv igen");
            }
        }
    }
}