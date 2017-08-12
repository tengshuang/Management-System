'user strict';

app.controller('list_bottomCtrl',function($scope,downloadService,httpService,listService,dialogService,globalUrl) {
	$scope.title='list_bottomCtrl';

	$scope.toggleSearch=false;

	$scope.user_id = 1;

	$scope.base_link = "#/entrance/intermediate/activityDetail/";

	$scope.bool_children=listService.bool_children;
	$scope.empty=listService.empty;
    $scope.download = downloadService.download;

	var get_req = httpService.get(globalUrl.getCreatedActivity+"?user_id="+$scope.user_id);

	get_req.then(function successCallback(response) {
            if(response.data['error'] != null){
                dialogService.show(response['error']);
                return;
            }
            if(response.data['result'] == "200 OK"){
                var xx = response.data['activity'];
                var event_union=[];
                var activity_union=[];
                if (xx.length < 1){
                    dialogService.show('活动列表为空！');
                    return;
                }
                else{
                        for(var i = 0; i < xx.length; i++){
                            event_union.push(xx[i].event);
                            activity_union.push(xx[i].activity);
                        }

                        $scope.event_union = event_union;
            	        $scope.activity_union = listService.merge_EandA(event_union,activity_union,$scope.base_link);
                        console.log($scope.event_union);
                        console.log($scope.activity_union);
                    }
          }
        }, function errorCallback(response) {
            dialogService.show("服务器无响应！");
        });
});