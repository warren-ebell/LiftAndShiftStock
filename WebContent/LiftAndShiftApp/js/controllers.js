function HeaderController($scope, $location) {
    DataManager.getInstance().showMenu = 0;
    $scope.showMenu = DataManager.getInstance().showMenu;
    $scope.isActive = function (viewLocation) {
        return viewLocation === $location.path();
    };
    $scope.setRoute = function(route) {
        $location.path(route);
    }

    $scope.goLogout = function() {
        $scope.setShowMenu(0);
        $scope.setUserName("");
        $scope.setRoute('/login');
    }
    $scope.setShowMenu = function(value) {
        DataManager.getInstance().showMenu = value;
        $scope.showMenu = DataManager.getInstance().showMenu;
    }
    $scope.setUserName = function(value) {
        $scope.userName = value;
    }
    $scope.setShowAdmin = function(value) {
        $scope.showAdmin = value;
    }
    $scope.convertArrayToString = function(array) {
        return array + "";
    }
}

function MainCtrl($scope, $location, Util) {
}

function HomeCtrl($scope, Util) {
    Util.checkLoggedIn($scope);
}

function LoginCtrl($scope, AuthenticationService) {
    $scope.login = function () {
        var userName = $scope.loginUserName;
        var passWord = $scope.loginPassword;
        if (userName &&
            passWord) {

            AuthenticationService.getLogin({username:userName,password:passWord,serverMethod:'doLogin'},
                function(result) {
                    var authenticatedUser = result;
                    if (authenticatedUser.enabled === '0') {
                        alert('User not enabled, Please speak to a system admin to enable this user.');
                        $scope.loginUserName = '';
                        $scope.loginPassword = '';
                    }
                    else {
                        DataManager.getInstance().validUser = true;
                        DataManager.getInstance().user = authenticatedUser;

                        $scope.setUserName(DataManager.getInstance().user.userName);
                        $scope.setShowMenu(1);
                        if (DataManager.getInstance().user.admin == 1)
                            $scope.setShowAdmin("1");
                        else
                            $scope.setShowAdmin("0");
                        $scope.setRoute('/home');
                    }
                },
                function(error) {
                    alert('User not validated');
                    $scope.loginUserName = '';
                    $scope.loginPassword = '';
                }
            );
        }
        else {
            alert('No Login information supplied.');
        }
    };
}

function QuoteCtrl($scope, StockService) {
    $scope.showSerial = 0;
    StockService.getStockManufacturers({serverMethod:'getStockManufacturers'},
        function(result) {
            $scope.stockManufacturers = result;
        },
        function(error) {
            alert('Error getting stock manufacturers');
        }
    );
    $scope.getModelsForManufacturer = function() {
        var manufacturer = $scope.selectedManufacturer.manufacturer;
        StockService.getStockModelsForManufacturer({serverMethod:'getModelsForManufacturer', stockManufacturer:manufacturer},
            function(result) {
                $scope.stockModels = result;
            },
            function(error) {
                alert('Error getting stock models');
            }
        );
    }
    $scope.getStockItemsForManufacturerAndModel = function() {
        var manufacturer = $scope.selectedManufacturer.manufacturer;
        var model = $scope.selectedModel.model;
        StockService.getStockItemsForManufacturerAndModel({serverMethod:'getStockItemsForStockManufacturerAndModel', stockManufacturer:manufacturer, stockModel:model},
            function(result) {
                $scope.stockItems = result;
            },
            function(error){
                alert('Error getting stock items for make and model - or no stock items for this make and model.')
            }
        );
    }
    $scope.getStockForStockId = function(stockId) {
        StockService.getAvailableStockForStockId({serverMethod:'getAvailableStockForStockId',stockId:stockId},
            function(result) {
                var stockItem = result;
                DataManager.getInstance().selectedStockItem = stockItem;
                $scope.selectedStockItem = stockItem;
                //show the serial number section...
                $scope.showSerial = 1;
                //$scope.setRoute('/quoteCustomer');
            },
            function(error) {
                alert('Error getting the stock item');
            }
        );
    };
    $scope.selectSerialForQuote = function(serialNumber, stockId) {
        DataManager.getInstance().selectedSerialNumber = serialNumber;
        $scope.setRoute('/quoteAccessories');
    }
}

