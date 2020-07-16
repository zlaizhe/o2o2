/*
店铺注册/修改页面的js代码
异步操作
 */
$(function () {
    var shopId = getQueryString('shopId');
    var isEdit = shopId ? true : false;//根据判断地址栏有没有shopId参数来决定这个页面是用来注册店铺还是修改店铺

    var initUrl = '/o2o/shopadmin/getshopinitinfo';
    var registerShopUrl = '/o2o/shopadmin/registershop';
    var shopInfoUrl = '/o2o/shopadmin/getshopbyid?shopId=' + shopId;
    var editShopUrl = '/o2o/shopadmin/modifyshop';
    if (!isEdit) {//从服务器获取相关数据
        getShopInitInfo();//注册时
    } else {
        getShopInfo(shopId);//修改时
    }

    function getShopInfo(shopId) {
        $.getJSON(shopInfoUrl, function (data) {//从服务器获取
            if (data.success) {
                var shop = data.shop;
                $('#shop-name').val(shop.shopName);
                $('#shop-addr').val(shop.shopAddr);
                $('#shop-phone').val(shop.phone);
                $('#shop-desc').val(shop.shopDesc);
                var shopCategory = '<option data-id="' + shop.shopCategory.shopCategoryId + '" selected>'
                    + shop.shopCategory.shopCategoryName + '</option>';
                var tempAreaHtml = '';
                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">'
                        + item.areaName + '</option>';
                });
                $('#shop-category').html(shopCategory);
                $('#shop-category').attr('disabled', 'disabled');
                $('#area').html(tempAreaHtml);
                $('#area option[' + shop.area.areaId + ']').attr('selected', 'selected');
            }
        });
    }


    function getShopInitInfo() {
        $.getJSON(initUrl, function (data) {//从服务器获取商铺分类和地区的信息，显示在表单的下拉列表中
            if (data.success) {
                var tempHtml = '';
                var tempAreaHtml = '';
                //以map方式遍历data，将数据的主键存在data-id属性中
                data.shopCategoryList.map(function (item, index) {
                    tempHtml += '<option data-id="' + item.shopCategoryId + '">'
                        + item.shopCategoryName + '</option>';
                });
                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">'
                        + item.areaName + '</option>';
                });
                $('#shop-category').html(tempHtml);
                $('#area').html(tempAreaHtml);
            }
        });
    }

    $('#submit').click(function () {
        var shop = {};
        if(isEdit){
            shop.shopId = shopId;
        }
        shop.shopName = $('#shop-name').val();
        shop.shopAddr = $('#shop-addr').val();
        shop.phone = $('#phone').val();
        shop.shopDesc = $('#shop-desc').val();
        shop.shopCategory = {//shopCategory属性是一个json对象，目前只封装shopCategoryId
            shopCategoryId: $('#shop-category').find('option').not(function () {//选中下拉列表中选择的option值
                return !this.selected;
            }).data('id')//获取data-id属性
        };
        shop.area = {//area属性是一个json对象，目前只封装areaId
            areaId: $('#area').find('option').not(function () {//选中下拉列表中选择的option值
                return !this.selected;
            }).data('id')//获取data-id属性
        };
        var shopImg = $('#shop-img')[0].files[0];//文件
        var formData = new FormData();//封装表单提交对象
        formData.append('shopImg', shopImg);//上传图片的文件流
        formData.append('shopStr', JSON.stringify(shop));//shop对象以json字符串格式提交
        var verifyCodeActual = $('#j_captcha').val();//验证码
        if (!verifyCodeActual) {
            $.toast('请输入验证码！');
            return;
        }
        formData.append('verifyCodeActual', verifyCodeActual);
        $.ajax({//发送提交ajax请求
            url: isEdit ? editShopUrl : registerShopUrl,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    $.toast('提交成功！');
                } else {
                    $.toast('提交失败！' + data.errMsg);
                }
                $('#captcah_img').click();//点击验证码图片，更换验证码
            }
        })
    });
});