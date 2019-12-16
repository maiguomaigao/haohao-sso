<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8" />
    <title>websample - index</title>
</head>
<body>

    <div style="text-align: center;margin-top: 100px;">
        <h3>Hi ${app_ticket.userName}  <a href="${request.contextPath}/logout">Logout</a></h3>
        <div>${app_ticket}</div>
    </div>
</body>
</html>