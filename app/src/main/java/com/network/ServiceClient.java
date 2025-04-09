package com.network;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.utilities.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ServiceClient {
    private static ServiceClient instance;

	/*
     * server base url for testing purpose
	 */

    public static final String SERVER_BASE_URL = "http://vroomcar.elasticbeanstalk.com/rest/";

    private RestAdapter mRestAdapter;
    private Map<String, Object> mClients = new HashMap<String, Object>();

    private String mBaseUrl = SERVER_BASE_URL;

    private ServiceClient() {
    }

    public static ServiceClient getInstance() {
        if (null == instance) {
            instance = new ServiceClient();
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> T getClient(Context context, Class<T> clazz) {

        if (mRestAdapter == null) {

            mRestAdapter = new RestAdapter.Builder().setEndpoint(SERVER_BASE_URL).setClient(new OkClient(configureClient(new OkHttpClient()))).setLogLevel(RestAdapter.LogLevel.FULL).setLog(new RestAdapter.Log() {

                @Override
                public void log(String arg0) {
                    Logger.logger("** ServiceClient ** ", arg0);
                    Log.i("", "Inside Resule ### " + arg0);
                }
            })

                    .build();

        }
        T client = null;
        if ((client = (T) mClients.get(clazz.getCanonicalName())) != null) {
            return client;
        }
        client = mRestAdapter.create(clazz);
        mClients.put(clazz.getCanonicalName(), client);

        return client;
    }

    public void setRestAdapter(RestAdapter restAdapter) {
        mRestAdapter = restAdapter;
    }

    public String getBaseUrl(Context context) {
        return mBaseUrl;
    }


    public static OkHttpClient configureClient(final OkHttpClient client) {
        final TrustManager[] certs = new TrustManager[]{new X509TrustManager() {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
            }
        }};

        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, certs, new SecureRandom());
        } catch (final java.security.GeneralSecurityException ex) {
        }

        try {
            final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(final String hostname,
                                      final SSLSession session) {
                    return true;
                }
            };
            client.setHostnameVerifier(hostnameVerifier);
            client.setSslSocketFactory(ctx.getSocketFactory());
        } catch (final Exception e) {
        }

        return client;
    }

    public static OkHttpClient trustcert(Context context) {
        final OkHttpClient client = new OkHttpClient();
        return configureClient(client);
    }

}

