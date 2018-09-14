$(function() {
    var url = '/o2o/frontend/listmainpageinfo';
    //alert(getContextPath());

    $.getJSON(url, function (data) {
        if (data.success) {
            // 获取头条列表
            var headLineList = data.headLineList;
            var swiperHtml = '';
            // 遍历头条列表，拼接轮播图
            headLineList.map(function (item, index) {
                swiperHtml += ''
                            + '<div class="swiper-slide img-wrap">'
                            +      '<img class="banner-img" src="'+ item.lineImg +'" alt="'+ item.lineName +'">'
                            + '</div>';
            });
            $('.swiper-wrapper').html(swiperHtml);
            // 设置轮播图转换时间
            $(".swiper-container").swiper({
                autoplay: 3000,
                autoplayDisableOnInteraction: false
            });
            // 获取大类列表
            var shopCategoryList = data.shopCategoryList;
            var categoryHtml = '';
            // 拼接大类列表
            shopCategoryList.map(function (item, index) {
                categoryHtml += ''
                             +  '<div class="col-50 shop-classify" data-category='+ item.shopCategoryId +'>'
                             +      '<div class="word">'
                             +          '<p class="shop-title">'+ item.shopCategoryName +'</p>'
                             +          '<p class="shop-desc">'+ item.shopCategoryDesc +'</p>'
                             +      '</div>'
                             +      '<div class="shop-classify-img-warp">'
                             +          '<img class="shop-img" src="'+ item.shopCategoryImg +'">'
                             +      '</div>'
                             +  '</div>';
            });
            $('.row').html(categoryHtml);
        }
    });

    // 点击显示侧栏
    $('#me').click(function () {
        $.openPanel('#panel-left-demo');
    });

    // 给类别按钮绑定事件跳转
    $('.row').on('click', '.shop-classify', function (e) {
        var shopCategoryId = e.currentTarget.dataset.category;
        var newUrl = '/o2o/frontend/shoplist?parentId=' + shopCategoryId;
        window.location.href = newUrl;
    });

});
