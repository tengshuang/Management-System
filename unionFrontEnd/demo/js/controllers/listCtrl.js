'user strict';

app.controller('listCtrl',function($scope,listService,httpService,globalUrl) {

    $scope.base_link = "demo/#/detail/";

    $scope.title = "我收到的活动列表";

	var get_req = httpService.get(globalUrl.getRecvEvent+"?user_id="+$scope.getUserId());

    get_req.then(function successCallback(response) {
            if(response.data['error'] != null){
                alert(response['error']);
                return;
            }
            if(response.data['result'] == "200 OK"){
                var xx = JSON.parse(response.data['resultList']);
                console.log(xx);
                if (xx.length < 1){
                    alert('活动列表为空！');
                    return;
                }
                else{
                   $scope.allAct=listService.getAllAct_activity(xx,$scope.base_link);
                }
          }
        }, function errorCallback(response) {
            dialogService.show("服务器无响应！");
        });

});