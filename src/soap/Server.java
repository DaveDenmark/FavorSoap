package soap;

import javax.xml.ws.Endpoint;

public class Server {
    public static void main(String[] args) throws Exception {
        System.out.println("publicerer Galgelegtjeneste");
        Logik logik = new Logik();
        // Ipv6-addressen [::] svarer til Ipv4-adressen 0.0.0.0, der matcher alle maskinens netkort og
        Endpoint.publish("http://[::]:18372/galgeservice", logik);
        System.out.println("Galgelegtjeneste publiceret.");
    }
}
