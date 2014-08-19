package er.angular;

import java.util.concurrent.ConcurrentHashMap;

import com.impetus.annovention.ClasspathDiscoverer;
import com.impetus.annovention.Discoverer;
import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;
import com.webobjects.appserver.WODirectAction;

import er.extensions.ERXFrameworkPrincipal;
import er.rest.routes.ERXRouteRequestHandler;

public class ERAngular extends ERXFrameworkPrincipal implements ClassAnnotationDiscoveryListener {

	public static final String FRAMEWORK_NAME = "ERAngular";

	public static final ConcurrentHashMap<String, Class<WODirectAction>> REGISTERED_DIRECT_ACTION_SERVICES = new ConcurrentHashMap<String, Class<WODirectAction>>();

	static {
		setUpFrameworkPrincipalClass(ERAngular.class);
	}

	@Override
	public void finishInitialization() {
		Discoverer discoverer = new ClasspathDiscoverer();
		discoverer.addAnnotationListener(this);
		discoverer.discover();
	}

	@Override
	public void didFinishInitialization() {
		ERXRouteRequestHandler ngHandler = new ERXRouteRequestHandler();
		ngHandler.addRoutes(ERAngularRouteController.class);
		ERXRouteRequestHandler.register(ngHandler);
		log.info("AngularJS Routes Registered. Call /app/ra/ng/<serviceName>/<actionName> to invoke registered angular services.");
	}

	@Override
	public String[] supportedAnnotations() {
		return new String[] { ERAngularService.class.getName() };
	}

	private static void registerDirectActionService(String serviceName, Class<WODirectAction> directActionClass) {
		REGISTERED_DIRECT_ACTION_SERVICES.put(serviceName, directActionClass);
	}

	public static Class<WODirectAction> directActionService(String serviceName) {
		return REGISTERED_DIRECT_ACTION_SERVICES.get(serviceName);
	}

	@Override
	public void discovered(String className, String annotationName) {
		if (annotationName.equals(ERAngularService.class.getName())) {
			try {
				ERAngularService service = Class.forName(className).getAnnotation(ERAngularService.class);
				switch (service.serviceType()) {
				case DIRECT_ACTION:
					Class<WODirectAction> directActionClass = (Class<WODirectAction>) Class.forName(className);
					registerDirectActionService(service.serviceName(), directActionClass);
					break;
				case COMPONENT:
					break;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
