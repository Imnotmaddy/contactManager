package app.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/add")
public class AddServlet extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> strings = new ArrayList<String>();
        strings.add("Mike");
        strings.add("John");
        strings.add("Whatever");
        req.setAttribute("data",strings);
        getServletContext().getRequestDispatcher("/views/add.jsp").forward(req,resp);
    }
}
