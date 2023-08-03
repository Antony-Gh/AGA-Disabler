package com.aga.disabler.pro.samsung;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.aga.disabler.pro.R;

public class CustomDialog extends Dialog implements android.view.View.OnClickListener {

  public Activity c;
  public Dialog d;
  public Button yes, no;
  public final Context con;
  private final onClick monClick;
  private final String tit;
  private final String ms;

  public CustomDialog(Activity a, Context cc, onClick mon, String titlee, String msgg) {
    super(a);
    this.c = a;
    this.con = cc;
    this.monClick = mon;
    this.ms = msgg;
    this.tit = titlee;
  }


  public interface onClick{
    void onOkClick();
    void onCancelClick();
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.dialog_list_layout);
    yes = findViewById(R.id.alert_dialog_list_ok);
    no = findViewById(R.id.alert_dialog_list_cancel);
    yes.setOnClickListener(this);
    no.setOnClickListener(this);
    TextView title = findViewById(R.id.alert_dialog_list_title);
    TextView msg = findViewById(R.id.alert_dialog_list_content);
    title.setText(tit);
    msg.setText(ms);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.alert_dialog_list_ok:
        monClick.onOkClick();
        break;
      case R.id.alert_dialog_list_cancel:
        monClick.onCancelClick();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + v.getId());
    }
    dismiss();
  }

  public static class CustomDialogBuilder {
    public Activity act;
    public Context con;
    public onClick lis;
    public String msg;
    public String title;


    public CustomDialogBuilder setActvity(Activity z) {
      this.act = z;
      return this;
    }

    public CustomDialogBuilder setContexttt(Context z) {
      this.con = z;
      return this;
    }

    public CustomDialogBuilder setlisteners(onClick on) {
      this.lis = on;
      return this;
    }

    public CustomDialogBuilder settitle(String on) {
      this.title = on;
      return this;
    }

    public CustomDialogBuilder setmsg(String on) {
      this.msg = on;
      return this;
    }


    public void build() {
      CustomDialog cdd = new CustomDialog(act, con, lis, title, msg);
      cdd.show();
    }
  }


}
