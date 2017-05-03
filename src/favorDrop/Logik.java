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
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;


@WebService(endpointInterface = "favorDrop.LogikI")
public class Logik {
    private Brugeradmin ba;
    private String brugerNavn;
    private String adgangsKode;
    private String auth;
    
    public boolean login(String bruger, String adgangskode) throws Exception {
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
        ba.setEkstraFelt(bruger, adgangskode, "timer", System.currentTimeMillis()+100000);
        
        return true;
    }
    
    public boolean checkAuth(String brugerNavn, String adgangskode) {
        try {
            if(((boolean)ba.getEkstraFelt(brugerNavn, adgangskode, "auth") == true) && (System.currentTimeMillis()<=(long)ba.getEkstraFelt(brugerNavn, adgangskode, "timer"))) {     
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
        try {
            if (checkAuth(brugerNavn, adgangskode)) {
                response = makeServiceCall("http://52.213.91.0:8080/FavorDrop_war/orders/");
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
                response = makeServiceCall("http://52.213.91.0:8080/FavorDrop_war/clients/");
            }
            else {
                response = "Adgang nægtet";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    
    public String getPartners(String brugerNavn, String adgangskode) {
        String response = null;
        try {
            if (checkAuth(brugerNavn, adgangskode)) {
                response = makeServiceCall("http://52.213.91.0:8080/FavorDrop_war/partners/");
            }
            else {
                response = "Adgang nægtet";
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
    
    public Object getClientsA(String brugerNavn, String adgangskode) {
        String response;
        int count = 0;
        if (checkAuth(brugerNavn, adgangskode)) {
            response = makeServiceCall("https://favordrop.firebaseio.com/clients.json");
            if (response != null) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    // Getting JSON Array node
                    Iterator<?> keys = jsonObj.keys();
                    
                    while ( keys.hasNext() ) {
                        String key = (String) keys.next();
                        if (jsonObj.get(key) instanceof JSONObject) {
                            count++;
                        }
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
            }
            return count;
        }
        else {
            response = "Adgang nægtet";
            return response;
        }
    }
    
    public Object getPartnersA(String brugerNavn, String adgangskode) {
        String response;
        int count = 0;
        if (checkAuth(brugerNavn, adgangskode)) {
            response = makeServiceCall("https://favordrop.firebaseio.com/partners.json");
            if (response != null) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    // Getting JSON Array node
                    Iterator<?> keys = jsonObj.keys();
                    
                    while ( keys.hasNext() ) {
                        String key = (String) keys.next();
                        if (jsonObj.get(key) instanceof JSONObject) {
                            count++;
                        }
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
            }
            return count;
        }
        else {
            response = "Adgang nægtet";
            return response;
        }
    }
    
    public Object getSuccededOrdersA(String brugerNavn, String adgangskode) {
        String response;
        int count = 0;
        if (checkAuth(brugerNavn, adgangskode)) {
            response = makeServiceCall("https://favordrop.firebaseio.com/orders/completed.json");
            if (response != null) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                   // JSONObject orders = jsonObj.getJSONObject("completed");
                    // Getting JSON Array node
                    Iterator<?> keys = jsonObj.keys();
                    
                    while ( keys.hasNext() ) {
                        String key = (String) keys.next();
                        if (jsonObj.get(key) instanceof JSONObject) {
                            count++;
                        }
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
            }
            return count;
        }
        else {
            response = "Adgang nægtet";
            return response;
        }
    }
}