function QuoteAccessoriesCtrl($scope, AccessoryService) {
    $scope.accessories = [];
    AccessoryService.getAccessoryManufacturers({serverMethod:'getAccessoryManufacturers'},
        function(result) {
            $scope.accessoryManufacturers = result;
        },
        function(error) {
            alert('Error getting accessory manufacturers');
        }
    );
    $scope.getAccessoryItemsForManufacturer = function() {
        var manufacturer = $scope.selectedManufacturer.manufacturer;
        AccessoryService.getAccessoryItemsForAccessoryManufacturer({serverMethod:'getAccessoryItemsForAccessoryManufacturer', accessoryManufacturer:manufacturer},
            function(result) {
                $scope.accessoryItems = result;
            },
            function(error){
                alert('Error getting accessory items for manufacturer - or no accessory items for this manufacturer.')
            }
        );
    }
    $scope.getAvailableAccessoryForAccessoryId = function(accessoryId) {
        AccessoryService.getAvailableAccessoryForAccessoryId({serverMethod:'getAvailableAccessoryForAccessoryId',accessoryId:accessoryId},
            function(result) {
                DataManager.getInstance().selectedAccessoryItem = result;

                $scope.selectedAccessoryItem = result;
                //show the serial number section...
                $scope.showSerial = 1;
            },
            function(error) {
                alert('Error getting the accessory item');
            }
        );
    };
    $scope.selectAccessorySerialForQuote = function(serialNumber) {
        //need to add this item to the list of accessories....
        var serial = serialNumber;
        var code = $scope.selectedAccessoryItem.accessoryCode;
        var price = $scope.selectedAccessoryItem.pricing;
        var accessoryId = $scope.selectedAccessoryItem.accessoryId;

        var tempAccessory = {};
        tempAccessory.serial = serial;
        tempAccessory.code = code;
        tempAccessory.price = price;
        tempAccessory.accessoryId = accessoryId;

        $scope.accessories.push(tempAccessory);
    };
    $scope.next = function() {
        DataManager.getInstance().selectedAccessories = $scope.accessories;
        $scope.setRoute('/quoteCustomer');
    };
    $scope.removeAccessory = function(selectedAccessory) {
        var index = $scope.accessories.indexOf(selectedAccessory);

        if (index > -1) {
            $scope.accessories.splice(index, 1);
        }
    }
}

function QuoteNotesCtrl($scope, QuoteService) {
    $scope.htmlNotes = '';
    $scope.htmlDelivery = '';
    $scope.htmlInstallation = '';
    QuoteService.getQuoteDefaults({serverMethod:'getQuoteDefaults'},
        function(result) {
            var serverResult = result;
            $scope.htmlNotes = serverResult.notes;
            $scope.htmlDelivery = serverResult.delivery;
            $scope.htmlInstallation = serverResult.installation;
        },
        function(error) {
            alert('Error getting defaults.');
        }
    );

    $scope.saveQuote = function() {
        var stockId = DataManager.getInstance().selectedStockItem.stockId;
        var userId = DataManager.getInstance().user.userId;
        var accessories = DataManager.getInstance().selectedAccessories;

        var customerName = DataManager.getInstance().customerName;
        var customerAddress = DataManager.getInstance().customerAddress;
        var customerEmailAddress = DataManager.getInstance().customerEmailAddress;
        var customerPhoneNumber = DataManager.getInstance().customerPhoneNumber;
        var customerAttention = DataManager.getInstance().customerAttention;

        var pricing = DataManager.getInstance().selectedStockItem.pricing;

        var notes = $scope.htmlNotes;
        var delivery = $scope.htmlDelivery;
        var installation = $scope.htmlInstallation;

        var accString = JSON.stringify(accessories);

        var serialNumber = DataManager.getInstance().selectedSerialNumber;
        QuoteService.saveQuote({serverMethod:'saveQuote', stockId:stockId , name:customerName, address:customerAddress, emailAddress:customerEmailAddress, phoneNumber:customerPhoneNumber, attention:customerAttention, userId:userId, serialNumber:serialNumber, pricing:pricing, accessories:accString, notes:notes, delivery:delivery, installation:installation},
            function(result) {
                var serverResult = result;
                if (serverResult.quoteId === '0') {
                    //something has gone wrong with saving the user - need to show that...
                    alert('Error creating Quote: '+serverResult.message);
                }
                else {
                    //everything is done - show alert to say done - then back home...
                    alert('Quote created successfully.');
                    DataManager.getInstance().quoteId = serverResult.quoteId;
                    DataManager.getInstance().quoteHistory = 0;
                    $scope.setRoute('/document');
                }
            },
            function(error) {
                alert('Error saving quote.');
            }
        );
    }
}

