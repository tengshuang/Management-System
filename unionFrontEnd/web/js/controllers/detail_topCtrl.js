'user strict';

app.controller('detail_topCtrl',function($scope,$mdDialog,globalUrl,listService,downloadService,$stateParams,$http,httpService,dialogService,$cookies) {

	$scope.empty=listService.empty;
	$scope.bool_children=listService.bool_children;

	$scope.event_id=$stateParams.event_id;

	$scope.user_id = JSON.parse($cookies.get('user')).id;
	
	var get_request = httpService.get(globalUrl.getSingleEvent+"?event_id="+$scope.event_id);

	get_request.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response['error']);
			}
			if(response.data['result'] == "200 OK"){
				$scope.event_union = JSON.parse(response.data['eventSingle']);
				$scope.chat_init();
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
		});


	$scope.show_list = function(activity,feedback,chat){
		if(activity == 1) return 1;
		if(feedback == 1) return 1;
		//if(chat == 1) return 1;
		return 0;
	}

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

	$scope.signed_list = [];

	var get_signed = httpService.get(globalUrl.getParticipants+"?event_id="+$scope.event_id);

	get_signed.then(function successCallback(response) {
		if(response.data['error'] != null){
			dialogService.show(response['error']);
			return;
		}
		if(response.data['result'] == "200 OK"){
			$scope.signed_list = response.data['user'];
			$scope.updateSignedList();
		}
	});

	$scope.showReplyContent = dialogService.show_title;

	$scope.showCheckin = function(activity_id) {
       	$mdDialog.show({
       		clickOutsideToClose: true,
          	locals:{
          		activity_id: activity_id
          	},
         	templateUrl:"templates/checkinTable.html",
         	controller: function DialogController($scope, $mdDialog, $http, httpService, globalUrl) {

            var get_signed = httpService.get(globalUrl.getParticipants+"?activity_id="+activity_id);

            get_signed.then(function successCallback(response) {
                if(response.data['error'] != null){
                    //dialogService.show(response['error']);
                    return;
                }
                if(response.data['result'] == "200 OK"){
                    $scope.signed_list = response.data['user'];
                }
            });

            $scope.closeDialog = function() {
              $mdDialog.hide();
            }
          }
       });
    };

	$scope.end_event=function(){
		var data ={
    		"option":{
				"id":$scope.event_union.id,
    			"status":"1"
    		}
    	}

		var post_update = httpService.post(globalUrl.updateEvent,data);

		post_update.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response['error']);
				return;
			}
			if(response.data['result'] == "200 OK"){
				dialogService.show('本活动变为结束状态');
				$scope.event_union.status = 1;
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
		});
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

	var getStatList = httpService.get(globalUrl.getStatList+"?event_id="+$scope.event_id);

	getStatList.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response['error']);
			}
			if(response.data['result'] == "200 OK"){
				$scope.child_list = response.data['eventStatList'];
				$scope.graph_init();
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
		});
	
	$scope.activityShow = function(detail){
		console.log(detail);
       	$mdDialog.show({
       		clickOutsideToClose: true,
          	locals:{
          		detail:detail
          	},
         	templateUrl:"templates/actShow.html",
         	controller:"actShowCtrl"
        });
	}

	$scope.download = downloadService.download;

	$scope.identify = function(user_id){
		//
		//alert($scope.event_union.creator);
		if(user_id == $scope.event_union.creator) return 0;
		return 1;
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
	
	$scope.qrcode={
		v:4,
		e:'M',
		s:274,
		//bar:'https://github.com/monospaced/angular-qrcode',
		foo:'event:'+$scope.event_id
	}

	$scope.downloadQrcode = function($event) {
		var parentE3 = angular.element(document.body);
       	$mdDialog.show({
         	parent: parentE3,
         	scope:$scope,
          	preserveScope: true,
         	targetEvent: $event,
         	templateUrl:"templates/qrcodePro.html",
         });
	};

	$scope.download = downloadService.download;

	$scope.downloadXlsx = function() {
		var get_xlsx = httpService.get(globalUrl.generateExcel+"?event_id="+$scope.event_id);

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

	$scope.inform_again=function(activity_id){
		//alert(activity_id);
		var post_inform = httpService.post(globalUrl.informAgain,{"activity_id": activity_id});

		post_inform.then(function successCallback(response) {
    		if(response.data['error'] != null){
				dialogService.show(response.data['error']);
				return;
			}
			if(response.data['result'] == "200 OK"){
				dialogService.show('已再次通知');
			}
		}, function errorCallback(response) {
			dialogService.show("服务器无响应！");
		});

	}

	$scope.number1 = 0;
	$scope.number2 = 0;

	$scope.number3 = 0;
	$scope.number4 = 0;

	$scope.number5 = 0;
	$scope.number6 = 0;

	$scope.number7 = 0;
	$scope.number8 = 0;

	$scope.number9 = 0;
	$scope.numbe0 = 0;

	$scope.numbe1 = 0;
	$scope.numbe2 = 0;

	$scope.reply_content_time = [];


	$scope.graph_init=function(){
		console.log($scope.child_list);
		for(i = 0; i < $scope.child_list.length; i++){
			//if($scope.child_list[i].reply_content_time==null)
				//$scope.reply_content_time.push({"label":$scope.child_list[i].realname,"value":0});

			if($scope.child_list[i].is_reply == true)
				$scope.number1 = $scope.number1 + 1;
			if($scope.child_list[i].is_reply == false)
				$scope.number2 = $scope.number2 + 1;

			if($scope.child_list[i].reply_content != null)
				$scope.number3 = $scope.number3 + 1;
			if($scope.child_list[i].reply_content == null)
				$scope.number4 = $scope.number4 + 1;

			if($scope.child_list[i].reply_file != null)
				$scope.number5 = $scope.number5 + 1;
			if($scope.child_list[i].reply_file == null)
				$scope.number6 = $scope.number6 + 1;

			if($scope.child_list[i].activity_created == true)
				$scope.number7 = $scope.number7 + 1;
			if($scope.child_list[i].activity_created == false)
				$scope.number8 = $scope.number8 + 1;

			if($scope.child_list[i].report_file != null)
				$scope.numbe1 = $scope.numbe1 + 1;
			if($scope.child_list[i].report_file == null)
				$scope.numbe2 = $scope.numbe2 + 1;

			if($scope.child_list[i].photo_file != null)
				$scope.number9 = $scope.number9 + 1;
			if($scope.child_list[i].photo_file == null)
				$scope.numbe0 = $scope.numbe0 + 1;
		}
		/*
		console.log($scope.number9);
		console.log($scope.numbe1);
		console.log($scope.numbe0);
		console.log($scope.numbe2);
		*/
		$scope.respondSource = {
    		chart: {
        		caption: "反馈情况",
        		startingangle: "120",
        		showlabels: "0",
        		showlegend: "1",
        		enablemultislicing: "0",
        		slicingdistance: "15",
        		showpercentvalues: "1",
        		showpercentintooltip: "0",
        		plottooltext: "数量: $value",
        		tag: 1
        		//theme: "fint"
    		},
    		data: [
        	{
            	label: "已反馈",
            	value: $scope.number1,
            	
        	},
        	{
            	label: "未反馈",
            	value: $scope.number2,
        	}
    		]
		};
		$scope.respondtxt = {
    		chart: {
        		caption: "反馈文本情况",
        		startingangle: "120",
        		showlabels: "0",
        		showlegend: "1",
        		enablemultislicing: "0",
        		slicingdistance: "15",
        		showpercentvalues: "1",
        		showpercentintooltip: "0",
        		plottooltext: "数量: $value",
        		tag:2
        //theme: "fint"
    		},
    		data: [
        		{
            		label: "已反馈",
            		value: $scope.number3
        		},
        		{
            		label: "未反馈",
            		value: $scope.number4
        		}
    		]
			};

		$scope.respondfile = {
    		chart: {
        		caption: "反馈文件情况",
        		startingangle: "120",
        		showlabels: "0",
        		showlegend: "1",
        		enablemultislicing: "0",
        		slicingdistance: "15",
        		showpercentvalues: "1",
        		showpercentintooltip: "0",
        		plottooltext: "数量: $value",
        		tag:3
        //theme: "fint"
    		},
    		data: [
        		{
           			label: "已反馈",
            		value: $scope.number5
        		},
        		{
            		label: "未反馈",
            		value: $scope.number6
        		}
    		]
		};

		$scope.respondactiv = {
    	chart: {
        	caption: "子活动数量",
        	startingangle: "120",
        	showlabels: "0",
        	showlegend: "1",
        	enablemultislicing: "0",
        	slicingdistance: "15",
        	showpercentvalues: "1",
        	showpercentintooltip: "0",
        	plottooltext: "数量: $value",
        	tag:4
        //theme: "fint"
    	},
    	data: [
        	{
            	label: "已创建",
            	value: $scope.number7
        	},
        	{
            	label: "未创建",
            	value: $scope.number8
        	}
   			]
		};

		$scope.respondactivpic = {
    	chart: {
        	caption: "子活动照片",
        	startingangle: "120",
        	showlabels: "0",
        	showlegend: "1",
        	enablemultislicing: "0",
        	slicingdistance: "15",
        	showpercentvalues: "1",
        	showpercentintooltip: "0",
        	plottooltext: "数量: $value",
        	tag:5
        //theme: "fint"
    	},
    	data: [
        	{
            	label: "已提交",
            	value: $scope.number9
        	},
        	{
            	label: "未提交",
            	value: $scope.numbe0
        	}
   			]
		};

		$scope.respondactivfile = {
    	chart: {
        	caption: "子活动报告",
        	startingangle: "120",
        	showlabels: "0",
        	showlegend: "1",
        	enablemultislicing: "0",
        	slicingdistance: "15",
        	showpercentvalues: "1",
        	showpercentintooltip: "0",
        	plottooltext: "数量: $value",
        	tag:6
        //theme: "fint"
    	},
    	data: [
        	{
            	label: "已提交",
            	value: $scope.numbe1
        	},
        	{
            	label: "未提交",
            	value: $scope.numbe2
        	}
   			]
		};
		$scope.initialized = true;
	};

	$scope.events = {
    	dataplotclick: function (ev, props) {
        $scope.$apply(function (type) {
        	
        	if(props.index == 0){
        		if(!isNaN(props.chartX)){

        			var time = [];
        			var type = ev.sender.options.dataSource.chart.tag;
					console.log(type);
					for(i = 0; i < $scope.child_list.length; i++){
						if(type==1){
						if($scope.child_list[i].is_reply_time!=null)
							time.push({
								"label":$scope.child_list[i].realname,
								"value":(Date.parse($scope.child_list[i].is_reply_time) 
										- Date.parse($scope.event_union.creation_time))/1000
							});
						}
						if(type==2){
						if($scope.child_list[i].reply_content_time!=null)
							time.push({
								"label":$scope.child_list[i].realname,
								"value":(Date.parse($scope.child_list[i].reply_content_time) 
										- Date.parse($scope.event_union.creation_time))/1000
							});
						}
						if(type==3){
						if($scope.child_list[i].reply_file_time!=null)
							time.push({
								"label":$scope.child_list[i].realname,
								"value":(Date.parse($scope.child_list[i].reply_file_time) 
										- Date.parse($scope.event_union.creation_time))/1000
							});
						}
						if(type==4){
						if($scope.child_list[i].creation_time!=null)
							time.push({
								"label":$scope.child_list[i].realname,
								"value":(Date.parse($scope.child_list[i].creation_time) 
										- Date.parse($scope.event_union.creation_time))/1000
							});
						}
						if(type==5){
						if($scope.child_list[i].photo_file_time!=null)
							if($scope.child_list[i].creation_time!=null){
							time.push({
								"label":$scope.child_list[i].realname,
								"value":(Date.parse($scope.child_list[i].photo_file_time) 
										- Date.parse($scope.child_list[i].creation_time))/1000
							});}
						}
						if(type==6){
						if($scope.child_list[i].report_file_time!=null)
							if($scope.child_list[i].creation_time!=null){
							time.push({
								"label":$scope.child_list[i].realname,
								"value":(Date.parse($scope.child_list[i].report_file_time) 
										- Date.parse($scope.child_list[i].creation_time))/1000
							});}
						}

					}
					if(type == 1){
						var titlea = "反馈时间间隔";
						var subtitle = "（活动发布至下级回复收到时间）"
					}
					if(type == 2){
						var titlea = "反馈文本信息时间间隔";
						var subtitle = "（活动发布至下级回复文本信息时间）"
					}
					if(type == 3){
						var titlea = "反馈文件时间间隔";
						var subtitle = "（活动发布至下级回复文件时间）"
					}
					if(type == 4){
						var titlea = "创建子活动时间间隔";
						var subtitle = "（活动发布至下级创建子活动时间）"
					}
					if(type == 5){
						var titlea = "反馈子活动照片时间间隔";
						var subtitle = "（创建子活动至提交子活动照片时间）"
					}
					if(type == 6){
						var titlea = "反馈子活动报告时间间隔";
						var subtitle = "（创建子活动至提交子活动报告时间）"
					}
            		$mdDialog.show({
            			locals:{
          					dataX:time,
          					titleX:titlea,
          					subTitleX:subtitle
          				},
       					clickOutsideToClose: true,
         				templateUrl:"templates/simpleChart.html",
         				controller:"simpleChartCtrl"
        			});
        		}
        	}
       
        });
    }};


});