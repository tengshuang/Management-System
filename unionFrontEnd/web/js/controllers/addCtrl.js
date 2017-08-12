'user strict';

app.controller('addCtrl',function($scope,$mdDialog,httpService,globalUrl,dialogService) {
	$scope.title='zengxiaopang';

	$scope.readonly = false;

    // Lists of fruit names and Vegetable objects
    $scope.list = [];

    $scope.selectedUsers = [];

    $scope.finish=function(){
    	var data = {
    		"activity_id":$scope.activity_union.id,
    		"authority_id":$scope.selectedUsers
    	}
    	
    	var postAdd = httpService.post(globalUrl.addActivityAuth,data);

		postAdd.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response.data['error']);
				return;
			}
			if(response.data['result'] == "200 OK"){

				dialogService.show('已成功添加授权者！');
				$mdDialog.hide();
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
		});
    	//$mdDialog.hide();
    }

    $scope.remove_user = function(idx){
    	$scope.selectedUsers.splice(idx, 1);
    	//console.log($scope.selectedUsers);
    	//console.log($scope.list);
    }

    $scope.addtoList = function(person){
    	//console.log(person);
    	$scope.selectedUsers.push(person.id);
    	$scope.list.push(person.realname);
    }

    $scope.searchPerson = function(searchContent){
    	//console.log(searchContent);
    	var get_req = httpService.get(globalUrl.searchUser+"?search="+searchContent);

		get_req.then(function successCallback(response) {
            if(response.data['error'] != null){
                dialogService.show(response.data['error']);
                return;
            }
            if(response.data['result'] == "200 OK"){
                $scope.recommend_person=response.data['users'];
          }
        }, function errorCallback(response) {
            dialogService.show("服务器无响应！");
        });
    }

    $scope.closeDialog=function(){
    	console.log("close");
    	$mdDialog.hide();
    }
});