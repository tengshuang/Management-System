'user strict';

app.controller('listCtrl',function($scope,downloadService,listService,httpService,dialogService,globalUrl) {
	$scope.title='zengxiaopang';

	$scope.toggleSearch=false;

	// need readFromCookie
	$scope.user_id = 1;

	$scope.base_link = "#/entrance/intermediate/eventDetail/";

	$scope.bool_children=listService.bool_children;
	$scope.empty=listService.empty;
    $scope.download = downloadService.download;

	var get_req = httpService.get(globalUrl.getRecvEvent+"?user_id="+$scope.user_id);

	get_req.then(function successCallback(response) {
            if(response.data['error'] != null){
                dialogService.show(response.data['error']);
                return;
            }
            if(response.data['result'] == "200 OK"){
                var xx = JSON.parse(response.data['resultList']);
                //console.log(xx);
                if (xx.length < 1){
                    dialogService.show('活动列表为空！');
                    return;
                }
                else{
            	   $scope.allAct=listService.getAllAct_activity(xx,$scope.base_link);
                   //console.log($scope.allAct);
                }
          }
        }, function errorCallback(response) {
            dialogService.show("服务器无响应！");
        });
});