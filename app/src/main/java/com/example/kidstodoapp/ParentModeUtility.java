package com.example.kidstodoapp;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.codec.binary.Hex;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Observable;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class ParentModeUtility extends Observable {

    private static ParentModeUtility INSTANCE = null;

    private static final String PASSWORD_HASH_FILE_NAME = "pwd-hash";
    private static String PARENT_DEVICE_FILE_NAME = "parent-device";

    private Context applicationContext;
    private String passwordHash;
    private Boolean parentDevice = false;
    private Boolean inParentMode = false;

    private Handler timeOutHandler;
    private Runnable timeOutRunnable;

    private ParentModeUtility() {}

    public static ParentModeUtility getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ParentModeUtility();
        }
        return INSTANCE;
    }

    public void initializeTimeout() {
        if (!parentDevice) {
            timeOutHandler = new Handler();
            timeOutRunnable = new Runnable() {
                @Override
                public void run() {
                    if (inParentMode) {
                        Toast.makeText(applicationContext,
                                "Exiting parent mode due to inactivity",
                                Toast.LENGTH_SHORT).show();
                        setInParentMode(false);
                    }
                }
            };
            timeOutHandler.postDelayed(timeOutRunnable, 60000);
        }
    }

    public void resetTimeout() {
        if (!parentDevice && inParentMode && timeOutHandler != null && timeOutRunnable != null) {
            timeOutHandler.removeCallbacks(timeOutRunnable);
            timeOutHandler.postDelayed(timeOutRunnable, 60000);
        }
    }

    public void setApplicationContext(Context context) {
        applicationContext = context;
    }

    public boolean retrievePasswordHash() {
        try {
            FileInputStream fis = applicationContext.openFileInput(PASSWORD_HASH_FILE_NAME);
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            passwordHash = reader.readLine();
            return true;
        } catch (Exception e) {
            Log.e("Utility", Objects.requireNonNull(e.getMessage()));
            return false;
        }
    }
    public void setPassword(String password) {
        passwordHash = hashPassword(password);
        storePasswordHash();
    }
    public boolean isCorrectPassword(String password) {
        return hashPassword(password).equals(this.passwordHash);
    }

    public Boolean isInParentMode() {return inParentMode;}
    public void setInParentMode(Boolean inParentMode) {
        this.inParentMode = inParentMode;
        if (inParentMode) {
            resetTimeout();
        }
        setChanged();
        notifyObservers();
    }

    private static String hashPassword(String password) {
        try {
            String salt = "1234";
            int iterations = 10000;
            int keyLength = 512;
            byte[] saltBytes = salt.getBytes();
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, keyLength);
            SecretKey key = skf.generateSecret(spec);
            return Hex.encodeHexString(key.getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
    private void storePasswordHash() {
        try (FileOutputStream fos = applicationContext.openFileOutput(PASSWORD_HASH_FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(passwordHash.getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception e) {
            Log.e("Utility", Objects.requireNonNull(e.getMessage()));
        }
    }

    public void checkDeviceType() {
        List<String> files = Arrays.asList(applicationContext.fileList());
        parentDevice = files.contains(PARENT_DEVICE_FILE_NAME);
        inParentMode = parentDevice;
    }

    public void setParentDevice() {
        parentDevice = true;
        inParentMode = true;
        try (FileOutputStream fos = applicationContext.openFileOutput(PARENT_DEVICE_FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write("Parent device".getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception e) {
            Log.e("Utility", Objects.requireNonNull(e.getMessage()));
        }
    }

    public static void executeLogout() {
        List<String> files = Arrays.asList(INSTANCE.applicationContext.fileList());
        if (files.contains(PARENT_DEVICE_FILE_NAME)) {
            boolean bool = INSTANCE.applicationContext.deleteFile(PARENT_DEVICE_FILE_NAME);
            if (bool){Log.d("Utility","Parent device file deleted");}
        }
        if (files.contains(PASSWORD_HASH_FILE_NAME)) {
            boolean bool = INSTANCE.applicationContext.deleteFile(PASSWORD_HASH_FILE_NAME);
            if (bool){Log.d("Utility","Password hash file deleted");}
        }
        INSTANCE = null;
    }
}
