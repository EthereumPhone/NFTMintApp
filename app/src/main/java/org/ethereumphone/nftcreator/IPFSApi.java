package org.ethereumphone.nftcreator;

import android.os.StrictMode;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
public class IPFSApi {
    public IPFSApi() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //ipfs = new IPFS("");
    }

    public String uploadFile(File file) throws IOException, JSONException {
        String comm = "curl -X POST -F file=@"+file.getAbsolutePath()+" -u \"2EPNnJ65ujBbliM2LiB9hkfDvk1:c552726a5ca40cce21831316a9460189\" \"https://ipfs.infura.io:5001/api/v0/add\"";
        String out = executeCommand(comm);
        JSONObject jsonObject = new JSONObject(out);
        return jsonObject.getString("Hash");
    }


    public String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            Process proc = Runtime.getRuntime().exec(new String[] { "sh", "-c", command });
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line;
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }
            proc.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }
}

