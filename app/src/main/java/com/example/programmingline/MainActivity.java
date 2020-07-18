package com.example.programmingline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private WebSettings webSettings;
    private ProgressBar progressBar;
    private String baseUrl;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView=findViewById(R.id.webView);
        progressBar=findViewById(R.id.progressBarH);
        imageView=findViewById(R.id.imageView);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        FirebaseInstanceId.getInstance().getInstanceId();

        baseUrl=getResources().getString(R.string.main_url);

        webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webView.loadUrl(baseUrl);
        webView.setVerticalScrollBarEnabled(true);

        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url1) {

                if(url1.startsWith(baseUrl))
                {
                    return false;
                }
                else
                {
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url1));
                    startActivity(intent);
                    return true;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                if(url.startsWith(baseUrl)) {
                    progressBar.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                if(webView.getVisibility()==View.GONE) {
                    webView.setVisibility(View.VISIBLE);
                }
                if(imageView.getVisibility() == View.VISIBLE)
                {
                    imageView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Toast.makeText(MainActivity.this,error.getDescription(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                Toast.makeText(MainActivity.this,errorResponse.getReasonPhrase(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(view.getUrl().startsWith(baseUrl)) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });




    }

    @Override
    public void onBackPressed() {
        if(webView!=null && webView.canGoBack())
        {
            webView.goBack();
        }
        else {
            super.onBackPressed();
        }
    }
}