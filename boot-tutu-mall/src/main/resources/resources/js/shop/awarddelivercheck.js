$(function () {
    let awardName = '';
    getList();
    function getList() {
        // 获取积分兑换记录的URL
        let listUrl = '/shopadmin/listuserawardmapsbyshop?pageIndex=1&pageSize=9999&awardName=' + awardName;
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                let userAwardMapList = data.userAwardMapList;
                let tempHtml = '';
                // 拼接成展示列表
                userAwardMapList.map(function (item, index) {
                    tempHtml += ''
                        + '<div class="row row-awarddeliver">'
                        + '<div class="col-33">'
                        + item.award.awardName
                        + '</div>'
                        + '<div class="col-33 awarddeliver-time">'
                        + new Date(item.createTime)
                            .Format("yyyy-MM-dd hh:mm:ss") + '</div>'
                        + '<div class="col-10">' + item.user.name
                        + '</div>' + '<div class="col-10">' + item.point
                        + '</div>' + '<div class="col-10">'
                        + item.operator.name + '</div>' + '</div>';
                });
                $('.awarddeliver-wrap').html(tempHtml);
            }
        });
    }

    // 搜索绑定，获取并按照奖品名模糊查询
    $('#search').on('change', function (e) {
        awardName = e.target.value;
        $('.awarddeliver-wrap').empty();
        getList();
    });
});