function QuoteCustomerCtrl($scope, QuoteService) {
    $scope.next = function() {
        DataManager.getInstance().customerName = $scope.customerName;
        DataManager.getInstance().customerAddress = $scope.customerAddress;
        DataManager.getInstance().customerEmailAddress = $scope.customerEmailAddress;
        DataManager.getInstance().customerPhoneNumber = $scope.customerPhoneNumber;
        DataManager.getInstance().customerAttention = $scope.customerAttention;

        $scope.setRoute('/quoteNotes');
    }
    $scope.cancel = function() {
        $scope.setRoute('/home');
    }
}

function QuoteHistoryCtrl($scope, QuoteService) {
    QuoteService.getMiniQuotes({serverMethod:'getMiniQuotes'},
        function(result) {
            var serverResult = result;
            $scope.miniQuotes = serverResult;
        },
        function(error) {
            alert('Error getting quote history.');
        }
    );
    $scope.viewQuotation = function(quotationId) {
        DataManager.getInstance().quoteId = quotationId;
        DataManager.getInstance().quoteHistory = 1;
        $scope.setRoute('/document');
    }
    $scope.declineQuote = function(quotationId) {
        QuoteService.declineQuote({serverMethod:'declineQuote', quotationId:quotationId},
            function(result) {
                var serverResult = result;
                if (serverResult.result == '1') {
                    alert('Quote declined');
                    QuoteService.getMiniQuotes({serverMethod:'getMiniQuotes'},
                        function(result) {
                            var serverResult = result;
                            $scope.miniQuotes = serverResult;
                        },
                        function(error) {
                            alert('Error getting quote history.');
                        }
                    );
                }
                else
                    alert('Error declining quote: '+serverResult.message);
            },
            function(error) {
                alert('Error declining quote.');
            }
        );
    }
    $scope.acceptQuote = function(quotationId) {
        QuoteService.acceptQuote({serverMethod:'acceptQuote', quotationId:quotationId},
            function(result) {
                var serverResult = result;
                if (serverResult.result == '1') {
                    alert('Quote accepted');
                    QuoteService.getMiniQuotes({serverMethod:'getMiniQuotes'},
                        function(result) {
                            var serverResult = result;
                            $scope.miniQuotes = serverResult;
                        },
                        function(error) {
                            alert('Error getting quote history.');
                        }
                    );
                }
                else
                    alert('Error accepting quote: '+serverResult.message);
            },
            function(error) {
                alert('Error accepting quote.');
            }
        );
    };
    $scope.completeQuote = function(quotationId) {
        QuoteService.completeQuote({serverMethod:'completeQuote', quotationId:quotationId},
            function(result) {
                var serverResult = result;
                if (serverResult.result == '1') {
                    alert('Quote complete');
                    QuoteService.getMiniQuotes({serverMethod:'getMiniQuotes'},
                        function(result) {
                            var serverResult = result;
                            $scope.miniQuotes = serverResult;
                        },
                        function(error) {
                            alert('Error getting quote history.');
                        }
                    );
                }
                else
                    alert('Error completing quote: '+serverResult.message);
            },
            function(error) {
                alert('Error completing quote.');
            }
        );
    }
}

