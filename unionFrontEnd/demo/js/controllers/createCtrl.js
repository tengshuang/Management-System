'user strict';

app.controller('createCtrl',function($scope,httpService,globalUrl, listService, timeService){

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

	$scope.checkBox  = ['子活动签到（二维码）','上传子活动照片','提交子活动总结'];

    $scope.postTargetBox = [];
    $scope.postTargetItems = [];

    var get_req = httpService.get(globalUrl.getPostTarget);
    get_req.then(function successCallback(response) {
            if(response.data['error'] != null){
                alert("error..?");
                return;
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
            alert("服务器无响应！");
    });

    for(var i = 0; i < $scope.postTargetBox.length; i++){
        $scope.postTargetItems.push(i+1);
    }

    $scope.postTargetSelected = [];

    $scope.toggle = function (item, list) {
        var idx = list.indexOf(item);
        if (idx > -1) list.splice(idx, 1);
        else list.push(item);
    };

    $scope.$on("eventIdCreate",function (event,file_id) {
        $scope.evt.attachment = file_id;
    });

    $scope.exists = function (item, list) {
        return list.indexOf(item) > -1;
    };

    $scope.creatable = function(aa){
        if(aa == 1) return 1;
        if($scope.postTargetSelected.length < 1) 
        {
            return 1;
        }
        if($scope.evt.activity == 1){
            if($scope.evt.req_photo == 0 && $scope.evt.req_qrcode == 0 && $scope.evt.req_report == 0){ 
                return 1;}
        }
        if($scope.evt.feedback == 1 ){
            if($scope.evt.recieve == 0 && $scope.evt.file == 0 && $scope.evt.txt == 0){ 
                 return 1;}
        }
        return 0;
    }

    $scope.createevt = function(){

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
        var post_request = httpService.post(globalUrl.createEvent,{"event":$scope.evt,"postTarget":postTarget_final});

        post_request.then(function successCallback(response) {
        // this callback will be called asynchronously
        // when the response is available
            if(response.data['result'] == "200 OK"){
                alert("活动创建成功！");
            }
            else{
                alert("活动创建失败，请重新创建！");
            }

        }, function errorCallback(response) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
            alert("服务器错误！");
        });

  	};

});