'user strict';

app.controller('detailbottomrCtrl',function($scope,$mdDialog) {
	$scope.hasbeenFeed=false;

	$scope.$on("Ctr1NameChange",function (event, msg) {
        console.log("parent", msg);
        $scope.reqContent.child = 1;
    });

	$scope.union={
			name:'工会特色活动',
			appar:'组织部',
			loca:'',
			detail:'本课程是2005年国家级精品课（面向对象的程序设计）的组成部分，2015年再次被清华大学认定为清华大学精品课。本课程以实例驱动、专题案例驱动为主，学习面向对象与可视化程序设计的基本知识与Windows编程开发的常用界面功能设计。希望能通过本课程的学习，加强用信息化手段解决相关学科问题的能力。',
			plus:'注意携带纸和笔',
			start_time:'2015/02/01',
			end_time:'2015/02/30',
			qcode:1, //???
			photo:1, //????
			upload:1, //????
			end:1,
			file:'asdada',
	};

	$scope.empty=function(str){
		if (str.length==0) return 1;
		return 0;
	}

	$scope.todos=[
		{
			who:"我",
			self:1,
			time:"2016年02月26日 9:14",
			content:'本课程是2005年国家级精品课（面向对象的程序设计）的组成部分，2015年再次被清华大学认定为清华大学精品课。本课程以实例驱动、专题案例驱动为主，学习面向对象与可视化程序设计的基本知识与Windows编程开发的常用界面功能设计。希望能通过本课程的学习，加强用信息化手段解决相关学科问题的能力。'
		},
		{
			who:"回复我",
			self:0,
			time:"2016年02月26日 9:14",
			content:'本课程是2005年国家级精品课（面向对象的程序设计）的组成部分，2015年再次被清华大学认定为清华大学精品课。本课程以实例驱动、专题案例驱动为主，学习面向对象与可视化程序设计的基本知识与Windows编程开发的常用界面功能设计。希望能通过本课程的学习，加强用信息化手段解决相关学科问题的能力。'
		}
	];

	$scope.Feed =function(){
		$scope.hasbeenFeed=true;
	};

	$scope.question=function(){
		console.log("question");
	};

});