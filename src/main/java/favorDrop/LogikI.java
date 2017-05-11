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
    @WebMethod Object deleteorderInService(String token, String OID);
    @WebMethod Object deleteorderNew(String token, String OID);
}
