package favorDrop;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by Rasmus on 12-04-2017.
 */

@WebService
public interface LogikI {
    @WebMethod String login(String bruger, String adgangskode) throws Exception;
    @WebMethod Object getClientsA(String token);
    @WebMethod Object getPartnersA(String token);
    @WebMethod Object getSuccededOrdersA(String token);
    @WebMethod Object deleteOrderInService(String token, String OID);
    @WebMethod Object deleteOrderNew(String token, String OID);
    @WebMethod Object getOrdersInService(String token);
    @WebMethod Object getOrdersNew(String token);
    
}
