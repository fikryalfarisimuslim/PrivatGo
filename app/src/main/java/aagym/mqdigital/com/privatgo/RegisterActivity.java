package aagym.mqdigital.com.privatgo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.school_level)
    AutoCompleteTextView school_level;
    @BindView(R.id.register)
    Button register;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.phone_number)
    EditText phone;
    @BindView(R.id.age)
    EditText age;
    String selection;
    Calendar myCalendar;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, getResources()
                .getStringArray(R.array.school_level));
        school_level.setAdapter(arrayAdapter);
        school_level.setCursorVisible(false);
        school_level.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                school_level.showDropDown();
                selection = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), selection,
                        Toast.LENGTH_SHORT);
            }
        });

        myCalendar = Calendar.getInstance();

        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RegisterActivity.this,AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String formatTanggal = "dd-MM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(formatTanggal);
                        age.setText("" + sdf.format(myCalendar.getTime()));
                    }
                },
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidEmail(email.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "Format email sudah benar", Toast.LENGTH_SHORT).show();
                } else {
                    email.setError("Format email salah");
                    Toast.makeText(RegisterActivity.this,"Format email salah", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @OnClick({R.id.school_level, R.id.register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.school_level:
                school_level.showDropDown();
                break;
            case R.id.register:
                break;
        }
    }

    public static boolean isValidEmail(String email) {
        boolean validate;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";

        if (email.matches(emailPattern)) {
            validate = true;
        } else if (email.matches(emailPattern2)) {
            validate = true;
        } else {
            validate = false;
        }

        return validate;
    }

/*
    private void sendJsonSignUp(){

        String url = "https://olmatix.com/wamonitoring/create_users.php";
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, response -> {
            Log.d(TAG, "onResponse: "+response);
            parsingJson(response);
        }, error -> Log.d(TAG, "onErrorResponse: "+error)) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("real_name", real_name); //Add the data you'd like to send to the server.
                MyData.put("email", email);
                MyData.put("password", password);
                MyData.put("company_name", company_name);
                MyData.put("company_id", String.valueOf(company_id));
                MyData.put("level_user", String.valueOf(level_user));
                MyData.put("date_created", String.valueOf(now.getTimeInMillis()));
                MyData.put("last_update", String.valueOf(now.getTimeInMillis()));

                return MyData;
            }
        };

        NetworkRequest.getInstance(this).addToRequestQueue(jsonObjReq);



    }

    public void parsingJson(String json) {
        try {
            JSONObject jObject = new JSONObject(json);
            String msg = jObject.optString("error_msg");
            Log.d("parsing", "parsingJson: "+msg);

            String result_json = jObject.getString("message");
            String result_code = jObject.getString("success");

            Log.d("parsing", "result_json: "+result_json +"result_code: "+result_code);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
