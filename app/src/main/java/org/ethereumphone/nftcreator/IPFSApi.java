package org.ethereumphone.nftcreator;

import android.os.StrictMode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;

public class IPFSApi {
    private final IPFS ipfs;1
    public IPFSApi() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ipfs = new IPFS("/dnsaddr/ipfs.best-practice.se/tcp/443/https");
        //ipfs = new IPFS("");
    }

    public String uploadImage(byte[] image) throws IOException {
        NamedStreamable.ByteArrayWrapper bytearray = new NamedStreamable.ByteArrayWrapper(image);
        MerkleNode response = ipfs.add(bytearray).get(0);
        System.out.println("Hash (base 58): " + response.hash.toBase58());
        return response.hash.toString();
    }

    public String uploadString (String data) throws IOException {
        NamedStreamable.ByteArrayWrapper bytearray = new NamedStreamable.ByteArrayWrapper(data.getBytes(StandardCharsets.UTF_8));
        MerkleNode response = ipfs.add(bytearray).get(0);
        System.out.println("Hash (base 58): " + response.hash.toBase58());
        return response.hash.toString();
    }
}
