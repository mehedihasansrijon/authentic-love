package com.rhc.authenticlove;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    RadioButton[] radioButtons;
    EditText userName, partnerName;
    Button calculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize
        radioButtons = new RadioButton[]{findViewById(R.id.friend), findViewById(R.id.love), findViewById(R.id.family), findViewById(R.id.crush), findViewById(R.id.soulmate), findViewById(R.id.best_friend), findViewById(R.id.partner), findViewById(R.id.ex_partner), findViewById(R.id.childhood_friend), findViewById(R.id.favorite_person),};
        userName = findViewById(R.id.userName);
        partnerName = findViewById(R.id.partnerName);
        calculate = findViewById(R.id.calculate);

        for (RadioButton radioButton : radioButtons) {
            radioButtonOnClick(radioButton);
        }

        // Calculation on click
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEditTextAndCalculate();
            }
        });
    }

    // Radio button on click
    private void radioButtonOnClick(RadioButton radioButton) {
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Uncheck all radio button first
                    for (RadioButton rb : radioButtons) {
                        rb.setChecked(false);
                    }

                    // Check only the clicked radio button
                    radioButton.setChecked(true);
                }
            }
        });
    }

    // Check edit text and calculate
    private void checkEditTextAndCalculate() {
        if (!userName.getText().toString().isEmpty()) {
            if (!partnerName.getText().toString().isEmpty()) {
                checkRelationshipAndCalculate();
            } else {
                partnerName.setError("দয়া করে আপনার সঙ্গীর নাম লিখুন।");
            }
        } else {
            userName.setError("দয়া করে আপনার নাম লিখুন।");
        }
    }

    // Check relationship and calculate
    private void checkRelationshipAndCalculate() {
        int selectedPosition = 0; // Selected position
        for (int i = 0; i < radioButtons.length; i++) {
            if (radioButtons[i].isChecked()) {
                selectedPosition = i;
                break;
            }
        }

        String radioButtonText = radioButtons[selectedPosition].getText().toString(); // Get radio button text

        int score = 0;
        switch (radioButtonText) {
            case "বন্ধু":
                score = calculateFriendCompatibility(userName.getText().toString(), radioButtonText, partnerName.getText().toString());
                break;

            case "প্রেম":
                score = calculateLoveCompatibility(userName.getText().toString(), radioButtonText, partnerName.getText().toString());
                break;

            case "পরিবার":
                score = calculateFamilyCompatibility(userName.getText().toString(), radioButtonText, partnerName.getText().toString());
                break;

            case "ক্রাশ":
                score = calculateCrushCompatibility(userName.getText().toString(), radioButtonText, partnerName.getText().toString());
                break;

            case "সোলমেট":
                score = calculateSoulmateCompatibility(userName.getText().toString(), radioButtonText, partnerName.getText().toString());
                break;

            case "সেরা বন্ধু":
                score = calculateBestFriendCompatibility(userName.getText().toString(), radioButtonText, partnerName.getText().toString());
                break;

            case "সঙ্গী":
                score = calculatePartnerCompatibility(userName.getText().toString(), radioButtonText, partnerName.getText().toString());
                break;

            case "প্রাক্তন সঙ্গী":
                score = calculateExPartnerCompatibility(userName.getText().toString(), radioButtonText, partnerName.getText().toString());
                break;

            case "শৈশবের বন্ধু":
                score = calculateChildhoodFriendCompatibility(userName.getText().toString(), radioButtonText, partnerName.getText().toString());
                break;

            case "প্রিয় মানুষ":
                score = calculateFavoritePersonCompatibility(userName.getText().toString(), radioButtonText, partnerName.getText().toString());
                break;

            default:
                return;
        }

        // Show calculation dialog
        showCalculationDialog(userName.getText().toString(), partnerName.getText().toString(), radioButtonText, score);
    }

    // Show calculation dialog
    private void showCalculationDialog(String userName, String partnerName, String relationType, int compatibilityScore) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("আপনার সম্পর্কের বিশ্লেষণ");

        // Inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);

        // Get references to the TextViews in the layout
        TextView userNameTextView = dialogView.findViewById(R.id.userNameTextView);
        TextView partnerNameTextView = dialogView.findViewById(R.id.partnerNameTextView);
        TextView relationTypeTextView = dialogView.findViewById(R.id.relationTypeTextView);
        TextView compatibilityTextView = dialogView.findViewById(R.id.compatibilityTextView);
        TextView tipsTextView = dialogView.findViewById(R.id.tipsTextView);

        userNameTextView.setText("আপনার নাম: " + userName);
        partnerNameTextView.setText("সঙ্গীর নাম: " + partnerName);
        relationTypeTextView.setText("সম্পর্কের ধরন: " + relationType);

        // Show score animated
        int duration = Math.max(1000, compatibilityScore * 50);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, compatibilityScore);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                int score = (int) animation.getAnimatedValue();
                compatibilityTextView.setText("সম্পর্কের অবস্থা: " + score + "%");
            }
        });
        valueAnimator.start();

        String tips = getTipsForRelationship(relationType);
        tipsTextView.setText(tips);

        // Add OK button
        builder.setPositiveButton("ঠিক আছে", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Get tips for relationship
    private String getTipsForRelationship(String relationType) {
        switch (relationType) {
            case "বন্ধু":
                return "বন্ধুত্বের মাঝে যোগাযোগ এবং বোঝাপড়া বজায় রাখুন।";
            case "প্রেম":
                return "প্রেমে বিশ্বাস ও রোমান্সের জন্য সময় বের করুন।";
            case "পরিবার":
                return "পরিবারের সাথে সময় কাটানো এবং একে অপরকে সমর্থন করা গুরুত্বপূর্ণ।";
            case "ক্রাশ":
                return "ক্রাশের প্রতি আপনার অনুভূতি প্রকাশ করতে দ্বিধা করবেন না।";
            case "সোলমেট":
                return "আপনার সোলমেটের সঙ্গে খোলামেলা আলোচনা করুন এবং তাদের সাপোর্ট করুন।";
            case "সেরা বন্ধু":
                return "সেরা বন্ধুদের সাথে সম্পর্ক মজবুত করতে ছোট ছোট পরিকল্পনা করুন।";
            case "সঙ্গী":
                return "সঙ্গীর সাথে গুণগত সময় কাটান এবং একে অপরের জন্য নতুন অভিজ্ঞতা তৈরি করুন।";
            case "প্রাক্তন সঙ্গী":
                return "প্রাক্তন সঙ্গীর সাথে সম্পর্ক ভালো রাখতে প্রয়োজনীয় সীমা নির্ধারণ করুন।";
            case "শৈশবের বন্ধু":
                return "শৈশবের বন্ধুর সাথে স্মৃতিচারণ করুন এবং পুরনো দিনের কথা বলুন।";
            case "প্রিয় মানুষ":
                return "প্রিয় মানুষটির প্রতি আপনার ভালোবাসা প্রকাশ করতে ভুলবেন না।";
            default:
                return "একটি সম্পর্কের মধ্যে সময় দিন এবং একে অপরকে বুঝুন।";
        }
    }

    // Calculate function
    private int calculateScore(String userName, String partnerName, String relationship, int adjustmentFactor) {
        int userFirstCharValue = userName.charAt(0);
        int userLastCharValue = userName.charAt(userName.length() - 1);
        int partnerFirstCharValue = partnerName.charAt(0);

        int partnerLastCharValue = partnerName.charAt(partnerName.length() - 1);
        int totalChars = userName.length() + partnerName.length();

        return (userFirstCharValue + userLastCharValue + partnerFirstCharValue + partnerLastCharValue + totalChars + adjustmentFactor) % 101;
    }

    private int calculateFriendCompatibility(String userName, String relationship, String partnerName) {
        return calculateScore(userName, partnerName, relationship, 10);
    }

    private int calculateLoveCompatibility(String userName, String relationship, String partnerName) {
        return calculateScore(userName, partnerName, relationship, 20);
    }

    private int calculateFamilyCompatibility(String userName, String relationship, String partnerName) {
        return calculateScore(userName, partnerName, relationship, 15);
    }

    private int calculateCrushCompatibility(String userName, String relationship, String partnerName) {
        return calculateScore(userName, partnerName, relationship, 25);
    }

    private int calculateSoulmateCompatibility(String userName, String relationship, String partnerName) {
        return calculateScore(userName, partnerName, relationship, 30);
    }

    private int calculateBestFriendCompatibility(String userName, String relationship, String partnerName) {
        return calculateScore(userName, partnerName, relationship, 12);
    }

    private int calculatePartnerCompatibility(String userName, String relationship, String partnerName) {
        return calculateScore(userName, partnerName, relationship, 22);
    }

    private int calculateExPartnerCompatibility(String userName, String relationship, String partnerName) {
        return calculateScore(userName, partnerName, relationship, 5);
    }

    private int calculateChildhoodFriendCompatibility(String userName, String relationship, String partnerName) {
        return calculateScore(userName, partnerName, relationship, 18);
    }

    private int calculateFavoritePersonCompatibility(String userName, String relationship, String partnerName) {
        return calculateScore(userName, partnerName, relationship, 28);
    }
}