'use strict';


app.controller('uploadCtrl', function($scope, FileUploader,$timeout,globalUrl) {


        //console.log($scope.table);
        //console.log($scope.key);
        //console.log($scope.id);

        $scope.record = 0;

        $scope.withForm = function(a1,a2,a3){
            if(a1 == null) return 0;
            if(a2 == null) return 0;
            if(a3 == null) return 0;
            $scope.record = 1;
            return 1;
        }

        if($scope.withForm($scope.table,$scope.key,$scope.id)){
            console.log("true");
            $scope.uploader = new FileUploader({
                url: globalUrl.fileUpload,
                formData:[{"table":$scope.table},{"key":$scope.key},{"id":$scope.id}]
            });
        }
        else{
            console.log("false");
            $scope.uploader = new FileUploader({
                url: globalUrl.fileUpload
            });
        }
        
        // FILTERS

        $scope.upload = function(){
            console.log('upload');
            $scope.uploader.uploadAll();
        }

        $scope.uploader.filters.push({
            name: 'customFilter',
            fn: function(item /*{File|FileLikeObject}*/, options) {
                if(this.queue.length >= 1){
                    $scope.uploader.clearQueue();
                }
                return true;
            }
        });

        // CALLBACKS

        $scope.uploader.onCancelItem = function(item, response, status, headers) {
            alert("上传已经取消");

        }
        $scope.uploader.onSuccessItem = function(item, response, status, headers) {
            if(response['result'] == "200 OK"){
                alert("上传成功！");
                if($scope.record == 0){
                    $scope.$emit("eventIdCreate",response['file_id']);
                }
            }
            else{
                alert(response['error']);
            }
        }
        
        $scope.uploader.onErrorItem = function(item, response, status, headers) {
            alert("上传失败！");
        }

});