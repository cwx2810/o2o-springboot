$(function () {
    // 获取商品列表
    var listUrl = '/o2o/shopadmin/getproductlistbyshop?pageIndex=1&pageSize=9999';
    // 商品下架url
    var deleteUrl = '/o2o/shopadmin/modifyproduct';

    getList();

    function getList() {
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                var productList = data.productList;
                var tempHtml = '';
                productList.map(function (item, index) {
                    var textOp = "下架";
                    var contraryStatus = 0;
                    if (item.enableStatus == 0) {
                        textOp = "上架";
                        contraryStatus = 1;
                    } else {
                        contraryStatus = 0;
                    }
                    tempHtml += '' + '<div class="row row-product">'
                        + '<div class="col-33">'
                        + item.productName
                        + '</div>'
                        + '<div class="col-20">'
                        + item.priority
                        + '</div>'
                        + '<div class="col-40">'
                        + '<a href="#" class="edit" data-id="'
                        + item.productId
                        + '" data-status="'
                        + item.enableStatus
                        + '">编辑</a>'
                        + '<a href="#" class="delete" data-id="'
                        + item.productId
                        + '" data-status="'
                        + contraryStatus
                        + '">'
                        + textOp
                        + '</a>'
                        + '<a href="#" class="preview" data-id="'
                        + item.productId
                        + '" data-status="'
                        + item.enableStatus
                        + '">预览</a>'
                        + '</div>'
                        + '</div>';
                });
                $('.product-wrap').html(tempHtml);
            }
        });
    }

    // 设置a标签绑定
    $('.product-wrap').on(
        'click',
        'a',
        function (e) {
            var target = $(e.currentTarget);
            // 点击了编辑，就进入编辑
            if (target.hasClass('edit')) {
                window.location.href = '/o2o/shopadmin/productoperation?productId='
                    + e.currentTarget.dataset.id;
            } else if (target.hasClass('delete')) {// 点击了下架，就执行下架
                deleteItem(e.currentTarget.dataset.id,
                    e.currentTarget.dataset.status);
            } else if (target.hasClass('preview')) {// 点击了预览，就进行预览
                window.location.href = '/o2o/frontend/productdetail?productId='
                    + e.currentTarget.dataset.id;
            }
        });

    // 下架功能
    function deleteItem(id, enableStatus) {
        var product = {};
        product.productId = id;
        product.enableStatus = enableStatus;
        $.confirm('确定么?', function () {
            $.ajax({
                url: deleteUrl,
                type: 'POST',
                data: {
                    productStr: JSON.stringify(product),
                    statusChange: true
                },
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        $.toast('操作成功！');
                        getList();
                    } else {
                        $.toast('操作失败！');
                    }
                }
            });
        });
    }

});