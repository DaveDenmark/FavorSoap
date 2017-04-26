package favorDrop;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.core.Cookie;

/**
 * Created by Rasmus on 12-04-2017.
 */

@WebService
public interface LogikI {
    @WebMethod boolean login2(String bruger, String adgangskode) throws Exception;
    @WebMethod String getClients(String user, String adgangskode);
    @WebMethod String getOrders(String user, String adgangskode);
    @WebMethod String getPartners(String user, String adgangskode);
    @WebMethod int getClientsA(String user, String adgangskode);
}
