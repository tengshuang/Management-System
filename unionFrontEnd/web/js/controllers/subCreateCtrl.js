'user strict';

app.controller('subCreateCtrl',function($scope,globalUrl,$mdDialog,httpService,dialogService,timeService) {
	$scope.title='zengxiaopang';
    $scope.user_id = 1;

	$scope.time_tmp = new Date("");

    $scope.createacti = function(str){
        //console.log(str);

        $scope.acti.id = $scope.activity_union.id;
        $scope.acti.activity_created = 1;
        $scope.acti.tim = timeService.convert_time($scope.time_tmp);
        $scope.acti.attachment = $scope.event_union.attachment;

        var data={"activity":$scope.acti}

        var post_req = httpService.post(globalUrl.createActi,data);
        post_req.then(function successCallback(response) {
        // this callback will be called asynchronously
        // when the response is available
            if(response.data['result'] == "200 OK"){
            	$scope.$emit("Ctr1NameChange",response.data['activity_id']);
                $scope.closeDialog();
            }
            else{
                dialogService.show("子活动创建失败，请重新创建！");
            }

        }, function errorCallback(response) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
            dialogService.show("服务器错误！");
        });
    }

    $scope.closeDialog = function(){
    	$mdDialog.hide();
    }
});