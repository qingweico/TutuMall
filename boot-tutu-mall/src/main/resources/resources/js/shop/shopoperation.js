$(function () {
    // 从URL里获取shopId参数的值
    let shopId = getQueryString('shopId');
    // 由于店铺注册和编辑使用的是同一个页面,
    // 该标识符用来标明本次是添加还是编辑操作
    let isEdit = !!shopId;
    // 用于店铺注册时候的店铺类别以及区域列表的初始化的URL
    let initUrl = '/shopadmin/getshopinitinfo';
    // 注册店铺的URL
    let registerOrModifyShopUrl = '/shopadmin/registerormodifyshop';
    // 编辑店铺前需要获取店铺信息,这里为获取当前店铺信息的URL
    let shopInfoUrl = "/shopadmin/getshopbyid?shopId=" + shopId;
    // 判断是编辑操作还是注册操作
    if (!isEdit) {
        getShopInitInfo();
    } else {
        getShopInfo(shopId);
    }

    // 通过店铺Id获取店铺信息
    function getShopInfo(shopId) {
        $.getJSON(shopInfoUrl, function (data) {
            if (data.success) {
                // 若访问成功,则依据后台传递过来的店铺信息为表单元素赋值
                let shop = data.shop;
                $('#shop-name').val(shop.shopName);
                $('#shop-addr').val(shop.shopAddress);
                $('#shop-phone').val(shop.phone);
                $('#shop-desc').val(shop.shopDescription);
                // 给店铺类别选定原先的店铺类别值
                let shopCategory = '<option data-id="'
                    + shop.shopCategory.shopCategoryId + '" selected>'
                    + shop.shopCategory.shopCategoryName + '</option>';
                let tempAreaHtml = '';
                // 初始化区域列表
                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">'
                        + item.areaName + '</option>';
                });
                $('#shop-category').html(shopCategory).attr('disabled', 'disabled');
                // 不允许选择店铺类别
                $('#area').html(tempAreaHtml);
                // 给店铺选定原先的所属的区域
                $("#area option[data-id='" + shop.area.areaId + "']").attr(
                    "selected", "selected");
            }
        });
    }

    // 取得所有二级店铺类别以及区域信息,并分别赋值进类别列表以及区域列表
    function getShopInitInfo() {
        $.getJSON(initUrl, function (data) {
            if (data.success) {
                let tempHtml = '';
                let tempAreaHtml = '';
                data.shopCategoryList.map(function (item, index) {
                    tempHtml += '<option data-id="' + item.shopCategoryId
                        + '">' + item.shopCategoryName + '</option>';
                });
                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">'
                        + item.areaName + '</option>';
                });
                $('#shop-category').html(tempHtml);
                $('#area').html(tempAreaHtml);
            }
        });
    }
    // 提交按钮的事件响应, 分别对店铺注册和编辑操作做不同响应
    $('#submit').bind('click', debounce( function () {
        // 创建shop对象
        let shop = {};
        if (isEdit) {
            // 若属于编辑,则给shopId赋值
            shop.shopId = shopId;
        }
        // 获取表单里的数据并填充进对应的店铺属性中
        shop.shopName = $('#shop-name').val();
        shop.shopAddress = $('#shop-addr').val();
        shop.phone = $('#shop-phone').val();
        shop.shopDescription = $('#shop-desc').val();
        // 选择选定好的店铺类别
        shop.shopCategory = {
            shopCategoryId: $('#shop-category').find('option').not(function () {
                return !this.selected;
            }).data('id')
        };
        // 选择选定好的区域信息
        shop.area = {
            areaId: $('#area').find('option').not(function () {
                return !this.selected;
            }).data('id')
        };
        // 获取上传的图片文件流
        let shopImage = $('#shop-img')[0].files[0];
        // 生成表单对象,用于接收参数并传递给后台
        let formData = new FormData();
        // 添加图片流进表单对象里
        formData.append('shopImage', shopImage);
        // 将shop json对象转成字符流保存至表单对象key为shopString的的键值对里
        formData.append('shopString', JSON.stringify(shop));
        formData.append("isRegister", !isEdit);
        // 获取表单里输入的验证码
        let userInputVerificationCode = $('#captcha').val();
        if (!userInputVerificationCode) {
            $.toast('请输入验证码!');
            return;
        }
        formData.append('userInputVerificationCode', userInputVerificationCode);
        // 将数据提交至后台处理相关操作
        $.ajax({
            url: registerOrModifyShopUrl,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    $.toast('提交成功！');
                    if (!isEdit) {
                        // 若为注册操作,成功后返回店铺列表页
                        setTimeout(() => {
                            window.location.href = "/shopadmin/shoplist";
                        }, 1000);
                    } else {
                        setTimeout(() => {
                            window.location.href = "/shopadmin/shopmanagement";
                        }, 1000);

                    }
                } else {
                    if(data.errorMessage !== undefined) {
                        $.toast(data.errorMessage);
                    }else {
                        $.toast("请检查您输入的信息!");
                    }
                }
                // 点击验证码图片的时候,注册码会改变
                $('#captcha_img').click();
            }
        });
    }));

})