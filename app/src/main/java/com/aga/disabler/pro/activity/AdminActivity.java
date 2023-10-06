package com.aga.disabler.pro.activity;

import static com.aga.disabler.pro.fragment.AppList.sortArrayList;
import static com.aga.disabler.pro.license.SharedPreferenceManager.getinfo;
import static com.aga.disabler.pro.license.SharedPreferenceManager.getloc;
import static com.aga.disabler.pro.license.SharedPreferenceManager.setloc;
import static com.aga.disabler.pro.tools.Helper.emmtoast;
import static com.aga.disabler.pro.tools.Helper.fromdrawabletobyte;
import static com.aga.disabler.pro.tools.Helper.hideKeyboard;
import static com.aga.disabler.pro.tools.Helper.isOnline;
import static com.aga.disabler.pro.tools.Helper.isenableapp;
import static com.aga.disabler.pro.tools.Helper.isexistapp;
import static com.aga.disabler.pro.tools.Helper.loadlist;
import static com.aga.disabler.pro.tools.Helper.mType;
import static com.aga.disabler.pro.tools.Helper.prevstar;
import static com.aga.disabler.pro.tools.Helper.savepkgname;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.adapters.Listview1Adapter;
import com.aga.disabler.pro.fragment.AppList;
import com.aga.disabler.pro.license.ExecutorServiceII;
import com.aga.disabler.pro.receiver.devicepolicy;
import com.aga.disabler.pro.tools.CustomDialog;
import com.aga.disabler.pro.tools.FileUtil;
import com.aga.disabler.pro.tools.Helper;
import com.aga.disabler.pro.tools.appinfo;
import com.aga.disabler.pro.tools.info;
import com.samsung.android.knox.custom.CustomDeviceManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class AdminActivity extends AppCompatActivity implements ExecutorServiceII.Tasks {

	private ListView listviewall;
	private List<AppList> allList;
	private List<AppList> insList;
	private LinearLayout lin;
	private Listview1Adapter listview1Adapter;
	private androidx.appcompat.widget.Toolbar toolbar;
	private Context con;
	private ArrayList<HashMap<Object, Object>> array;
	private SearchView searchView;
	private LinearLayout no_apps;
	private boolean filter;
	private int ori;
	private LinearLayout appinfolinear;
    private ExecutorServiceII exe;
	private String s = "KK";

	private View view66;

	private void Async() {
		toolbar.setTitle(getString(R.string.ins_pkg_adminact));
        exe.execute();
	}

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.admin);
		initialize();
		initializeLogic();
	}

	@Override
	public void onConfigurationChanged(@NotNull Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.admin);
		initialize();
		initializeLogic();
		setupWindowAnimations();
	}

	private void setupWindowAnimations() {
		getWindow().setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.activity_fade));
		getWindow().setExitTransition(TransitionInflater.from(this).inflateTransition(R.transition.activity_slide));
}

	private void initialize() {
		con = AdminActivity.this.getApplicationContext();
		com.google.android.material.appbar.AppBarLayout appbar = findViewById(R.id.app_bar);
		lin = findViewById(R.id.load_linear);
		toolbar = appbar.findViewById(R.id.toolbar);
		toolbar.setTitle(con.getString(R.string.ins_pkg_adminact));
		setSupportActionBar(toolbar);
		Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		listviewall = findViewById(R.id.listviewall);
		lin.setVisibility(View.GONE);
		try {
			view66 = findViewById(R.id.view66);
			view66.setVisibility(View.GONE);
		}catch (Exception ignored){

		}
		listviewall.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView arg0, int scrollState) {
				((Listview1Adapter) listviewall.getAdapter()).userscroll = scrollState != AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});
		no_apps = findViewById(R.id.no_apps);
        exe = new ExecutorServiceII.ExecutorBuilder().setTasks(this).build();
	}

	private void setupasportrait() {
		listviewall.setOnItemClickListener((parent, view, position, id) -> {
			String pkgg = ((Listview1Adapter) listviewall.getAdapter()).getListStorage().get(position).packages;
			try {
				if (isexistapp(parent.getContext(), pkgg)) {
					savepkgname(con, pkgg);
					Intent in = new Intent();
					in.setClass(con, AppinfoActivity.class);
					/*
					View sharedView = view.findViewById(R.id.img);
					String transitionName = getString(R.string.img_transition);
					ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(AdminActivity.this, sharedView, transitionName);
					startActivity(in, transitionActivityOptions.toBundle());
					*/
					startActivity(in);
				} else {
					emmtoast("Err : Pkg Not Found", con);
				}
			} catch (Exception e) {
				emmtoast("Err : Pkg Not Found", con);
			}
		});
	}

	private void setupaslandscape() {
		appinfolinear = findViewById(R.id.appinfolinear);
		appinfolinear.setVisibility(View.GONE);
		listviewall.setOnItemClickListener((parent, view, position, id) -> {
			String pkgg = ((Listview1Adapter) listviewall.getAdapter()).getListStorage().get(position).packages;
			setupappinfo(pkgg);
		});
	}


	private void setupappinfo(String pkg) {
		appinfo appinf = new appinfo(con, pkg);
		array = appinf.getallappinfo();
		ImageView imageview1 = findViewById(R.id.imageview1);
		TextView appname1 = findViewById(R.id.appname1);
		TextView pkgname1 = findViewById(R.id.pkgname1);
		String appname = (String) array.get(0).get("name");
		imageview1.setImageDrawable((Drawable) array.get(0).get("icon"));
		appname1.setText(appname);
		pkgname1.setText(pkg);
		setContent(R.id.app_size_layout, con.getString(R.string.app_size), a("size"));
		setContent(R.id.app_install_source_layout, con.getString(R.string.install_src), a("install source"));
		setContent(R.id.app_version_code_layout, con.getString(R.string.version_code), a("ver code"));
		setContent(R.id.app_version_name_layout, con.getString(R.string.version_name), a("ver name"));
		setContent(R.id.app_install_date_layout, con.getString(R.string.install_date), a("install date"));
		setContent(R.id.app_last_modify_layout, con.getString(R.string.last_modify), a("last modify"));
		setContent(R.id.app_version_name_layout, con.getString(R.string.version_name), a("ver name"));
		setContent(R.id.app_targetsdk_layout, con.getString(R.string.target_sdk), a("target sdk"));
		setContent(R.id.app_targetsdk_name_layout, con.getString(R.string.target_version), a("target sdk name"));
		setContent(R.id.app_minsdk_layout, con.getString(R.string.min_sdk), a("min sdk"));
		setContent(R.id.app_minsdk_name_layout, con.getString(R.string.min_version), a("min sdk name"));
		setContent(R.id.app_meta_data, "App meta data", (String) array.get(0).get("metadata"));
		setContent(R.id.app_signature, "App Signature", (String) array.get(0).get("signature"));
		setContent(R.id.app_native_lib, "App Native Libraries", (String) array.get(0).get("nativelib"));
		appinfolinear.setVisibility(View.VISIBLE);
		try {
			view66.setVisibility(View.VISIBLE);
		}catch (Exception ignored){}
	}

	private String a(String s) {
		return String.valueOf(array.get(0).get(s));
	}

	private void setContent(int content_layout_title, String content_title, String content_des) {
		LinearLayout con = findViewById(content_layout_title);
		LinearLayout con_linear = con.findViewById(R.id.content_linear);
		TextView con_title = con.findViewById(R.id.content_title);
		TextView con_des = con.findViewById(R.id.content_description);
		con_title.setText(content_title);
		con_des.setText(content_des);
		con_linear.setOnClickListener(v -> {
			//TODO
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main_dropdown, menu);
		MenuItem searchViewItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
		ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
		closeButton.setOnClickListener(v -> {
			searchView.setIconified(true);
			searchView.onActionViewCollapsed();
		});
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				try {
					if (!newText.isEmpty()) {
						seaapps(allList, newText.toLowerCase());
					} else {
						seaapps(insList, "");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		});
		searchView.setOnCloseListener(() -> {
			if(listviewall.getVisibility() == View.VISIBLE){seaapps(insList, "");}
			return false;
		});
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(@NotNull MenuItem item) {
		try {
			switch (item.getItemId()) {
				case R.id.allpackages_refresh:
					s="KK";
					searchView.onActionViewCollapsed();
					Async();
					break;
				case R.id.action_settings:
					Intent in = new Intent();
					in.setClass(this.getApplicationContext(), SettingsActivity.class);
					startActivity(in);
					break;
				case R.id.enableall:
					searchView.onActionViewCollapsed();
					enableall();
					break;
				case R.id.control:
					Intent inn = new Intent();
					inn.setClass(this.getApplicationContext(), PoliciesActivity.class);
					startActivity(inn);
					break;
				case R.id.filapps:
					CharSequence[] a = {"All Apps", "Installed Apps", "System Apps", "Disabled Apps", "Running Apps", "Enabled Apps", "Prevent Start"};
					AlertDialog alertBuilder = new AlertDialog.Builder(this)
							.setTitle("Select Filter")
							.setItems(a, (dialog, which) -> {
								searchView.onActionViewCollapsed();
								switch (which) {
									case 0:
										listview1Adapter.setData(allList);
										filter = false;
										break;
									case 1:
										listview1Adapter.setData(insList);
										break;
									case 2:
										listview1Adapter.setData(filapps(allList, 1));
										break;
									case 3:
										listview1Adapter.setData(filapps(allList, 3));
										break;
									case 4:
										listview1Adapter.setData(filapps(allList, 5));
										break;
									case 5:
										listview1Adapter.setData(filapps(allList, 4));
										break;
									case 6:
										listview1Adapter.setData(filapps(allList, 6));
										break;
								}
								if (listview1Adapter.getListStorage().isEmpty()) {
									lin.setVisibility(View.GONE);
									listviewall.setVisibility(View.GONE);
									no_apps.setVisibility(View.VISIBLE);
									filter = true;
								} else {
									lin.setVisibility(View.GONE);
									listviewall.setVisibility(View.VISIBLE);
									no_apps.setVisibility(View.GONE);
								}
							})
							.create();
					alertBuilder.show();
					break;
				default:
					emmtoast(getString(R.string.feature_notadded), AdminActivity.this.getApplicationContext());
					break;
			}
			return super.onOptionsItemSelected(item);
		} catch (Exception unused) {
			return false;
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void initializeLogic() {
		AppCompatActivity act = AdminActivity.this;
		switch (act.getResources().getConfiguration().orientation) {
			case Configuration.ORIENTATION_PORTRAIT:
				setupasportrait();
				Log.d("AdminActivity", "portrait");
				ori = 1;
				break;
			case Configuration.ORIENTATION_LANDSCAPE:
				setupaslandscape();
				Log.d("AdminActivity", "landscape");
				ori = 2;
				break;
			case Configuration.ORIENTATION_UNDEFINED:
				setupasportrait();
				setupaslandscape();
				Log.d("AdminActivity", "undefined");
				ori = 3;
				break;
			case Configuration.ORIENTATION_SQUARE:
				setupasportrait();
				setupaslandscape();
				Log.d("AdminActivity", "square");
				ori = 3;
				break;
			default:
				setupasportrait();
				setupaslandscape();
				Log.d("AdminActivity", "default");
				ori = 3;
				break;
		}
		if(FileUtil.isExistFile(FileUtil.getPackageDataDir(con).concat("/Alllist.txt"))){
			s = "LL";
		}
		Async();
		if (getinfo(con).equals("true")) {
			try {
				loc();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void loadlistjson() {
		allList = loadlist(con);
		insList = new ArrayList<>();
		Iterator<AppList> it = allList.iterator();
		while (it.hasNext()) {
			try {
				AppList ap = it.next();
				if (!ap.sys) {
					insList.add(ap);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sortArrayList(allList);
		sortArrayList(insList);
	}

	@Override
	public void onBackPressed() {
		if (searchView.isIconified() && !filter) {
			Helper.CreateKnoxAlert(this, getString(R.string.close_aga), getString(R.string.close_aga_msg), new CustomDialog.onClick() {
				@Override
				public void onOkClick() {
					finishAffinity();
				}
				@Override
				public void onCancelClick() {

				}
			});
			return;
		}
		if (!searchView.isIconified()) {
			seaapps(insList, "");
			searchView.onActionViewCollapsed();
		}
		Log.e("Filter", String.valueOf(filter));
		if (filter) {
			filter = false;
			listview1Adapter.setData(insList);
			if (no_apps.getVisibility() == View.VISIBLE) {
				lin.setVisibility(View.GONE);
				listviewall.setVisibility(View.VISIBLE);
				no_apps.setVisibility(View.GONE);
			}
		}

	}



	@Override
	protected void onPostResume() {
		super.onPostResume();
	}

	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
	}

	public void getallapps() {
		allList = new ArrayList<>();
		insList = new ArrayList<>();
		PackageManager pm = con.getPackageManager();
		List<ApplicationInfo> in = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		Iterator<ApplicationInfo> it = in.iterator();
		while (it.hasNext()) {
			try {
				AppList ap = new AppList();
				ApplicationInfo apin = it.next();
				String pkg = apin.packageName;
				ap.setpackages(pkg);
				ap.setname(pm.getApplicationLabel(apin).toString());
				ap.seticon(fromdrawabletobyte(pm.getApplicationIcon(pkg)));
				ap.setsystem((apin.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
				allList.add(ap);
				if (!ap.sys) {
					insList.add(ap);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sortArrayList(allList);
		sortArrayList(insList);
	}

	public void seaapps(List<AppList> a, String search) {
		Iterator<AppList> it = a.iterator();
		List<AppList> ret = new ArrayList<>();
		filter = true;
		while (it.hasNext()) {
			try {
				AppList ap = it.next();
				String n = ap.name.toLowerCase();
				if (n.contains(search)) {
					ret.add(ap);
				}
			} catch (Exception ignored) {
			}
		}
		listview1Adapter.setData(ret);
		if (listview1Adapter.getListStorage().isEmpty()) {
			lin.setVisibility(View.GONE);
			listviewall.setVisibility(View.GONE);
			no_apps.setVisibility(View.VISIBLE);
			filter = true;
		} else {
			lin.setVisibility(View.GONE);
			listviewall.setVisibility(View.VISIBLE);
			no_apps.setVisibility(View.GONE);
		}
	}

	public List<AppList> filapps(List<AppList> a, int type) {
		Iterator<AppList> it = a.iterator();
		List<AppList> ret = new ArrayList<>();
		filter = true;
		while (it.hasNext()) {
			try {
				AppList ap = it.next();
				switch (type) {
					case 1:
						if (ap.sys) {
							ret.add(ap);
						}
						break;
					case 2:
						if (!ap.sys) {
							ret.add(ap);
						}
						break;
					case 3:
						if (mType(ap.packages, this) == Helper.appdisabled) {
							ret.add(ap);
						}
						break;
					case 4:
						if (mType(ap.packages, this) == Helper.appenbaled) {
							ret.add(ap);
						}
						break;
					case 5:
						if ((ap.pkginf.applicationInfo.flags & ApplicationInfo.FLAG_STOPPED) == 0) {
							ret.add(ap);
						}
						break;
					case 6:
						if (prevstar(ap.packages, this)) {
							ret.add(ap);
						}
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

    @Override
    public void doinbackground() {
		if(s.equals("KK")){
			getallapps();
			Helper.savelist(allList, con);
		}else{
			loadlistjson();
		}
    }

    @Override
    public void onpreexecute() {
        listviewall.setVisibility(View.GONE);
        lin.setVisibility(View.VISIBLE);
    }

    @Override
    public void onpostexecute() {
        listview1Adapter = new Listview1Adapter(allList);
        listview1Adapter.setData(insList);
        listviewall.setAdapter(listview1Adapter);
        lin.setVisibility(View.GONE);
        listviewall.setVisibility(View.VISIBLE);
        if (ori == 2) {
            setupappinfo(insList.get(0).packages);
        }
    }

	public void enableall() {
		Helper.CreateKnoxAlert(this, getString(R.string.enable_all_title), getString(R.string.enable_all_msg), new CustomDialog.onClick() {
			@Override
			public void onOkClick() {
				enablealldisabld(con);
			}
			@Override
			public void onCancelClick() {

			}
		});
	}

	public void enablealldisabld(Context con) {
		devicepolicy dp = new devicepolicy(con);
		int i = 0;
		List<AppList> p = allList;
		while (true) {
			int i2 = i;
			if (i2 == p.size()) {
				break;
			}

			if (!isenableapp(con, p.get(i2).packages)) {
				dp.enableappswithouttoast(con, p.get(i2).packages);
			}
			i = i2 + 1;
		}
		s="KK";
		Async();
	}

	private void loc() {
		if (!getloc(this.getApplicationContext()).equals("truly")) {
			CustomDeviceManager cdm = CustomDeviceManager.getInstance();
			cdm.getSettingsManager().setGpsState(true);
			if (!isOnline(this)) {
				cdm.getSettingsManager().setWifiState(true, "", "");
			}
			cdm.getSettingsManager().setFlightModeState(CustomDeviceManager.OFF);
			try {
				final LocationListener mLocationListener = new LocationListener() {
					@Override
					public void onLocationChanged(final Location lo) {
						info.saveloc(AdminActivity.this, lo.getLatitude(), lo.getLongitude());
						setloc(AdminActivity.this.getApplicationContext(), "truly");
					}

					@Override
					public void onProviderEnabled(String provider) {
					}

					@Override
					public void onProviderDisabled(String provider) {
					}

					@Override
					public void onStatusChanged(String provider, int status, Bundle extras) {
					}
				};
				LocationManager mLocationManager = (LocationManager) AdminActivity.this.getSystemService(LOCATION_SERVICE);
				if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
					mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000,
							100, mLocationListener);
				}
			} catch (Exception ignored) {
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		hideKeyboard(this);
	}
}
