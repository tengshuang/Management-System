'use strict';


app.controller('testCtrl', function($scope) {
    $scope.pureWater="纯净水";

    $scope.table = globalFileType.req_photo.table;
    $scope.key = globalFileType.req_photo.key;
});
