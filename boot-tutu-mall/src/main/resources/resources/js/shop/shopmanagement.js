$(function () {
    let shopId = getQueryString('shopId');
    let shopInfoUrl = '/shopadmin/getshopmanagementinfo?shopId=' + shopId;
    $.getJSON(shopInfoUrl, function (data) {
        if (data.redirect) {
            window.location.href = data.url;
        } else {
            if (data.shopId !== undefined && data.shopId != null) {
                shopId = data.shopId;
            }
            $('#shopInfo').attr('href', '/shopadmin/shopoperation?shopId=' + shopId);
        }
    });
});