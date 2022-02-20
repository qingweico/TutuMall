$(function () {
    let listUrl = '/shopadmin/getproductcategorylist';
    let addUrl = '/shopadmin/addproductcategories';
    let deleteUrl = '/shopadmin/removeproductcategory';
    let bind = '.category-wrap';
    getList();

    function getList() {
        $.getJSON(listUrl,
            function (data) {
                if (data.success) {
                    let dataList = data.data;
                    $(bind).html('');
                    let tempHtml = '';
                    dataList
                        .map(function (item, index) {
                            tempHtml += ''
                                + '<div class="row row-product-category now">'
                                + '<div class="col-33 product-category-name">'
                                + item.productCategoryName
                                + '</div>'
                                + '<div class="col-33">'
                                + item.priority
                                + '</div>'
                                + '<div class="col-33"><a href="#" class="button delete" data-id="'
                                + item.productCategoryId
                                + '">删除</a></div>'
                                + '</div>';
                        });
                    $(bind).append(tempHtml);
                }
            });
    }

    $('#new')
        .click(
            function () {
                let tempHtml = '<div class="row row-product-category temp">'
                    + '<div class="col-33"><input class="category-input category" type="text" placeholder="分类名"></div>'
                    + '<div class="col-33"><input class="category-input priority" type="number" placeholder="优先级"></div>'
                    + '<div class="col-33"><a href="#" class="button delete">删除</a></div>'
                    + '</div>';
                $('.category-wrap').append(tempHtml);
            });
    $('#submit').bind('click',debounce(function () {
        let tempArr = $('.temp');
        let productCategoryList = [];
        tempArr.map(function (index, item) {
            let tempObj = {};
            tempObj.productCategoryName = $(item).find('.category').val();
            tempObj.priority = $(item).find('.priority').val();
            if (tempObj.productCategoryName && tempObj.priority) {
                productCategoryList.push(tempObj);
            }
        });
        $.ajax({
            url: addUrl,
            type: 'POST',
            data: JSON.stringify(productCategoryList),
            contentType: 'application/json',
            success: function (data) {
                if (data.success) {
                    $.toast('提交成功!');
                    getList();
                } else {
                    $.toast('提交失败!');
                }
            }
        });
    }));

    $(bind).on('click', '.row-product-category.temp .delete',
        function (e) {
            $(this).parent().parent().remove();

        });
    $(bind).on('click', '.row-product-category.now .delete',
        function (e) {
            let target = e.currentTarget;
            $.confirm('确定么?', function () {
                $.ajax({
                    url: deleteUrl,
                    type: 'GET',
                    data: {
                        productCategoryId: target.dataset.id
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.success) {
                            $.toast('删除成功!');
                            getList();
                        } else {
                            $.toast('删除失败!');
                        }
                    }
                });
            });
        });
});