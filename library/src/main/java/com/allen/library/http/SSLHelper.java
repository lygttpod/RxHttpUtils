package com.allen.library.http;


import com.allen.library.base.BaseRxHttpApplication;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


/**
 * @author allen
 * @date 2017/6/22
 */

public class SSLHelper {
    public static String CERTIFICATE;//自己的ssl文件your_SSL.cer
    private final static String PROTOCOL_TYPE = "TLS";
    private final static String CERTIFICATE_STANDARD = "X.509";

    public static class SSLParams {
        public SSLSocketFactory sSLSocketFactory;
        public X509TrustManager trustManager;
    }

    public static SSLParams getSSLParams(String certificateName) {
        CERTIFICATE = certificateName;
        SSLParams sslParams = new SSLParams();
        try {
            CertificateFactory cf = CertificateFactory.getInstance(CERTIFICATE_STANDARD);
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream caInput = BaseRxHttpApplication.getContext().getAssets().open(CERTIFICATE);
            Certificate ca = cf.generateCertificate(caInput);
            caInput.close();
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
            TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());
            SSLContext sslContext = SSLContext.getInstance(PROTOCOL_TYPE);
            sslContext.init(null, wrappedTrustManagers, null);
            sslParams.sSLSocketFactory = sslContext.getSocketFactory();
            sslParams.trustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sslParams;
    }

    public static TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                originalTrustManager.checkClientTrusted(x509Certificates, s);
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                originalTrustManager.checkServerTrusted(x509Certificates, s);
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return originalTrustManager.getAcceptedIssuers();
            }
        }
        };
    }
}
