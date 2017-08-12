// 
// Here is how to define your module 
// has dependent on mobile-angular-ui
// 
var app = angular.module('MobileAngularUiExamples', ['angularFileUpload',
  'ngRoute',
  'mobile-angular-ui',
  
  // touch/drag feature: this is from 'mobile-angular-ui.gestures.js'
  // it is at a very beginning stage, so please be careful if you like to use
  // in production. This is intended to provide a flexible, integrated and and 
  // easy to use alternative to other 3rd party libs like hammer.js, with the
  // final pourpose to integrate gestures into default ui interactions like 
  // opening sidebars, turning switches on/off ..
  'mobile-angular-ui.gestures','monospaced.qrcode',
  'ngCookies'
]);

app.run(function($transform) {
  window.$transform = $transform;
});

// 
// You can configure ngRoute as always, but to take advantage of SharedState location
// feature (i.e. close sidebar on backbutton) you should setup 'reloadOnSearch: false' 
// in order to avoid unwanted routing.
// 
app.config(function($routeProvider, $httpProvider) {
  
  $httpProvider.defaults.withCredentials = true;

  $routeProvider.when('/',              {templateUrl: 'demo/templates/home.html', controller:'homeCtrl', reloadOnSearch: false});
  $routeProvider.when('/list_top',        {templateUrl: 'demo/templates/list.html', controller:'list_topCtrl',reloadOnSearch: false});
  
  $routeProvider.when('/list_bottom',        {templateUrl: 'demo/templates/list.html', controller:'list_bottomCtrl',reloadOnSearch: false});

  $routeProvider.when('/list',        {templateUrl: 'demo/templates/list.html', controller:'listCtrl',reloadOnSearch: false});

  $routeProvider.when('/upload',         {templateUrl: 'demo/templates/uploadFile.html',controller:"uploadCtrl",reloadOnSearch: false});
  $routeProvider.when('/detail_top/:event_id',         {templateUrl: 'demo/templates/detail_top.html',controller:"detail_topCtrl",reloadOnSearch: false});
  $routeProvider.when('/detail/:activity_id',         {templateUrl: 'demo/templates/detail.html',controller:"detailCtrl",reloadOnSearch: false});
  $routeProvider.when('/detail_bottom/:activity_id',         {templateUrl: 'demo/templates/detail_bottom.html',controller:"detail_bottomCtrl",reloadOnSearch: false});
  
  $routeProvider.when('/create',         {templateUrl: 'demo/templates/create.html',controller:"createCtrl",reloadOnSearch: false});
});

//
// For this trivial demo we have just a unique MainController 
// for everything
//

var address_server = "http://grayluck.vicp.cc:8080/UnionBackend/";

app.constant("globalUrl",{
  createEvent:address_server + "api/CreateEvent",
  createActi:address_server + "api/CreateActivity",
  updateEvent:address_server+"api/UpdateEvent",
  getListEvent:address_server + "api/GetListEvent",
  getSingleEvent:address_server + "api/GetSingleEvent",
  getRecvEvent:address_server+"api/GetRecvEvent",
  replyEvent:address_server+"api/ReplyEvent",
  getAWithE:address_server+"api/GetActivityWithEvent",
  newMessage:address_server+"api/NewMessage",
  getMessage:address_server+"api/GetMessage",
  getCreatedActivity:address_server+"api/GetCreatedActivity",
  fileUpload:address_server+"api/UploadFile",
  updateActivity:address_server+'api/EndActivity',
  getStatList:address_server+'api/GetEventStat',
  getFile:address_server+'api/GetFile',
  getKey:address_server+'api/GetUserPubKey',
  LoginAccess:address_server+'api/Login',
  getPostTarget:address_server+'api/GetPostTarget'
});

app.constant("FILETYPE",{
  req_photo:{
    table:'activity',
    key:'photo_file'
  },
  req_report:{
    table:'activity',
    key:'report_file'
  },
  reply_txt:{
    table:'activity',
    key:'reply_file'
  }
});

app.controller('MainController', function($rootScope, $scope, $cookies){
  
  // Needed for the loading screen
  $rootScope.$on('$routeChangeStart', function(){
    $rootScope.loading = true;
  });

  $rootScope.$on('$routeChangeSuccess', function(){
    $rootScope.loading = false;
  });

  $scope.bottomReached = function() {
    /* global alert: false; */
    alert('您已经翻到底部');
  };

  $scope.setUser = function(user) {
    $cookies.put('user',JSON.stringify(user));
    $scope.userId = user.id;
    $scope.user_id = user.id;
  }

  $scope.getUserId = function() {
    return $scope.userId;
  }

});