function DocumentViewCtrl($scope, QuoteService) {
    var quoteId = DataManager.getInstance().quoteId;
    $scope.quoteId = quoteId;
    //$scope.url = 'http://197.221.7.50:8080/LiftAndShiftStock/document?quotationId='+quoteId+'';
    $scope.url = 'http://localhost:8080/LiftAndShiftStock/document?quotationId='+quoteId+'';
    $scope.done = function() {
        if (DataManager.getInstance().quoteHistory === 1) {
            $scope.setRoute('/quoteHistory');
        }
        else {
            $scope.setRoute('/home');
        }
    }
    $scope.sendEMail = function(quoteId) {
        alert('Please wait while the quote is prepared for emailing, a message will display when the operation is complete...');
        QuoteService.sendEMail({serverMethod:'sendEMail', quotationId:quoteId},
            function(result) {
                var serverResult = result;
                if (serverResult.result === '1') {
                    alert('Email sent successfully');
                }
                else {
                    alert('Error sending email: '+serverResult.message);
                }
            },
            function(error) {
                alert('Error sending email.');
            }
        );
    }
}

function UserEditCtrl($scope, UserService) {
    $scope.userAdmin = false;
    $scope.userEnabled = false;
    if (DataManager.getInstance().selectedUser) {
        $scope.selectedUser = DataManager.getInstance().selectedUser;
        if ($scope.selectedUser.admin === '1') {
            $scope.userAdmin = true;
        }
        if ($scope.selectedUser.enabled === '1') {
            $scope.userEnabled = true;
        }
    }
    $scope.saveUser = function() {
        if ($scope.userAdmin === true) {
            $scope.selectedUser.admin = '1';
        }
        else {
            $scope.selectedUser.admin = '0';
        }
        if ($scope.userEnabled === true) {
            $scope.selectedUser.enabled = '1';
        }
        else {
            $scope.selectedUser.enabled = '0';
        }
        UserService.saveUser({serverMethod:'saveUser', userId:$scope.selectedUser.userId, userName:$scope.selectedUser.userName, userPassword:$scope.selectedUser.userPassword, displayName:$scope.selectedUser.displayName, emailAddress:$scope.selectedUser.emailAddress, contactNumber:$scope.selectedUser.contactNumber, admin:$scope.selectedUser.admin, enabled:$scope.selectedUser.enabled},
            function(result) {
                var serverResult = result;
                if (serverResult.result === '1') {
                    //everything is done - show alert to say done - then back home...
                    alert('User saved successfully.');
                    $scope.setRoute('/user');
                }
                else {
                    //something has gone wrong with saving the user - need to show that...
                    alert('Error saving the user: '+serverResult.message);
                }
            },
            function(error){

            }
        );
    }
    $scope.cancel = function() {
        $scope.setRoute("/user");
    }
    $scope.delete = function() {
        UserService.deleteUser({serverMethod:'deleteUser', userId:$scope.selectedUser.userId},
            function(result) {
                var serverResult = result;
                if (serverResult.result === '1') {
                    //everything is done - show alert to say done - then back home...
                    alert('User deleted successfully.');
                    $scope.setRoute('/user');
                }
                else {
                    //something has gone wrong with saving the user - need to show that...
                    alert('Error deleting the user: '+serverResult.message);
                }
            },
            function(error) {
                alert('Error deleting the user');
            }
        );
    }
}

function UserCtrl($scope, UserService) {
    UserService.getAllUsers({userId: '',serverMethod: 'getAllUsers'},
        function(result) {
            var userList = result;
            $scope.userList = userList;
        },
        function(error) {
            alert('Error retrieving user list');
        }
    );
    $scope.getUserForUserId = function(userId) {
        UserService.getUserForUserId({userId: userId, serverMethod: 'getUserForUserId'},
            function(result) {
                var user = result;
                DataManager.getInstance().selectedUser = user;
                $scope.setRoute("/userEdit");

            },
            function(error){
                alert('Error retrieving user');
            }
        );
    }
    $scope.addUser = function() {
        DataManager.getInstance().selectedUser = null;
        $scope.setRoute("/userEdit");
    }
}

