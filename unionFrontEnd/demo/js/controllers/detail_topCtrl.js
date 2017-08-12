'user strict';

app.controller('detail_topCtrl',function($scope,globalUrl,listService,$routeParams,httpService) {

	$scope.string_convert = listService.string_convert;
	//$scope.bool_children=listService.bool_children;

	$scope.event_id = $routeParams.event_id;
	
	var get_request = httpService.get(globalUrl.getSingleEvent+"?event_id="+$scope.event_id);

	get_request.then(function successCallback(response) {
    		if(response.data['error'] != null){
				alert(response['error']);
			}
			if(response.data['result'] == "200 OK"){
				$scope.event_union = JSON.parse(response.data['eventSingle']);
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
	
	$scope.end_event=function(){
		var data ={
    		"option":{
				"id":$scope.event_union.id,
    			"status":"1"
    		}
    	}

		var post_update = httpService.post(globalUrl.updateEvent,data);

		post_update.then(function successCallback(response) {
    		if(response.data['error'] != null){
				alert(response['error']);
				return;
			}
			if(response.data['result'] == "200 OK"){
				alert('本活动变为结束状态');
				$scope.event_union.status = 1;
			}
		}, function errorCallback(response) {
			alert("服务器无响应！");
		});
	}

});