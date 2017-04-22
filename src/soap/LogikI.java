package soap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.core.Cookie;

/**
 * Created by Rasmus on 12-04-2017.
 */
@WebService 
public interface LogikI {
    @WebMethod Cookie login(String bruger, String adgangskode) throws Exception;
    @WebMethod boolean getClients(Cookie cookie);
    @WebMethod boolean getOrders(Cookie cookie);
    @WebMethod boolean getPartners(Cookie cookie);
}
