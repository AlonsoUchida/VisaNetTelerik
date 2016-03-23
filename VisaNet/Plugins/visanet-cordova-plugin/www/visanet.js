/*global cordova, module*/

module.exports = {
     callvisanet: function (options, successCallback, errorCallback) {
            console.log(options);
			options = options || {};

			var amount = options[0];
			var customerFirstName = options[1];
			var customerLastName = options[2];
			var customerEmail = options[3];
			var externalTransactionId = options[4];
			var merchantId = options[5];
			var userTokenId = options[6];
			var endpoint = options[7];
			var accessKeyId = options[8];
			var secretAccess = options[9];

			var args = [amount, customerFirstName, customerLastName, customerEmail, externalTransactionId, 
						merchantId, userTokenId, endpoint, accessKeyId, secretAccess];

	
			cordova.exec(successCallback, errorCallback, "VisaNet", "callvisanet", args);
    }
    
};