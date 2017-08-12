'user strict';

var app = angular.module('starterApp',["ng-fusioncharts",'ngMaterial','ngAnimate',"ui.router",'angularFileUpload','md.data.table','ngCookies', 'monospaced.qrcode']);
/*
app.config(function ($routeProvider,$mdThemingProvider) {
	$routeProvider.when( "/login",	{templateUrl: "templates/login.html",controller: 'loginCtrl'});
	$routeProvider.when( "/regis",	{templateUrl: "templates/regis.html",controller: 'regisCtrl'});
	$routeProvider.when( "/create",	{templateUrl: "templates/create.html",controller: 'createCtrl'});
	$routeProvider.when( "/subcreate",	{templateUrl: "templates/subCreate.html",controller: 'subCreateCtrl'});
	$routeProvider.when( "/uploadFile",	{templateUrl: "templates/uploadFile.html",controller: 'uploadCtrl'});
	$routeProvider.when( "/test",	{templateUrl: "templates/test.html",controller: 'testCtrl'});
	$routeProvider.when( "/list",	{templateUrl: "templates/list.html",controller: 'listCtrl'});
	$routeProvider.when( "/listtop",	{templateUrl: "templates/list_top.html",controller: 'list_topCtrl'});
	$routeProvider.when( "/listbottomr",	{templateUrl: "templates/list_bottomr.html",controller: 'list_bottomrCtrl'});
	$routeProvider.when( "/chart",{templateUrl: "templates/chart.html",controller: 'chartCtrl'})
	$routeProvider.when( "/listbottom",	{templateUrl: "templates/list_bottom.html",controller: 'list_bottomCtrl'});
	$routeProvider.when( "/detail/:activity_id",	{templateUrl: "templates/detail.html",controller: 'detailCtrl'});
	$routeProvider.when( "/detailtop/:event_id",	{templateUrl: "templates/detail_top.html",controller: 'detail_topCtrl'});
	$routeProvider.when( "/detailbottom/:activity_id",	{templateUrl: "templates/detail_bottom.html",controller: 'detail_bottomCtrl'});	
	$routeProvider.when( "/detailbottomr",	{templateUrl: "templates/detail_bottomr.html",controller: 'detailbottomrCtrl'});	

	$routeProvider.when( "/add",	{templateUrl: "templates/addPerson.html",controller: 'addCtrl'});

	$routeProvider.when( "/update",	{templateUrl: "templates/update.html",controller: 'updateCtrl'});

    $routeProvider.otherwise("/");

    /*$mdThemingProvider.theme('default').primaryPalette('blue');
});
*/

