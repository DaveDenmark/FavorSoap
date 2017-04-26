package favorDrop;

import brugerautorisation.transport.soap.Brugeradmin;
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
import java.util.Iterator;
import javax.ws.rs.core.NewCookie;
import org.json.JSONArray;
import org.json.JSONException;
import sun.net.www.http.HttpClient;
import org.json.JSONObject;


@WebService(endpointInterface = "favorDrop.LogikI")
public class Logik {
    private Brugeradmin ba;
    private String brugerNavn;
    private String adgangsKode;
    private String auth;
    
//    public Cookie login(String bruger, String adgangskode) throws Exception {
//        try {
//            URL url = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
//            QName qname = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
//            Service service = Service.create(url, qname);
//            ba = service.getPort(Brugeradmin.class);
//            ba.hentBruger(bruger, adgangskode);
//        }
//        catch(Throwable e) {
//            e.printStackTrace();
//            return null;
//        }
//       return new NewCookie(bruger, adgangskode);
//    }
    
//    public NewCookie createCookie(String bruger, String adgangskode) throws Exception {
//       NewCookie c = new NewCookie(bruger, adgangskode);
//        return c;
//    }
    
    public boolean login2(String bruger, String adgangskode) throws Exception {
        try {
            URL url = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
            QName qname = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
            Service service = Service.create(url, qname);
            ba = service.getPort(Brugeradmin.class);
            ba.hentBruger(bruger, adgangskode);
        }
        catch (Throwable e) {
            return false;
        }
        ba.setEkstraFelt(bruger, adgangskode, "auth", true);
        return true;
    }
    
    public boolean checkAuth(String brugerNavn, String adgangskode) {
        try {
            if((boolean)ba.getEkstraFelt(brugerNavn, adgangskode, "auth") == true) {
                return true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
    
    public String getOrders(String brugerNavn, String adgangskode) {
        String response = null;
        String test = null;
        try {
            if (checkAuth(brugerNavn, adgangskode)) {
                response = makeServiceCall("https://favordrop.firebaseio.com/orders.json");
                //   test = jsonParser(response);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    
    public String getClients(String brugerNavn, String adgangskode) {
        String response = null;
        try {
            if (checkAuth(brugerNavn, adgangskode)) {
                response = makeServiceCall("https://favordrop.firebaseio.com/clients.json");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    
        public int getClientsA(String brugerNavn, String adgangskode) {
        String response = null;
        int amount = 0;
        try {
            if (checkAuth(brugerNavn, adgangskode)) {
                response = makeServiceCall("https://favordrop.firebaseio.com/clients.json");
                amount = getClientsAmount(response);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return amount;
    }
    
    
    
    public String getPartners(String brugerNavn, String adgangskode) {
        String response = null;
        try {
            if (checkAuth(brugerNavn, adgangskode)) {
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
//    public String jsonParser(String JSONString) throws JSONException {
//        String string = "";
//        JSONObject root = new JSONObject(JSONString);
//        JSONObject completeArray = root.getJSONObject("completed");
//        JSONObject firstComplet = completeArray.getJSONObject(0);
//        String status = firstComplet.getString("status");
//
//        return status;
//}
    
    public int getClientsAmount(String jsonStr) throws JSONException {
        int count = 0;
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                
                // Getting JSON Array node
                // JSONObject orders = jsonObj.getJSONObject("new");
                Iterator<?> keys = jsonObj.keys();
                
// looping through All Contacts
    
while ( keys.hasNext() ) {
    String key = (String) keys.next();
    if (jsonObj.get(key) instanceof JSONObject) {
        count++;
      //  JSONObject c = (JSONObject) jsonObj.get(key);
    }
    
    
    
}
            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }
        return count;
    }
}
