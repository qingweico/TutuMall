$(function () {
    $(document).on('click','.open-preloader-title', function () {
        $.showPreloader('注销中')
        setTimeout(function () {
            $.hidePreloader();
            // 清除session
            $.ajax({
                url: "/local/logout",
                type: "post",
                async: false,
                cache: false,
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        // 清除成功后退出到登录界面
                        window.location.href = "/local/login";
                        return false;
                    }
                },
                error: function (data, error) {
                    alert(error);
                }
            });
        }, 1000);
    });
});