$(function () {
    // 从URL里获取awardId参数的值
    let awardId = getQueryString('awardId');
    // 通过awardId获取奖品信息的URL
    let infoUrl = '/shopadmin/getawardbyid?awardId=' + awardId;
    // 更新奖品信息的URL
    let addOrModifyAwardPostUrl = '/shopadmin/addormodifyaward';
    // 由于奖品添加和编辑使用的是同一个页面，
    // 该标识符用来标明本次是添加还是编辑操作
    let isEdit = !!awardId;
    if (awardId) {
        // 若有awardId则为编辑操作
        getInfo(awardId);
    }

    // 获取需要编辑的奖品信息，并赋值给表单
    function getInfo(id) {
        $.getJSON(infoUrl, function (data) {
            if (data.success) {
                // 从返回的JSON当中获取award对象的信息，并赋值给表单
                let award = data.award;
                $('#award-name').val(award.awardName);
                $('#priority').val(award.priority);
                $('#award-desc').val(award.awardDescription);
                $('#point').val(award.point);
            }
        });
    }

    // 提交按钮的事件响应，分别对奖品添加和编辑操作做不同响应
    $('#submit').bind('click', debounce(function () {
        // 创建奖品json对象，并从表单里面获取对应的属性值
        let award = {};
        award.awardName = $('#award-name').val();
        award.priority = $('#priority').val();
        award.awardDescription = $('#award-desc').val();
        award.point = $('#point').val();
        award.awardId = awardId ? awardId : '';
        // 获取缩略图文件流
        let thumbnail = $('#small-img')[0].files[0];
        // 生成表单对象，用于接收参数并传递给后台
        let formData = new FormData();
        formData.append('thumbnail', thumbnail);
        // 将award json对象转成字符流保存至表单对象key为awardStr的的键值对里
        formData.append('awardString', JSON.stringify(award));
        // 获取表单里输入的验证码
        formData.append('isModify', JSON.stringify(isEdit));
        let userInputVerificationCode = $('#captcha').val();
        if (!userInputVerificationCode) {
            $.toast('请输入验证码!');
            return;
        }
        formData.append("userInputVerificationCode", userInputVerificationCode);
        // 将数据提交至后台处理相关操作
        $.ajax({
            url: addOrModifyAwardPostUrl,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    $.toast('提交成功!');
                    setTimeout(() => {
                        window.location.href = "/shopadmin/awardmanagement";
                    }, 1000);
                    $('#captcha_img').click();
                } else {
                   if(data.errorMessage !== undefined) {
                       $.toast(data.errorMessage);
                   }
                   else {
                       $.toast("请检查您输入的信息!");

                   }
                    $('#captcha_img').click();
                }
            }
        });
    }));

});