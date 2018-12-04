package aagym.mqdigital.com.privatgo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import aagym.mqdigital.com.privatgo.util.Config;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alfatih.me on 26/8/2018
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // bind view
    @BindView(R.id.phone_number)
    EditText phoneNumber;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.sign_in)
    ImageView signIn;
    @BindView(R.id.help)
    TextView help;
    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.layout)
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set activity layout
        setContentView(R.layout.activity_login);
        // bind view to this activity context
        ButterKnife.bind(this);
    }

    // view clicklistener generate by butterknife injection
    @OnClick({R.id.sign_in, R.id.help, R.id.register})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.sign_in:
                sendJsonLogin();
                /*
                if (phoneNumber.getText().toString().equals("1234") && password.getText().toString().equals("1234")) {
                    intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar.make(layout, "anda belum terdaftar", Snackbar.LENGTH_SHORT);
                }*/
                break;
            case R.id.help:
                break;
            case R.id.register:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void sendJsonLogin() {

        //String tag_json_obj = "login";

        String url = Config.DOMAIN + Config.LOGIN;
        Toast.makeText(this, phoneNumber.getText().toString() + password.getText().toString(), Toast.LENGTH_SHORT).show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, this::parsingJson,
                error -> Log.d(TAG, "onErrorResponse: " + error)) {
            protected Map<String, String> getParamss() {
                Map<String, String> MyData = new HashMap<>();
                MyData.put("user_phone", phoneNumber.getText().toString());
                MyData.put("user_password", password.getText().toString());
                return MyData;
            }
        };

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        NetworkRequest.getInstance(this).addToRequestQueue(jsonObjReq);

    }

    public void parsingJson(String json) {
        Toast.makeText(this, ""+json, Toast.LENGTH_SHORT).show();
//        try {
//
//            JSONObject jObject = new JSONObject(json);
//
//            String result_json = jObject.getString("message");
//            String result_code = jObject.getString("code");
//            String result_user = jObject.getString("data");
//
//            Log.d(TAG, "result_json: " + result_json + "result_code: " + result_code);
//
//            Toast.makeText(this, ""+result_json, Toast.LENGTH_SHORT).show();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
}