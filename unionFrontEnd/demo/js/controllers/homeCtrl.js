'user strict';

app.controller('homeCtrl',function($scope,$routeParams,$location,httpService,globalUrl,$cookies) {

	$scope.login = function(ticket){
		var data = {ticket:ticket};
		//var data = {test_no:'2012011284'};
		var post_update = httpService.post(globalUrl.LoginAccess, data);

		post_update.then(function successCallback(response) {
    		if(response.data['error'] != null){
				alert(response['error']);
				return;
			}
			if(response.data['result'] == "200 OK"){
				console.log("登陆成功！");
				var user = response.data['user'];
				$scope.setUser(user);
    			//$cookies.put('JSESSIONID',response.data['sessionId']);
			}
		}, function errorCallback(response) {
			alert("服务器无响应！");
		});
	};

	var ticket = $location.search().ticket;
	$scope.login(ticket);
	

});