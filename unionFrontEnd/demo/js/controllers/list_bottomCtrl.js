'user strict';

app.controller('list_bottomCtrl',function($scope,httpService,listService,globalUrl) {
	
	$scope.base_link = "demo/#/detail_bottom/";

    $scope.title = "我创建的子活动列表";

	//$scope.bool_children=listService.bool_children;
	//$scope.empty=listService.empty;
    //$scope.download = downloadService.download;

	var get_req = httpService.get(globalUrl.getCreatedActivity+"?user_id="+$scope.user_id);

	get_req.then(function successCallback(response) {
            if(response.data['error'] != null){
                alert(response['error']);
                return;
            }
            if(response.data['result'] == "200 OK"){
                var xx = response.data['activity'];
                var event_union=[];
                var activity_union=[];
                if (xx.length < 1){
                    alert('活动列表为空！');
                    return;
                }
                else{
                        for(var i = 0; i < xx.length; i++){
                            event_union.push(xx[i].event);
                            activity_union.push(xx[i].activity);
                        }
                        //$scope.event_union = event_union;
            	        $scope.allAct = listService.merge_EandA(event_union,activity_union,$scope.base_link);
                    }
          }
        }, function errorCallback(response) {
            alert("服务器无响应！");
        });
});