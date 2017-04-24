package favorDrop;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.core.Cookie;

/**
 * Created by Rasmus on 12-04-2017.
 */

@WebService
public interface LogikI {
   // @WebMethod Cookie login(String bruger, String adgangskode) throws Exception;
    @WebMethod boolean login2(String bruger, String adgangskode) throws Exception;
    @WebMethod String getClients(Cookie cookie);
    @WebMethod String getOrders(Cookie cookie);
    @WebMethod String getPartners(Cookie cookie);
}
