package com.example;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.List;


public class MyClass {

    public static void main(String[] args) {

       // String strNmae = "/Users/zk/Documents/andriod/Development/workspace/ndk练习/app/build/outputs/apk/app-debug.apk";


    }





    public static void jiagu(String strNmae){
     String path = strNmae.substring(0, strNmae.length() - 4);
        String[] split = strNmae.split("/");
        String appname = split[split.length - 1];
          System.out.println("开始加固");
        界面.TextViewSHow("开始加固\n");
        CMDUtil.getDir(strNmae);
        CMDUtil.runCMD("apktool d -f -s  " + strNmae);
        System.out.println("反编译结束");


        writeManifest(path);
        CMDUtil.getDir(strNmae);
        CMDUtil.runCMD("apktool b   " + path+" -o one_"+appname);

        CMDUtil.runCMD("rm -rf  "  +appname.substring(0,appname.length()-4));

        CMDUtil.runCMD("apktool d -f -s -r " + "one_"+appname+" -o "+appname.substring(0,appname.length()-4));
        CMDUtil.runCMD("rm -rf  "  +"one_"+appname);

        CMDUtil.setDir(path);
        // CMDUtil.runCMD("mkdir assets");
        //CMDUtil.runCMD("cp classes.dex assets/");
        //CMDUtil.runCMD("cp -r /Users/zk/Desktop/netive_dex/smali " + path);


        ZipCompressor zc = new ZipCompressor(path+"/res.zip");
        zc.compress(path+"/assets",path+"/resources.arsc",path+"/res");

        CMDUtil.runCMD("rm -rf  " + path+"/res");
        CMDUtil.runCMD("rm -rf  " + path+"/assets");




        DexUtil.DexWith("force/ForceApkObj.dex",path+"/res.zip",path+"/classes1.dex");

        DexUtil.DexWith(path+"/classes1.dex",path+"/classes.dex",path+"/classes.dex");
        CMDUtil.runCMD("cp -r /Users/zk/Desktop/netive_dex/lib " + path);
        CMDUtil.runCMD("rm -rf  " + path+"/res.zip");
        CMDUtil.runCMD("rm -rf  " + path+"/classes1.dex");
        CMDUtil.getDir(strNmae);
        CMDUtil.runCMD("apktool b   " + path);


        String signs = "jarsigner -verbose -keystore /Users/zk/Desktop/kai.keystore -storepass 111111 -keypass 111111 -signedjar  " + path + "signed.apk " + path + "/dist/"+"one_"+appname + " kai.keystore -digestalg SHA1 -sigalg MD5withRSA";
        CMDUtil.runCMD(signs);
        CMDUtil.runCMD("rm -rf "+path);
        界面.TextViewSHow("\n加固完成");
        界面.TextViewSHow("\n加固完成");
        界面.TextViewSHow("\n加固完成");
    }


    public static void writeManifest(String file) {
        file = file + "/" + "AndroidManifest.xml";
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(new File(file));

            Element root = document.getRootElement();

            Element application = root.element("application");

            String name = application.attributeValue("name");
            if (name == null) {
                application.addAttribute("android:name", "com.example.nativedex.MyAppLication");




            }else {
                Element  application3 = root.element("application");
                List<Element> elements = application3.elements("meta-data");
                for (int i = 0; i < elements.size(); i++) {
                    if (elements.get(i).attributeValue("name").equals("APPLICATION_CLASS_NAME")){
                        return;
                    }
                }


                Element element = application3.addElement("meta-data");
                element.addAttribute("android:name", "APPLICATION_CLASS_NAME");
                element.addAttribute("android:value", name);

                //1.2 得到id属性对象
                Attribute idAttr=application.attribute("name");
                //1.3 修改属性值
                idAttr.setValue("com.example.nativedex.MyAppLication");

            }

            XMLWriter writer = new XMLWriter(new FileWriter(new File(file)));


            writer.write(document);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
