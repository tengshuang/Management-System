'use strict';


app.controller('sidebarCtrl', function($scope,$cookies) {

    $scope.current_user = JSON.parse($cookies.get('user'));

    if($scope.current_user == null){
        $location.path("/login");
    }


});