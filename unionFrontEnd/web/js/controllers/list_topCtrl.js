'user strict';

app.controller('list_topCtrl',function($scope,downloadService,listService,httpService,dialogService,globalUrl) {

	$scope.toggleSearch=false;

	$scope.base_link = "#/entrance/top/eventDetail/";

	$scope.bool_children=listService.bool_children;
    $scope.empty=listService.empty;

    $scope.download = downloadService.download;

    var get_request = httpService.get(globalUrl.getListEvent+"?creator=1");
	get_request.then(function successCallback(response) {
            if(response.data['error'] != null){
                dialogService.show(response['error']);
                return;
            }
            if(response.data['result'] == "200 OK"){
                var xx = JSON.parse(response.data['eventList']);
                if (xx.length < 1){
                    dialogService.show('活动列表为空！');
                    return;
                }
                else{
            	   $scope.allAct=listService.getAllAct(xx,$scope.base_link);
                }
          }
        }, function errorCallback(response) {
            dialogService.show("服务器无响应！");
        });
	

});