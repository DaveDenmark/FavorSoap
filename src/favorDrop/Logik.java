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
    
    public int getClientsA(String brugerNavn, String adgangskode) {
        String response = null;
        int count = 0;
        if (checkAuth(brugerNavn, adgangskode)) {
            response = makeServiceCall("http://52.213.91.0:8080/FavorDrop_war/clients/");
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
        }
        return count;
    }
    
    public int getPartnersA(String brugerNavn, String adgangskode) {
        String response = null;
        int count = 0;
        if (checkAuth(brugerNavn, adgangskode)) {
            response = makeServiceCall("http://52.213.91.0:8080/FavorDrop_war/partners/");
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
        }
        return count;
    }
    
    public int getOrdersA(String brugerNavn, String adgangskode) {
        String response = null;
        int count = 0;
        if (checkAuth(brugerNavn, adgangskode)) {
            response = makeServiceCall("http://52.213.91.0:8080/FavorDrop_war/orders/");
            if (response != null) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONObject orders = jsonObj.getJSONObject("completed");
                    // Getting JSON Array node
                    Iterator<?> keys = orders.keys();
                    
                    while ( keys.hasNext() ) {
                        String key = (String) keys.next();
                        if (orders.get(key) instanceof JSONObject) {
                            count++;
                        }
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return count;
    }
    
    
}
