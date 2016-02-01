<%-- 
    Document   : test
    Created on : 2016/1/7, 上午 11:54:24
    Author     : G718
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
        <script language=javascript>
            var url = "http://192.168.11.96:8088/RestApache/webresources/WS_TCS201/GetID/V?callback=?"; 
            var revData = "";
            $.getJSON(url, 
                function (data)
                {
                    alert(data.record.length);
                    for (var i = 0; i < data.record.length; i++) 
                    {
                        revData = revData + data.record[i].TCENAM;
                    }
                    document.getElementById("comments").value = revData;
                });
        </script>
    </head>
    <body>
        <div>
            aaaaaa
            bbbbbb
            cccccc
            Data:<BR>
            TEST:<BR>
            ABC:<BR>
        </div>
            <TEXTAREA id="comments" COLS=40 ROWS=6></TEXTAREA>
    </body>
</html>
