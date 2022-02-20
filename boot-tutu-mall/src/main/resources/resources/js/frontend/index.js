$(function () {
    //定义访问后台,获取头条列表以及一级类别列表的URL
    let url = '/frontend/initializeHomePageInfo';
    //访问后台,获取头条列表以及一级类别列表
    $.getJSON(url, function (data) {
        if (data.success) {
            //获取后台传递过来的头条 列表
            let headLineList = data.headLineList;
            let swiperHtml = '';
            //遍历头条列表,并拼接出轮播图组
            headLineList.map(function (item, index) {
                swiperHtml += '' + '<div class="swiper-slide img-wrap">'
                    + '<a href="' + item.lineLink
                    + '" external><img class="banner-img" src="' + item.lineImage
                    + '" alt="' + item.lineName + '"></a>' + '</div>';
            });
            //将轮播图组赋值给前端HTML控件
            $('.swiper-wrapper').html(swiperHtml);
            //设定轮播图轮换时间为3秒
            $(".swiper-container").swiper({
                autoplay: 3000,
                //用户对轮播图进行操作时,是否自动停止autoplay
                autoplayDisableOnInteraction: false
            });
            //获取后台传递过来的大类列表
            let shopCategoryList = data.shopCategoryList;
            let categoryHtml = '';
            shopCategoryList.map(function (item, index) {
                categoryHtml += ''
                    + '<div class="col-50 shop-classify" data-category='
                    + item.shopCategoryId + '>' + '<div class="word">'
                    + '<p class="shop-title">' + item.shopCategoryName
                    + '</p>' + '<p class="shop-desc">'
                    + item.shopCategoryDescription + '</p>' + '</div>'
                    + '<div class="shop-classify-img-warp">'
                    + '<img class="shop-img" src="' + item.shopCategoryImage
                    + '" alt="">' + '</div>' + '</div>';
            });
            $('.row').html(categoryHtml);
        }
    });

    $('#me').click(function () {
        $.openPanel('#panel-right-demo');
    });

    $('.row').on('click', '.shop-classify', function (e) {
        let shopCategoryId = e.currentTarget.dataset.category;
        window.location.href = '/frontend/shoplist?parentId=' + shopCategoryId;
    });
});
