package favorDrop;

import javax.xml.ws.Endpoint;

public class Server {
    public static void main(String[] args) throws Exception {
        System.out.println("Publicerer FavorDropSoap");
        Logik logik = new Logik();
        // Ipv6-addressen [::] svarer til Ipv4-adressen 0.0.0.0, der matcher alle maskinens netkort og
        Endpoint.publish("http://[::]:18372/FavorDropSoap", logik);
        System.out.println("FavorDropSoap publiceret");
    }
}
