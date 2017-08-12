'user strict';

app.controller('addAdminCtrl',function($scope,globalUrl,httpService,dialogService) {
	$scope.title='addAdmin';

	$scope.addadmin = function(no){
		alert(no);

		var data={
			"is_root":1,
			"no":no
		}

		var post_addAdmin = httpService.post(globalUrl.setRoot,data);

		post_addAdmin.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response.data['error']);
				return;
			}
			if(response.data['result'] == "200 OK"){
				dialogService.show('已添加管理员！');
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
		});


	}


});