package org.ethereumphone.nftcreator;

import static io.ipfs.kotlin.defaults.DefaultsKt.createMoshi;
import static io.ipfs.kotlin.defaults.DefaultsKt.createOKHTTP;

import android.os.StrictMode;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import io.ipfs.kotlin.IPFS;
import io.ipfs.kotlin.IPFSConfiguration;
import io.ipfs.kotlin.defaults.InfuraIPFS;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IPFSApi {
    IPFS ipfs;
    public IPFSApi() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ipfs = new IPFS(new IPFSConfiguration("https://ipfs.infura.io:5001/api/v0/", new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Authorization", "Basic "+ Base64.getEncoder().encodeToString("2EPNnJ65ujBbliM2LiB9hkfDvk1:c552726a5ca40cce21831316a9460189".getBytes()))
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        }).build(), new Moshi.Builder().add(new KotlinJsonAdapterFactory()).build()));
    }

    public String uploadFile(File file)  {
        return ipfs.getAdd().file(file, file.getName(), file.getName()).getHash();
    }


}

