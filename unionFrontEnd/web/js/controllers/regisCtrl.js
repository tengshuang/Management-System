
(function () {
	'use strict';
	app.controller('regisCtrl', DemoCtrl);
	function DemoCtrl ($timeout, $q, $log, authenService, dialogService) {
		var self = this;
		self.simulateQuery = false;
		//self.selectedItem	= "empty";
		// list of `state` value/display objects
		self.states = loadAll();

		self.regis = regis;
		self.querySearch = querySearch;
		self.selectedItemChange = selectedItemChange;
		self.searchTextChange = searchTextChange;
		self.newState = newState;

		function regis(user){
            var pubKey_req = authenService.getKey(user.no);
            pubKey_req.then(function successCallback(response) {
                if(response.data['error'] != null){
                    dialogService.show(response['error']);
                    return;
                }
                var pubkey = response.data['pubkey'];
                var p = {
                	password : authenService.encode(pubkey, user.password),
                	no : user.no,
                	realname : user.realname,
                	belongs : user.selectedItem.display
                };
                var post_req = authenService.signin(p);
                post_req.then(function successCallback(response) {
                    if(response.data['error'] != null){
                        dialogService.show(response['error']);
                        return;
                    }
                    if(response.data['result'] == "200 OK"){
                        console.log(response.data);
                        dialogService.show("注册成功！");
                    }
                }, function errorCallback(response) {
                    dialogService.show("服务器无响应！");
                    return;
                });
            }, function errorCallback(response) {
                dialogService.show("服务器无响应！");
                return;
            });
            /*
			$log.info(user);
			alert("注册成功！");
            */
		}

		function newState(state) {
			alert("Sorry! You'll need to create a Constituion for " + state + " first!");
		}
		// ******************************
		// Internal methods
		// ******************************
		/**
		 * Search for states... use $timeout to simulate
		 * remote dataservice call.
		 */
		function querySearch (query) {
			var results = query ? self.states.filter( createFilterFor(query) ) : self.states,
					deferred;
			if (self.simulateQuery) {
				deferred = $q.defer();
				$timeout(function () { deferred.resolve( results ); }, Math.random() * 1000, false);
				return deferred.promise;
			} else {
				return results;
			}
		}
		function searchTextChange(text) {
			$log.info('Text changed to ' + text);
		}
		function selectedItemChange(item) {
			$log.info('Item changed to ' + JSON.stringify(item));
		}
		/**
		 * Build `states` list of key/value pairs
		 */
		function loadAll() {
			var allStates = 'Alabama, Alaska, Arizona, Arkansas, California, Colorado, Connecticut, Delaware,\
							Florida, Georgia, Hawaii, Idaho, Illinois, Indiana, Iowa, Kansas, Kentucky, Louisiana,\
							Maine, Maryland, Massachusetts, Michigan, Minnesota, Mississippi, Missouri, Montana,\
							Nebraska, Nevada, New Hampshire, New Jersey, New Mexico, New York, North Carolina,\
							North Dakota, Ohio, Oklahoma, Oregon, Pennsylvania, Rhode Island, South Carolina,\
							South Dakota, Tennessee, Texas, Utah, Vermont, Virginia, Washington, West Virginia,\
							Wisconsin, Wyoming';
			return allStates.split(/, +/g).map( function (state) {
				return {
					value: state.toLowerCase(),
					display: state
				};
			});
		}
		/**
		 * Create filter function for a query string
		 */
		function createFilterFor(query) {
			var lowercaseQuery = angular.lowercase(query);
			return function filterFn(state) {
				return (state.value.indexOf(lowercaseQuery) === 0);
			};
		}
	}
})();