$(function () {
    let usertype = getQueryString('usertype');
    // 修改平台密码的controller url
    let url = '/local/changelocalpwd';
    $('#submit').bind('click', debounce(function () {
        // 获取帐号
        let userName = $('#userName').val();
        // 获取原密码
        let password = $('#password').val();
        // 获取新密码
        let newPassword = $('#newPassword').val();
        let confirmPassword = $('#confirmPassword').val();
        if (newPassword !== confirmPassword) {
            $.toast('两次输入的新密码不一致!');
            return;
        }
        // 添加表单数据
        let formData = new FormData();
        formData.append('userName', userName);
        formData.append('password', password);
        formData.append('newPassword', newPassword);
        // 获取验证码
        let userInputVerificationCode = $('#captcha').val();
        if (!userInputVerificationCode) {
            $.toast('请输入验证码！');
            return;
        }
        formData.append("userInputVerificationCode", userInputVerificationCode);
        // 将参数post到后台去修改密码
        $.ajax({
            url: url,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    $.toast('修改成功!');
                    let userType = data.userType;
                    if (userType === 1) {
                        // 若用户在前端展示系统页面则自动退回到前端展示系统首页
                        setTimeout(() => {
                            window.location.href = '/frontend/index';
                        }, 1000);

                    } else {
                        // 若用户是在店家管理系统页面则自动回退到店铺列表页中
                        setTimeout(() => {
                            window.location.href = '/shopadmin/shoplist';
                        }, 1000);
                    }
                } else {
                    $.toast('提交失败!' + data.errorMessage);
                    $('#captcha_img').click();
                }
            }
        });
    }));

    $('#back').click(function () {
        if (usertype === '1') {
            // 若用户在前端展示系统页面则自动退回到前端展示系统首页
            window.location.href = '/frontend/index';
        } else {
            // 若用户是在店家管理系统页面则自动回退到店铺列表页中
            window.location.href = '/shopadmin/shoplist';
        }
    });
});
