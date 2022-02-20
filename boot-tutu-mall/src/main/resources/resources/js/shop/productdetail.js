$(function () {
    // 从地址栏的URL里获取productId
    let productId = getQueryString('productId');
    // 获取商品信息的URL
    let productUrl = '/frontend/listproductdetailpageinfo?productId='+ productId;
    // 访问后台获取该商品的信息并渲染
    $.getJSON(productUrl, function (data) {
        if (data.success) {
            // 获取商品信息
            let product = data.product;
            // 给商品信息相关HTML控件赋值

            // 商品缩略图
            $('#product-img').attr('src', product.imageAddress);
            // 商品更新时间
            $('#product-time').text(new Date(product.lastEditTime).Format("yyyy-MM-dd"));
            if (product.point !== null) {
                $('#product-point').text('购买可得' + product.point + '积分');
            }
            // 商品名称
            $('#product-name').text(product.productName);
            // 商品简介
            $('#product-desc').text(product.productDescription);
            // 商品价格展示逻辑, 主要判断原价现价是否为空, 所有都为空则不显示价格栏目
            if (product.normalPrice !== ''
                && product.promotionPrice !== '') {
                // 如果现价和原价都不为空则都展示,并且给原价加个删除符号
                $('#price').show();
                $('#normalPrice').html(
                    '<del>' + '￥' + product.normalPrice + '</del>');
                $('#promotionPrice').text('￥' + product.promotionPrice);
            } else if (product.normalPrice !== ''
                && product.promotionPrice === '') {
                // 如果原价不为空而现价为空则只展示原价
                $('#price').show();
                $('#promotionPrice').text('￥' + product.normalPrice);
            } else if (product.normalPrice === ''
                && product.promotionPrice !== '') {
                // 如果现价不为空而原价为空则只展示现价
                $('#price').show();
                $('#promotionPrice').text('￥' + product.promotionPrice);
            }
        }
    });
    $.init();
});
