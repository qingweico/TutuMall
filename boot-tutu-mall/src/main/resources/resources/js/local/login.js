$(function () {
    // 登录验证的controller url
    let loginUrl = '/local/logincheck';

    let userType = '';
    // 登录次数, 累积登录三次失败之后自动弹出验证码要求输入
    let loginCount = 0;
    $('#submit').bind("click", debounce(function () {
        // 获取输入的帐号
        let userName = $('#username').val();
        // 获取输入的密码
        let password = $('#psw').val();
        if (userName === '') {
            $.toast('请输入用户名!');
            return;
        }
        if (password === '') {
            $.toast('请输入密码!');
            return;
        }
        // 获取验证码信息
        let userInputVerificationCode = $('#captcha').val();
        // 是否需要验证码验证，默认为false,即不需要
        let needVerify = false;
        // 如果登录三次都失败
        if (loginCount >= 3) {
            // 那么就需要验证码校验了
            if (!userInputVerificationCode) {
                $.toast('请输入验证码!');
                return;
            } else {
                needVerify = true;
            }
        }
        // 访问后台进行登录验证
        $.ajax({
            url: loginUrl,
            async: false,
            cache: false,
            type: "post",
            dataType: 'json',
            data: {
                userName: userName,
                password: password,
                userInputVerificationCode: userInputVerificationCode,
                //是否需要做验证码校验
                needVerify: needVerify
            },
            success: function (data) {
                if (data.success) {
                    $.toast('登录成功!');
                    userType = data.userType;
                    if (userType === 1) {
                        // 若用户在前端展示系统页面则自动链接到前端展示系统首页
                        setTimeout(() => {
                            window.location.href = '/frontend/index';
                        }, 1000)
                    } else {
                        // 若用户是在店家管理系统页面则自动链接到店铺列表页中
                        setTimeout(() => {
                            window.location.href = '/shopadmin/shoplist';
                        }, 1000);
                    }
                } else {
                    $.toast('登录失败!' + data.errorMessage);
                    loginCount++;
                    if (loginCount >= 3) {
                        // 登录失败三次，需要做验证码校验
                        $('#verifyPart').show();
                    }
                }
            }
        });
    }));
});