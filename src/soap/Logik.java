package soap;

import brugerauth.Bruger;
import brugerauth.Brugeradmin;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import javax.jws.WebService;
import javax.ws.rs.core.Cookie;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import sun.net.www.http.HttpClient;


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
  public String makeServiceCall(String reqUrl) {
            String response = null;
            try {
                URL url = new URL(reqUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response = convertStreamToString(in);
            } catch (MalformedURLException e) {
                
            } catch (ProtocolException e) {
                
            } catch (IOException e) {
                
            } catch (Exception e) {
                
            }
            return response;
        }

    public String getOrders(Cookie cookie) {
       String get = makeServiceCall("https://favordrop.firebaseio.com/orders.json");
        return get;
    }   

    public boolean getClients(Cookie cookie) {
        return false;
    }
    
    public boolean getPartners(Cookie cookie) {
        return false;
    }

    private String convertStreamToString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
}
