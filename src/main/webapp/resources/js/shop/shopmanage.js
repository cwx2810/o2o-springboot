$(function () {

    var shopId = getQueryString('shopId');
    var shopInfoUrl = '/o2o/shopadmin/getshopmanagementinfo?shopId=' + shopId;

    $.getJSON(shopInfoUrl, function (data) {
        // 如果没有得到shopId，需要跳转会店铺列表
        if (data.redirect) {
            window.location.href = data.url;
        } else {// 获取到了shopId，进入编辑
            if (data.shopId != undefined && data.shopId != null) {
                shopId = data.shopId;
            }
            $('#shopInfo').attr('href', '/o2o/shopadmin/shopoperation?shopId=' + shopId);
        }
    });
});