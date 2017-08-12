'use strict';


app.controller('uploadCtrl', function($scope, FileUploader,$timeout,dialogService,globalUrl) {

        $scope.record = 0;

        $scope.withForm = function(a1,a2,a3){
            if(a1 == null) return 0;
            if(a2 == null) return 0;
            if(a3 == null) return 0;
            $scope.record = 1;
            return 1;
        }

        if($scope.withForm($scope.table,$scope.key,$scope.id)){
            $scope.uploader = new FileUploader({
                url: globalUrl.fileUpload,
                formData:[{"table":$scope.table},{"key":$scope.key},{"id":$scope.id}]
            });
        }
        else{
            $scope.uploader = new FileUploader({
                url: globalUrl.fileUpload
            });
        }
        
        // FILTERS

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
            dialogService.show("上传已经取消");

        }
        $scope.uploader.onSuccessItem = function(item, response, status, headers) {
            if(response['result'] == "200 OK"){
                dialogService.show("上传成功！");
                if($scope.record == 0){
                    if($scope.type="event"){
                        $scope.$emit("eventIdCreate",response['file_id']);
                    }
                    if($scope.type="label"){
                        $scope.$emit("excelIdCreate",response['file_id']);
                    }
                }
            }
            else{
                dialogService.show(response['error']);
            }
        }
        
        $scope.uploader.onErrorItem = function(item, response, status, headers) {
            dialogService.show("上传失败！");
        }

});
