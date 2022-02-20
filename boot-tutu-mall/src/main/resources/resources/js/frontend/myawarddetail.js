$(function () {
    // 从地址栏中获取userAwardId
    let userAwardId = getQueryString('userAwardId');
    // 根据userAwardId获取用户奖品映射信息
    let awardUrl = '/frontend/getAwardByUserAwardId?userAwardId=' + userAwardId;

    $.getJSON(awardUrl,
        function (data) {
            if (data.success) {
                // 获取奖品信息并显示
                let award = data.award;
                $('#award-img').attr('src', award.awardImage);
                $('#create-time').text(new Date(data.userAwardMap.createTime).Format("yyyy-MM-dd"));
                $('#award-name').text(award.awardName);
                $('#award-desc').text(award.awardDescription);
                let qrImg = '';
                // 若未去实体店兑换实体奖品,生成兑换礼品的二维码供商家扫描
                if (data.usedStatus === 0) {
                    qrImg += '<img src="/frontend/generateQrcodeForAward?userAwardId='
                        + userAwardId
                        + '" width="100%" alt=""/>';
                    $('#content').html("扫码领取奖品");
                    $('#qrImg').html(qrImg);
                }
            }
        });
    // 若点击"我的",则显示侧栏
    $('#me').click(function () {
        $.openPanel('#panel-right-demo');
    });
    $.init();
});
