$(function () {

    getlist();


	// 获取店铺列表
	function getlist(e) {
		$.ajax({
			url : "/o2o/shopadmin/getshoplist",
			type : "get",
			dataType : "json",
			success : function(data) {
				if (data.success) {
					handleList(data.shopList);
					handleUser(data.user);
				}
			}
		});
	}
	// 渲染用户名
	function handleUser(data) {
		$('#user-name').text(data.name);
	}
	// 渲染列表
	function handleList(data) {
		var html = '';
		data.map(function (item, index) {
			html += '<div class="row row-shop"><div class="col-40">'
				 + item.shopName +'</div><div class="col-40">'
				 + shopStatus(item.enableStatus) +'</div><div class="col-20">'
				 + goShop(item.enableStatus, item.shopId) +'</div></div>';
		});
		$('.shop-wrap').html(html);
	}
	// 生成一个链接，我们点击进入，就能进入相应的店铺管理页面
	function goShop(status, id) {
		if (status != 0 && status != -1) {
			return '<a href="/o2o/shopadmin/shopmanage?shopId='+ id +'">进入</a>';
		} else {
			return '';
		}
	}
	// 根据后端返回的状态值，用文字叙述
	function shopStatus(status) {
		if (status == 0) {
			return '审核中';
		} else if (status == -1) {
			return '店铺非法';
		} else {
			return '审核通过';
		}
	}

});
