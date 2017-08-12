package com.tsinghua.unionbackend.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class PemDecrypter {

	public void genKey(String nam, String workDir) {
		try {
			File workFile = new File(workDir);
			Runtime.getRuntime()
					.exec("openssl genrsa -out login.pem 2048".replace("login",
							nam), null, workFile).waitFor();
			Runtime.getRuntime()
					.exec("openssl rsa -in login.pem -outform PEM -pubout -out login_pub.pem"
							.replace("login", nam), null, workFile).waitFor();
			Runtime.getRuntime()
					.exec("openssl rsa -inform PEM -in login.pem -outform DER -pubout -out login_pub.der"
							.replace("login", nam), null, workFile).waitFor();
			Runtime.getRuntime()
					.exec("openssl pkcs8 -topk8 -inform PEM -in login.pem -outform DER -nocrypt -out login.der"
							.replace("login", nam), null, workFile).waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String decrypt(String fil, String path, String secret) {
		String ret = "";
		try {
			File pubKeyFile = new File(path + fil + "_pub.der");
			File privKeyFile = new File(path + fil + ".der");

			// read public key DER file
			DataInputStream dis = new DataInputStream(new FileInputStream(
					pubKeyFile));
			byte[] pubKeyBytes = new byte[(int) pubKeyFile.length()];
			dis.readFully(pubKeyBytes);
			dis.close();

			// read private key DER file
			dis = new DataInputStream(new FileInputStream(privKeyFile));
			byte[] privKeyBytes = new byte[(int) privKeyFile.length()];
			dis.read(privKeyBytes);
			dis.close();

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			/*
			 * // decode public key X509EncodedKeySpec pubSpec = new
			 * X509EncodedKeySpec(pubKeyBytes); RSAPublicKey pubKey =
			 * (RSAPublicKey) keyFactory.generatePublic(pubSpec);
			 */
			// decode private key
			PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privKeyBytes);
			RSAPrivateKey privKey = (RSAPrivateKey) keyFactory
					.generatePrivate(privSpec);
			Cipher rsa = Cipher.getInstance("RSA");
			rsa.init(Cipher.DECRYPT_MODE, privKey);
			byte[] msg = Base64.getDecoder().decode(secret);
			ret = new String(rsa.doFinal(msg));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static void main(String[] args) {

	}
}
