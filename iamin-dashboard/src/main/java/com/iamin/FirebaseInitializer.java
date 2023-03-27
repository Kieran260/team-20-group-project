package com.iamin;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;

public class FirebaseInitializer {

    public static void initialize() {
        try {
            FileInputStream serviceAccount = new FileInputStream("iamin-dashboard/src/main/resources/iamin-381803-firebase-adminsdk-avmmx-343d35756d.json");

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("iamin-381803.appspot.com")
                .build();
            FirebaseApp.initializeApp(options);
            
            Bucket bucket = StorageClient.getInstance().bucket();
            
        } catch (IOException e) {
            throw new RuntimeException("Error reading Firebase service account key", e);
        }
    }
}
