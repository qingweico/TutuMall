$(function () {
    $('.checkLogin').click(function () {
        $.ajax({
            url: "/local/checklogin",
            type: "post",
            async: false,
            cache: false,
            dataType: 'json',
            success: function (data) {
                if (data.success) {
                    return true;
                } else {
                    window.alert("请先登陆!")
                    $(".checkLogin").attr("href", "/local/login?usertype=1");
                }
            },
            error: function (data, error) {
                window.alert(error);
            }
        });
    })
})

