$(function () {
    var productId = getQueryString('productId');
    var infoUrl = '/o2o/shopadmin/getproductbyid?productId=' + productId;
    var categoryUrl = '/o2o/shopadmin/getproductcategorylist';
    var productPostUrl = '/o2o/shopadmin/modifyproduct';

    // 默认是添加页面

    var isEdit = false;
    if (productId) {
        getInfo(productId);
        isEdit = true;
    } else {
        // 添加页面需要先加载类别下拉框
        getCategory();
        productPostUrl = '/o2o/shopadmin/addproduct';
    }
    // 编辑时获取所有表单信息
    function getInfo(id) {
        $.getJSON(infoUrl, function (data) {
            if (data.success) {
                var product = data.product;
                // 从返回的json中获取商品对象赋给表单
                $('#product-name').val(product.productName);
                $('#product-desc').val(product.productDesc);
                $('#priority').val(product.priority);
                $('#normal-price').val(product.normalPrice);
                $('#promotion-price').val(product.promotionPrice);
                // 获取商品类别
                var optionHtml = '';
                var optionArr = data.productCategoryList;
                var optionSelected = product.productCategory.productCategoryId;
                // 生成前端列表
                optionArr
                    .map(function (item, index) {
                        var isSelect = optionSelected === item.productCategoryId ? 'selected'
                            : '';
                        // 默认选择编辑的商品类别
                        optionHtml += '<option data-value="'
                            + item.productCategoryId
                            + '"'
                            + isSelect
                            + '>'
                            + item.productCategoryName
                            + '</option>';
                    });
                $('#category').html(optionHtml);
            }
        });
    }
    // 添加时只用加载类别下拉框
    function getCategory() {
        $.getJSON(categoryUrl, function (data) {
            if (data.success) {
                var productCategoryList = data.data;
                var optionHtml = '';
                productCategoryList.map(function (item, index) {
                    optionHtml += '<option data-value="'
                        + item.productCategoryId + '">'
                        + item.productCategoryName + '</option>';
                });
                $('#category').html(optionHtml);
            }
        });
    }

    // 绑定控件，使得每添加最后一个商品详情图，下面自动多出来一个还可以继续添加的按钮
    $('.detail-img-div').on('change', '.detail-img:last-child', function () {
        // 当然也要保证最多6张
        if ($('.detail-img').length < 6) {
            $('#detail-img').append('<input type="file" class="detail-img">');
        }
    });

    $('#submit').click(
        function () {
            // 创建商品json对象，并从表单中获取对应的属性值
            var product = {};
            product.productName = $('#product-name').val();
            product.productDesc = $('#product-desc').val();
            product.priority = $('#priority').val();
            product.normalPrice = $('#normal-price').val();
            product.promotionPrice = $('#promotion-price').val();
            // 获取选定的商品类别值
            product.productCategory = {
                productCategoryId: $('#category').find('option').not(
                    function () {
                        return !this.selected;
                    }).data('value')
            };
            product.productId = productId;
            // 获取缩略图文件流
            var thumbnail = $('#small-img')[0].files[0];
            // 生成表单对象，用于接收参数并传递给后台
            var formData = new FormData();
            formData.append('thumbnail', thumbnail);
            // 遍历商品详情图控件，获取里面的文件流
            $('.detail-img').map(function (index, item) {
                // 判断该控件是否已经选择了文件
                if ($('.detail-img')[index].files.length > 0) {
                    // 将第i个文件流赋值到key为productImg的键值对中
                    formData.append('productImg' + index,
                        $('.detail-img')[index].files[0]);
                }
            });
            // 将商品json对象转换成字符流保存到key为productStr的键值对中
            formData.append('productStr', JSON.stringify(product));
            // 获取表单中输入的验证码
            var verifyCodeActual = $('#j_captcha').val();
            if (!verifyCodeActual) {
                $.toast('请输入验证码！');
                return;
            }
            formData.append("verifyCodeActual", verifyCodeActual);
            $.ajax({
                url: productPostUrl,// 根据添加还是修改选择不同的url
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                cache: false,
                success: function (data) {
                    if (data.success) {
                        $.toast('提交成功！');
                        $('#captcha_img').click();// 成功还是失败都换一个验证码
                    } else {
                        $.toast('提交失败！');
                        $('#captcha_img').click();
                    }
                }
            });
        });

});