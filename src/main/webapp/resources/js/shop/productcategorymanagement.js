$(function () {
    var listUrl = '/o2o/shopadmin/getproductcategorylist';
    var addUrl = '/o2o/shopadmin/addproductcategorys';
    var deleteUrl = '/o2o/shopadmin/removeproductcategory';
    getList();

    //显示该店铺所有商品类别
    function getList() {
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                var dataList = data.data;
                $('.category-wrap').html('');
                var tempHtml = '';
                dataList.map(function (item, index) {
                    tempHtml += ''
                        + '<div class="row row-product-category now">'
                        + '<div class="col-33 product-category-name">' + item.productCategoryName + '</div>'
                        + '<div class="col-33">' + item.priority + '</div>'
                        + '<div class="col-33"><a href="#" class="button delete" data-id="' + item.productCategoryId + '">删除</a></div>'
                        + '</div>';
                });
                $('.category-wrap').append(tempHtml);
            } else {
                $.toast(data.errMsg);
            }
        });
    }

    //每点击一次新增按钮，添加一个临时新行，临时新行具有temp的class属性
    $('#new').click(function () {
        var tempHtml = '<div class="row row-product-category temp">'
            + '<div class="col-33"><input class="category-input category" type="text" placeholder="分类名"></div>'
            + '<div class="col-33"><input class="category-input priority" type="number" placeholder="优先级"></div>'
            + '<div class="col-33"><a href="#" class="button delete">删除</a></div>'
            + '</div>';
        $('.category-wrap').append(tempHtml);
    });
    //将添加的临时新行（.temp）提交
    $('#submit').click(function () {
        var tempArr = $('.temp');
        var productCategoryList = [];
        tempArr.map(function (index, item) {
            var tempObj = {};
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
                    $.toast('提交成功！');
                    getList();
                } else {
                    $.toast('提交失败！' + data.errMsg);
                }
            }
        });
    });

    //临时行直接在页面删除
    $('.category-wrap').on('click', '.row-product-category.temp .delete',
        function (e) {
            console.log($(this).parent().parent());
            $(this).parent().parent().remove();
        });
    //原有行需要提交到服务删除
    $('.category-wrap').on('click', '.row-product-category.now .delete', function (e) {
        var target = e.currentTarget;
        $.confirm('确定么?', function () {
            $.ajax({
                url: deleteUrl,
                type: 'POST',
                data: {
                    productCategoryId: target.dataset.id
                },
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        $.toast('删除成功！');
                        getList();
                    } else {
                        $.toast('删除失败！' + data.errMsg);
                    }
                }
            });
        });
    });
});