'user strict';

app.controller('detail_bottomCtrl',function($scope,$routeParams,listService,httpService,globalUrl) {

	//$scope.empty = listService.empty;
	//$scope.bool_children = listService.bool_children;
	$scope.string_convert = listService.string_convert;

	$scope.activity_id = $routeParams.activity_id;

	var get_request = httpService.get(globalUrl.getAWithE+"?activity_id="+$scope.activity_id);

	get_request.then(function successCallback(response) {
    		if(response.data['error'] != null){
				alert(response['error']);
			}
			if(response.data['result'] == "200 OK"){
				$scope.event_union = response.data['activity']['event'];
				$scope.activity_union = response.data['activity']['activity'];
				$scope.activity_union.role="管理者";
				console.log($scope.event_union);
				console.log($scope.activity_union);
			}
		}, function errorCallback(response) {
			alert("服务器无响应！");
	});

	$scope.download = function(file_id){
		var get_request = httpService.get(globalUrl.getFile+"?id="+file_id);

		get_request.then(function successCallback(response) {
    		if(response.data['error'] != null){
				alert(response['error']);
			}
			if(response.data['result'] == "200 OK"){
				$scope.file = response.data['file'];
				//console.log(address_server);
				$scope.file.url = address_server + $scope.file.url;
			}
		}, function errorCallback(response) {
			alert("服务器无响应！");
		});
	}

	$scope.end_activity = function(activity_id){

		var data ={"activity_id":activity_id}

		var post_update = httpService.post(globalUrl.updateActivity,data);

		post_update.then(function successCallback(response) {
    		if(response.data['error'] != null){
				alert(response['error']);
				return;
			}
			if(response.data['result'] == "200 OK"){
				$scope.activity_union.status = 1;
			}
		}, function errorCallback(response) {
			alert("服务器无响应！");
		});
	}

});