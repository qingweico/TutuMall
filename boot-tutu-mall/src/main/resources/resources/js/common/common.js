Date.prototype.Format = function (fmt) {
    let o = {
        "M+": this.getMonth() + 1, // 月份
        "d+": this.getDate(),      // 日
        "h+": this.getHours(),     // 小时
        "m+": this.getMinutes(),   // 分
        "s+": this.getSeconds(),   // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        "S": this.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    for (let k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k])
                : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

/**
 * 点击切换验证码
 */
function changeVerifyCode(img) {
    img.src = "/Kaptcha?" + Math.floor(Math.random() * 100);
}

/**
 *    组织正则表达式,主要是定位name所在的位置,并截取name=value
 *    如www.baidu.com:8080?a=1&b=2&c=3
 *    若传入b,则获取b=2,并经过后续步骤拆解成2返回
 */
function getQueryString(name) {
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    let r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]);
    }
    return '';
}

function getContextPath() {
    return "/";
}
// 防抖动
function debounce(fn) {
    // 创建一个标记用来存放定时器的返回值
    let timeout = null;
    return function () {
        // 每当用户输入的时候把前一个 setTimeout clear掉
        clearTimeout(timeout);
        // 然后又创建一个新的 setTimeout,这样就能保证输入字符后的
        // interval 间隔内如果还有字符输入的话,就不会执行 fn 函数
        timeout = setTimeout(() => {
            fn.apply(this, arguments);
        }, 1000);
    };
}
// 侧边栏按钮事件绑定
$('#me').click(function () {
    $.openPanel('#panel-right-demo');
});