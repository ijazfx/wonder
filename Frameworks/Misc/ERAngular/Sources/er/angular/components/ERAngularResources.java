package er.angular.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;

import er.angular.ERAngular;
import er.extensions.appserver.ERXResponseRewriter;
import er.extensions.components.ERXStatelessComponent;

public class ERAngularResources extends ERXStatelessComponent {

	public static final String ANGULAR_JS = "bower_components/angular/angular.min.js";

	public static final String ANGULAR_ROUTE_JS = "bower_components/angular-route/angular-route.js";

	public static final String ANIMATE_CSS = "animate.css";

	public ERAngularResources(WOContext context) {
		super(context);
	}

	@Override
	public void appendToResponse(WOResponse response, WOContext context) {
		super.appendToResponse(response, context);
		// Add scripts to head tag.
		ERXResponseRewriter.addScriptResourceInHead(response, context, ERAngular.FRAMEWORK_NAME, ANGULAR_JS);
		ERXResponseRewriter.addScriptResourceInHead(response, context, ERAngular.FRAMEWORK_NAME, ANGULAR_ROUTE_JS);
		// Add stylesheets to head tag.
		ERXResponseRewriter.addStylesheetResourceInHead(response, context, ERAngular.FRAMEWORK_NAME, ANIMATE_CSS);
	}

}