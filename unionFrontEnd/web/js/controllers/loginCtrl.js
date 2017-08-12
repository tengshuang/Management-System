'user strict';

app.controller('loginCtrl',function($scope,authenService,dialogService,listService,globalUrl,$location, $cookies) {
	$scope.title='loginCtrl';

	$scope.pubkey="";

	$scope.login = function(user){

		var get_req = authenService.getKey(user.no);

		get_req.then(function successCallback(response) {
            if(response.data['error'] != null){
                //console.log(response.data['error']);
                dialogService.show(response.data['error']);
                return;
            }
            if(response.data['result'] == "200 OK"){
                //alert($cookies.get('JSESSIONID'));
            	$scope.pubkey = response.data['pubkey'];

            	var encode_user = JSON.parse(JSON.stringify(user));
            	encode_user.password = authenService.encode($scope.pubkey,encode_user.password);
            	var post_req = authenService.loginCre(encode_user);
				post_req.then(function successCallback(response) {
            		if(response.data['error'] != null){
                        //console.log(JSON.stringify(response));
                		dialogService.show(response.data['error']);
                		return;
            		}
            		if(response.data['result'] == "200 OK"){
            			//console.log(JSON.stringify(response.data['user']));
                        dialogService.show("您已成功登录");
                        $cookies.put('user',JSON.stringify(response.data['user']));
                        $location.path('/entrance');
          			}
        		}, function errorCallback(response) {
            		dialogService.show("服务器无响应！");
            		return;
        		});
			}
        }, function errorCallback(response) {
            dialogService.show("服务器无响应！");
            return;
        });

	}
});