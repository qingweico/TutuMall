$(function () {
    let loading = false;
    let maxItems = 999;
    let pageSize = 10;
    // 获取奖品列表的URL
    let listUrl = '/frontend/listAwardsByShop';
    // 兑换奖品的URL
    let exchangeUrl = '/frontend/addUserAwardMap';
    let pageNum = 1;
    // 从地址栏URL里获取shopId
    let shopId = getQueryString('shopId');
    let awardName = '';
    let canProceed = false;
    let totalPoint = 0;
    // 预先加载20条
    addItems(pageSize, pageNum);

    // 按照查询条件获取奖品列表,并生成对应的HTML元素添加到页面中
    function addItems(pageSize, pageIndex) {
        // 生成新条目的HTML
        let url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
            + pageSize + '&shopId=' + shopId + '&awardName=' + awardName;
        loading = true;
        $.getJSON(url, function (data) {
            if (data.success) {
                // 获取总数
                maxItems = data.count;
                let html = '';
                data.awardList.map(function (item) {
                    html += '' + '<div class="card" data-award-id="'
                        + item.awardId + '" data-point="' + item.point
                        + '">' + '<div class="card-header">'
                        + item.awardName + '<span class="pull-right">需要积分'
                        + item.point + '</span></div>'
                        + '<div class="card-content">'
                        + '<div class="list-block media-list">' + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-media">' + '<img src="'
                        + item.awardImage
                        + '" width="44" alt="">' + '</div>'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + item.awardDescription
                        + '</div>' + '</div>' + '</li>' + '</ul>'
                        + '</div>' + '</div>' + '<div class="card-footer">'
                        + '<p class="color-gray">'
                        + new Date(item.lastEditTime).Format("yyyy-MM-dd")
                        + '更新</p>';
                    if (data.totalPoint !== undefined) {
                        // 若用户有积分,则显示领取按钮
                        html += '<span>点击领取</span></div></div>'
                    } else {
                        html += '</div></div>'
                    }
                });
                $('.list-div').append(html);
                if (data.totalPoint !== undefined) {
                    // 若用户在该店铺有积分,则显示
                    canProceed = true;
                    $("#title").text('当前积分' + data.totalPoint);
                    totalPoint = data.totalPoint;
                }
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

    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        // 无极滚动
        if (loading)
            return;
        addItems(pageSize, pageNum);
    });

    $('.award-list').on(
        'click',
        '.card',
        debounce(function (e) {
            // 若用户在该店铺有积分并且积分数大于该奖品需要消耗的积分
            if (canProceed
                && (totalPoint >= e.currentTarget.dataset.point)) {
                // 则弹出操作确认框
                $.confirm(
                    '需要消耗'
                    + e.currentTarget.dataset.point
                    + '积分,确定兑换么',
                    function () {
                        // 访问后台, 领取奖品
                        $.ajax({
                            url: exchangeUrl,
                            type: 'POST',
                            data: {
                                awardId: e.currentTarget.dataset.awardId
                            },
                            dataType: 'json',
                            success: function (
                                data) {
                                if (data.success) {
                                    $.toast('领取奖品成功!');
                                    totalPoint = totalPoint
                                        - e.currentTarget.dataset.point;
                                    $("#title")
                                        .text(
                                            '当前积分'
                                            + totalPoint);
                                } else {
                                    $.toast('领取奖品失败!');
                                }
                            }
                        });
                    });
            } else {
                $.toast('积分不足或无权限操作!');
            }
        }));

    // 搜索查询条件,按照奖品名模糊查询
    $('#search').on('change', function (e) {
        awardName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });
    $.init();


});
