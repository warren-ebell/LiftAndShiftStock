'use strict';

// Declare app level module which depends on filters, and services
angular.module('liftandshift', ['ngRoute', 'liftandshift.filters', 'liftandshift.services', 'liftandshift.directives', 'textAngular']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/login', {templateUrl: 'partials/login.html', controller: LoginCtrl});
    $routeProvider.when('/home', {templateUrl: 'partials/home.html', controller: HomeCtrl});
    $routeProvider.when('/quote', {templateUrl: 'partials/quoteStart.html', controller: QuoteCtrl});
    $routeProvider.when('/quoteAccessories', {templateUrl: 'partials/quoteAccessories.html', controller: QuoteAccessoriesCtrl});
    $routeProvider.when('/quoteCustomer', {templateUrl: 'partials/quoteCustomer.html', controller: QuoteCustomerCtrl});
    $routeProvider.when('/quoteNotes', {templateUrl: 'partials/quoteNotes.html', controller: QuoteNotesCtrl});
    $routeProvider.when('/document', {templateUrl: 'partials/document.html', controller: DocumentViewCtrl});
    $routeProvider.when('/quoteHistory', {templateUrl: 'partials/quoteHistory.html', controller: QuoteHistoryCtrl});
    $routeProvider.when('/user', {templateUrl: 'partials/user.html', controller: UserCtrl});
    $routeProvider.when('/userEdit', {templateUrl: 'partials/editUser.html', controller: UserEditCtrl});
    $routeProvider.when('/stock', {templateUrl: 'partials/stock.html', controller: StockCtrl});
    $routeProvider.when('/stockEdit', {templateUrl: 'partials/editStock.html', controller: StockEditCtrl});
    $routeProvider.when('/defaults', {templateUrl: 'partials/defaults.html', controller: DefaultsCtrl});
    $routeProvider.otherwise({redirectTo: '/login'});
  }]);