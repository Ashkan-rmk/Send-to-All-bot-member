package com.example.smn.paypingbotbroadcast;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;

import java.io.IOException;
import java.util.ArrayList;

public class ScrollingActivity extends AppCompatActivity {

    private ProgressDialog progressBar;
    private Handler handler;

    final Boolean[] pic = {false};
    final Boolean[] md = {false};

    Boolean test = false;

    final ArrayList<String> chatIds= new ArrayList<>();
    final ArrayList<String> testChatIds= new ArrayList<>();
    final ArrayList<String> notSendIds= new ArrayList<>();

    String BOT_TOKEN = "518881868:111111111111111111111111-H2XJBzZtlxY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TelegramBot bot = new TelegramBot(BOT_TOKEN);

        testChatIds.add("109416039"); //me
        testChatIds.add("90210964"); //smn
        testChatIds.add("152445158"); //nima
        testChatIds.add("55622476"); //saeed
        testChatIds.add("56038909"); //baride
        testChatIds.add("84989023"); //masood
        testChatIds.add("107230941"); //amir

        Button btn_test = (Button) findViewById(R.id.btn_test);
        final EditText ed_msg = (EditText) findViewById(R.id.ed_message);
        final EditText ed_url = (EditText) findViewById(R.id.ed_url);
        Switch pic_swith = (Switch) findViewById(R.id.pic_switch);
        Switch md_swith = (Switch) findViewById(R.id.md_switch);
        ed_url.setEnabled(false);

