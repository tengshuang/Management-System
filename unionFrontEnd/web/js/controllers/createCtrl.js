'user strict';

app.controller('createCtrl',function($scope,FILETYPE,globalUrl,listService,$location,timeService,httpService,dialogService) {

    $scope.temp ={
        start_date:new Date(""),
        end_date:new Date("")
    }

    $scope.evt={
        name:"",
        location:"",
        department:"",
        event_qrcode:0,
        status:0,
        activity:0,
        req_qrcode:0,
        req_photo:0,
        req_report:0,
        feedback:0,
        recieve:0,
        txt:0,
        file:0,
        attachment:-1,
    }

    $scope.$on("eventIdCreate",function (event,file_id) {
        $scope.evt.attachment = file_id;
    });

	$scope.checkBox  = ['子活动签到（二维码）','上传子活动照片','提交子活动总结'];

    $scope.postTargetBox = [];
    $scope.listObject = [];
    $scope.postTargetItems = [];
    

    var get_req = httpService.get(globalUrl.getPostTarget);

    get_req.then(function successCallback(response) {
            if(response.data['error'] != null){
                dialogService.show(response.data['error']);
                $location.path('/login');
            }
            if(response.data['result'] == "200 OK"){
                $scope.listObject = response.data['targetList'];
                $scope.postTargetBox = listService.getList($scope.listObject);
                for(var i = 0; i < $scope.postTargetBox.length; i++){
                    $scope.postTargetItems.push(i+1);
                }
                //console.log($scope.listObject);
                //console.log($scope.postTargetBox);
          }
        }, function errorCallback(response) {
            dialogService.show("服务器无响应！");
    });

    $scope.postTargetSelected = [];

	$scope.items = [1,2,3];
    $scope.selected = [];

    $scope.creatable = function(aa){
        if(aa == 1) return 1;
        if($scope.postTargetSelected.length < 1) return 1;
        else{
            if($scope.evt.activity == 1){
                if($scope.selected.length < 1) return 1;}
            if($scope.evt.feedback == 1 ){
                if(listService.bool_feedback($scope.evt) == 0) return 1;}
            }
        return 0;
    }

    $scope.toggle = function (item, list) {
        var idx = list.indexOf(item);
        if (idx > -1) list.splice(idx, 1);
        else list.push(item);
    };

    $scope.exists = function (item, list) {
        return list.indexOf(item) > -1;
    };

    $scope.createevt = function(){

        for(var i=0; i < $scope.selected.length; i++){
            var tmp = $scope.selected[i];
            console.log(tmp);
            if(tmp == 1) {$scope.evt.req_qrcode=1; continue;}
            if(tmp == 2) {$scope.evt.req_photo=1; continue;}
            if(tmp == 3) {$scope.evt.req_report=1;continue;}
        } 

        if($scope.temp.end_date && !isNaN($scope.temp.end_date.getTime())) {
            $scope.evt.end_date= timeService.convert_time($scope.temp.end_date);
        } else
            $scope.evt.end_date = undefined;
            
        if($scope.temp.start_date && !isNaN($scope.temp.start_date.getTime())) {
            $scope.evt.start_date = timeService.convert_time($scope.temp.start_date);
        } else
            $scope.evt.start_date = undefined;

        //console.log($scope.evt);
        //console.log($scope.postTargetSelected);

        var postTarget_final = listService.getPostargetID($scope.listObject,$scope.postTargetSelected);

        console.log(postTarget_final);

        var post_request = httpService.post(globalUrl.createEvent,{"event":$scope.evt,"postTarget":postTarget_final});

        post_request.then(function successCallback(response) {
        // this callback will be called asynchronously
        // when the response is available
            if(response.data['result'] == "200 OK"){
                dialogService.show("活动创建成功！");
            }
            else{
                dialogService.show("活动创建失败，请重新创建！");
            }

        }, function errorCallback(response) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
            dialogService.show("服务器错误！");
        });

  	};

});