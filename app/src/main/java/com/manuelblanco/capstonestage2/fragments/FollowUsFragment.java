package com.manuelblanco.capstonestage2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fenjuly.mylibrary.SpinnerLoader;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.base.BaseFragment;
import com.manuelblanco.capstonestage2.utils.URLUtils;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 8/08/16.
 */
public class FollowUsFragment extends BaseFragment {

    @BindView(R.id.webview)
    WebView mWebView;

    private SpinnerLoader mProgress;
    private View mView;

    public static FollowUsFragment newInstance() {
        FollowUsFragment fragment = new FollowUsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_follow_us, container, false);
        ButterKnife.bind(this, v);

        mProgress = (SpinnerLoader)getActivity().findViewById(R.id.progressbar);
        mView = (View)getActivity().findViewById(R.id.back_progress);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        try {
            setUrlOnWebView(URLUtils.getURLFacebook());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return v;
    }

    private void setUrlOnWebView(String url) {

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new AppWebViewClients());
        mWebView.loadUrl(url);
    }

    public class AppWebViewClients extends WebViewClient {

        public AppWebViewClients() {
            mProgress.setVisibility(View.VISIBLE);
            mView.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            mView.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
        }
    }

}
