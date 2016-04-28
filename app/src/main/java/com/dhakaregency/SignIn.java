package com.dhakaregency;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dhakaregency.quickkot.R;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class SignIn extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    EditText _useridText;
    EditText _passwordText;
    Button _loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        _useridText=(EditText)findViewById(R.id.input_userid);
        _passwordText=(EditText)findViewById(R.id.input_password);
        _loginButton=(Button)findViewById(R.id.btn_login);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }
    public void login() {


        if (!validate()) {
            onLoginFailed();
            return;
        }

       // _loginButton.setEnabled(false);

        String user_id = _useridText.getText().toString();
        String password = _passwordText.getText().toString();

        ArrayList<String> passing = new ArrayList<String>();
        passing.add(user_id);
        passing.add(password);

        int isUserAuthenticated=0;
        UserAuthCheck userAuthCheck=new UserAuthCheck();
        userAuthCheck.execute(passing);
    }


    public boolean validate() {
        boolean valid = true;

        String user_id= _useridText.getText().toString();
        String password = _passwordText.getText().toString();

        if (user_id.isEmpty() ) {
            _useridText.setError("enter a user id");
            valid = false;
        } else {
            _useridText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void onLoginSuccess() {

        Intent intent = new Intent(getApplicationContext(),outlets.class );
        //Create the bundle
        Bundle bundle = new Bundle();
        //Add your data to bundle
        bundle.putString("userid", _useridText.getText().toString());
        //Add the bundle to the intent
        intent .putExtras(bundle);
        startActivity(intent);

    }

    public void onLoginFailed() {
        _loginButton.setEnabled(true);
        Toast.makeText(getApplicationContext(),"Invalid User",Toast.LENGTH_LONG).show();;

    }



    public class UserAuthCheck extends AsyncTask<ArrayList<String>, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);

            if (i==1)
            {
            onLoginSuccess();
                  }
            else
            {
                onLoginFailed();
            }
        }

        @Override
        protected Integer  doInBackground(ArrayList<String>... params) {

            String str = "http://192.168.99.23:8080/AuthService.svc/GetAuth";
            String response="";
            URL url=null;
            try {
                url = new URL(str);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {

                HttpURLConnection conn=null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                ArrayList<String> passed = params[0];

                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");


                JSONObject jsonObject = new JSONObject();
                // Build JSON string
                JSONStringer userJson = new JSONStringer()
                        .object()
                        .key("user")
                        .object()
                        .key("userid").value(passed.get(0).toString())
                        .key("password").value(passed.get(1).toString())
                        .endObject()
                        .endObject();

                //byte[] outputBytes = jsonParam.toString().getBytes("UTF-8");
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
                outputStreamWriter.write(userJson.toString());
                outputStreamWriter.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
                else {
                    response="";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Integer result=0;
            JSONObject jObject=null;
            if (!response.isEmpty()) {
                try {
                    jObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    result= Integer.parseInt(jObject.getString("GetAuthResult"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ;
            }
            return result;
        }

    }
}
