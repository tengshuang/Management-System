'user strict';

app.controller('list_topCtrl',function($scope,listService,httpService,globalUrl) {
    
	$scope.base_link = "demo/#/detail_top/";

    $scope.title = "我创建的活动列表";
    var get_request = httpService.get(globalUrl.getListEvent+"?creator=" + $scope.userId);
	  get_request.then(function successCallback(response) {
            if(response.data['error'] != null){
                alert(response['error']);
                return;
            }
            if(response.data['result'] == "200 OK"){
                var xx = JSON.parse(response.data['eventList']);
                if (xx.length < 1){
                    alert('活动列表为空！');
                    return;
                }
                else{
            	   $scope.allAct=listService.getAllAct(xx,$scope.base_link);
                   
                }
          }
        }, function errorCallback(response) {
            alert("服务器无响应！");
        });
});