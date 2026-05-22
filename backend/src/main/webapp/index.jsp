<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Test JSP and Tailwind CSS</title>
        <link rel="stylesheet" href="css/output.css" />
</head>
<body>
<h1>TEST 456</h1>
<h2>Wassupppppp!</h2>
<p class="text-lg font-bold">Current date: <%= new java.util.Date() %></p>
<p class="text-md">Current time: <%= System.currentTimeMillis() %></p>
<p class="text-sm">Random number: <%= Math.random() %></p>
</body>
</html>
