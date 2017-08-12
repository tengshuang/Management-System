'user strict';

app.controller('updateCtrl',function($scope,globalUrl,httpService,dialogService,listService,$location) {
	$scope.title='updateCtrl';

	$scope.catelist=[];
	$scope.listObject=[];

	var get_req = httpService.get(globalUrl.getPostTarget);

	get_req.then(function successCallback(response) {
            if(response.data['error'] != null){
                dialogService.show(response.data['error']);
                $location.path("/login");
                return;
            }
            if(response.data['result'] == "200 OK"){
                //console.log(response.data['targetList']);
                $scope.listObject = response.data['targetList'];
                $scope.catelist = listService.getList($scope.listObject);
                //console.log($scope.listObject);
                //console.log($scope.catelist);
          }
        }, function errorCallback(response) {
            dialogService.show("服务器无响应！");
        });

	$scope.remove_cate = function(index){
		var data = {"id":$scope.listObject[index].id}

		var postAdd = httpService.post(globalUrl.deleteLabel,data);

		postAdd.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response.data['error']);
				$location.path("/login");
				return;
			}
			if(response.data['result'] == "200 OK"){
				dialogService.show('已成功删除分组！');
				$scope.catelist.splice(1,index);
				$scope.listObject.splice(1,index);
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
		});
	}

	$scope.produceLable = function(){
		var data = {
			"listname":$scope.listname,
			"fileId":$scope.fileId
		};

		//alert(JSON.stringify(data));

		var postAdd = httpService.post(globalUrl.createLabel,data);

		postAdd.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response.data['error']);
				return;
			}
			if(response.data['result'] == "200 OK"){

				var insert = {};
				insert.id = response.data['id'];
				insert.listname = $scope.listname;

				$scope.catelist.push($scope.listname);
				$scope.listObject.push(insert);

				$scope.listname="";
				$scope.fileId="";

				dialogService.show('已成功添加分组！');
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
		});
	}

	$scope.submitInvalid = function(){
		if(listService.empty($scope.fileId)){
			return true;
		}
		if(listService.empty($scope.listname)){
			return true;
		}
		return false;
	}

	$scope.$on("excelIdCreate",function (event,file_id) {
		//console.log(file_id);
        $scope.fileId = file_id;
    });

});