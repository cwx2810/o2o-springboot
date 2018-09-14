$(function () {

    // 通过commons定义的正则表达式获取url中的shopId
    var shopId = getQueryString('shopId');
    // shopId可能为空，如果不空，则网页可以被编辑更新，如果为空，则说明当前只是一个注册页
    var isEdit = shopId ? true : false;

    // 获取店铺分类和区域
    var initUrl = '/o2o/shopadmin/getshopinitinfo';
    // 注册信息发送给后台
    var registerShopUrl = '/o2o/shopadmin/registershop';

    // 通过shopId获取全部shop信息
    //var shopInfoUrl = '/o2o/shopadmin/getshopbyid?shopId=1';
    var shopInfoUrl = '/o2o/shopadmin/getshopbyid?shopId=' + shopId;
    // 更新信息发送给后台
    var editShopUrl = '/o2o/shopadmin/modifyshop';

    if (isEdit) {
        getShopInfo(shopId);
    } else {
        getShopInitInfo();
    }

    // 更新店铺调用，获取所有信息显示到前台
    function getShopInfo(shopId) {
        $.getJSON(shopInfoUrl, function(data) {
            if (data.success) {
                // 通过shopId查询到shop对象，给html元素赋值
                var shop = data.shop;
                $('#shop-name').val(shop.shopName);
                $('#shop-address').val(shop.shopAddr);
                $('#shop-phone').val(shop.phone);
                $('#shop-desc').val(shop.shopDesc);
                var shopCategory = '<option data-id="'
                    + shop.shopCategory.shopCategoryId + '" selected>'
                    + shop.shopCategory.shopCategoryName + '</option>';
                var tempAreaHtml = '';
                data.areaList.map(function(item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">'
                        + item.areaName + '</option>';
                });
                // 把我们写的控件绑定到元素中
                $('#shop-category').html(shopCategory);
                // 设置店铺分类默认为不能修改
                $('#shop-category').attr('disabled','disabled');
                $('#area').html(tempAreaHtml);
                // 设置区域默认为当前区域
                $("#area option[data-id='"+shop.area.areaId+"']").attr("selected","selected");
            }
        });
    }

    // 注册店铺调用，获取后台的店铺分类和区域列表，显示到前台
    function getShopInitInfo() {
        $.getJSON(initUrl, function (data) {
            if (data.success) {
                var tempHtml = '';
                var tempAreaHtml = '';
                data.shopCategoryList.map(function (item, index) {
                    tempHtml += '<option data-id="' + item.shopCategoryId + '">' + item.shopCategoryName + '</option>';
                });
                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">' + item.areaName + '</option>';
                });
                $('#shop-category').html(tempHtml);
                $('#area').html(tempAreaHtml);
            }
        });
    }

    // 前台提交的注册信息，发送到后台
    $('#submit').click(function () {
        var shop = {};
        // 如果是更新店铺，传入shopId
        if (isEdit) {
            shop.shopId = shopId;
        }
        // 接收普通信息
        shop.shopName = $('#shop-name').val();
        shop.shopAddr = $('#shop-address').val();
        shop.phone = $('#shop-phone').val();
        shop.shopDesc = $('#shop-desc').val();
        // 接收选择列表信息
        shop.shopCategory = {
            shopCategoryId : $('#shop-category').find('option').not(function() {
                return !this.selected;
            }).data('id')
        };
        shop.area = {
            areaId : $('#area').find('option').not(function() {
                return !this.selected;
            }).data('id')
        };
        // 接收图片信息
        var shopImg = $("#shop-img")[0].files[0];
        var formData = new FormData();
        formData.append('shopImg', shopImg);
        formData.append('shopStr', JSON.stringify(shop));

        // 声明验证码控件
        var verifyCodeActual = $('#j-kaptcha').val();
        // 如果验证码为空，则提示需要输入验证码
        if (!verifyCodeActual) {
            $.toast('请输入验证码！');
            return;
        }
        formData.append("verifyCodeActual", verifyCodeActual);

        //提交
        $.ajax({
            url : isEdit ? editShopUrl : registerShopUrl,
            type : 'POST',
            // contentType: "application/x-www-form-urlencoded; charset=utf-8",
            data : formData,
            contentType : false,
            processData : false,
            cache : false,
            success : function(data) {
                // 这里就是我们后台定义的那么多success信息的用处
                if (data.success) {
                    $.toast('提交成功！');
                } else {
                    $.toast('提交失败！' + data.errMsg);
                }
                // 不管提交成功失败都更新验证码
                $('#kaptcha-img').click();
            }
        });

    });
})