app.config(function($stateProvider, $urlRouterProvider,$mdThemingProvider, $httpProvider){

    $httpProvider.defaults.withCredentials = true;

	$urlRouterProvider.when("", "/login");
	$urlRouterProvider.when("/", "/login");

    $urlRouterProvider.otherwise("/");

	$stateProvider
        .state('regist', {
            url: '/regist',
            templateUrl: 'templates/regis.html',
            controller: 'regisCtrl',
            onEnter:function(){
                console.log('enter regist!');
            }
    		
        });

	$stateProvider
        .state('login', {
            url: '/login',
            templateUrl: 'templates/login.html',
            controller: 'loginCtrl',
            onEnter:function(){
                console.log('enter login!');
            }	
        })

    $stateProvider
        .state('entrance', {
            url: '/entrance',
            templateUrl: 'templates/navbar.html',
            controller: 'navbarCtrl'
        })
        .state('entrance.topCreate', {
            url: '/top/create',
            templateUrl: 'templates/create.html',
            controller: 'createCtrl'
        })
        .state('entrance.topEventList', {
            url: '/top/eventList',
            templateUrl: 'templates/list_top.html',
            controller: 'list_topCtrl'
        })

        .state('entrance.topEventDetail', {
            url: '/top/eventDetail/:event_id',
            templateUrl: "templates/detail_top.html",
            controller: 'detail_topCtrl'
        })

        .state('entrance.topUpdatePostarget', {
            url: '/top/updatePostarget',
            templateUrl: 'templates/update.html',
            controller: 'updateCtrl'
        })

        .state('entrance.addAdmin', {
            url: '/top/addAdmin',
            templateUrl: 'templates/addAdmin.html',
            controller: 'addAdminCtrl'
        })

        .state('entrance.interEventList', {
            url: '/intermediate/eventList',
            templateUrl: 'templates/list.html',
            controller: 'listCtrl'
        })

        .state('entrance.interEventDetail', {
            url: '/intermediate/eventDetail/:activity_id',
            templateUrl: "templates/detail.html",
            controller: 'detailCtrl'
        })

        .state('entrance.interActivityList', {
            url: '/intermediate/activityList',
            templateUrl: 'templates/list_bottom.html',
            controller: 'list_bottomCtrl'
        })

        .state('entrance.interActivityDetail', {
            url: '/intermediate/activityDetail/:activity_id',
            templateUrl: 'templates/detail_bottom.html',
            controller: 'detail_bottomCtrl'
        })

        //$mdThemingProvider.theme('default').primaryPalette('red');
         // Configure all charts
        /*ChartJsProvider.setOptions({
        colours: ['#F33210','#88E1ED'],
        responsive: true
        });*/



});

app.run(function ($rootScope,$cookies,$location,globalUrl,httpService,dialogService) {
    $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams, error) {
        //event.preventDefault();
        //console.log(toState.url);
        if(toState.url!="/login"){
            var get_req = httpService.get(globalUrl.getCurrentUser);

            get_req.then(function successCallback(response) {
                if(response.data['error'] != null){
                    //dialogService.show(response.data['error']);
                    alert("会话访问失效，请重新登录！");
                    $location.path('/login');
                    return;
                }
                if(response.data['result'] == "200 OK" ){
                    console.log(toState.url);
                    if(response.data['is_root'] == "0"){
                        if(toState.url == "/top/create"){
                            alert("您没有操作权限，请重新登录！");
                            $location.path('/login');
                            return;
                        }
                        if(toState.url == "/top/eventList"){
                            alert("您没有操作权限，请重新登录！");
                            $location.path('/login');
                            return;
                        }
                        if(toState.url == "/top/updatePostarget"){
                            alert("您没有操作权限，请重新登录！");
                            $location.path('/login');
                            return;
                        }
                        if(toState.url == "/top/addAdmin"){
                            alert("您没有操作权限，请重新登录！");
                            $location.path('/login');
                            return;
                        }
                    }
            }
            }, function errorCallback(response) {
                dialogService.show("服务器无响应！");
            });
        }
    })
});


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
    SigninAccess:address_server+'api/SignIn',
    logOutAccess:address_server+'api/Logout',
    searchUser:address_server+'api/SearchUser',
    addActivityAuth:address_server+'api/AddActivityAuth',
    createLabel:address_server+'api/NewPostTarget',
    deleteLabel:address_server+'api/DelPostTarget',
    getPostTarget:address_server+'api/GetPostTarget',
    getCurrentUser:address_server+'api/GetCurrentUser',
    getParticipants:address_server+'api/GetParticipants',
    manulSign:address_server+'api/ManualCheckin',
    generateExcel:address_server+'api/GenerateExcel',
    informAgain:address_server+'api/RenoticeActivity',
    setRoot:address_server+"api/SetRoot"
});

app.constant('AUTH_EVENTS', {
  loginSuccess: 'auth-login-success',
  loginFailed: 'auth-login-failed',
  logoutSuccess: 'auth-logout-success',
  sessionTimeout: 'auth-session-timeout',
  notAuthenticated: 'auth-not-authenticated',
  notAuthorized: 'auth-not-authorized'
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