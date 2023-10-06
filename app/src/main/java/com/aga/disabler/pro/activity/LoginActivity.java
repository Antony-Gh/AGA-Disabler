package com.aga.disabler.pro.activity;

import static com.aga.disabler.pro.tools.Helper.getimei;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.receiver.devicepolicy;
import com.aga.disabler.pro.tools.Helper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LoginActivity extends  AppCompatActivity {


    private static final int ERR_LICENCE_NOT_EXIST = 1;
    private static final int ERR_IMEI_MISMATCHED = 2;
    private static final int ERR_LICENCE_UNAVAILABLE = 3;
    private static final int ERR_UNKNOWN = 0;
    private String IMEI;

    private TextInputLayout textField;
    private TextInputEditText lice;
    private ProgressBar progressbar1;
    private Button stnbut;
    private Context c;
    private final Intent sub = new Intent();


    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.login);
        initialize();
        initializeLogic();
    }

    private void initialize() {
        c = LoginActivity.this.getApplicationContext();
        ImageView imageview9 = findViewById(R.id.imageview9);
        ImageView imageview10 = findViewById(R.id.imageview10);
        ImageView imageview11 = findViewById(R.id.imageview11);
        ImageView imageview12 = findViewById(R.id.imageview12);
        ImageView imageview13 = findViewById(R.id.imageview13);
        ImageView imageview21 = findViewById(R.id.imageview21);
        boolean b = getIntent().getBooleanExtra("st", false);
        textField = findViewById(R.id.textinputlayout5);
        lice = findViewById(R.id.lice);
        progressbar1 = findViewById(R.id.aga_progress_bar);
        stnbut = findViewById(R.id.stnbut);
        IMEI = getimei(c);
        if ((Helper.getregi(c)).equals("true") && !b) {
            sub.setClass(getApplicationContext(), AdminActivity.class);
            startActivity(sub);
            finish();
        }

        //insta
        imageview9.setOnClickListener(_view -> links("https://instagram.com/agaantony?igshid=ZDdkNTZiNTM="));

        //whats 015
        imageview10.setOnClickListener(_view -> links("https://wa.me/qr/GTRT4CVIECKWK1"));

        //facebook
        imageview11.setOnClickListener(_view -> links("https://www.facebook.com/agaantony?mibextid=ZbWKwL"));

        //Tele 015
        imageview12.setOnClickListener(_view -> links("https://t.me/aga758"));

        //youtube
        imageview13.setOnClickListener(_view -> links("https://youtube.com/@AgaAndroid"));

        //twitter
        imageview21.setOnClickListener(_view -> o());


        stnbut.setOnClickListener(_view -> {
            setErrorTextField(false, "");
            stnbut.setVisibility(View.GONE);
            progressbar1.setVisibility(View.VISIBLE);
            if (!Objects.requireNonNull(lice.getText()).toString().equals("")) {
                getmyacc();
            }else{
                stnbut.setVisibility(View.VISIBLE);
                progressbar1.setVisibility(View.GONE);
                setErrorTextField(true, getString(R.string.empty_field));
            }
        });

        lice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setErrorTextField(false, "");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void texterror(String msg) {
        stnbut.setVisibility(View.VISIBLE);
        progressbar1.setVisibility(View.GONE);
        setErrorTextField(true, msg);
    }

    private void initializeLogic() {
        progressbar1.setVisibility(View.GONE);
        setErrorTextField(false, "");
    }

    private void o() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", "MY INFO");
        intent.putExtra("android.intent.extra.TEXT", IMEI);
        startActivity(intent);
    }


    private void getmyacc() {
        String typelic = Objects.requireNonNull(lice.getText()).toString();
        DatabaseReference list_db = FirebaseDatabase.getInstance().getReference("allaccounts");
        list_db.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                Log.d("All Accounts :  ", dataSnapshot.toString());
                final long all_usr = dataSnapshot.getChildrenCount();
                Log.d("Child Count :  ",  "S : " + all_usr);
                int i4 = ERR_UNKNOWN;
                for (DataSnapshot p : dataSnapshot.getChildren()) {
                    String block = Objects.requireNonNull(p.child("block").getValue()).toString();
                    String lic = Objects.requireNonNull(p.child("lic").getValue()).toString();
                    String imei = Objects.requireNonNull(p.child("usri").getValue()).toString();
                    Log.d("Now Account : ", p.toString());
                    if(lic.equals(typelic)){
                        if(IMEI.equals(imei)){
                            if ("false".equals(block)) {
                                Log.d("Selected Account : ", p.toString());
                                View view = LoginActivity.this.getCurrentFocus();
                                if(view != null){
                                    InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                                Context con = LoginActivity.this.getApplicationContext();
                                devicepolicy dp = new devicepolicy(con);
                                dp.myApppolicy(true);
                                setErrorTextField(false, "");
                                Helper.setregi(c, "true");
                                Helper.setlic(c, lic);
                                sub.setClass(c, AdminActivity.class);
                                sub.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(sub);
                                finish();
                            }else{
                                i4 = ERR_LICENCE_UNAVAILABLE;
                                break;
                            }
                        }else{
                            Log.d("IMEI: ", "imei : " + imei + " Type imei : " + IMEI);
                            i4 = ERR_IMEI_MISMATCHED;
                            break;
                        }
                    }else{
                        Log.d("License: ", "Lice : " + lic + " Type Lic : " + typelic);
                        i4 = ERR_LICENCE_NOT_EXIST;
                    }
                }
                checkerror(i4);
            }
            @Override public void onCancelled(@NotNull DatabaseError databaseError) {
                checkerror(ERR_UNKNOWN);
            }
        });
    }

    private void checkerror(int i4) {
        switch (i4){
            case ERR_LICENCE_NOT_EXIST:
                texterror(getString(R.string.no_license));
                break;
            case ERR_IMEI_MISMATCHED:
                texterror(getString(R.string.differnet_imei));
                break;
            case ERR_LICENCE_UNAVAILABLE:
                texterror(getString(R.string.license_states_false));
                break;
            case ERR_UNKNOWN:
                texterror(getString(R.string.unknown_error));
                break;
        }
    }

    public void links(String pkg) {
        try{
            final Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse(pkg));
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setErrorTextField(boolean error, String s) {
        textField.setErrorEnabled(error);
        textField.setError(s);
    }

}
