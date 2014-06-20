package com.musicapp.urmusic;
///**
// * Copyright 2010-present Facebook.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *    http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.android.musicplayer;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.android.musicplayer.CommentsFragment.OnHeadlineSelectedListener;
//import com.facebook.LoggingBehavior;
//import com.facebook.Session;
//import com.facebook.SessionState;
//import com.facebook.Settings;
//import com.facebook.android.AsyncFacebookRunner;
//import com.facebook.android.AsyncFacebookRunner.RequestListener;
//import com.facebook.android.Facebook;
//
//public class SessionLoginFragment extends Fragment {
//    private static final String URL_PREFIX_FRIENDS = "https://graph.facebook.com/me/friends?access_token=";
//
//    private TextView textInstructionsOrLink;
//    private Button buttonLoginLogout;
//    private Session.StatusCallback statusCallback = new SessionStatusCallback();
//    OnHeadlineSelectedListener mCallback;
//    long playlistId;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.login_fragment, container, false);
//
//        buttonLoginLogout = (Button) view.findViewById(R.id.buttonLoginLogout);
//        textInstructionsOrLink = (TextView) view.findViewById(R.id.instructionsOrLink);
//        
//        playlistId = Long.parseLong(getArguments().getString("playlistId")); 
//
//        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
//
//        Session session = Session.getActiveSession();
//        if (session == null) {
//            if (savedInstanceState != null) {
//                session = Session.restoreSession(getActivity(), null, statusCallback, savedInstanceState);
//            }
//            if (session == null) {
//                session = new Session(getActivity());
//            }
//            Session.setActiveSession(session);
//            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
//                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
//            }
//        }
//
//        updateView();
//
//        return view;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        Session.getActiveSession().addCallback(statusCallback);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        Session.getActiveSession().removeCallback(statusCallback);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode, data);
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Session session = Session.getActiveSession();
//        Session.saveSession(session, outState);
//    }
//
//    private void updateView() {
//        Session session = Session.getActiveSession();
//        if (session.isOpened()) {
//        	mCallback.onArticleSelected(26, "" + playlistId);
//            //textInstructionsOrLink.setText(URL_PREFIX_FRIENDS + session.getAccessToken());
//            
////            Facebook fb = new Facebook("");
////            
////            AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(fb);
////            String query="SELECT uid, name, pic_square FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1 = me())";
////                        Bundle bundal=new Bundle();
////                        bundal.putString("method", "fql.query");
////                        bundal.putString("query", query);
////                        return bundal;
////                mAsyncRunner.request(bundal, new RequestListener() {
////                                @Override
////                                public void onComplete(final String response, Object state) {
////                                    // TODO Auto-generated method stub
////                                    //parse json and get all friends        
////                                }
////                            });
//            //buttonLoginLogout.setText("Logout");
//            //buttonLoginLogout.setOnClickListener(new View.OnClickListener() {
//            //    public void onClick(View view) { onClickLogout(); }
//            //});
//        } else {
//            textInstructionsOrLink.setText("Login with Facebook");
//            buttonLoginLogout.setText("Login");
//            buttonLoginLogout.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) { onClickLogin(); }
//            });
//        }
//    }
//
//    private void onClickLogin() {
//        Session session = Session.getActiveSession();
//        if (!session.isOpened() && !session.isClosed()) {
//            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
//        } else {
//            Session.openActiveSession(getActivity(), this, true, statusCallback);
//        }
//    }
//
//    private void onClickLogout() {
//        Session session = Session.getActiveSession();
//        if (!session.isClosed()) {
//            session.closeAndClearTokenInformation();
//        }
//    }
//
//    private class SessionStatusCallback implements Session.StatusCallback {
//        @Override
//        public void call(Session session, SessionState state, Exception exception) {
//            updateView();
//        }
//    }
//    
// // Container Activity must implement this interface
//    public interface OnHeadlineSelectedListener {
//        public void onArticleSelected(int position, String str);
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        
//        // This makes sure that the container activity has implemented
//        // the callback interface. If not, it throws an exception
//        try {
//            mCallback = (OnHeadlineSelectedListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnHeadlineSelectedListener");
//        }
//    }
//}