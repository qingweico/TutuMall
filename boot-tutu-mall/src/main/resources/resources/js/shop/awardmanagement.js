$(function () {
    // 获取该店铺下的奖品列表URL
    let listUrl = '/shopadmin/listawardsbyshop?pageIndex=1&pageSize=9999';
    // 设置奖品的可用状态
    let changeUrl = '/shopadmin/addormodifyaward';
    getList();
    function getList() {
        // 获取奖品列表
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                let awardList = data.awardList;
                let tempHtml = '';
                awardList.map(function (item, index) {
                    let textOp = "下架";
                    let contraryStatus = 0;
                    if (item.enableStatus === 0) {
                        // 若状态值为0,表明是已下架的奖品,操作变为上架(即点击上架按钮上架相关奖品)
                        textOp = "上架";
                        contraryStatus = 1;
                    } else {
                        contraryStatus = 0;
                    }
                    // 拼接每件奖品的行信息
                    tempHtml += '' + '<div class="row row-award">'
                        + '<div class="col-33">'
                        + item.awardName
                        + '</div>'
                        + '<div class="col-20">'
                        + item.point
                        + '</div>'
                        + '<div class="col-40">'
                        + '<a href="#" class="edit" data-id="'
                        + item.awardId
                        + '" data-status="'
                        + item.enableStatus
                        + '">编辑</a>'
                        + '<a href="#" class="delete" data-id="'
                        + item.awardId
                        + '" data-status="'
                        + contraryStatus
                        + '">'
                        + textOp
                        + '</a>'
                        + '<a href="#" class="preview" data-id="'
                        + item.awardId
                        + '" data-status="'
                        + item.enableStatus
                        + '">预览</a>'
                        + '</div>'
                        + '</div>';
                });
                $('.award-wrap').html(tempHtml);
            }
        });
    }

    // 将class为product-wrap里面的a标签绑定上点击的事件
    $('.award-wrap')
        .on('click', 'a', function (e) {
                let target = $(e.currentTarget);
                if (target.hasClass('edit')) {
                    // 如果有class edit则点击就进入奖品信息编辑页面，并带有awardId参数
                    window.location.href = '/shopadmin/awardoperation?awardId='
                        + e.currentTarget.dataset.id;
                } else if (target.hasClass('delete')) {
                    // 如果有class status则调用后台功能上/下架相关奖品，并带有productId参数
                    changeItem(e.currentTarget.dataset.id,
                        e.currentTarget.dataset.status);
                } else if (target.hasClass('preview')) {
                    // 如果有class preview则去前台展示系统该奖品详情页预览奖品情况
                    window.location.href = '/shopadmin/awarddetail?awardId='
                        + e.currentTarget.dataset.id;
                }
            });

    // 给新增按钮绑定点击事件
    $('#new').click(function () {
        window.location.href = '/shopadmin/awardoperation';
    });

    function changeItem(awardId, enableStatus) {
        // 定义award json对象并添加awardId以及状态(上架/下架)
        let award = {};
        award.awardId = awardId;
        award.enableStatus = enableStatus;
        $.confirm('确定么?', function () {
            //上下架相关奖品
            $.ajax({
                url: changeUrl,
                type: 'POST',
                data: {
                    awardString: JSON.stringify(award),
                    statusChange: true
                },
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        if(data.enableStatus === 0) {
                            $.toast('下架成功!');
                        }
                        else {
                            $.toast('上架成功!');
                        }

                        getList();
                    } else {
                        if(data.errorMessage !== undefined) {
                            $.toast(data.errorMessage);
                        }
                        else {
                            $.toast("出错了!");
                        }
                    }
                }
            });
        });
    }
});