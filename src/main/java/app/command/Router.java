package app.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Router {

    public enum RouteType {
        FORWARD,
        REDIRECT
    }

    private String pagePath;

    private RouteType routeType = RouteType.FORWARD;

    public RouteType getRoute() {
        return routeType;
    }

    public void setRoute(RouteType routeType) {
        this.routeType = routeType;

    }
}
