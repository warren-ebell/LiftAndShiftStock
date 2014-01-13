function DataManager(){
    if ( arguments.callee.instance )
        return arguments.callee.instance;
    arguments.callee.instance = this;
}


DataManager.getInstance = function() {
    var dataManager = new DataManager();
    return dataManager;
};