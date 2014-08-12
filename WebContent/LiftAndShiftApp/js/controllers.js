function HeaderController($scope, $location) {
    DataManager.getInstance().showMenu = 0;
    DataManager.getInstance().serverURL = '/LiftAndShiftStock';
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

function QuoteCtrl($scope, StockService, UserService) {
    $scope.companies = [{companyName:'Lift and Shift', companyId:1},{companyName:'Bowman Cranes', companyId:2}];
    $scope.showItemPriceOptions = [{label:'Show line item Prices', option:'0'}, {label:'Hide line item prices', option:'1'}];
    $scope.showSerial = 0;
    StockService.getStockManufacturers({serverMethod:'getStockManufacturers'},
        function(result) {
            $scope.stockManufacturers = result;
        },
        function(error) {
            alert('Error getting stock manufacturers');
        }
    );
    UserService.getAllUsers({userId: '',serverMethod: 'getAllUsers'},
        function(result) {
            var userList = result;
            $scope.userList = userList;
        },
        function(error) {
            alert('Error retrieving user list');
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
                if (stockItem.installLocation.length > 0) {
                    $scope.stockInstallLocations = stockItem.installLocation;
                    $scope.showLocations = 1;
                }
                else {
                    $scope.stockInstallLocations = [];
                    $scope.showLocations = 0;
                }
                //$scope.setRoute('/quoteCustomer');
            },
            function(error) {
                alert('Error getting the stock item');
            }
        );
    };
    $scope.selectSerialForQuote = function(serialNumber, stockId) {
        DataManager.getInstance().selectedSerialNumber = serialNumber;
        DataManager.getInstance().selectedCompany = $scope.selectedCompany;
        DataManager.getInstance().selectedUser = $scope.selectedUser;
        DataManager.getInstance().showItemPrices = $scope.selectedShowOption.option;
        $scope.setRoute('/quoteAccessories');
    };
    $scope.setSelectedInstallLocation = function() {
        DataManager.getInstance().selectedInstallLocation = $scope.selectedLocation;
    }
}

