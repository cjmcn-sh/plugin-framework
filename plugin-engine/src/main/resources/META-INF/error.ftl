<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>出错啦</title>
    <link rel="stylesheet" href="http://libs.baidu.com/bootstrap/3.2.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="http://libs.baidu.com/bootstrap/3.2.0/css/bootstrap-theme.min.css"/>
    <script type="text/javascript" src="http://libs.baidu.com/jquery/2.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="http://libs.baidu.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <h1>您在访问地址:[${uri}]是出错了</h1>

    <h2>请求参数:</h2>
    <pre>
        <span>parameters:${parameters}</span>
        <span>user_id:${user_id}</span>
    </pre>

    <h2>堆栈信息:</h2>
    <pre>
        ${stack}
    </pre>
</div>
</body>
</html>