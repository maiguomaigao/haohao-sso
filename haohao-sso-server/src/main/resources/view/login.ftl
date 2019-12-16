<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <#import "common/macro.ftl" as macro>
    <#import "./common/spring.ftl" as spring>
    <@macro.commonStyle />
    <link rel="stylesheet" href="adminlte/plugins/iCheck/square/blue.css"/>

    <title><@spring.message code="login.title"/></title>
</head>
<body class="hold-transition login-page">

<div class="login-box">
    <div class="login-logo">
        <a><img src="./logo.png"></a>
    </div>
    <form action="${request.contextPath}/doLogin" method="post" onsubmit="checkForm()">
        <div class="login-box-body">
            <a href="?lang=en" >English</a>
            <a href="?lang=zh_CN" >简体中文</a>
            <p class="login-box-msg"><@spring.message code="application.name" /></p>
            <div class="form-group has-feedback">
                <input type="text" name="merchantCode" class="form-control" placeholder="<@spring.message "login.merchantCode.placeHolder"/>" value="default" maxlength="50" >
                <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="text" name="loginName" class="form-control" placeholder="<@spring.message "login.loginName.placeHolder"/>" value="haohao" maxlength="50" >
                <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="password" name="password" class="form-control" placeholder="<@spring.message "login.password.placeHolder"/>" value="test001" maxlength="50" >
                <input type="hidden" id="password"  value="">
                <span class="glyphicon glyphicon-lock form-control-feedback"></span>
            </div>

            <#if errorMsg?exists>
                <p style="color: red;">${errorMsg}</p>
            </#if>

            <div class="row">
                <div class="col-xs-8">
                    <div class="checkbox icheck">
                        <label>
                            &nbsp;&nbsp;&nbsp;<input type="checkbox" name="rememberMe" ><@spring.message "login.rememberMe"/>
                        </label>
                    </div>
                </div><!-- /.col -->
                <div class="col-xs-4">
                    <input type="hidden" name="app" value="${app!''}" />
                    <input type="hidden" name="ref" value="${ref!''}" />
                    <button type="submit" class="btn btn-primary btn-block btn-flat"><@spring.message "login.submit"/></button>
                </div>
            </div>
        </div>
    </form>
</div>

</body>
<@macro.commonScript />
<script src="/adminlte/plugins/iCheck/icheck.min.js"/>
<script src="/js/plugins/crypto-js/sha256.js"/>
<script src="/js/plugins/crypto-js/enc-base64.js"/>
<script src="/js/plugins/crypto-js/md5.js"/>
<script src="/js/login.1.js"/>
</html>