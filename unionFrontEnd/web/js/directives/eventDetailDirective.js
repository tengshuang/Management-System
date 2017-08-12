'use strict';

app.directive('eventDetail', function() {
    return {
         templateUrl: 'templates/eventDetail.html',
         controller:'eventDetailCtrl',
         scope:true
  };
});