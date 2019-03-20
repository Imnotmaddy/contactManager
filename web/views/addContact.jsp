<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 07.03.2019
  Time: 21:58
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add contact</title>
</head>
<body>
<form action="/addContact" method="post" name="commentform" id="commentform">
    <p><input type="text" name="author" id="author" value="" size="25"/>
        <small> Имя</small>
    </p>
    <p><input type="text" name="email" id="email" value="" size="25"/>
        <small> Mail</small>
    </p>
    <p><textarea name="comment" id="comment" cols="48" rows="8"> </textarea>
    </p>
    <p><input name="submit" type="submit" id="submit" value="Отправить"/>
    </p>
</form>
</body>
</html>
