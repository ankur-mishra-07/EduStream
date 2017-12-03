package com.pace.edustream.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pace.edustream.ModelData;
import com.pace.edustream.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import io.fabric.sdk.android.Fabric;


/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {

    private SharedPreferences appPref;
    private SharedPreferences.Editor prefEditor;
    private ModelData modelData;
    private Gson gson = new Gson();
    public String movie_path = null, key_value = "^sahdfhsfhvaffafhdkfdf;oisdkd298", path, kitkat_path;
    public String INTERNAL_STORAGE_PATH;
    public static String SDCARD_PATH = "", EDU_DATA = "";
    private boolean is_path = false, is_data = false, isFirstTime = true;
    private String KITKAT_IMAGE_NAME = "noteplay.png";


    static {
        System.loadLibrary("logicjni");
    }

    /**
     * Native method for encryption of key file.
     *
     * @param actual_file_path    the actual file path
     * @param encrypted_file_path the encrypted file path
     * @param key_path            the key path
     * @return the int
     */

    public native int jvEncryptFileWithKey(String actual_file_path, String encrypted_file_path, String key_path);

    /**
     * Native method for decryption of key file.
     *
     * @param encrypted_file_path the encrypted file path
     * @param decrypted_file_path the decrypted file path
     * @param key_path            the key path
     * @return the int
     */
    public native int jvDecryptFileWithKey(String encrypted_file_path, String decrypted_file_path, String key_path);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        appPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        INTERNAL_STORAGE_PATH = getFilesDir().getAbsolutePath();

        Log.i("path new ", "..........." + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));

        Log.i("path", "..............." + INTERNAL_STORAGE_PATH);

        //Check Android version to ask runtime permission
        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            if (isFirstTime)
                continueFlow();
            else
                searchforDRM();

        }

    }

    /**
     * Check if SD card path is found and if DRM folder is present.
     */
    public void searchforDRM() {
        if (!SDCARD_PATH.equals("")) {
            File f = new File(SDCARD_PATH + "/EDUSTREAM");
            if (f.exists() && f.isDirectory()) {
                startSplashScreen();
            } else {
                prefEditor = appPref.edit();
                prefEditor.putBoolean("Choosed", false);
                prefEditor.apply();

                Toast.makeText(this, R.string.no_drm, Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            prefEditor = appPref.edit();
            prefEditor.putBoolean("Choosed", false);
            prefEditor.apply();

            Toast.makeText(this, R.string.no_drm, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Edit the stream list file and store the key path and streams path. creates a local .m3u8 file
     *
     * @param chapterBean  the movie model
     * @param SD_list_path the sd list path
     * @param list_name    the list name
     * @param stream_path  the stream path
     * @param key_path     the key path
     */

    public void editVideoStreamFile(ModelData.SubjectBean.UnitsBean.ChapterBean chapterBean, String SD_list_path, String list_name, String stream_path, String key_path) {
        InputStream in = null;
        try {
            in = new FileInputStream(new File(SD_list_path));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line + "\n");
            }
            System.out.println(out.toString());
            String op = out.toString();

            FileOutputStream fOut = openFileOutput(list_name, Context.MODE_PRIVATE);
            String str_stream = op.replaceAll("here_stream", stream_path);
            String str_key = str_stream.replaceAll("here_key", key_path);
            fOut.write(str_key.getBytes());

            File outFile = getFileStreamPath(list_name);
            Uri paa = Uri.fromFile(outFile);
            movie_path = paa.toString();
            prefEditor = appPref.edit();
            prefEditor.putString("Chapter" + chapterBean.getChapter_key_name(), movie_path).apply();
            fOut.close();
            reader.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    /**
     * Continue flow
     */
    public void continueFlow() {
        initialValidations();
        getSdCardId();

        Type collectionType = new TypeToken<ModelData>() {
        }.getType();
        modelData = gson.fromJson(MainActivity.EDU_DATA, collectionType);

        if (is_path || is_data) {
            isFirstTime = appPref.getBoolean("isFirstTime", true);
            if (isFirstTime) {
                try {
                    Toast.makeText(this, R.string.loading, Toast.LENGTH_SHORT).show();
                    encryptFileUsingJNI();
                    decryptFileUsingJNI();

                    prefEditor = appPref.edit();
                    prefEditor.putBoolean("isFirstTime", false).apply();
                    startSplashScreen();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                startSplashScreen();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            switch (requestCode) {
                case 0:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        continueFlow();
                    } else {
                        Toast.makeText(this, R.string.permission, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
            }
        } catch (Exception e) {
        }
    }


    /**
     * Initial validations to check if SD card is available or not
     */
    public void initialValidations() {
        if (isLocationSD()) {
            if (!isExteranlStorageAvailable()) {
                Toast.makeText(this, R.string.no_sd_card, Toast.LENGTH_SHORT).show();
                prefEditor = appPref.edit();
                prefEditor.putBoolean("Choosed", false);
                prefEditor.apply();

                finish();
            } else {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                    if (isImagePresent()) {
                        path = kitkat_path;
                        SDCARD_PATH = path;
                        is_path = true;
                        searchDRMFolder();
                    } else {
                        prefEditor = appPref.edit();
                        prefEditor.putBoolean("Choosed", false);
                        prefEditor.apply();
                        Toast.makeText(this, R.string.no_sd_card, Toast.LENGTH_SHORT).show();
                        finish();
                    }


                } else
                    getThePath();
            }
        } else {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
            SDCARD_PATH = path;
            searchDRMFolder();

        }

    }

    private boolean isLocationSD() {
        return appPref.getString("DATA_LOCATION", "1").equals("1");
    }


    /**
     * Gets the path of SD card
     */
    public void getThePath() {
        File[] externalStorageFiles = ContextCompat.getExternalFilesDirs(this, null);
        for (File ex : externalStorageFiles) {
            if (ex != null) {
                path = getRootOfExternalStorage(ex);
                if (!path.equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                    SDCARD_PATH = path;
                    is_path = true;
                }
            }
        }

        if (is_path)
            searchDRMFolder();
        else {
            prefEditor = appPref.edit();
            prefEditor.putBoolean("Choosed", false);
            prefEditor.apply();

            Toast.makeText(this, R.string.no_sd_card, Toast.LENGTH_SHORT).show();
            finish();

        /*    path = Environment.getExternalStorageDirectory().getAbsolutePath();
            SDCARD_PATH = path;
            searchDRMFolder();*/
        }

    }


    /**
     * Search for DRM folder in the SD card
     */
    public void searchDRMFolder() {
        try {
            File f = new File(path);
            Log.i("ur", "...........sd path....." + path);
            File[] files = f.listFiles();
            for (File inFile : files) {
                if (inFile.isDirectory()) {
                    if (inFile.getName().equals("EDUSTREAM")) {
                        String content = null;
                        try {
                            Log.i("ur", "...........1......." + path + "/EDUSTREAM/edu_data");
                            EDU_DATA = deserializeString(new File(path + "/EDUSTREAM/edu_data"));
                            is_data = true;
                            return;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        System.out.println(content);
                    } else {
                        Log.i("ur", "...........here......");
                        if (!isLocationSD()) {
                            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                            SDCARD_PATH = path;
                            Log.i("ur", "...........2......" + path);
                            searchDRMFolder();
                        } else {
                            if (!path.contains("Download")) {
                                path = path + "/Download";
                                SDCARD_PATH = path;
                            }
                            Log.i("ur", "...........3......" + path);
                            searchDRMFolder();
                        }
                    }
                }
            }

        } catch (Exception ex) {
            Toast.makeText(this, R.string.enable_permission, Toast.LENGTH_SHORT).show();
            finish();
        }

        if (!is_data) {

            prefEditor = appPref.edit();
            prefEditor.putBoolean("Choosed", false);
            prefEditor.apply();

            Toast.makeText(this, R.string.no_drm, Toast.LENGTH_SHORT).show();
            finish();

        }
    }


    /**
     * Copy the contents of json file to string variable.
     *
     * @param file the file
     * @return the string
     * @throws IOException the io exception
     */
    public static String deserializeString(File file)
            throws IOException {
        int len;
        char[] chr = new char[4096];
        final StringBuffer buffer = new StringBuffer();
        final FileReader reader = new FileReader(file);
        try {
            while ((len = reader.read(chr)) > 0) {
                buffer.append(chr, 0, len);
            }
        } finally {
            reader.close();
        }
        return buffer.toString();
    }


    /**
     * Gets file size.
     *
     * @param filename the filename
     * @return the file size
     */
    public static long getFileSize(String filename) {
        File file = new File(filename);
        if (!file.exists() || !file.isFile()) {
            System.out.println("File doesn\'t exist");
            return -1;
        }
        return file.length();
    }


    /**
     * Gets the path of file if exists
     *
     * @param file_name the filename
     * @return the file size
     */
    private String getRootOfExternalStorage(File file_name) {
        if (file_name == null)
            return null;
        String path = file_name.getAbsolutePath();
        return path.replaceAll("/Android/data/" + getPackageName() + "/files", "");
    }


    /**
     * Starts the splash screen, moves to next screen
     */
    private void startSplashScreen() {
        Runnable lObjRunnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, SubjectsActivity.class));
                finish();
            }
        };
        Handler lObjHandler = new Handler();
        lObjHandler.postDelayed(lObjRunnable, 2000);
    }


    private boolean isExteranlStorageAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * Creates a .key and .m3u8 file in app's private storage.
     *
     * @param chapterBean the movie model
     * @param in_loc      the in loc
     * @param dest_loc    the dest loc
     * @param list_path   the list path
     * @throws Exception the exception
     */
    public void copyFileContents(ModelData.SubjectBean.UnitsBean.ChapterBean chapterBean, String in_loc, String dest_loc, String list_path) throws Exception {
        FileInputStream instream = null;
        FileOutputStream outstream;

        outstream = openFileOutput(dest_loc, MODE_PRIVATE);

        try {
            File infile = new File(in_loc);
            instream = new FileInputStream(infile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = instream.read(buffer)) > 0) {
                outstream.write(buffer, 0, length);
            }

            File outFile = getFileStreamPath(dest_loc);
            Uri key_path = Uri.fromFile(outFile);
            try {
                String stream_path = SDCARD_PATH + "/EDUSTREAM" + chapterBean.getChapter_path();

                Log.i("ur", ".....5........." + chapterBean + "\n" +
                        list_path + "\n" +
                        chapterBean.getChapter_key_name() + ".m3u8" + "\n" +
                        stream_path + "\n" +
                        key_path.toString());
                editVideoStreamFile(chapterBean,
                        list_path,
                        chapterBean.getChapter_key_name() + ".m3u8",
                        stream_path,
                        key_path.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Closing the input/output file streams
            instream.close();
            outstream.close();
            if (infile.delete()) {
                System.out.println(infile.getName() + " is deleted!");
            } else {
                System.out.println("Delete operation is failed.");
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    /**
     * Encrypt the key file and creates .enc file in app's private storage
     *
     * @throws Exception the exception
     */
    public void encryptFileUsingJNI() throws Exception {

        for (int i = 0; i < modelData.getSubject().size(); i++) {


            for (int j = 0; j < modelData.getSubject().get(i).getUnits().size(); j++) {

                for (int k = 0; k < modelData.getSubject().get(i).getUnits().get(j).getChapter().size(); k++) {

                    Log.i("ur", ".......2......." + SDCARD_PATH + "/EDUSTREAM" +
                            modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k).getChapter_path() + "/" +
                            "chapter_" + modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k).getChapter_id() + ".key" + "\n" +
                            INTERNAL_STORAGE_PATH + "/" + modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k).getChapter_key_name() + "_enc.enc");


                    jvEncryptFileWithKey(SDCARD_PATH + "/EDUSTREAM" +
                                    modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k).getChapter_path() + "/" +
                                    "chapter_" + modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k).getChapter_id() + ".key",
                            INTERNAL_STORAGE_PATH + "/" + modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k).getChapter_key_name() + "_enc.enc",
                            key_value);
                }

            }


        }
    }


    /**
     * Decrypt .enc file in app's private folder and creates a .key file in app's private storage
     *
     * @throws Exception the exception
     */

    public void decryptFileUsingJNI() throws Exception {

        for (int i = 0; i < modelData.getSubject().size(); i++) {

            for (int j = 0; j < modelData.getSubject().get(i).getUnits().size(); j++) {

                for (int k = 0; k < modelData.getSubject().get(i).getUnits().get(j).getChapter().size(); k++) {

                    Log.i("ur", ".......3......" + INTERNAL_STORAGE_PATH + "/" + modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k).getChapter_key_name() + "_enc.enc"
                            + "\n" +
                            INTERNAL_STORAGE_PATH + "/" + modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k).getChapter_key_name() + "_dec.key");


                    if (jvDecryptFileWithKey(INTERNAL_STORAGE_PATH + "/" + modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k).getChapter_key_name() + "_enc.enc",
                            INTERNAL_STORAGE_PATH + "/" + modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k).getChapter_key_name() + "_dec.key",
                            key_value) == 1) {


                        Log.i("ur", "......4......." + modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k) + "\n" +
                                INTERNAL_STORAGE_PATH + "/" + modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k).getChapter_key_name() + "_dec.key" + "\n" +

                                modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k).getChapter_key_name() + ".key" + "\n" +

                                SDCARD_PATH + "/EDUSTREAM" + modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k).getChapter_path() + "/" +
                                "chapter_" + modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k).getChapter_id() + ".m3u8");
                        copyFileContents(modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k),

                                INTERNAL_STORAGE_PATH + "/" + modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k).getChapter_key_name() + "_dec.key",

                                modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k).getChapter_key_name() + ".key",

                                SDCARD_PATH + "/EDUSTREAM" + modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k).getChapter_path() + "/" +
                                        "chapter_" + modelData.getSubject().get(i).getUnits().get(j).getChapter().get(k).getChapter_id() + ".m3u8");


                    }


                }
            }


        }
    }


    /**
     * Calls decrypt method for a particular file
     *
     * @param en_path the en path
     * @param de_path the de path
     * @param key     the key
     */

    public void callMeDecrypt(String en_path, String de_path, String key) {
        if (jvDecryptFileWithKey(en_path, de_path, key) == 1) {
            if (getFileSize(de_path) != 16) {
                callMeDecrypt(en_path, de_path, key);
            }
        }
    }


    /**
     * Get the SD card id if present
     */
    private void getSdCardId() {
        if (isExteranlStorageAvailable()) {
            try {
                File input = new File("/sys/class/mmc_host/mmc1");
                String cid_directory = null;
                int i = 0;
                File[] sid = input.listFiles();

                for (i = 0; i < sid.length; i++) {
                    if (sid[i].toString().contains("mmc1:")) {
                        cid_directory = sid[i].toString();
                        String SID = (String) sid[i].toString().subSequence(
                                cid_directory.length() - 4,
                                cid_directory.length());

                        prefEditor = appPref.edit();
                        prefEditor.putString("SDCARD_ID", SID).apply();
                        break;
                    }
                }

            } catch (Exception e) {
                Log.e("CID_APP", "Can not read SD-card cid");
            }

        } else {
            Toast.makeText(this, R.string.no_sd_card, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Search for image and get the path if present
     *
     * @return the boolean
     */
    public boolean isImagePresent() {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        int count = cursor.getCount();

        String[] arrPath = new String[count];

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            arrPath[i] = cursor.getString(dataColumnIndex);
            if (!arrPath[i].startsWith(INTERNAL_STORAGE_PATH)) {
                if (arrPath[i].endsWith(KITKAT_IMAGE_NAME)) {
                    kitkat_path = arrPath[i].replace("/EDUSTREAM/edustream.png", "");
                    return true;
                }
            }
        }

        return false;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
