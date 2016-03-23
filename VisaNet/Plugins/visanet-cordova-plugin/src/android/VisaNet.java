package com.visanet.plugin;

import android.content.Intent;
import android.util.Base64;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URI;
import java.util.HashMap;

import java.net.URI;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import pe.com.visanet.lib.VisaNetConfigurationContext;
import pe.com.visanet.lib.VisaNetPaymentActivity;
import pe.com.visanet.lib.VisaNetConfigurationContext.VisaNetCurrency;
import pe.com.visanet.lib.VisaNetPaymentInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by ALONSO UCHIDA on 7/03/2016.
 */
public class VisaNet extends CordovaPlugin {

    private static final String TAG = "VISANET";
    private static int REQUEST_CODE_PAYMENT = 1;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if (action.equals("callvisanet")) {

            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            try {
                boolean isTesting = true;
                boolean useToken = false;

                double amount = data.getDouble(0);
                Alert("Field:", Double.toString(amount));
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
				 Alert("Field:",secretAccess);
                
                // https://devapi.vnforapps.com/api.tokenization/api/v2/merchant/132272410/nextCounter
                String nextCounterUri = isTesting ? String.format("https://devapi.vnforapps.com/api.tokenization/api/v2/merchant/%s/nextCounter", merchantId)
                        : String.format("https://devapi.vnforapps.com/api.tokenization/api/v2/merchant/%s/nextCounter", merchantId);

                DefaultHttpClient client = new DefaultHttpClient();

                HttpGet nextCounterGet = new HttpGet();
                nextCounterGet.setURI(new URI(nextCounterUri));
                String authorization = String.format("%s:%s", accessKeyId, secretAccess);
                String usernamePasswordEncoded = Base64.encodeToString(authorization.getBytes(), Base64.NO_WRAP);
                nextCounterGet.addHeader("Authorization", String.format("Basic %s", usernamePasswordEncoded));
                nextCounterGet.addHeader("Content-Type", "application/json");

                // response
                HttpResponse httpResponse = client.execute(nextCounterGet);
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                HttpEntity entity = httpResponse.getEntity();
                String transactionId = EntityUtils.toString(entity, "UTF-8");


                // inicializar objeto VisaNetConfigurationContext
                VisaNetConfigurationContext ctx = new VisaNetConfigurationContext();
                if (isTesting) {
                    ctx.setTesting(true);
                    ctx.setEndPointURL(endpoint);
                } else {
                    ctx.setEndPointURL("https://api.vnforapps.com/api.tokenization/api/v2");
                }
                // inicializar data
                HashMap<String, String> data2 = new HashMap<String, String>();
                ctx.setData(data2);
                ctx.setAccessKeyId(accessKeyId);
                ctx.setSecretAccess(secretAccess);
                ctx.setCustomerFirstName(customerFirstName);
                ctx.setCustomerLastName(customerLastName);
                ctx.setCustomerEmail(customerEmail);
                // ctx.setLanguage(VisaNetLanguage.SPANISH);
                ctx.setMerchantId(merchantId);
                ctx.setCurrency(VisaNetCurrency.PEN);
                ctx.setAmount(amount);
                ctx.setTransactionId(transactionId);
                ctx.setExternalTransactionId(externalTransactionId);
                ctx.setUserTokenId(userTokenId);
                // ctx.setMaxCardsPerToken(4); // 3 is default

                ctx.getMerchantDefinedData().put("field3", "movil");

                // iniciar actividad VisaNet
                Intent intent = new Intent(this.cordova.getActivity().getApplicationContext(), VisaNetPaymentActivity.class);
                intent.putExtra(
                        VisaNetConfigurationContext.VISANET_CONTEXT,
                        ctx);
                this.cordova.startActivityForResult(this, intent, REQUEST_CODE_PAYMENT);

            } catch (Exception e) {
                Alert("Error:", e.getMessage());
            }

            return true;

        } else {

            return false;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK
                    || resultCode == Activity.RESULT_CANCELED) {

                /*EditText edtProduct = (EditText) findViewById(R.id.editTextProducto);
                String product = edtProduct.getText().toString();*/

                if (data != null) {
                    VisaNetPaymentInfo info = (VisaNetPaymentInfo) data
                            .getSerializableExtra(VisaNetConfigurationContext.VISANET_CONTEXT);
                    Log.i(TAG, "TransactionId: " + info.getTransactionId());
                    String email = info.getEmail();
                    String firstName = info.getFirstName();
                    String lastName = info.getLastName();

                    String codAccion = info.getData().get("CODACCION");
                    Log.i(TAG, "codAccion: " + codAccion);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.cordova.getActivity().getApplicationContext());
                    alertDialog.setTitle("Resultado");
                    String infoData = "";
                    for (String item : info.getData().keySet()) {
                        infoData += String.format("%s: %s\n", item, info
                                .getData().get(item));
                    }

                    String message = String.format("Resultado: %s\n"
                                    + "Mensaje: %s\n" + "Data: %s\n"
                                    + "Cliente: %s %s\n" + "Email: %s",
                            info.getPaymentStatus(),
                            info.getPaymentDescription(), infoData, firstName,
                            lastName, email);
                    alertDialog.setMessage(message);
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            return;
                        }
                    });
                    alertDialog.show();

                    Toast.makeText(this.cordova.getActivity().getApplicationContext(), "Payment Information received from VisaNet"
                            + info.getTransactionId(), Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "Processing Error!!!.");
                Alert("Error", "Processing Error!!.");

            }
        }
    }
    
        public void Alert(String title, String message){
          //Alert
                new AlertDialog.Builder(this.cordova.getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { 
                        // continue with delete
                    }
                 })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { 
                        // do nothing
                    }
                 })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



}