function StockCtrl($scope, StockService) {
    StockService.getStockManufacturers({serverMethod:'getStockManufacturers'},
        function(result) {
            $scope.stockManufacturers = result;
        },
        function(error) {
            alert('Error getting stock manufacturers');
        }
    );
    $scope.getModelsForManufacturer = function() {
        var manufacturer = $scope.selectedManufacturer.manufacturer;
        StockService.getStockModelsForManufacturer({serverMethod:'getModelsForManufacturer', stockManufacturer:manufacturer},
            function(result) {
                $scope.stockModels = result;
            },
            function(error) {
                alert('Error getting stock models');
            }
        );
    }
    $scope.getStockItemsForManufacturerAndModel = function() {
        var manufacturer = $scope.selectedManufacturer.manufacturer;
        var model = $scope.selectedModel.model;
        StockService.getStockItemsForManufacturerAndModel({serverMethod:'getStockItemsForStockManufacturerAndModel', stockManufacturer:manufacturer, stockModel:model},
            function(result) {
                $scope.stockItems = result;
            },
            function(error){
                alert('Error getting stock items for make and model - or no stock items for this make and model.')
            }
        );
    }
    $scope.getStockForStockId = function(stockId) {
        StockService.getStockForStockId({serverMethod:'getStockForStockId',stockId:stockId},
            function(result) {
                var stockItem = result;
                DataManager.getInstance().selectedStockItem = stockItem;
                $scope.setRoute('/stockEdit');
            },
            function(error) {
                alert('Error getting the stock item');
            }
        );
    };
    $scope.addStockItem = function() {
        DataManager.getInstance().selectedStockItem = null;
        $scope.setRoute('/stockEdit');
    }
}

function StockEditCtrl($scope, StockService) {
    $scope.htmlTechnicalSpecs = "";
    $scope.showAddSerial = 0;
    $scope.showSerialSection = 0;
    if (DataManager.getInstance().selectedStockItem) {
        $scope.selectedStockItem = DataManager.getInstance().selectedStockItem;
        $scope.showDelete = 1;
        $scope.htmlTechnicalSpecs = $scope.selectedStockItem.technicalSpecs;
        $scope.showSerialSection = 1;
    }
    else
        $scope.showDelete = 0;
    $scope.saveStockItem = function() {
        var htmlTechSpecs = $scope.htmlTechnicalSpecs;
        var stockCode = $scope.selectedStockItem.stockManufacturer+"-"+$scope.selectedStockItem.stockModel+"-"+$scope.selectedStockItem.stockSeries;
        StockService.saveStockItem({serverMethod:'saveStockItem', stockId:$scope.selectedStockItem.stockId, stockManufacturer:$scope.selectedStockItem.stockManufacturer, stockModel:$scope.selectedStockItem.stockModel, stockSeries:$scope.selectedStockItem.stockSeries, pricing:$scope.selectedStockItem.pricing, serialNumber:$scope.selectedStockItem.serialNumber, stockCode:stockCode, technicalSpecs:$scope.htmlTechnicalSpecs, description:$scope.selectedStockItem.stockDescription},
            function(result) {
                var serverResult = result;
                if (serverResult.result === '1') {
                    //everything is done - show alert to say done - then back home...
                    alert('Stock Item saved successfully.');
                    //$scope.setRoute('/stock');
                }
                else {
                    //something has gone wrong with saving the user - need to show that...
                    alert('Error saving the stock Item: '+serverResult.message);
                }
            },
            function(error){

            }
        );
    }
    $scope.cancel = function() {
        $scope.setRoute('/stock');
    }
    $scope.delete = function() {
        StockService.deleteStockItem({serverMethod:'deleteStockItem', stockId:$scope.selectedStockItem.stockId},
            function(result) {
                var serverResult = result;
                if (serverResult.result === '1') {
                    //everything is done - show alert to say done - then back home...
                    alert('Stock Item deleted successfully.');
                    $scope.setRoute('/stock');
                }
                else {
                    //something has gone wrong with saving the user - need to show that...
                    alert('Error deleting the stock item: '+serverResult.message);
                }
            },
            function(error) {
                alert('Error deleting the user');
            }
        );
    }
    $scope.showAddSerialNumber = function() {
        $scope.showAddSerial = 1;
    }
    $scope.addSerialNumber = function(serialNumber) {
        var serialNumber = $scope.serialNumber;
        StockService.addSerialNumber({serverMethod:'addSerialNumber', stockId:$scope.selectedStockItem.stockId, serialNumber:serialNumber},
            function(result) {
                var serverResult = result;
                if (serverResult.result === '1') {
                    //everything is done - show alert to say done - then back home...
                    alert('Serial Number added successfully.');
                    StockService.getStockForStockId({serverMethod:'getStockForStockId',stockId:$scope.selectedStockItem.stockId},
                        function(result) {
                            var stockItem = result;
                            DataManager.getInstance().selectedStockItem = stockItem;
                            $scope.selectedStockItem = stockItem;
                            $scope.serialNumber = '';
                            $scope.showAddSerial = 0;
                        },
                        function(error) { }
                    );
                }
                else {
                    //something has gone wrong with saving the user - need to show that...
                    alert('Error adding serial number: '+serverResult.message);
                }
            },
            function(error) {
                alert('Error adding serial number to stock item.')
            }
        );
    }
    $scope.removeSerialNumber = function(serialNumber, status) {
        if (status == 'Available') {
            StockService.deleteSerialNumber({serverMethod:'deleteSerialNumber', stockId:$scope.selectedStockItem.stockId, serialNumber:serialNumber},
                function(result) {
                    var serverResult = result;
                    if (serverResult.result === '1') {
                        //everything is done - show alert to say done - then back home...
                        alert('Serial Number removed successfully.');
                        StockService.getStockForStockId({serverMethod:'getStockForStockId',stockId:$scope.selectedStockItem.stockId},
                            function(result) {
                                var stockItem = result;
                                DataManager.getInstance().selectedStockItem = stockItem;
                                $scope.selectedStockItem = stockItem;
                                $scope.serialNumber = '';
                                $scope.showAddSerial = 0;
                            },
                            function(error) { }
                        );
                    }
                    else {
                        //something has gone wrong with saving the user - need to show that...
                        alert('Error removing serial number: '+serverResult.message);
                    }
                },
                function(error) {
                    alert('Error removing serial number to stock item.')
                }
            );
        }
        else {
            alert('Stock serial number cannot be removed if it has been quoted on, or is unavailable.')
        }
    }
}

