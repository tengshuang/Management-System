'user strict';

app.controller('detail_bottomCtrl',function($scope,$stateParams,listService,dialogService,httpService,globalUrl) {

	$scope.empty = listService.empty;
	$scope.bool_children = listService.bool_children;

	$scope.activity_id = $stateParams.activity_id;

	$scope.$on("activityEnd",function (event,response) {
        dialogService.show("子活动已结束！");
        $scope.activity_union.status = 1;
    });



	var get_request = httpService.get(globalUrl.getAWithE+"?activity_id="+$scope.activity_id);

	get_request.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response['error']);
			}
			if(response.data['result'] == "200 OK"){
				$scope.event_union = response.data['activity']['event'];
				$scope.activity_union = response.data['activity']['activity'];
				$scope.activity_union.role="管理者";
				console.log($scope.event_union);
				console.log($scope.activity_union);
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
	});

});