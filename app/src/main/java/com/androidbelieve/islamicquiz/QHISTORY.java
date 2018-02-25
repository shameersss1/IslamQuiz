package com.androidbelieve.islamicquiz;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QHISTORY extends ActionBarActivity {

    String myJSON;
    private static final String TAG_RESULTS="result";
    private static final String TAG_NAME = "qno";
    private static final String TAG_ADD ="date";

    String sender,phone;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;

    JSONArray peoples = null;


    ArrayList<HashMap<String, String>> personList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qhistory);
        phone="1";



        setTitle("Question History");


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        list = (ListView) findViewById(R.id.listView);

        personList = new ArrayList<HashMap<String,String>>();
        login(phone);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */


        //mFragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                if(menuItem.getItemId() == R.id.home)
                {
                    Intent i = new Intent(QHISTORY.this,Post.class);
                    startActivity(i);
                }

                if (menuItem.getItemId() == R.id.notify) {
                    Intent i =new Intent(QHISTORY.this,temp.class);
                    startActivity(i);

                }

                if (menuItem.getItemId() == R.id.feedback) {
                    Intent i =new Intent(QHISTORY.this,FeedBack.class);
                    startActivity(i);
                }


                if (menuItem.getItemId() == R.id.contact) {
                    Intent i =new Intent(QHISTORY.this,ContactUs.class);
                    startActivity(i);
                }
                if(menuItem.getItemId() ==R.id.inbox)
                {
                    Intent i = new Intent(QHISTORY.this,Inbox.class);
                    startActivity(i);
                }

                if(menuItem.getItemId() == R.id.pro)
                {
                    Intent i = new Intent(QHISTORY.this,UserProfile.class);
                    startActivity(i);
                }

                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();


    }


    protected void showList()
    {
        try
        {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<peoples.length();i++)
            {
                JSONObject c = peoples.getJSONObject(i);

                String name = c.getString(TAG_NAME);
                String phone = c.getString(TAG_ADD);
                HashMap<String,String> persons = new HashMap<String,String>();

                persons.put(TAG_NAME,name);
                persons.put(TAG_ADD,phone);
                personList.add(persons);
            }
            ListAdapter adapter = new SimpleAdapter(QHISTORY.this, personList, R.layout.ll22,
                    new String[]{TAG_NAME,TAG_ADD},
                    new int[]{R.id.date, R.id.phone}
            );

            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String sel = list.getItemAtPosition(position).toString();

                    String temp[] = new String[10];
                    String delimiter = "\\{";
                    String temp1[] = new String[10];
                    String delimiter1 = "\\=";

                    temp = sel.split(delimiter);
                    temp1 = temp[0].split(delimiter1);

                    String temp2[] = new String[10];
                    String delimiter2 = "\\,";
                    String delimiter3 = "\\:";


                    String temp3[] = new String[5];
                    temp3 = sel.split(delimiter2);

                    String temp4[] = new String[5];
                    temp4 = temp3[1].split(delimiter3);
//
//
//                    String temp5[] = new String[5];
//                    temp5 = sel.split(delimiter2);
////
//
//                    String temp6[] = new String[5];
//                    temp6 = temp5[1].split(delimiter3);
//
//                    temp2 = temp1[1].split(delimiter2);

                    String e = temp4[1];
                    e = e.substring(0, e.length() - 1);

                    //Toast.makeText(getApplicationContext(),e,Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(QHISTORY.this,History.class);
                    i.putExtra("msg",e);
//                    i.putExtra("rec",temp6[1]);
////                    i.putExtra("sender",sender);
//
                    //Toast.makeText(getApplicationContext(),sel,Toast.LENGTH_LONG).show();
                    startActivity(i);

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void login(final String phone) {

        class LoginAsync extends AsyncTask<String, Void, String>{

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(QHISTORY.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String uname = params[0];



                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("phone", uname));


                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://islamquiz.in/Android/User/client_question.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){

                loadingDialog.dismiss();
                String s=result.trim();


                myJSON=s;
                showList();

            }
        }
        LoginAsync g = new LoginAsync();
        g.execute(phone);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
