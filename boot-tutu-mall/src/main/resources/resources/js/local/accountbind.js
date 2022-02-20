$(function () {
    //绑定帐号的controller url
    let bindUrl = '/local/bindLocalAuth';
    $('#submit').bind('click', debounce(function () {
        // 获取输入的帐号
        let userName = $('#username').val();
        // 获取输入的密码
        let password = $('#psw').val();
        let needVerify = false;
        // 获取输入的验证码
        let userInputVerificationCode = $('#captcha').val();
        if (!userInputVerificationCode) {
            $.toast('请输入验证码!');
            return;
        }
        // 访问后台, 绑定帐号
        $.ajax({
            url: bindUrl,
            async: false,
            cache: false,
            type: "post",
            dataType: 'json',
            data: {
                userName: userName,
                password: password,
                userInputVerificationCode: userInputVerificationCode
            },
            success: function (data) {
                if (data.success) {
                    $.toast('绑定成功!');
                    let usertype = data.userType;
                    if (usertype === 1) {
                        // 若用户在前端展示系统页面则自动退回到前端展示系统首页
                        setTimeout(() => {
                            window.location.href = '/frontend/index';
                        }, 1000)
                    } else {
                        // 若用户是在店家管理系统页面则自动回退到店铺列表页中
                        setTimeout(() => {
                            window.location.href = '/shop/shoplist';
                        }, 1000)
                    }

                } else {
                    $.toast('提交失败!' + data.errorMessage);
                    $('#captcha_img').click();
                }
            }
        });
    }));
});