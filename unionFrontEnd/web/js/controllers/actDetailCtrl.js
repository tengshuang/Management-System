'user strict';

app.controller('actDetailCtrl',function($scope,httpService,listService,downloadService,globalUrl) {
	$scope.title='actDetailCtrl';

	$scope.download = downloadService.download;

	$scope.empty = listService.empty;

	$scope.is_end=function(str1,str2){
		return (str1 || str2);
	}

	$scope.end_activity = function(activity_id){

		var data ={"activity_id":activity_id}

		var post_update = httpService.post(globalUrl.updateActivity,data);

		post_update.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response['error']);
				return;
			}
			if(response.data['result'] == "200 OK"){
				$scope.$emit("activityEnd",response);	
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
		});
	}

});