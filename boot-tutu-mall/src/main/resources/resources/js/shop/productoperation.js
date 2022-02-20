$(function () {
    // 从URL里获取productId参数的值
    let productId = getQueryString('productId');
    // 通过productId获取商品信息的URL
    let infoUrl = '/shopadmin/getproductbyid?productId=' + productId;
    // 获取当前店铺设定的商品类别列表的URL
    let categoryUrl = '/shopadmin/getproductcategorylist';
    // 更新商品信息的URL
    let addOrModifyProductUrl = '/shopadmin/addormodifyproduct';
    // 由于商品添加和编辑使用的是同一个页面,
    // 该标识符用来标明本次是添加还是编辑操作
    let isEdit = !!productId;
    if (productId) {
        // 若有productId则为编辑操作
        getInfo(productId);
    } else {
        getCategory();
    }

    // 获取需要编辑的商品的商品信息, 并赋值给表单
    function getInfo() {
        $.getJSON(
            infoUrl,
            function (data) {
                if (data.success) {
                    // 从返回的JSON当中获取product对象的信息, 并赋值给表单
                    let product = data.product;
                    $('#product-name').val(product.productName);
                    $('#product-desc').val(product.productDescription);
                    $('#priority').val(product.priority);
                    $('#point').val(product.point);
                    $('#normal-price').val(product.normalPrice);
                    $('#promotion-price').val(product.promotionPrice);
                    // 获取原本的商品类别以及该店铺的所有商品类别列表
                    let optionHtml = '';
                    let optionArr = data.productCategoryList;
                    let optionSelected = product.productCategory.productCategoryId;
                    // 生成前端的HTML商品类别列表, 并默认选择编辑前的商品类别
                    optionArr.map(function (item, index) {
                        let isSelect = optionSelected === item.productCategoryId ? 'selected'
                            : '';
                        optionHtml += '<option data-value="'
                            + item.productCategoryId
                            + '"'
                            + isSelect
                            + '>'
                            + item.productCategoryName
                            + '</option>';
                    });
                    $('#category').html(optionHtml);
                }
            });
    }

    // 为商品添加操作提供该店铺下的所有商品类别列表
    function getCategory() {
        $.getJSON(categoryUrl, function (data) {
            if (data.success) {
                let productCategoryList = data.data;
                let optionHtml = '';
                productCategoryList.map(function (item) {
                    optionHtml += '<option data-value="'
                        + item.productCategoryId + '">'
                        + item.productCategoryName + '</option>';
                });
                $('#category').html(optionHtml);
            }
        });
    }

    // 针对商品详情图控件组, 若该控件组的最后一个元素发生变化（即上传了图片）,
    // 且控件总数未达到6个, 则生成新的一个文件上传控件
    $('.detail-img-div').on('change', '.detail-img:last-child', function () {
        if ($('.detail-img').length < 6) {
            $('#detail-img').append('<input type="file" class="detail-img">');
        }
    });

    // 提交按钮的事件响应, 分别对商品添加和编辑操作做不同响应
    $('#submit').bind('click', debounce(function () {
        // 创建商品json对象, 并从表单里面获取对应的属性值
        let product = {};
        product.productName = $('#product-name').val();
        product.productDescription = $('#product-desc').val();
        product.priority = $('#priority').val();
        product.point = $('#point').val();
        product.normalPrice = $('#normal-price').val();
        product.promotionPrice = $('#promotion-price').val();
        let normal = parseInt(product.normalPrice);
        let promotion = parseInt(product.promotionPrice);
        if(normal === promotion) {
            $.toast('原价和现价不可以一样的!');
            return;
        }
        // 获取选定的商品类别值
        product.productCategory = {
            productCategoryId: $('#category').find('option').not(
                function () {
                    return !this.selected;
                }).data('value')
        };
        product.productId = productId;

        // 获取缩略图文件流
        let thumbnail = $('#small-img')[0].files[0];
        // 生成表单对象, 用于接收参数并传递给后台
        let formData = new FormData();
        const bind = '.detail-img';
        formData.append('thumbnail', thumbnail);
        // 遍历商品详情图控件, 获取里面的文件流
        $(bind).map(
            function (index, item) {
                // 判断该控件是否已选择了文件
                if ($(bind)[index].files.length > 0) {
                    // 将第i个文件流赋值给key为productImg的表单键值对里
                    formData.append('productImage' + index,
                        $(bind)[index].files[0]);
                }
            });
        // 将product json对象转成字符流保存至表单对象key为productStr的的键值对里
        formData.append('productString', JSON.stringify(product));
        formData.append('isModify', JSON.stringify(isEdit));
        // 获取表单里输入的验证码
        let userInputVerificationCode = $('#captcha').val();
        if (!userInputVerificationCode) {
            $.toast('请输入验证码!');
            return;
        }
        formData.append('userInputVerificationCode', userInputVerificationCode);
        // 将数据提交至后台处理相关操作
        $.ajax({
            url: addOrModifyProductUrl,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    $.toast('提交成功!');
                    setTimeout(() => {
                        window.location.href = "/shopadmin/productmanagement";
                    }, 1000);
                    $('#captcha_img').click();
                } else {
                    if(data.errorMessage !== undefined) {
                        $.toast(data.errorMessage);
                    }
                    else {
                        $.toast("请检查商品信息!");
                    }
                    $('#captcha_img').click();
                }
            }
        });
    }));
});