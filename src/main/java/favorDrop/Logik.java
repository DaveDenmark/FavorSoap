package favorDrop;

import brugerautorisation.transport.soap.Brugeradmin;
import io.jsonwebtoken.JwtBuilder;
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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.DatatypeConverter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebService(endpointInterface = "favorDrop.LogikI")
public class Logik {
    private Brugeradmin ba;
    private String brugerNavn;
    private String adgangsKode;
    private String auth;
    private final int logInTimeMax = 600000;
    
    public String login(String bruger, String adgangskode) throws Exception {
        try {
            URL url = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
            QName qname = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
            Service service = Service.create(url, qname);
            ba = service.getPort(Brugeradmin.class);
            ba.hentBruger(bruger, adgangskode);
        }
        catch (Throwable e) {
            return "Not authorized";
        }
        
        return createJWT(bruger, "Soap Server", "Favor Drop Client", System.currentTimeMillis()+logInTimeMax);
    }
    
    public String makeServiceCall(String reqUrl) {
        System.out.println("starter service kald");
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
        System.out.println("slutter service kald");
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
    
    public String makeServiceCallDelete(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
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
    
    public Object getClientsA(String token) {
        String response;
        int count = 0;
        
        try {
            parseJWT(token);
            System.out.println("Ægtetete token: " + token);
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
        catch(Exception e) {
            System.out.println("lorte token: " + token);
            return "Adgang nægtet";
        }
    }
    
    public Object getPartnersA(String token) {
        String response;
        int count = 0;
        try {
            parseJWT(token);
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
        catch(Exception e) {
            response = "Adgang nægtet";
            return response;
        }
    }
    
    public Object getCompletedOrdersLength(String token) {
        String response;
        int count = 0;
        try {
            parseJWT(token);
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
        catch(Exception e) {
            response = "Adgang nægtet";
            return response;
        }
    }
    
    public Object deleteOrderNew(String token, String OID) {
        String response;
        try {
            parseJWT(token);
            response = makeServiceCall("https://favordrop.firebaseio.com/orders/new/"+OID+".json");
            
            System.out.println(response);
            
            if (!("null\n".equals(response))) {
                System.out.println("response 2: " + response);
                JSONObject jsonObj = new JSONObject(response);
                String clientID = jsonObj.getString("client id");
                String a = makeServiceCallDelete("https://favordrop.firebaseio.com/orders/new/"+OID+".json");
                String b = makeServiceCallDelete("https://favordrop.firebaseio.com/clients/" + clientID.substring(clientID.lastIndexOf('/') + 1, clientID.length()) + "/orders/new/"+OID+".json");
                
                if ("null\n".equals(a) && "null\n".equals(b)) {
                    response = "Ordren blev slettet med succes";
                }
                else {
                    response = "Noget gik galt under sletningen";
                }
                
            }
            else {
                response = "Du forsøger at slette en ikke eksisterende ordre";
            }
        }
        catch (JSONException ex) {
            Logger.getLogger(Logik.class.getName()).log(Level.SEVERE, null, ex);
            response = "noget gik galt med data formatteringen. Kontakt adminstrator";
        }
        catch (Exception e) {
            response = "Adgang nægtet";
            System.out.println("response 4: " + response);
        }
        return response;
    }
    
    public Object deleteOrderInService(String token, String OID) {
        String response;
        try {
            parseJWT(token);
            response = makeServiceCall("https://favordrop.firebaseio.com/orders/inservice/"+OID+".json");
            
            System.out.println(response);
            
            if (!("null\n".equals(response))) {
                System.out.println("response 2: " + response);
                JSONObject jsonObj = new JSONObject(response);
                String uID = jsonObj.getString("client id");
                String pID = jsonObj.getString("partner id");
                String a = makeServiceCallDelete("https://favordrop.firebaseio.com/orders/inservice/"+OID+".json");
                String b = makeServiceCallDelete("https://favordrop.firebaseio.com/clients/" + uID.substring(uID.lastIndexOf('/') + 1,uID.length()) + "/orders/inservice/"+OID+".json");
                String c = makeServiceCallDelete("https://favordrop.firebaseio.com/partners/" + pID.substring(pID.lastIndexOf('/') + 1,pID.length()) + "/orders/inservice/"+OID+".json");
                
                if ("null\n".equals(a) && "null\n".equals(b) && "null\n".equals(c)) {
                    response = "Ordren blev slettet med succes";
                }
                else {
                    response = "Noget gik galt under sletningen";
                }
                
            }
            else {
                response = "Du forsøger at slette en ikke eksisterende ordre";
            }
        }
        catch (JSONException ex) {
            Logger.getLogger(Logik.class.getName()).log(Level.SEVERE, null, ex);
            response = "noget gik galt med data formatteringen. Kontakt adminstrator";
        }
        catch (Exception e) {
            response = "Adgang nægtet";
            System.out.println("response 4: " + response);
        }
        return response;
    }
    
    //Sample method to construct a JWT
    private String createJWT(String id, String issuer, String subject, long ttlMillis) {
        
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        
        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("detenhemmelighed");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        
        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);
        
        //if it has been specified, let's add the expiration
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        
        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }
    
//Sample method to validate and read the JWT
    private void parseJWT(String jwt) {
        
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary("detenhemmelighed"))
                .parseClaimsJws(jwt).getBody();
        System.out.println("ID: " + claims.getId());
        System.out.println("Subject: " + claims.getSubject());
        System.out.println("Issuer: " + claims.getIssuer());
        System.out.println("Expiration: " + claims.getExpiration());
    }
    
    public String getOrdersInService(String token) {
        String response;
        try {
            parseJWT(token);
            response = makeServiceCall("https://favordrop.firebaseio.com/orders/inservice.json");
            ArrayList<String> ordersList = new ArrayList<String>();
            if (response != null) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    // JSONObject orders = jsonObj.getJSONObject("completed");
                    // Getting JSON Array node
                    Iterator<?> keys = jsonObj.keys();
                    
                    while ( keys.hasNext() ) {
                        String key = (String) keys.next();
                        if (jsonObj.get(key) instanceof JSONObject) {
                            ordersList.add(key);
                        }
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(ordersList);
            return ordersList.toString();
        }
        catch(Exception e) {
            response = "Adgang nægtet";
            return response;
        }
    }
    
    public String getOrdersNew(String token) {
        String response;
        try {
            parseJWT(token);
            response = makeServiceCall("https://favordrop.firebaseio.com/orders/new.json");
            ArrayList<String> ordersList = new ArrayList<String>();
            if (response != null) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    // JSONObject orders = jsonObj.getJSONObject("completed");
                    // Getting JSON Array node
                    Iterator<?> keys = jsonObj.keys();
                    
                    while ( keys.hasNext() ) {
                        String key = (String) keys.next();
                        if (jsonObj.get(key) instanceof JSONObject) {
                            ordersList.add(key);
                        }
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(ordersList);
            return ordersList.toString();
        }
        catch(Exception e) {
            response = "Adgang nægtet";
            return response;
        }
    }
    
    
    
}

