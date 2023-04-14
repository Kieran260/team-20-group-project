package com.iamin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.google.auth.ServiceAccountSigner;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.google.auth.oauth2.GoogleCredentials;
// import com.google.auth.oauth2.ServiceAccountSigner;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.google.cloud.storage.Bucket;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class FirebaseInitializer {

    static {
        initialize();
    }

    public static void initialize() {
        boolean appExists = FirebaseApp.getApps().stream()
                .anyMatch(app -> app.getName().equals(FirebaseApp.DEFAULT_APP_NAME));
    
        if (!appExists) {
            try {
                InputStream serviceAccount = FirebaseInitializer.class.getClassLoader().getResourceAsStream("iamin-381803-138505f81084.json");
    
                if (serviceAccount == null) {
                    throw new FileNotFoundException("iamin-381803-firebase-adminsdk-avmmx-343d35756d.json not found in the classpath");
                }
    
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
    

    public ServiceAccountSigner getServiceAccountSigner() {
        GoogleCredentials credentials;
        try {
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("iamin-381803-138505f81084.json");
            credentials = GoogleCredentials.fromStream(serviceAccount);
        } catch (IOException e) {
            throw new RuntimeException("Error reading Firebase service account key", e);
        }

        if (credentials instanceof ServiceAccountSigner) {
            return (ServiceAccountSigner) credentials;
        }
        throw new IllegalStateException("GoogleCredentials is not an instance of ServiceAccountSigner");
    }
}
