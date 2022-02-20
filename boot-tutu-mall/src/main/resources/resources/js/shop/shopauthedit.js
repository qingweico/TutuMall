$(function () {
    let shopAuthId = getQueryString('shopAuthId');
    // 根据主键获取授权信息的URL
    let infoUrl = '/shopadmin/getshopauthmapbyid?shopAuthId=' + shopAuthId;
    // 修改授权信息的URL
    let shopAuthPostUrl = '/shopadmin/modifyshopauthmap';

    if (shopAuthId) {
        getInfo(shopAuthId);
    } else {
        $.toast('用户不存在!');
        window.location.href = '/shopadmin/shopmanage';
    }

    function getInfo(id) {
        $.getJSON(infoUrl, function (data) {
            if (data.success) {
                let shopAuthMap = data.shopAuthMap;
                // 给HTML元素赋值
                $('#shopauth-name').val(shopAuthMap.employee.name);
                $('#title').val(shopAuthMap.title);
            }
        });
    }

    $('#submit').bind('click', debounce(function () {
        let shopAuth = {};
        // 获取要修改的信息并传入后台处理
        shopAuth.employee = {};
        shopAuth.employee.name = $('#shopauth-name').val();
        shopAuth.title = $('#title').val();
        shopAuth.shopAuthId = shopAuthId;
        let userInputVerificationCode = $('#captcha').val();
        if (!userInputVerificationCode) {
            $.toast('请输入验证码!');
            return;
        }
        $.ajax({
            url: shopAuthPostUrl,
            type: 'POST',
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            data: {
                // 将json参数转化为字符串
                shopAuthMapString: JSON.stringify(shopAuth),
                userInputVerificationCode: userInputVerificationCode
            },
            success: function (data) {
                if (data.success) {
                    $.toast('提交成功!');
                    setTimeout(() => {
                        window.location.href = "/shopadmin/shopauthmanagement";
                    }, 1000);
                    $('#captcha_img').click();
                } else {
                    if(data.errorMessage !== undefined) {
                        $.toast(data.errorMessage);
                    }
                    else {
                        $.toast("出错了!");
                    }
                    $('#captcha_img').click();
                }
            }
        });
    }));

});