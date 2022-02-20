$(function () {
    let loading = false;
    let maxItems = 20;
    let pageSize = 10;
    // 获取该用户的奖品领取记录列表的URL
    let listUrl = '/frontend/listUserAwardMapsByCustomer';

    let pageNum = 1;
    let awardName = '';
    addItems(pageSize, pageNum);

    // 按照查询条件获取奖品兑换记录列表,并生成对应的HTML元素添加到页面中
    function addItems(pageSize, pageIndex) {
        // 生成新条目的HTML
        let url = listUrl + '?pageIndex=' + pageIndex + '&pageSize=' + pageSize
            + '&awardName=' + awardName;
        loading = true;
        $.getJSON(url, function (data) {
            if (data.success) {
                // 获取总数
                maxItems = data.count;
                let html = '';
                data.userAwardMapList.map(function (item, index) {
                    let status = '';
                    // 根据usedStatus显示是否已在实体店领取过奖品
                    if (item.usedStatus === 0) {
                        status = "未领取";
                    } else if (item.usedStatus === 1) {
                        status = "已领取";
                    }
                    html += '' + '<div class="card" data-user-award-id='
                        + item.userAwardId + '>'
                        + '<div class="card-header">' + item.shop.shopName
                        + '<span class="pull-right">' + status
                        + '</sapn></div>' + '<div class="card-content">'
                        + '<div class="list-block media-list">' + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + item.award.awardName
                        + '</div>' + '</div>' + '</li>' + '</ul>'
                        + '</div>' + '</div>' + '<div class="card-footer">'
                        + '<p class="color-gray">'
                        + new Date(item.createTime).Format("yyyy-MM-dd hh:mm:ss")
                        + '</p>' + '<span>消耗积分:' + item.point + '</span>'
                        + '</div>' + '</div>';
                });
                $('.list-div').append(html);
                let total = $('.list-div .card').length;
                if (total >= maxItems) {
                    // 加载完毕,则注销无限加载事件,以防不必要的加载
                    $.detachInfiniteScroll($('.infinite-scroll'));
                    // 删除加载提示符
                    $('.infinite-scroll-preloader').remove();
                    return;
                }
                pageNum += 1;
                loading = false;
                $.refreshScroller();
            }
        });
    }

    // 绑定卡片点击的事件,若点击卡片,则进入奖品领取的详情页
    // 顾客凭详情页里的二维码到实体店给店员扫描领取实物奖品
    $('.list-div')
        .on(
            'click',
            '.card',
            function (e) {
                let userAwardId = e.currentTarget.dataset.userAwardId;
                window.location.href = '/frontend/myawarddetail?userAwardId='
                    + userAwardId;
            });
    // 无极滚动
    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading)
            return;
        addItems(pageSize, pageNum);
    });

    // 绑定搜索事件,主要是传入奖品名进行模糊查询
    $('#search').on('change', function (e) {
        awardName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });
    $.init();
});