function AccessoryCtrl($scope, AccessoryService) {
    AccessoryService.getAccessoryManufacturers({serverMethod:'getAccessoryManufacturers'},
        function(result) {
            $scope.accessoryManufacturers = result;
        },
        function(error) {
            alert('Error getting accessory manufacturers');
        }
    );
    $scope.getAccessoryItemsForManufacturer = function() {
        var manufacturer = $scope.selectedManufacturer.manufacturer;
        AccessoryService.getAccessoryItemsForAccessoryManufacturer({serverMethod:'getAccessoryItemsForAccessoryManufacturer', accessoryManufacturer:manufacturer},
            function(result) {
                $scope.accessoryItems = result;
            },
            function(error){
                alert('Error getting accessory items for manufacturer - or no accessory items for this manufacturer.')
            }
        );
    }
    $scope.getAccessoryForAccessoryId = function(accessoryId) {
        AccessoryService.getAccessoryForAccessoryId({serverMethod:'getAccessoryForAccessoryId',accessoryId:accessoryId},
            function(result) {
                DataManager.getInstance().selectedAccessoryItem = result;
                $scope.setRoute('/accessoryEdit');
            },
            function(error) {
                alert('Error getting the accessory item');
            }
        );
    };
    $scope.addAccessoryItem = function() {
        DataManager.getInstance().selectedAccessoryItem = null;
        $scope.setRoute('/accessoryEdit');
    }
}

