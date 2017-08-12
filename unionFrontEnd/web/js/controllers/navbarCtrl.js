'user strict';

app.controller('navbarCtrl', function($scope,$mdSidenav,$location,httpService,dialogService,globalUrl,$cookies){

  $scope.name="navbarCtrl";

  $scope.toggleSidenav = function(menuId) {
    $mdSidenav(menuId).toggle();
  };

  $scope.current_user = JSON.parse($cookies.get('user'));

  if($scope.current_user == null){
    $location.path("/login");
  }

  $scope.logout = function(){
    console.log("logout");
    var data = {};
    var post_logout = httpService.post(globalUrl.logOutAccess,data);

    post_logout.then(function successCallback(response) {
        if(response.data['error'] != null){
          console.log(response);
          dialogService.show(response.data['error']);
        return;
      }
      if(response.data['result'] == "200 OK"){
        dialogService.show('已登出');
        $location.path('/login');     
      }
    }, function errorCallback(response) {
      dialogService.show("服务器无响应！");
    });
  }

  $scope.clicked_str = "";

  $scope.click_href = function(str){
    $scope.clicked_str = str;
  }
 	$scope.top_menu = [
    {
      link : '#/entrance/top/create',
      title: '创建我的活动',
      icon: 'img/icons-svg/content/svg/design/ic_create_24px.svg'
    },
    {
      link : '#/entrance/top/eventList',
      title: '我创建的活动列表',
      icon: 'img/icons-svg/action/svg/design/ic_receipt_24px.svg'
    },
    {
      link : '#/entrance/top/updatePostarget',
      title: '更新资料',
      icon: 'img/icons-svg/notification/svg/design/ic_sync_24px.svg'
    },
    {
      link : '#/entrance/top/addAdmin',
      title: '添加管理员',
      icon: 'img/icons-svg/action/svg/design/ic_note_add_24px.svg'
    }
  ];

  $scope.menu = [
    {
      link : '#/entrance/intermediate/eventList',
      title: '我收到的活动列表',
      icon: 'img/icons-svg/action/svg/design/ic_receipt_24px.svg'
    },
    {
      link : '#/entrance/intermediate/activityList',
      title: '我创建的子活动列表',
      icon: 'img/icons-svg/action/svg/design/ic_list_24px.svg'
    }
  ];

  $scope.bottom_menu = [
    {
      link : '#/listbottomr',
      title: '我收到的活动列表',
      icon: 'img/icons-svg/action/svg/design/ic_receipt_24px.svg'
    },
    {
      link : '#/listbottom',
      title: '我收到的子活动列表',
      icon: 'img/icons-svg/action/svg/design/ic_list_24px.svg'
    }
  ];
  
});
