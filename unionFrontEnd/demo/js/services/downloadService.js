'user strict'

app.service('downloadService', function($mdDialog, httpService) {
    this.download = function(file_id){
		console.log(file_id);
    /*
		$mdDialog.show({
       		clickOutsideToClose: true,
          	locals:{
          		file_id :file_id
          	},
         	templateUrl:"templates/download.html",
         	controller:"downloadCtrl"
        });
    */
    var get_request = httpService.get(globalUrl.getFile+"?id="+$scope.file_id);

    get_request.then(function successCallback(response) {
        if(response.data['error'] != null){
          alert(response['error']);
        }
        if(response.data['result'] == "200 OK"){
          $scope.file = response.data['file'];
          console.log(address_server);
          window.location.href = address_server + $scope.file.url;
        }
      }, function errorCallback(response) {
        alert("服务器无响应！");
      });
	}
});