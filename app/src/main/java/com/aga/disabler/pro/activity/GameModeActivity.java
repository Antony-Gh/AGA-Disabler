package com.aga.disabler.pro.activity;

import static com.aga.disabler.pro.fragment.AppGameModeList.App_Disable;
import static com.aga.disabler.pro.fragment.AppGameModeList.App_Enable;
import static com.aga.disabler.pro.tools.Helper.emmtoast;
import static com.aga.disabler.pro.tools.Helper.fromdrawabletobyte;
import static com.aga.disabler.pro.tools.Helper.isMyServiceRunning;
import static com.aga.disabler.pro.tools.Helper.loadGameList;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.adapters.GameModeAdapter;
import com.aga.disabler.pro.fragment.AppGameModeList;
import com.aga.disabler.pro.license.ExecutorServiceII;
import com.aga.disabler.pro.receiver.JobsService;
import com.aga.disabler.pro.tools.CustomDialog;
import com.aga.disabler.pro.tools.FileUtil;
import com.aga.disabler.pro.tools.Helper;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class GameModeActivity extends  AppCompatActivity implements ExecutorServiceII.Tasks {

	public List<AppGameModeList> list = new ArrayList<>();
	private Context c;
	private CheckBox gm;
	private String path = "";
	private GameModeAdapter adapter;
	private ListView listview;
	private LinearLayout load;
	private LinearLayout listl;
	private String s = "KK";
	private ExecutorServiceII exe;

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.gamemode);
		initialize();
	}

	@Override
	protected void onStart() {
		gm.setChecked(isMyServiceRunning(JobsService.class, c));
		gm.setEnabled(!gm.isChecked());
		gm.setOnClickListener(view -> {
			try {
				Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		super.onStart();
	}

	private void initialize() {
		c = this.getApplicationContext();
		exe = new ExecutorServiceII.ExecutorBuilder().setTasks(this).build();
		path = FileUtil.getPackageDataDir(c).concat("/GameModeList.txt");
		gm = findViewById(R.id.gm);
		listview = findViewById(R.id.list);
		ImageView reload = findViewById(R.id.reload);
		load = findViewById(R.id.load_linear);
		listl = findViewById(R.id.list_linear);
		reload.setOnClickListener(view -> {
			s = "LL";
			exe.execute();
		} );

		listview.setOnItemClickListener((adapterView, view, i, l) -> {
			AppGameModeList a = ((GameModeAdapter) listview.getAdapter()).getListStorage().get(i);
			int type = a.type;
			String n = a.name;
			switch (type){
				case 0:
					((GameModeAdapter) listview.getAdapter()).getListStorage().get(i).type = 1;
					emmtoast(n + " Will Be Enabled", this);
				break;
				case 1:
					((GameModeAdapter) listview.getAdapter()).getListStorage().get(i).type = 2;
					emmtoast(n + " Will Be Disabled", this);
				break;
				case 2:
					((GameModeAdapter) listview.getAdapter()).getListStorage().get(i).type = 0;
				break;
			}
			((GameModeAdapter) listview.getAdapter()).notifyDataSetChanged();
		});
		adapter = new GameModeAdapter();
		Button b = findViewById(R.id.save);
		b.setOnClickListener(view -> {
			Runnable r = () -> savelist(((GameModeAdapter) listview.getAdapter()).getListStorage());
			r.run();
			emmtoast("Saved", c);
			finish();
		});
		exe.execute();
	}


	public List<AppGameModeList> getallapps() {
		List<String> pkgs = addSomeSystem();
		List<AppGameModeList> listk = new ArrayList<>();
		PackageManager pm = c.getPackageManager();
		List<ApplicationInfo> in = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		Iterator<ApplicationInfo> it = in.iterator();
		while (it.hasNext()){
			try {
				AppGameModeList ap = new AppGameModeList();
				ApplicationInfo apin = it.next();
				String pkg = apin.packageName;
				ap.setpackages(pkg);
				ap.setname(pm.getApplicationLabel(apin).toString());
				ap.seticon(fromdrawabletobyte(pm.getApplicationIcon(pkg)));
				ap.settype(AppGameModeList.App_None);
				if((apin.flags & ApplicationInfo.FLAG_SYSTEM) == 0 || pkgs.contains(pkg)){
					listk.add(ap);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listk;
	}

	public void savelist(List<AppGameModeList> obj) {
		Gson gson = new Gson();
		String g = gson.toJson(obj);
		try {
			FileUtil.writeFile(path, g);
		} catch (Exception i) {
			i.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		if(checksave()){
			super.onBackPressed();
		}else{
			createclosedialog();
		}
	}

	private void createclosedialog(){
		Helper.CreateKnoxAlert(this, getString(R.string.save_changes), getString(R.string.save_changes_alert), new CustomDialog.onClick() {
			@Override
			public void onOkClick() {
				finish();
			}
			@Override
			public void onCancelClick() {

			}
		});
	}

	public boolean checksave() {
		try {
			List<AppGameModeList> obj = ((GameModeAdapter) listview.getAdapter()).getListStorage();
			return list == obj;
		}catch (Exception e){
			return true;
		}
	}

	@Override
	public void doinbackground() {
		if(s.equals("KK")) {
			if(new File(path).exists()){
				list = loadGameList(path);
			}else{
				list = getallapps();
			}
		}else{
			loadnewitems();
		}
	}

	@Override
	public void onpreexecute() {
		listl.setVisibility(View.GONE);
		load.setVisibility(View.VISIBLE);
	}

	@Override
	public void onpostexecute() {
		adapter.setData(list);
		listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		load.setVisibility(View.GONE);
		listl.setVisibility(View.VISIBLE);
		if(!s.equals("KK")) {
			s="KK";
			emmtoast("Successfully Loaded new items", c);
		}
	}

	public void loadnewitems(){
		try {
			List<AppGameModeList> alllist = getallapps();
			List<AppGameModeList> loaded = loadGameList(path);
			assert loaded != null;
			Iterator<AppGameModeList> it = loaded.iterator();
			HashMap<String, Integer> ah = new HashMap<>();

			while (it.hasNext()) {
				try {
					AppGameModeList a = it.next();
					if (a.type == App_Enable || a.type == App_Disable) {
						ah.put(a.packages, a.type);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			for (int i = 0; i < alllist.size(); i++) {
				try {
					AppGameModeList a = alllist.get(i);
					String pp = a.packages;
					if (ah.get(pp) != null && ah.containsKey(pp)) {
						alllist.get(i).type = ah.get(pp);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			AppGameModeList.sortArrayList(alllist);
			list = alllist;
			savelist(alllist);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private List<String> addSomeSystem() {
		List<String> pkgs = new ArrayList<>();
		pkgs.add("com.google.android.apps.docs");
		pkgs.add("com.microsoft.office.excel");
		pkgs.add("com.facebook.katana");
		pkgs.add("com.sec.android.app.samsungapps");
		pkgs.add("com.google.android.gm");
		pkgs.add("com.google.android.googlequicksearchbox");
		pkgs.add("com.android.vending");
		pkgs.add("com.google.android.videos");
		pkgs.add("com.google.android.apps.maps");
		pkgs.add("com.google.android.apps.tachyon");
		pkgs.add("com.microsoft.skydrive");
		pkgs.add("com.google.android.apps.photos");
		pkgs.add("com.microsoft.office.powerpoint");
		pkgs.add("com.google.android.apps.docs");
		pkgs.add("com.sec.android.app.shealth");
		pkgs.add("com.sec.android.app.sbrowser");
		pkgs.add("com.samsung.android.voc");
		pkgs.add("com.samsung.android.samsungpass");
		pkgs.add("com.samsung.knox.securefolder");
		pkgs.add("com.skype.raider");
		pkgs.add("com.microsoft.office.word");
		pkgs.add("com.google.android.youtube");
		return pkgs;
	}
}