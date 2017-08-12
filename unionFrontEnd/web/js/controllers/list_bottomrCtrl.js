'user strict';

app.controller('list_bottomrCtrl',function($scope) {
	$scope.title='zengxiaopang';

	$scope.toggleSearch=false;

	$scope.bool_children = function(item){
		if(item.qcode == 1) return 1;
		if(item.photo == 1) return 1;
		if(item.upload == 1) return 1;
		return 0;
	};

	$scope.empty=function(str){
		if (str.length==0) return 1;
		return 0;
	}

	$scope.allAct=[
		{
			name:'工会特色活动',
			department:'组织部',
			location:'大礼堂',
			start_date:'2015/02/01',
			end_date:'',
			attachment:-1,
			status:0,
			href:'/web/#/detailbottomr'
		},
		{
			name:'工会特色活动',
			department:'组织部',
			location:'大礼堂',
			start_date:'2015/02/01',
			end_date:'',
			attachment:'aaaaaaa',
			status:0,
			href:'/web/#/detailbottomr'
		},
		{
			name:'工会特色活动',
			department:'组织部',
			location:'大礼堂',
			start_date:'2015/02/01',
			end_date:'',
			attachment:'aaaaaaa',
			status:0,
			href:'/web/#/detailbottomr'
		}];

});