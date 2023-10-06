package com.aga.disabler.pro.tools;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.aga.disabler.pro.R;

public class CustomDialog extends Dialog {


  public Dialog d;
  public Button yes, no;

  public final Context con;
  private final onClick monClick;
  private final String tit;
  private final String ms;

  public CustomDialog(Context cc, onClick mon, String titlee, String msgg) {
    super(cc);
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
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.dialog_list_layout);
    yes = findViewById(R.id.alert_dialog_list_ok);
    no = findViewById(R.id.alert_dialog_list_cancel);

    yes.setOnClickListener(v -> {
      monClick.onOkClick();
      if (isShowing()) dismiss();
    });
    no.setOnClickListener(v -> {
      monClick.onCancelClick();
      if (isShowing()) dismiss();
    });
    TextView title = findViewById(R.id.alert_dialog_list_title);
    TextView msg = findViewById(R.id.alert_dialog_list_content);
    title.setText(tit);
    msg.setText(ms);
    setCancelable(false);
    setCanceledOnTouchOutside(false);
    super.onCreate(savedInstanceState);
  }

  public static class CustomDialogBuilder {
    public Context con;
    public onClick lis;
    public String msg;
    public String title;


    public CustomDialogBuilder setDialogContext(Context z) {
      this.con = z;
      return this;
    }

    public CustomDialogBuilder setListeners(onClick on) {
      this.lis = on;
      return this;
    }

    public CustomDialogBuilder setTitle(String on) {
      this.title = on;
      return this;
    }

    public CustomDialogBuilder setMessage(String on) {
      this.msg = on;
      return this;
    }


    public void build() {
      CustomDialog cdd = new CustomDialog(con, lis, title, msg);
      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        cdd.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
      }
      cdd.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
      cdd.show();
    }
  }


}
