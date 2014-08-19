/**
 * 
 */
package er.angular;

import java.lang.reflect.Constructor;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WODirectAction;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;

import er.extensions.eof.ERXKeyFilter;
import er.rest.routes.ERXRouteController;
import er.rest.routes.jsr311.GET;
import er.rest.routes.jsr311.Path;
import er.rest.routes.jsr311.PathParam;

/**
 * @author fijaz
 * 
 */
public class ERAngularRouteController extends ERXRouteController {

	public ERAngularRouteController(WORequest request) {
		super(request);
	}

	@Path("/ng/{serviceName:String}/{actionName:String}")
	@GET
	public WOActionResults invokeAction(@PathParam("serviceName") String serviceName, @PathParam("actionName") String actionName) {
		try {
			Class<WODirectAction> directActionClass = ERAngular.directActionService(serviceName);
			if (directActionClass != null) {
				Constructor<WODirectAction> constructor = directActionClass.getConstructor(WORequest.class);
				WODirectAction da = constructor.newInstance(request());
				WOActionResults results = da.performActionNamed(actionName);
				// The filter should be provided by the user though.
				return response(results, ERXKeyFilter.filterWithAll());
			}
			return response(WOResponse.HTTP_STATUS_NOT_FOUND);
		} catch (Exception ex) {
			return response(WOResponse.HTTP_STATUS_INTERNAL_ERROR);
		}
	}

}
