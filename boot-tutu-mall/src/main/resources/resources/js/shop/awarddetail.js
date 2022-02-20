$(function () {
    // 从地址栏中获取userAwardId
    let awardId = getQueryString('awardId');
    // 根据userAwardId获取用户奖品映射信息
    let awardUrl = '/shopadmin/getawardbyid?awardId=' + awardId;

    $.getJSON(awardUrl,
        function (data) {
            if (data.success) {
                // 获取奖品信息并显示
                let award = data.award;
                $('#award-img').attr('src', award.awardImage);
                $('#create-time').text(new Date(award.createTime).Format("yyyy-MM-dd"));
                $('#award-name').text(award.awardName);
                $('#award-desc').text(award.awardDescription);
            }
        });
    // 若点击"我的",则显示侧栏
    $('#me').click(function () {
        $.openPanel('#panel-right-demo');
    });
    $.init();
});
