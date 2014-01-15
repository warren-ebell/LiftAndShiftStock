'use strict';

/* Services */

var serverURL = '/LiftAndShiftStock';

// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('liftandshift.services', ['ngResource']).
    value('version', '0.2').
    service('Util',function Util() {
        this.checkLoggedIn = function($scope){
            /*if (!DataManager.getInstance().validUser){
             $scope.setRoute('/login');
             alert("Not logged in, Please log in.");
             }*/
        }
    }).
    factory('AuthenticationService', function($resource){
        return $resource(serverURL+'/login',
            {port: ':8080', serverMethod:'', username:'', password:'', callback:'JSON_CALLBACK'},
            {
                getLogin: {method: 'JSONP', isArray:false}
            }
        );
    }).
    factory('UserService', function($resource){
        return $resource(serverURL+'/user',
            {port: ':8080', serverMethod:'', userId:'', userName:'', userPassword:'', displayName:'', emailAddress:'', contactNumber:'', admin:'', enabled:'', callback:'JSON_CALLBACK'},
            {
                getAllUsers: {method: 'JSONP', isArray:true},
                getUserForUserId: {method: 'JSONP', isArray:false},
                saveUser: {method: 'JSONP', isArray:false},
                deleteUser: {method: 'JSONP', isArray:false}
            }
        );
    }).
    factory('StockService', function($resource){
        return $resource(serverURL+'/stock',
            {port: ':8080', serverMethod:'', stockId:'', stockManufacturer:'', stockSeries:'', stockModel:'', pricing:'', serialNumber:'', stockCode:'', technicalSpecs:'', description:'', location:'', stockUsed:'', callback:'JSON_CALLBACK'},
            {
                getStockManufacturers: {method: 'JSONP', isArray:true},
                getStockModelsForManufacturer: {method: 'JSONP', isArray:true},
                getStockForStockId: {method: 'JSONP', isArray:false},
                getAvailableStockForStockId: {method: 'JSONP', isArray:false},
                getStockItemsForManufacturerAndModel: {method: 'JSONP', isArray:true},
                saveStockItem: {method: 'JSONP', isArray:false},
                deleteStockItem: {method: 'JSONP', isArray:false},
                addSerialNumber: {method: 'JSONP', isArray:false},
                deleteSerialNumber: {method:'JSONP', isArray:false},
                addInstallLocation: {method: 'JSONP', isArray:false},
                deleteInstallLocation: {method:'JSONP', isArray:false}
            }
        );
    }).
    factory('AccessoryService', function($resource){
        return $resource(serverURL+'/accessory',
            {port: ':8080', serverMethod:'', accessoryId:'', accessoryManufacturer:'', pricing:'', serialNumber:'', accessoryCode:'', accessoryModel:'', description:'', callback:'JSON_CALLBACK'},
            {
                getAccessoryManufacturers: {method: 'JSONP', isArray:true},
                getAccessoryForAccessoryId: {method: 'JSONP', isArray:false},
                getAvailableAccessoryForAccessoryId: {method: 'JSONP', isArray:false},
                getAccessoryItemsForAccessoryManufacturer: {method: 'JSONP', isArray:true},
                saveAccessoryItem: {method: 'JSONP', isArray:false},
                deleteAccessoryItem: {method: 'JSONP', isArray:false},
                addSerialNumber: {method: 'JSONP', isArray:false},
                deleteSerialNumber: {method:'JSONP', isArray:false}
            }
        );
    }).
    factory('CustomerService', function($resource){
        return $resource(serverURL+'/customer',
            {port: ':8080', name:'', address:'', emailAddress:'', phoneNumber:'', attention:'', serverMethod:'',  callback:'JSON_CALLBACK'},
            {
                saveCustomer: {method: 'JSONP', isArray:false}
            }
        );
    }).
    factory('QuoteService', function($resource){
        return $resource(serverURL+'/quote',
            {port: ':8080', stockId: '', name:'', address:'', emailAddress:'', phoneNumber:'', attention:'', userId:'', serverMethod:'', quotationId:'', serialNumber:'', pricing:'', accessories:'', notes:'', delivery:'', installation:'', installationLocation:'', usedItem:'', callback:'JSON_CALLBACK'},
            {
                saveQuote: {method: 'JSONP', isArray:false},
                getMiniQuotes: {method: 'JSONP', isArray:true},
                sendEMail: {method: 'JSONP', isArray:false},
                acceptQuote: {method: 'JSONP', isArray:false},
                declineQuote: {method: 'JSONP', isArray:false},
                completeQuote: {method: 'JSONP', isArray:false},
                getQuoteDefaults: {method: 'JSONP', isArray:false}
            }
        );
    })
;
