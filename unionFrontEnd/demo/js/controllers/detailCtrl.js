'user strict';

app.controller('detailCtrl',function($scope,globalUrl,listService,$routeParams,httpService,FILETYPE) {

	$scope.string_convert = listService.string_convert;
	//$scope.bool_children=listService.bool_children;
	$scope.empty = listService.empty;

	$scope.relyFileTable = FILETYPE.reply_txt.table;
    $scope.relyFileKey = FILETYPE.reply_txt.key;

    $scope.reqPhotoTable = FILETYPE.req_photo.table;
    $scope.reqPhotoKey = FILETYPE.req_photo.key;

    $scope.reqReportTable = FILETYPE.req_report.table;
    $scope.reqReportKey = FILETYPE.req_report.key;


	$scope.activity_id = $routeParams.activity_id;

	$scope.event_union = undefined;
	$scope.activity_union = undefined;

	$scope.reqSubmit = function(str){
		var data = {reply:{
			id:$scope.activity_id,
			reply_content:str}};
		var post_update = httpService.post(globalUrl.replyEvent,data);

		post_update.then(function successCallback(response) {
    		if(response.data['error'] != null){
				alert(response['error']);
				return;
			}
			if(response.data['result'] == "200 OK"){
				alert('已确认收到');
				$scope.activity_union.reply_content=str;
			}
		}, function errorCallback(response) {
			alert("服务器无响应！");
		});
	}

	$scope.Feed =function(){

		var data = {reply:{id:$scope.activity_id,is_reply:1}};
		var post_update = httpService.post(globalUrl.replyEvent,data);

		post_update.then(function successCallback(response) {
    		if(response.data['error'] != null){
				alert(response['error']);
				return;
			}
			if(response.data['result'] == "200 OK"){
				alert('已确认收到');
				$scope.activity_union.is_reply = 1;
			}
			else{
				alert("ERROR");
			}
		}, function errorCallback(response) {
			alert("服务器无响应！");
		});

	};

	$scope.downloadQrcode = function() {
		$scope.qrcode={
			v:4,
			e:'M',
			s:274,
			//bar:'https://github.com/monospaced/angular-qrcode',
			foo:"activity:" + $scope.activity_id
		}
	};
	
	$scope.download = function(file_id){
		var get_request = httpService.get(globalUrl.getFile+"?id="+file_id);

		get_request.then(function successCallback(response) {
    		if(response.data['error'] != null){
				alert(response['error']);
			}
			if(response.data['result'] == "200 OK"){
				$scope.file = response.data['file'];
				//console.log(address_server);
				$scope.file.url = address_server + $scope.file.url;
			}
		}, function errorCallback(response) {
			alert("服务器无响应！");
		});
	}

	var get_request = httpService.get(globalUrl.getAWithE+"?activity_id="+$scope.activity_id);

	get_request.then(function successCallback(response) {
    		if(response.data['error'] != null){
				alert(response['error']);
			}
			if(response.data['result'] == "200 OK"){
				$scope.event_union = response.data['activity']['event'];
				$scope.activity_union = response.data['activity']['activity'];
				console.log($scope.event_union);
				console.log($scope.activity_union);
			}
		}, function errorCallback(response) {
			alert("服务器无响应！");
		});

});