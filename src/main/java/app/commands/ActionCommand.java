package app.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ActionCommand {
    String execute(HttpServletRequest request, HttpServletResponse response);


    default String getDefaultPage(){
        return PagePaths.INDEX_PAGE.getJspPath();
    }

}