function QuoteAccessoriesCtrl($scope, AccessoryService) {
    $scope.accessories = [];
    $scope.availableAccessories = [];
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

                //need to build a list of the available accessories...
                for (var i = 0; i < $scope.selectedAccessoryItem.accessoryLevel.length; i++) {
                    var acc = $scope.selectedAccessoryItem.accessoryLevel[i];
                    var availableAcc = {};
                    availableAcc.serial = acc.serialNumber;
                    availableAcc.code = $scope.selectedAccessoryItem.accessoryCode;
                    availableAcc.price = $scope.selectedAccessoryItem.pricing;
                    availableAcc.currency = $scope.selectedAccessoryItem.accessoryCurrency;
                    availableAcc.sellingPrice = $scope.selectedAccessoryItem.sellingPrice;
                    availableAcc.accessoryId = $scope.selectedAccessoryItem.accessoryId;
                    availableAcc.status = acc.status;

                    $scope.availableAccessories.push(availableAcc);
                }

            },
            function(error) {
                alert('Error getting the accessory item');
            }
        );
    };
    $scope.selectAccessorySerialForQuote = function(accessory) {
        //need to add this item to the list of accessories....
        $scope.accessories.push(accessory);

        //need to remove the accessory from the list of available accessories...
        var index = $scope.availableAccessories.indexOf(accessory);

        if (index > -1) {
            $scope.availableAccessories.splice(index, 1);
        }
    };
    $scope.next = function() {
        DataManager.getInstance().selectedAccessories = $scope.accessories;
        $scope.setRoute('/quoteCustomer');
    };
    $scope.removeAccessory = function(selectedAccessory) {
        //need to remove from the selected list...
        var index = $scope.accessories.indexOf(selectedAccessory);

        if (index > -1) {
            $scope.accessories.splice(index, 1);
        }

        //need to add it to the available list again...
        $scope.availableAccessories.push(selectedAccessory);
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

    $scope.cancel = function() {
        $scope.setRoute('/home');
    }

    $scope.saveQuote = function() {
        var stockId = DataManager.getInstance().selectedStockItem.stockId;
        var usedItem = DataManager.getInstance().selectedStockItem.stockUsed;
        var userId;
        if (!DataManager.getInstance().selectedUser) {
           userId = DataManager.getInstance().user.userId;
        }
        else {
            userId = DataManager.getInstance().selectedUser.userId;
        }
        var accessories = DataManager.getInstance().selectedAccessories;
        var installLocation = DataManager.getInstance().selectedInstallLocation;
        var showItemPrices = DataManager.getInstance().showItemPrices;

        var customerName;
        var customerAddress;
        var customerEmailAddress;
        var customerPhoneNumber;
        var customerAttention;

        customerName = DataManager.getInstance().customerName;
        customerAddress = DataManager.getInstance().customerAddress;
        customerEmailAddress = DataManager.getInstance().customerEmailAddress;
        customerPhoneNumber = DataManager.getInstance().customerPhoneNumber;
        customerAttention = DataManager.getInstance().customerAttention;

        var customerId = '';
        if (DataManager.getInstance().customerId)
            customerId = DataManager.getInstance().customerId;

        var pricing = DataManager.getInstance().selectedStockItem.sellingPrice;
        var companyId = DataManager.getInstance().selectedCompany.companyId;

        var notes = $scope.htmlNotes;
        var delivery = $scope.htmlDelivery;
        var installation = $scope.htmlInstallation;

        var accString = JSON.stringify(accessories);
        var locString = JSON.stringify(installLocation)

        var serialNumber = DataManager.getInstance().selectedSerialNumber;
        QuoteService.saveQuote({serverMethod:'saveQuote', stockId:stockId , name:customerName, address:customerAddress, emailAddress:customerEmailAddress, phoneNumber:customerPhoneNumber, attention:customerAttention, userId:userId, serialNumber:serialNumber, pricing:pricing, accessories:accString, notes:notes, delivery:delivery, installation:installation, installationLocation:locString, usedItem:usedItem, companyId:companyId, customerId:customerId, showItemPrices:showItemPrices},
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
                    DataManager.getInstance().customerId = 0;
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
    QuoteService.getAllCustomers({serverMethod:'getAllCustomers'},
        function(result) {
            $scope.customers = result;
        },
        function(error) {
            alert('Error retrieving customers');
        }
    );
    $scope.setSelectedCustomer = function() {
        if ($scope.selectedCustomer.name.length > 0) {
            $scope.customerName = $scope.selectedCustomer.name;
            $scope.customerAddress = $scope.selectedCustomer.address;
            $scope.customerEmailAddress = $scope.selectedCustomer.emailAddress;
            $scope.customerPhoneNumber = $scope.selectedCustomer.phoneNumber;
            $scope.customerAttention = $scope.selectedCustomer.attention;
            DataManager.getInstance().oldCustomer = 1;
            DataManager.getInstance().customerId = $scope.selectedCustomer.customerId;
        }
        else {
            $scope.customerName = '';
            $scope.customerAddress = '';
            $scope.customerEmailAddress = '';
            $scope.customerPhoneNumber = '';
            $scope.customerAttention = '';
            DataManager.getInstance().oldCustomer = 0;
        }
    };
    $scope.next = function() {
        if ($scope.customerName.length == 0) {
            // need to have at least a name to store this against...
            alert('Customer Name cannot be empty\n\r');
        }
        else {
            // we can store this customer information, and it will "save"...
            DataManager.getInstance().customerName = $scope.customerName;
            DataManager.getInstance().customerAddress = $scope.customerAddress;
            DataManager.getInstance().customerEmailAddress = $scope.customerEmailAddress;
            DataManager.getInstance().customerPhoneNumber = $scope.customerPhoneNumber;
            DataManager.getInstance().customerAttention = $scope.customerAttention;
            // move onto the next screen...
            $scope.setRoute('/quoteNotes');
        }
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
    $scope.url = DataManager.getInstance().serverURL+'/document?quotationId='+quoteId+'';
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
    $scope.showAddLocation = 0;
    $scope.currencyCode = 'EUR';
    $scope.changeCurrency = function() {
        if ($scope.selectedStockItem.stockUsed == '1') {
            $scope.currencyCode = 'ZAR';
        }
        else {
            $scope.currencyCode = 'EUR';
        }
    }
    if (DataManager.getInstance().selectedStockItem) {
        $scope.selectedStockItem = DataManager.getInstance().selectedStockItem;
        $scope.showDelete = 1;
        $scope.htmlTechnicalSpecs = $scope.selectedStockItem.technicalSpecs;
        $scope.showSerialSection = 1;
        if ($scope.selectedStockItem.stockUsed == '1') {
            $scope.currencyCode = 'ZAR';
        }
    }
    else
        $scope.showDelete = 0;
    $scope.saveStockItem = function() {
        var htmlTechSpecs = $scope.htmlTechnicalSpecs;
        var errorMessage = '';
        var reg = /^\d*\.?\d*$/;
        var test = reg.test($scope.selectedStockItem.stockMarkup);
        if (!$scope.selectedStockItem.stockManufacturer ||
            $scope.selectedStockItem.stockManufacturer == '') {
            errorMessage = errorMessage + 'Please ensure that a manufacturer has been captured.\n';
        }
        if (!$scope.selectedStockItem.stockModel ||
            $scope.selectedStockItem.stockModel == '') {
            errorMessage = errorMessage + 'Please ensure that a model has been captured.\n';
        }
        if (!$scope.selectedStockItem.stockSeries ||
            $scope.selectedStockItem.stockSeries == '') {
            errorMessage = errorMessage + 'Please ensure that a series has been captured.\n';
        }
        if (!$scope.selectedStockItem.pricing ||
            $scope.selectedStockItem.pricing == '') {
            errorMessage = errorMessage + 'Please ensure that pricing has been captured.\n';
        }
        if (!$scope.selectedStockItem.stockMarkup ||
            $scope.selectedStockItem.stockMarkup == '' ||
            !reg.test($scope.selectedStockItem.stockMarkup)) {
            errorMessage = errorMessage + 'Please ensure that the markup percentage has been captured correctly (no decimal points).\n';
        }
        if (!$scope.selectedStockItem.stockShipping ||
            $scope.selectedStockItem.stockShipping == '' ||
            !reg.test($scope.selectedStockItem.stockShipping)) {
            errorMessage = errorMessage + 'Please ensure that the shipping percentage has been captured correctly (no decimal points).\n';
        }
        if (errorMessage.length > 0) {
            alert(errorMessage);
        }
        else {
            var stockCode = $scope.selectedStockItem.stockManufacturer+"-"+$scope.selectedStockItem.stockModel+"-"+$scope.selectedStockItem.stockSeries;
            StockService.saveStockItem({serverMethod:'saveStockItem', stockId:$scope.selectedStockItem.stockId, stockManufacturer:$scope.selectedStockItem.stockManufacturer, stockModel:$scope.selectedStockItem.stockModel, stockSeries:$scope.selectedStockItem.stockSeries, pricing:$scope.selectedStockItem.pricing, serialNumber:$scope.selectedStockItem.serialNumber, stockCode:stockCode, technicalSpecs:$scope.htmlTechnicalSpecs, description:$scope.selectedStockItem.stockDescription, stockUsed:$scope.selectedStockItem.stockUsed, stockMarkup:$scope.selectedStockItem.stockMarkup, stockShipping:$scope.selectedStockItem.stockShipping},
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
    };
    $scope.cancel = function () {
        $scope.setRoute('/stock');
    };
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
    };
    $scope.showAddSerialNumber = function() {
        $scope.showAddSerial = 1;
    };
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
    };
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
    };
    $scope.addInstallLocation = function() {
        var location = $scope.location;
        var price = $scope.pricing;
        var stockId = $scope.selectedStockItem.stockId;
        StockService.addInstallLocation({location:location, pricing:price, stockId:stockId, serverMethod:'addInstallLocation'},
            function(result) {
                if (result.result === '1') {
                    //everything is done - show alert to say done - then back home...
                    alert('Installation location added successfully.');
                    StockService.getStockForStockId({serverMethod:'getStockForStockId',stockId:$scope.selectedStockItem.stockId},
                        function(result) {
                            DataManager.getInstance().selectedStockItem = result;
                            $scope.selectedStockItem = result;
                            $scope.location = '';
                            $scope.pricing = '';
                            $scope.showAddLocation = 0;
                        },
                        function(error) { }
                    );
                }
                else {
                    //something has gone wrong with saving the user - need to show that...
                    alert('Error adding install location: '+result.message);
                }
            },
            function(error) {
                alert('Error adding install location');
            }
        );
    };
    $scope.removeLocation = function(location, price) {
        var stockId = $scope.selectedStockItem.stockId;
        StockService.deleteInstallLocation({location:location, pricing:price, stockId:stockId, serverMethod:'deleteInstallLocation'},
            function(result) {
                if (result.result === '1') {
                    //everything is done - show alert to say done - then back home...
                    alert('Installation location removed successfully.');
                    StockService.getStockForStockId({serverMethod:'getStockForStockId',stockId:$scope.selectedStockItem.stockId},
                        function(result) {
                            DataManager.getInstance().selectedStockItem = result;
                            $scope.selectedStockItem = result;
                        },
                        function(error) { }
                    );
                }
                else {
                    //something has gone wrong with saving the user - need to show that...
                    alert('Error removing install location: '+result.message);
                }
            },
            function(error) {
                alert('Error removing install location');
            }
        );
    };
    $scope.showAddInstallLocation = function() {
        $scope.showAddLocation = 1;
    };
    $scope.uploadImageFile = function() {
        var stockId = $scope.selectedStockItem.stockId;
        if (stockId) {
            DataManager.getInstance().stockId = stockId;
            $scope.setRoute('/stockImageUpload');
        }
        else {
            alert('Please save the stock item before trying to upload an image.')
        }
    }
}

function StockImageUploadCtrl($scope) {
    $scope.uploadFile = function() {
        var fileOk = 0;
        for (var i in $scope.files) {
            if ($scope.files[i].type != 'image/jpeg') {
                fileOk = 1;
                break;
            }
        }
        if (fileOk == 0) {
            var stockId = DataManager.getInstance().stockId;
            var fd = new FormData();
            fd.append("stockId",stockId);
            fd.append("action","stockImageUpload");
            //add the files to the formData...
            for (var i in $scope.files) {
                fd.append("uploadedFile", $scope.files[i]);
            }
            var xhr = new XMLHttpRequest();
            xhr.upload.addEventListener("progress", uploadProgress, false);
            xhr.addEventListener("load", uploadComplete, false);
            xhr.addEventListener("error", uploadFailed, false);
            xhr.addEventListener("abort", uploadCanceled, false);
            xhr.open("POST", "/LiftAndShiftStock/upload");
            $scope.progressVisible = true;
            xhr.send(fd);
        }
        else {
            alert('Only JPEG images can be uploaded');
        }
    }

    $scope.setFiles = function(element) {
        $scope.$apply(function($scope) {
            console.log('files:', element.files);
            // Turn the FileList object into an Array
            $scope.files = []
            for (var i = 0; i < element.files.length; i++) {
                $scope.files.push(element.files[i])
            }
            $scope.progressVisible = false
        });
    };

    function uploadProgress(evt) {
        $scope.$apply(function(){
            if (evt.lengthComputable) {
                $scope.progress = Math.round(evt.loaded * 100 / evt.total);
            } else {
                $scope.progress = 'unable to compute';
            }
        })
    }

    function uploadComplete(evt) {
        /* This event is raised when the server send back a response */
        var response = evt.target.responseText;
        if (response > 0) {
            alert('Stock Image Uploaded successfully.');
            $scope.setRoute('/stockEdit');
        }
        else {
            alert('Error uploading template');
        }
    }

    function uploadFailed(evt) {
        alert("There was an error attempting to upload the file.");
    }

    function uploadCanceled(evt) {
        $scope.$apply(function(){
            $scope.progressVisible = false;
        })
        alert("The upload has been canceled by the user or the browser dropped the connection.");
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
    $scope.currencies = [{currencyCode:'ZAR', id:'0'},{currencyCode:'EUR', id:'1'}];
    if (DataManager.getInstance().selectedAccessoryItem) {
        $scope.selectedAccessoryItem = DataManager.getInstance().selectedAccessoryItem;
        $scope.showDelete = 1;
        $scope.showSerialSection = 1;
        var tempCurr = {};
        tempCurr.currencyCode = $scope.selectedAccessoryItem.accessoryCurrency;
        if (tempCurr.currencyCode === 'ZAR') {
            tempCurr.id = '0';
        }
        else {
            tempCurr.id = '1';
        }
        $scope.initCurrency = tempCurr;
        $scope.selectedCurrency = tempCurr;
    }
    else
        $scope.showDelete = 0;
    $scope.saveAccessoryItem = function() {
        var stockCode = $scope.selectedAccessoryItem.accessoryManufacturer+"-"+$scope.selectedAccessoryItem.accessoryModel;
        AccessoryService.saveAccessoryItem({serverMethod:'saveAccessoryItem', accessoryId:$scope.selectedAccessoryItem.accessoryId, accessoryManufacturer:$scope.selectedAccessoryItem.accessoryManufacturer, accessoryModel:$scope.selectedAccessoryItem.accessoryModel, currency:$scope.selectedCurrency, pricing:$scope.selectedAccessoryItem.pricing, accessoryCode:stockCode, description:$scope.selectedAccessoryItem.accessoryDescription, accessoryMarkup:$scope.selectedAccessoryItem.accessoryMarkup, accessoryShipping:$scope.selectedAccessoryItem.accessoryShipping},
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

function StockListAllCtrl($scope, ReportService) {
    $scope.exportAllStockForReport = DataManager.getInstance().serverURL+'/report?serverMethod=exportAllStockForReport';
    ReportService.getAllAvailableStockForReport({serverMethod:'getAllStockForReport'},
        function(result) {
            $scope.reportStock = result;
        },
        function(error) {
            alert('Error retrieving stock items for report.')
        }
    );
}

function StockListCtrl($scope, ReportService) {
    $scope.exportAllAvailableStockForReport = DataManager.getInstance().serverURL+'/report?serverMethod=exportAllAvailableStockForReport';
    ReportService.getAllAvailableStockForReport({serverMethod:'getAllAvailableStockForReport'},
        function(result) {
            $scope.reportStock = result;
        },
        function(error) {
            alert('Error retrieving stock items for report.')
        }
    );
}

function StockListSoldCtrl($scope, ReportService) {
    $scope.exportAllAvailableStockForReport = DataManager.getInstance().serverURL+'/report?serverMethod=exportAllSoldStockForReport';
    ReportService.getAllSoldStockForReport({serverMethod:'getAllSoldStockForReport'},
        function(result) {
            $scope.reportStock = result;
        },
        function(error) {
            alert('Error retrieving stock items for report.')
        }
    );
}

