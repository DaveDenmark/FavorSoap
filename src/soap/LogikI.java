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
    @WebMethod boolean addClient(Cookie cookie) throws Exception;
    @WebMethod boolean addOrder(Cookie cookie);
    @WebMethod boolean getUserOrders(Cookie cookie);
    @WebMethod boolean getClient(Cookie cookie);
    @WebMethod boolean updateClient(Cookie cookie);
    @WebMethod boolean updateOrder(Cookie cookie);
    @WebMethod boolean updateOrderToAccepted(Cookie cookie);
    @WebMethod boolean updateOrderToFailed(Cookie cookie);
    @WebMethod boolean updateOrderToSuccessfull(Cookie cookie);
}