function AccessoryEditCtrl($scope, AccessoryService) {
    $scope.showAddSerial = 0;
    $scope.showSerialSection = 0;
    if (DataManager.getInstance().selectedAccessoryItem) {
        $scope.selectedAccessoryItem = DataManager.getInstance().selectedAccessoryItem;
        $scope.showDelete = 1;
        $scope.showSerialSection = 1;
    }
    else
        $scope.showDelete = 0;
    $scope.saveAccessoryItem = function() {
        var stockCode = $scope.selectedAccessoryItem.accessoryManufacturer+"-"+$scope.selectedAccessoryItem.accessoryModel;
        AccessoryService.saveAccessoryItem({serverMethod:'saveAccessoryItem', accessoryId:$scope.selectedAccessoryItem.accessoryId, accessoryManufacturer:$scope.selectedAccessoryItem.accessoryManufacturer, accessoryModel:$scope.selectedAccessoryItem.accessoryModel, pricing:$scope.selectedAccessoryItem.pricing, accessoryCode:stockCode, description:$scope.selectedAccessoryItem.accessoryDescription},
            function(result) {
                var serverResult = result;
                if (serverResult.result === '1') {
                    //everything is done - show alert to say done - then back home...
                    alert('Accessory Item saved successfully.');
                    //$scope.setRoute('/stock');
                }
                else {
                    //something has gone wrong with saving the user - need to show that...
                    alert('Error saving the accessory Item: '+serverResult.message);
                }
            },
            function(error){

            }
        );
    }
    $scope.cancel = function() {
        $scope.setRoute('/accessory');
    }
    $scope.delete = function() {
        AccessoryService.deleteAccessoryItem({serverMethod:'deleteAccessoryItem', stockId:$scope.selectedAccessoryItem.accessoryId},
            function(result) {
                var serverResult = result;
                if (serverResult.result === '1') {
                    //everything is done - show alert to say done - then back home...
                    alert('Accessory Item deleted successfully.');
                    $scope.setRoute('/accessory');
                }
                else {
                    //something has gone wrong with saving the user - need to show that...
                    alert('Error deleting the accessory item: '+serverResult.message);
                }
            },
            function(error) {
                alert('Error deleting the accessory');
            }
        );
    }
    $scope.showAddSerialNumber = function() {
        $scope.showAddSerial = 1;
    }
    $scope.addSerialNumber = function(serialNumber, stockId) {
        var serialNumber = $scope.serialNumber;
        AccessoryService.addSerialNumber({serverMethod:'addSerialNumber', accessoryId:$scope.selectedAccessoryItem.accessoryId, serialNumber:serialNumber},
            function(result) {
                var serverResult = result;
                if (serverResult.result === '1') {
                    //everything is done - show alert to say done - then back home...
                    alert('Serial Number added successfully.');
                    AccessoryService.getAccessoryForAccessoryId({serverMethod:'getAccessoryForAccessoryId',accessoryId:$scope.selectedAccessoryItem.accessoryId},
                        function(result) {
                            var stockItem = result;
                            DataManager.getInstance().selectedAccessoryItem = stockItem;
                            $scope.selectedAccessoryItem = stockItem;
                            $scope.serialNumber = '';
                            $scope.showAddSerial = 0;
                        },
                        function(error) { }
                    );
                }
                else {
                    //something has gone wrong with saving the user - need to show that...
                    alert('Error adding serial number: '+serverResult.message);
                }
            },
            function(error) {
                alert('Error adding serial number to accessory item.')
            }
        );
    }
    $scope.removeSerialNumber = function(serialNumber, status) {
        if (status == 'Available') {
            AccessoryService.deleteSerialNumber({serverMethod:'deleteSerialNumber', accessoryId:$scope.selectedAccessoryItem.accessoryId, serialNumber:serialNumber},
                function(result) {
                    var serverResult = result;
                    if (serverResult.result === '1') {
                        //everything is done - show alert to say done - then back home...
                        alert('Serial Number removed successfully.');
                        AccessoryService.getAccessoryForAccessoryId({serverMethod:'getAccessoryForAccessoryId',accessoryId:$scope.selectedAccessoryItem.accessoryId},
                            function(result) {
                                var stockItem = result;
                                DataManager.getInstance().selectedAccessoryItem = stockItem;
                                $scope.selectedAccessoryItem = stockItem;
                                $scope.serialNumber = '';
                                $scope.showAddSerial = 0;
                            },
                            function(error) { }
                        );
                    }
                    else {
                        //something has gone wrong with saving the user - need to show that...
                        alert('Error removing serial number: '+serverResult.message);
                    }
                },
                function(error) {
                    alert('Error removing serial number from the accessory item.')
                }
            );
        }
        else {
            alert('Accessory serial number cannot be removed if it has been quoted on, or is unavailable.')
        }
    }
}

function DefaultsCtrl($scope) {

}

