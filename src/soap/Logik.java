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
import javax.ws.rs.core.NewCookie;
import sun.net.www.http.HttpClient;


@WebService(endpointInterface = "soap.LogikI")
public class Logik implements LogikI {
    private Brugeradmin ba;
    private String brugerNavn;
    private String adgangsKode;

    public Cookie login(String bruger, String adgangskode) {
        try {
            URL url = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
            QName qname = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
            Service service = Service.create(url, qname);
            ba = service.getPort(Brugeradmin.class);
            ba.hentBruger(bruger, adgangskode);
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
       return new NewCookie(bruger, adgangskode);
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
        String response = null;
        try {
            if (cookie != null) {
               ba.hentBruger(cookie.getName(), cookie.getValue());
               response = makeServiceCall("https://favordrop.firebaseio.com/orders.json");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }   

    public String getClients(Cookie cookie) {
        String response = null;
        try {
            if (cookie != null) {
               ba.hentBruger(cookie.getName(), cookie.getValue());
               response = makeServiceCall("https://favordrop.firebaseio.com/clients.json");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    
    public String getPartners(Cookie cookie) {
        String response = null;
        try {
            if (cookie != null) {
               ba.hentBruger(cookie.getName(), cookie.getValue());
               response = makeServiceCall("https://favordrop.firebaseio.com/partners.json");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private String convertStreamToString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        } 
        finally {
            try {
                in.close();
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
        }
}
