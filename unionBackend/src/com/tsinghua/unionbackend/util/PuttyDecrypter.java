package com.tsinghua.unionbackend.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class PuttyDecrypter {

	public BigInteger readInt(DataInputStream dis) throws IOException {
		int leng = dis.readInt();
		byte[] tmpBytes = new byte[leng];
		dis.readFully(tmpBytes);
		return new BigInteger(1, tmpBytes);
	}

	String pub64, priv64;

	public PuttyDecrypter(String pub64, String priv64) {
		this.pub64 = pub64;
		this.priv64 = priv64;
	}

	public String work(String s) throws UnionException {
		try {
			Cipher rsa = Cipher.getInstance("RSA");
			byte[] pubByte = Base64.getDecoder().decode(pub64);
			byte[] privByte = Base64.getDecoder().decode(priv64);
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
					pubByte));
			int leng = dis.readInt();

			byte[] tmpBytes = new byte[leng];
			dis.readFully(tmpBytes);
			// String keyAlgo = new String(tmpBytes);
			BigInteger publicExponent = readInt(dis);
			BigInteger modulus = readInt(dis);
			dis.close();

			dis = new DataInputStream(new ByteArrayInputStream(privByte));
			BigInteger privateExponent = readInt(dis);
			BigInteger primeP = readInt(dis);
			BigInteger primeQ = readInt(dis);
			BigInteger iqmp = readInt(dis);
			BigInteger primeExponentP = privateExponent.mod(primeP
					.subtract(BigInteger.ONE));
			BigInteger primeExponentQ = privateExponent.mod(primeQ
					.subtract(BigInteger.ONE));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			/*
			 * RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus,
			 * publicExponent); PublicKey publicKey =
			 * keyFactory.generatePublic(rsaPublicKeySpec);
			 */
			RSAPrivateCrtKeySpec rsaPrivateKeySpec = new RSAPrivateCrtKeySpec(
					modulus, publicExponent, privateExponent, primeP, primeQ,
					primeExponentP, primeExponentQ, iqmp);
			PrivateKey privateKey = keyFactory
					.generatePrivate(rsaPrivateKeySpec);

			rsa.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] buf = s.getBytes();
			byte[] res = rsa.doFinal(buf);
			return new String(res);
		} catch (Exception e) {
			throw new UnionException(e);
		}
	}
}
