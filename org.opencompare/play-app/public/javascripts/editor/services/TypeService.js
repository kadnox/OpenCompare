/**
 * Created by hvallee on 7/6/15.
 */


pcmApp.service('typeService', function(editorUtil) {

    this.getType = function(featureName, data) {
        var rowIndex = 0;
        var isInt = 0;
        var isBool = 0;
        var isString = 0;
        var codedFeatureName = editorUtil.convertStringToEditorFormat(featureName);
        while(data[rowIndex]) {
            if(data[rowIndex][codedFeatureName]) {
                if (!angular.equals(parseInt(data[rowIndex][codedFeatureName]), NaN)) {
                    isInt++;
                }
                else if (this.isBooleanValue(data[rowIndex][codedFeatureName])) {
                    isBool++;
                }
                else if (!editorUtil.isEmptyCell(data[rowIndex][codedFeatureName])) {
                    isString++;
                }
            }
            rowIndex++;
        }
        var type = "";
        if(isInt > isBool) {
            if(isInt > isString) {
                type = "number";
            }
            else {
                type = "string";
            }
        }
        else if(isBool > isString) {
            type = "boolean";
        }
        else {
            type = "string";
        }
        return type;
    };

    this.validateType = function (productName, featureType) {

        var type = "";
        if(!angular.equals(parseInt(productName), NaN)) {
            type = "number";
        }
        else if(this.isBooleanValue(productName)) {
            type = "boolean";
        }
        else if(!editorUtil.isEmptyCell(productName)){
            type = "string";
        }
        else {
            type = "none"
        }
        if(type == "none") {
            return true;
        }
        else if (featureType == "string") {
            return true;
        }
        else {
            return type === featureType;
        }
    };

    this.isBooleanValue = function (productName) {

        return((productName.toLowerCase() === "yes") ||  (productName.toLowerCase() === "true") ||  (productName.toLowerCase() === "no") ||  (productName.toLowerCase() === "false"));
    };

    this.getBooleanValue = function(name){

        if(name.toLowerCase() === "yes" || name.toLowerCase() === "true") {
            return "yes";
        }
        else  if(name.toLowerCase() === "no" || name.toLowerCase() === "false") {
            return "no";
        }
        else {
            return "unknown";
        }
    }
});

