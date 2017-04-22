package soap;

import brugerauth.Bruger;
import brugerauth.Brugeradmin;
import javax.jws.WebService;
import javax.ws.rs.core.Cookie;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

@WebService(endpointInterface = "soap.LogikI")
public class Logik implements LogikI {
    private Brugeradmin ba;
    private String brugerNavn;
    private String adgangsKode;

    public Cookie login(String bruger, String adgangskode) throws Exception {
        System.out.println("Du skal logge ind før du kan benytte SOAP API'et");

        URL url = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
        QName qname = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
        Service service = Service.create(url, qname);
        ba = service.getPort(Brugeradmin.class);

        Bruger b = ba.hentBruger(bruger, adgangskode);
        brugerNavn = bruger;
        adgangsKode = adgangskode;
        return new Cookie(bruger,adgangskode);
    }

    public boolean addClient(Cookie cookie) throws Exception{
        Bruger b = ba.hentBruger(cookie.getName(), cookie.getValue());

        // Tilføj bruger til firebase database
        //

        return false;
    }

    public boolean addOrder(Cookie cookie) {
        return false;
    }

    public boolean getUserOrders(Cookie cookie) {
        return false;
    }

    public boolean getClient(Cookie cookie) {
        return false;
    }

    public boolean updateClient(Cookie cookie) {
        return false;
    }

    public boolean updateOrder(Cookie cookie) {
        return false;
    }

    public boolean updateOrderToAccepted(Cookie cookie) {
        return false;
    }

    public boolean updateOrderToFailed(Cookie cookie) {
        return false;
    }

    public boolean updateOrderToSuccessfull(Cookie cookie) {
        return false;
    }


}
