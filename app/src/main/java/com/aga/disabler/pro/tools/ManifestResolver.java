package com.aga.disabler.pro.tools;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class ManifestResolver {
    
public String resolvemanifestfromapk(String pkg, Context con) {
String manifest = readmanifest(con, pkg);
    return formatemanifest(manifest);
}
              
public String readmanifest(Context con, String pkg) {
    StringBuilder sB = new StringBuilder();
    try{
    try{
        PackageManager pm = con.getPackageManager();
        Resources r8 = pm.getResourcesForApplication(pkg);
String r10 = "AndroidManifest.xml";
android.content.res.XmlResourceParser parser = r8.getAssets().openXmlResourceParser(r10);
int eventType = parser.next();

while (eventType != XmlResourceParser.END_DOCUMENT) {
     if (eventType == XmlResourceParser.START_TAG) {
       sB.append("<").append(parser.getName());
for(int ii = 0; ii < parser.getAttributeCount(); ii++) {
     String attributeName = parser.getAttributeName(ii);
     String attributeValue = getAttributeValue(attributeName, parser.getAttributeValue(ii), r8);
sB.append(" ").append(attributeName).append("=\"").append(attributeValue).append("\"");
}
sB.append(">");
if (parser.getText() != null) {
    sB.append(parser.getText());
}
} else
 if (eventType == XmlResourceParser.END_TAG) {
 sB.append("</").append(parser.getName()).append(">");
 }
 eventType = parser.next();
 }
 } catch (PackageManager.NameNotFoundException ignored) {}
} catch (Exception e) {e.printStackTrace();}
return sB.toString();
    }  
          
public String formatemanifest(String str) {
        try {
            Transformer newTransformer = TransformerFactory.newInstance().newTransformer();
            newTransformer.setOutputProperty("indent", "yes");
            newTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamResult streamResult = new StreamResult(new StringWriter());
            Charset forName = StandardCharsets.UTF_8;
            if (str != null) {
                byte[] bytes = str.getBytes(forName);
                newTransformer.transform(new StreamSource(new ByteArrayInputStream(bytes)), streamResult);
                return streamResult.getWriter().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }
    
public String getAttributeValue(String attributeName, String attributeValue, Resources resources) {    
     if (attributeValue.startsWith("@")) {
            try {
                String r0 = attributeValue.substring(1);
                int id = Integer.parseInt(r0);
                String value;
    if (attributeName.equals("theme") || attributeName.equals("resource")) {
       value = resources.getResourceEntryName(id); 
    } else {
       value = resources.getString(id);
    }
    return TextUtils.htmlEncode(value);
} catch (Exception e) {e.printStackTrace();}
}
return attributeValue;
        }
        
}