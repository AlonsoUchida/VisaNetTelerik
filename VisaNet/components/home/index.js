'use strict';

app.home = kendo.observable({
    onShow: function() {
       /* double amount = data.getDouble(0);
                Alert("Field:", amount.toString());
                String customerFirstName = data.getString(1);
                Alert("Field:", customerFirstName);
                String customerLastName = data.getString(2);
                Alert("Field:", customerLastName);
                String customerEmail = data.getString(3);
                Alert("Field:", customerEmail);
                String externalTransactionId = data.getString(4);//"my.external.id";
                Alert("Field:", externalTransactionId);
                String merchantId = isTesting ? "521040502" : data.getString(5); // edtMerchantId.getText().toString();
                Alert("Field:", merchantId);
                String userTokenId = useToken ? (isTesting ? "269c8764-2f39-453c-b11f-bbad462ecd71" : data.getString(6)) : null;
                Alert("Field:", userTokenId);
                String endpoint =  data.getString(7);// "https://devapi.vnforapps.com/api.tokenization/api/v2";
                Alert("Field:", endpoint);
                String accessKeyId = isTesting ? "AKIAI4VNT2CSVWSTJV6Q" : data.getString(8); //"AKIAI737YRU5WIQ5W6JQ";
                 Alert("Field:", accessKeyId);
                String secretAccess = isTesting ? "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" : data.getString(9); //"hssNV8/TJHGs2FQUYTrufGmu/nzudtXU9fPOj5CO";
				 Alert("Field:",secretAccess);*/
        var success = function(message){
            console.log(message);
        };
        var error = function(error){
            console.log(error);
        }
        
        var amount = 0;
			var customerFirstName = 'Alonso';
			var customerLastName = 'Uchida';
			var customerEmail = 'alonso.uchida@gmail.com';
			var externalTransactionId = 'externalTransactionId';
			var merchantId = 'options.merchantId';
			var userTokenId = 'options.userTokenId';
			var endpoint = 'options.endpoint';
			var accessKeyId = 'options.accessKeyId';
			var secretAccess = 'options.secretAccess';

			var args = [amount, customerFirstName, customerLastName, customerEmail, externalTransactionId, 
						merchantId, userTokenId, endpoint, accessKeyId, secretAccess];
        
        visanet.callvisanet(args, success, error);
    },
    afterShow: function() {}
});

// START_CUSTOM_CODE_home
// Add custom code here. For more information about custom code, see http://docs.telerik.com/platform/screenbuilder/troubleshooting/how-to-keep-custom-code-changes

// END_CUSTOM_CODE_home