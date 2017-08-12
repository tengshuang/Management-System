'user strict';

app.controller('detailCtrl',function($scope,downloadService,globalUrl,$mdDialog,listService,httpService,$stateParams,dialogService,FILETYPE,$cookies) {

	$scope.handlist = [];
	$scope.sign_cash=[];

	$scope.empty = listService.empty;
	$scope.bool_children = listService.bool_children;

	$scope.download=downloadService.download;

	$scope.relyFileTable = FILETYPE.reply_txt.table;
    $scope.relyFileKey = FILETYPE.reply_txt.key;

    $scope.reqPhotoTable = FILETYPE.req_photo.table;
    $scope.reqPhotoKey = FILETYPE.req_photo.key;

    $scope.reqReportTable = FILETYPE.req_report.table;
    $scope.reqReportKey = FILETYPE.req_report.key;

	$scope.activity_id = $stateParams.activity_id;


	// need readFromCookie
	$scope.user_id = JSON.parse($cookies.get('user')).id;
	//alert($scope.user_id);

	$scope.reqContent = {
		role:1,
		child:1,
	}

	$scope.add = function(str){

		$scope.handlist.push($scope.sign_list[str].realname);
		$scope.sign_cash.push($scope.sign_list[str].id)
	}

	$scope.remove = function(index){
		//$scope.handlist.splice(index,1);
		$scope.sign_cash.splice(index,1);
	}

	$scope.finish = function(){
		var post_sign = httpService.post(globalUrl.manulSign,{"participants":$scope.sign_cash, "activity_id": $scope.activity_id});

		post_sign.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response.data['error']);
				return;
			}
			if(response.data['result'] == "200 OK"){
				dialogService.show('已手动签到');
				$scope.handlist = [];
				$scope.sign_cash = [];
				$scope.sign_list = [];
				$scope.signed_list = response.data['user'];
				$scope.updateSignedList();
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
		});

	}

	$scope.$on("Ctr1NameChange",function (event, activity_id) {
        dialogService.show("子活动创建成功！");
        $scope.activity_union.activity_created = 1;
    });

	var get_request = httpService.get(globalUrl.getAWithE+"?activity_id="+$scope.activity_id);

	get_request.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response['error']);
			}
			if(response.data['result'] == "200 OK"){
				$scope.event_union = response.data['activity']['event'];
				$scope.activity_union = response.data['activity']['activity'];
				//console.log($scope.event_union);
				//console.log($scope.activity_union);
				$scope.chat_init();
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
		});

	$scope.reqSubmit=function(str){
		var data = {reply:{
			id:$scope.activity_id,
			reply_content:str}};
		var post_update = httpService.post(globalUrl.replyEvent,data);

		post_update.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response['error']);
				return;
			}
			if(response.data['result'] == "200 OK"){
				dialogService.show('已确认收到');
				$scope.activity_union.reply_content=str;
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
		});
	}

	$scope.signed_list=[];

	get_request = httpService.get(globalUrl.getParticipants+"?activity_id="+$scope.activity_id);

	get_request.then(function successCallback(response) {
		if(response.data['error'] != null){
			dialogService.show(response['error']);
			return;
		}
		if(response.data['result'] == "200 OK"){
			$scope.signed_list = response.data['user'];
			$scope.updateSignedList();
		}
	});

	$scope.updateSignedList = function() {
		for(var i = 0; i < $scope.signed_list.length; ++i) {
			switch($scope.signed_list[i].checkin_stat) {
				case 1:
					$scope.signed_list[i].checkin_type = "二维码";
				break;
				case 2:
					$scope.signed_list[i].checkin_type = "手动签到";
				break;
			}
		}
	}

	$scope.identify = function(user_id){
		//
		//alert($scope.event_union.creator);
		if(user_id == $scope.event_union.creator) return 0;
		return 1;
	}

	$scope.chat_init= function(){
		if($scope.event_union.chat == 1){
		var get_message = httpService.get(globalUrl.getMessage+"?event_id="+$scope.event_union.id);

		get_message.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response['error']);
			}
			if(response.data['result'] == "200 OK"){
				$scope.todos = JSON.parse(response.data['message']);
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
		});
		}
	}
	
	$scope.Feed =function(){

		var data = {reply:{id:$scope.activity_id,is_reply:1}};
		var post_update = httpService.post(globalUrl.replyEvent,data);

		post_update.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response['error']);
				return;
			}
			if(response.data['result'] == "200 OK"){
				dialogService.show('已确认收到');
				$scope.activity_union.is_reply = 1;
			}
			else{
				mdDialog.show("ERROR");
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
		});

	};

	$scope.addPerson=function($event){
		console.log("addPerson");
		var parentE2 = angular.element(document.body);
       	$mdDialog.show({
         	parent: parentE2,
         	targetEvent: $event,
         	scope:$scope,
          	preserveScope: true,
         	templateUrl:"templates/addPerson.html",
         	controller:"addCtrl"
         });
	};

	$scope.search = function(str){
		//search for people to sign in
		var get_user = httpService.get(globalUrl.searchUser+"?search="+str);

		get_user.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response['error']);
			}
			if(response.data['result'] == "200 OK"){
				$scope.sign_list=response.data['users'];
				console.log(response.data);
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
		});

	}

	$scope.question=function(str){
		var data = {message:
					{user_id:$scope.user_id,
					 event_id:$scope.event_union.id,
					 content:str}
					};

		var post_question = httpService.post(globalUrl.newMessage,data);

		post_question.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response['error']);
				return;
			}
			if(response.data['result'] == "200 OK"){
				$scope.todos.push(response.data['message']);
				dialogService.show('已成功提问！');
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
		});
	};


	$scope.groupforward = function($event) {
		var parentEl = angular.element(document.body);
       	$mdDialog.show({
         	parent: parentEl,
         	scope:$scope,
          	preserveScope: true,
         	targetEvent: $event,
         	templateUrl:"templates/subCreate.html",
        });

	};

	$scope.qrcode={
		v:4,
		e:'M',
		s:274,
		//bar:'https://github.com/monospaced/angular-qrcode',
		foo:'activity:'+$scope.activity_id
	}

	$scope.downloadQrcode = function($event) {
		var parentE3 = angular.element(document.body);
       	$mdDialog.show({
         	parent: parentE3,
         	scope:$scope,
          	preserveScope: true,
         	targetEvent: $event,
         	templateUrl:"templates/qrcodePro.html",
         	//locals: {
           		//title: $scope.title
         	//}
         });
	};
	
	$scope.download = downloadService.download;

	$scope.downloadXlsx = function() {
		var get_xlsx = httpService.get(globalUrl.generateExcel+"?activity_id="+$scope.activity_id);

		get_xlsx.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response['error']);
			}
			if(response.data['result'] == "200 OK"){
				$scope.download(response.data['id']);
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
		});

	}

});