        pic_swith.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    ed_url.setEnabled(true);
                    pic[0] = true;
                }
                else {
                    ed_url.setEnabled(false);
                    pic[0] = false;
                }
            }
        });

        md_swith.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) md[0] = true;
                else md[0] = false;
            }
        });


        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                test = true;

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScrollingActivity.this);
                alertDialogBuilder.setMessage("Are you sure to start broadcasting?!");
                        alertDialogBuilder.setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        progressBar.show();
                                        notSendIds.clear();
                                        final Boolean[] cancel = {false};
                                        new Thread(new Runnable() {
                                            public void run() {

                                                for (final String chat_id : testChatIds) {

                                                    if (!pic[0] && md[0]) {
                                                        SendMessage request = new SendMessage(chat_id, ed_msg.getText().toString())
                                                                .parseMode(ParseMode.Markdown);

                                                        bot.execute(request, new Callback<SendMessage, SendResponse>() {
                                                            @Override
                                                            public void onResponse(SendMessage request, SendResponse response) {
                                                                if (!response.isOk()) notSendIds.add(chat_id);
                                                            }
                                                            @Override
                                                            public void onFailure(SendMessage request, IOException e) {
                                                                notSendIds.add(chat_id);
                                                            }
                                                        });
                                                    }
                                                    if (pic[0] && !md[0]) {
                                                        if (TextUtils.isEmpty(ed_url.getText().toString())) {
                                                            cancel[0] = true;
                                                            progressBar.dismiss();

                                                            break;
                                                        }
                                                        else {
                                                            SendPhoto request = new SendPhoto(chat_id, ed_url.getText().toString())
                                                                    .caption(ed_msg.getText().toString());

                                                            bot.execute(request, new Callback<SendPhoto, SendResponse>() {
                                                                @Override
                                                                public void onResponse(SendPhoto request, SendResponse response) {
                                                                    if (!response.isOk()) notSendIds.add(chat_id);
                                                                }
                                                                @Override
                                                                public void onFailure(SendPhoto request, IOException e) {
                                                                    notSendIds.add(chat_id);
                                                                }
                                                            });
                                                        }


                                                    }
                                                    if (!pic[0] && !md[0]) {
                                                        SendMessage request = new SendMessage(chat_id, ed_msg.getText().toString());

                                                        bot.execute(request, new Callback<SendMessage, SendResponse>() {
                                                            @Override
                                                            public void onResponse(SendMessage request, SendResponse response) {
                                                                if (!response.isOk()) notSendIds.add(chat_id);
                                                            }
                                                            @Override
                                                            public void onFailure(SendMessage request, IOException e) {
                                                                notSendIds.add(chat_id);
                                                            }
                                                        });
                                                    }
                                                    if (pic[0] && md[0]) {
                                                        if (TextUtils.isEmpty(ed_url.getText().toString())) {
                                                            cancel[0] = true;
                                                            progressBar.dismiss();

                                                            break;
                                                        }
                                                        else {
                                                            SendPhoto request = new SendPhoto(chat_id, ed_url.getText().toString())
                                                                    .caption(ed_msg.getText().toString())
                                                                    .parseMode(ParseMode.Markdown);

                                                            bot.execute(request, new Callback<SendPhoto, SendResponse>() {
                                                                @Override
                                                                public void onResponse(SendPhoto request, SendResponse response) {
                                                                    if (!response.isOk())
                                                                        notSendIds.add(chat_id);
                                                                }

                                                                @Override
                                                                public void onFailure(SendPhoto request, IOException e) {
                                                                    notSendIds.add(chat_id);
                                                                }
                                                            });
                                                        }
                                                    }

                                                    try {
                                                        Thread.sleep(2000);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }


                                                Intent intent = new Intent(ScrollingActivity.this, ResultActivity.class);
                                                if (!cancel[0]) {
                                                    Bundle b = new Bundle();
                                                    b.putStringArrayList("notIds", notSendIds);
                                                    b.putStringArrayList("testIds", testChatIds);
                                                    b.putStringArrayList("chatIds", chatIds);
                                                    intent.putExtras(b);
                                                    intent.putExtra("test", test);
                                                    handler.sendEmptyMessage(0);

                                                    startActivity(intent);
                                                }

                                            }
                                        }).start();
                                        handler = new Handler() {
                                            public void handleMessage(android.os.Message msg) {
                                                progressBar.dismiss();
                                            }
                                        };

                                    }
                                });

                alertDialogBuilder.setNegativeButton("NO",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ScrollingActivity.this,"Operation Failed!",Toast.LENGTH_LONG).show();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        progressBar = new ProgressDialog(this);

        progressBar.setCancelable(false);
        progressBar.setMessage("Please wait several minutes ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                test = false;

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScrollingActivity.this);
                alertDialogBuilder.setMessage("Are you sure to start broadcasting?!");
                alertDialogBuilder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                progressBar.show();
                                notSendIds.clear();
                                final Boolean[] cancel = {false};

                                new Thread(new Runnable() {
                                    public void run() {

                                        for (final String chat_id : chatIds) {

                                            if (!pic[0] && md[0]) {
                                                SendMessage request = new SendMessage(chat_id, ed_msg.getText().toString())
                                                        .parseMode(ParseMode.Markdown);

                                                bot.execute(request, new Callback<SendMessage, SendResponse>() {
                                                    @Override
                                                    public void onResponse(SendMessage request, SendResponse response) {
                                                        if (!response.isOk()) notSendIds.add(chat_id);
                                                    }
                                                    @Override
                                                    public void onFailure(SendMessage request, IOException e) {
                                                        notSendIds.add(chat_id);
                                                    }
                                                });
                                            }
                                            if (pic[0] && !md[0]) {
                                                if (TextUtils.isEmpty(ed_url.getText().toString())) {
                                                    cancel[0] = true;
                                                    progressBar.dismiss();

                                                    break;
                                                }
                                                else {
                                                    SendPhoto request = new SendPhoto(chat_id, ed_url.getText().toString())
                                                            .caption(ed_msg.getText().toString());

                                                    bot.execute(request, new Callback<SendPhoto, SendResponse>() {
                                                        @Override
                                                        public void onResponse(SendPhoto request, SendResponse response) {
                                                            if (!response.isOk())
                                                                notSendIds.add(chat_id);
                                                        }

                                                        @Override
                                                        public void onFailure(SendPhoto request, IOException e) {
                                                            notSendIds.add(chat_id);
                                                        }
                                                    });
                                                }
                                            }
                                            if (!pic[0] && !md[0]) {
                                                SendMessage request = new SendMessage(chat_id, ed_msg.getText().toString());

                                                bot.execute(request, new Callback<SendMessage, SendResponse>() {
                                                    @Override
                                                    public void onResponse(SendMessage request, SendResponse response) {
                                                        if (!response.isOk()) notSendIds.add(chat_id);
                                                    }
                                                    @Override
                                                    public void onFailure(SendMessage request, IOException e) {
                                                        notSendIds.add(chat_id);
                                                    }
                                                });
                                            }
                                            if (pic[0] && md[0]) {
                                                if (TextUtils.isEmpty(ed_url.getText().toString())) {
                                                    cancel[0] = true;
                                                    progressBar.dismiss();

                                                    break;
                                                }
                                                else {
                                                    SendPhoto request = new SendPhoto(chat_id, ed_url.getText().toString())
                                                            .caption(ed_msg.getText().toString())
                                                            .parseMode(ParseMode.Markdown);

                                                    bot.execute(request, new Callback<SendPhoto, SendResponse>() {
                                                        @Override
                                                        public void onResponse(SendPhoto request, SendResponse response) {
                                                            if (!response.isOk())
                                                                notSendIds.add(chat_id);
                                                        }

                                                        @Override
                                                        public void onFailure(SendPhoto request, IOException e) {
                                                            notSendIds.add(chat_id);
                                                        }
                                                    });
                                                }
                                            }

                                            try {
                                                Thread.sleep(2000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }


                                        Intent intent = new Intent(ScrollingActivity.this, ResultActivity.class);
                                        if (!cancel[0]) {
                                            Bundle b = new Bundle();
                                            b.putStringArrayList("notIds", notSendIds);
                                            b.putStringArrayList("testIds", testChatIds);
                                            b.putStringArrayList("chatIds", chatIds);
                                            intent.putExtras(b);
                                            intent.putExtra("test", test);
                                            handler.sendEmptyMessage(0);
                                            startActivity(intent);
                                        }
                                    }
                                }).start();
                                handler = new Handler() {
                                    public void handleMessage(android.os.Message msg) {
                                        progressBar.dismiss();
                                    }
                                };

                            }
                        });

                alertDialogBuilder.setNegativeButton("NO",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ScrollingActivity.this,"Operation Failed!",Toast.LENGTH_LONG).show();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press Back Again!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}

