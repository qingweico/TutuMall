$(function () {
    let productName = '';
    getList();
    getProductSellDailyList();

    function getList() {
        // 获取用户购买信息的URL
        let listUrl = '/shopadmin/listuserproductmapsbyshop?pageIndex=1&pageSize=9999&productName='
            + productName;
        // 访问后台,获取该店铺的购买信息列表
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                let userProductMapList = data.userProductMapList;
                let tempHtml = '';
                // 遍历购买信息列表,拼接出列信息
                userProductMapList.map(function (item) {
                    tempHtml += '' + '<div class="row row-productbuycheck">'
                        + '<div class="col-33">' + item.product.productName
                        + '</div>'
                        + '<div class="col-33 productbuycheck-time">'
                        + new Date(item.createTime).Format("yyyy-MM-dd hh:mm:ss")
                        + '</div>' + '<div class="col-10">'
                        + item.user.name + '</div>'
                        + '<div class="col-10">' + item.point + '</div>'
                        + '<div class="col-10">' + item.operator.name
                        + '</div>' + '</div>';
                });
                $('.productbuycheck-wrap').html(tempHtml);
            }
        });
    }

    $('#search').on('change', function (e) {
        // 当在搜索框里输入信息的时候
        // 依据输入的商品名模糊查询该商品的购买记录
        productName = e.target.value;
        // 清空商品购买记录列表
        $('.productbuycheck-wrap').empty();
        // 再次加载
        getList();
    });

    /**
     * 获取7天的销量
     */
    function getProductSellDailyList() {
        // 获取该店铺商品7天销量的URL
        let listProductSellDailyUrl = '/shopadmin/listproductselldailyinfobyshop';
        // 访问后台, 该店铺商品7天销量的URL
        $.getJSON(listProductSellDailyUrl, (data) => {
            if (data.success) {
                let myChart = echarts.init(document.getElementById('chart'));
                // 生成静态的echarts信息的部分
                let option = generateStaticEchartsPart();
                // 遍历销量统计列表,动态设定echarts的值
                option.legend.data = data.legendData;
                option.xAxis = data.xAxis;
                option.series = data.series;
                myChart.setOption(option);
            }
        });
    }

    /**
     * 生成静态的Echarts信息的部分
     */
    function generateStaticEchartsPart() {
        return {
            // 提示框,鼠标悬浮交互时的信息提示
            tooltip: {
                trigger: 'axis',
                axisPointer: { // 坐标轴指示器,坐标轴触发有效
                    type: 'shadow' // 鼠标移动到轴的时候,显示阴影
                }
            },
            // 图例,每个图表最多仅有一个图例
            legend: {
                data:["墨香奶茶", "绿茶拿铁", "冰雪奇缘"]
            },
            // 直角坐标系内绘图网格
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            // 直角坐标系中横轴数组,数组中每一项代表一条横轴坐标轴
            xAxis: [{
                type: 'category',
                data: ['周一', '周二', '周三','周四','周五','周六','周日']
            }],
            // 直角坐标系中纵轴数组,数组中每一项代表一条纵轴坐标轴
            yAxis: [{
                type: 'value'
            }],
            series:[{
                name: 'SERGE LUTENS色唇膏',
                type: 'bar',
                data: [5, 9]
            }, {
                name: '韩国MISSHA谜尚眼线膏液体眼线笔',
                type: 'bar',
                data: [6]
            }, {
                name: 'Smartisan T恤 任天堂发售红白机',
                type: 'bar',
                data: [9 ,10 ,4]},
                {
                    name: 'Smartisan 帆布鞋',
                    type: 'bar',
                    data: [5, 7, 6]}
                ,
                {
                    name: '畅呼吸智能空气净化器除甲醛版',
                    type: 'bar',
                    data: [6, 8,8]},
                {
                    name: '坚果 3 "足迹 "背贴 乐高创始人出生',
                    type: 'bar',
                    data: [9, 6, 7]},
                {
                    name: '坚果 3 TPU 软胶保护套',
                    type: 'bar',
                    data: [8 , 5, 10]}
            ]

        }

    }
});