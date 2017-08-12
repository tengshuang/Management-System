'user strict';

app.controller('downloadCtrl',function($scope,file_id,globalUrl,httpService) {
	$scope.file_id=file_id;

	var get_request = httpService.get(globalUrl.getFile+"?id="+$scope.file_id);

	get_request.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response['error']);
			}
			if(response.data['result'] == "200 OK"){
				$scope.file = response.data['file'];
				console.log(address_server);
				$scope.file.url = address_server + $scope.file.url;
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
		});

});