package app.command;

import javax.servlet.http.HttpServletRequest;

public interface Action {

    Router execute(HttpServletRequest request);
}
