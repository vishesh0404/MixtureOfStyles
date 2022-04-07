package liftup.tech.mixtureofstyles.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import de.hdodenhof.circleimageview.CircleImageView;
import liftup.tech.mixtureofstyles.R;
import liftup.tech.mixtureofstyles.Utils.SessionManager;
import liftup.tech.mixtureofstyles.Utils.VolleyMultipartRequest;
import liftup.tech.mixtureofstyles.Utils.WebInterface;

import static liftup.tech.mixtureofstyles.Utils.WebInterface.EMAIL_ID;
import static liftup.tech.mixtureofstyles.Utils.WebInterface.ID;

public class ProfileActivity extends AppCompatActivity {
    public static final String TAG = ProfileActivity.class.getSimpleName();
    private String userId, strUserName, strFullName, strEmail;
    LinearLayout account_setting, passwordChange, dancePreferences, manageSubscription;
    private ProgressDialog progressDialog;
    private TextView txtUserName, txtUserEmail;
    String username, name, email, mob, member, goal, profilepic;
    SessionManager session;
    CircleImageView profileImage;
    private static final String ROOT_URL = "http://dance.liftup.tech/API/profile/profile_imgupload.php";
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int PICK_IMAGE_REQUEST =1 ;
    boolean flag1;
    Bitmap bitmap1;
    String pathToImage,string1,imageName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);
        session = new SessionManager(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        account_setting = findViewById(R.id.account_setting);
        passwordChange = findViewById(R.id.passwordChange);
        dancePreferences = findViewById(R.id.dancePreferences);
        manageSubscription = findViewById(R.id.manageSubscription);
        profileImage = findViewById(R.id.profile_image);
        txtUserName = findViewById(R.id.userName);
        txtUserEmail = findViewById(R.id.userEmail);
        @SuppressLint("WrongConstant")
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs",MODE_APPEND);
        userId = sharedPreferences.getString(ID, " ");
        strEmail = sharedPreferences.getString(EMAIL_ID, " ");
        Log.d(TAG,"Email ID >>>"+strEmail);

        getUserProfile(userId);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SignInActivity.class) ;
                startActivity( intent );
                session.logoutUser();
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag1 = true;
                pickFromGallery();
            }
        });

        account_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AccountSettingsActivity.class);
                intent.putExtra("USERNAME", username);
                intent.putExtra("NAME", name);
                intent.putExtra("EMAIL", email);
                intent.putExtra("MOBILE", mob);
                intent.putExtra("MEMBER", member);
                intent.putExtra("GOAL", goal);
                intent.putExtra("PROFILEPIC", profilepic);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        passwordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PasswordChangeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        dancePreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DancePreferanceActivity.class);
                intent.putExtra("USERNAME", username);
                intent.putExtra("NAME", name);
                intent.putExtra("EMAIL", email);
                intent.putExtra("MOBILE", mob);
                intent.putExtra("MEMBER", member);
                intent.putExtra("GOAL", goal);
                intent.putExtra("PROFILEPIC", profilepic);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        manageSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ManageSubsciptionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
/*
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);
            if (filePath != null) {
                try {

//                    textView.setText("File Selected");
                    Log.d("filePath", String.valueOf(filePath));
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    uploadBitmap(bitmap);
                    profileImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(
                        ProfileActivity.this,"no image selected",
                        Toast.LENGTH_LONG).show();
            }
        }

    }
*/

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    private void getUserProfile(String userId) {
        showProgressDialogWithTitle("Please Wait..");
        String url = "";
        url = WebInterface.BASE_URL + "profile/profile.php";
        Log.e(TAG, "getUserProfile URL: " + url);
        Map<String, String> params = new HashMap();
        params.put("loginid", userId);
        JSONObject parameters = new JSONObject(params);
        Log.d(TAG, "verifyUser: parameters: "+parameters);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            //JSONArray mArray;
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "getUserProfile Response" + response);
                hideProgressDialogWithTitle();
                try {
                    username = response.getString("username");
                    name = response.getString("name");
                    email = response.getString("email");
                    mob = response.getString("mob");
                    member = response.getString("member");
                    goal = response.getString("goal");
                    profilepic = response.getString("profilepic");

                    txtUserName.setText(name);
                    txtUserEmail.setText(email);
                    Glide.with(getApplicationContext()).load(profilepic).into(profileImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Server Not Responding..Please Try Again",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //TODO: handle failure
                hideProgressDialogWithTitle();
            }
        });
        Volley.newRequestQueue(this).add(jsonRequest);
    }
/*
    private void uploadBitmap(final Bitmap bitmap) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ROOT_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError",""+error.getMessage());
                    }
                }) {


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("login", "");
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
*/
    private void pickFromGallery(){
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                        {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        }
                        else
                        {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, 2);
                        }
                    }
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent=new Intent(Intent.ACTION_PICK);
                    // Sets the type as image/*. This ensures only components of type image are selected
                    intent.setType("image/*");
                    //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
                    String[] mimeTypes = {"image/jpeg", "image/png"};
                    intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                    // Launching the Intent
                    startActivityForResult(intent,1);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult( requestCode, resultCode, data );
        if (resultCode == Activity.RESULT_OK) switch (requestCode) {
            case 1:
                //data.getData return the content URI for the selected Image
                Uri selectedImage = data.getData();
                try {
                    bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // Get the cursor
                Cursor cursor = getContentResolver().query( selectedImage, filePathColumn, null, null, null );
                // Move to first row
                cursor.moveToFirst();
                //Get the column index of MediaStore.Images.Media.DATA
                int columnIndex = cursor.getColumnIndex( filePathColumn[0] );
                //Gets the String value in the column
                String imgDecodableString = cursor.getString( columnIndex );
                pathToImage = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                File filepath=new File( pathToImage);
                Intent intent = new Intent("custom-message1");
                intent.putExtra("pathToImage",""+pathToImage);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                Log.e( "pathToImage", "pathToImage:: " + imageName );
                //profileImage.setImageBitmap(bitmap1);
                if(flag1==true){
                    imageName =filepath.getName();
                    profileImage.setImageBitmap(bitmap1);
                    string1 =getStringImage( bitmap1 );
                    String path =filepath.getPath();
                    uploadImage(path);
                    Log.e( "String1", "pathToImage:: " + string1 );
                }
            case 2:
                if (requestCode == 2 && resultCode == Activity.RESULT_OK)
                {
                    if(flag1==true)
                    {  Bitmap photo = (Bitmap) data.getExtras().get("data");
                        profileImage.setImageBitmap(photo);
                       /* imageName =filepath.getName();
                        eemployee_photo.setImageBitmap(bitmap1);*/
                        string1 =getStringImage( photo );
                        Log.e( "String1", "pathToImage:: " + string1 );
                    }

                }
        }
    }
    private void uploadImage(String path){
        //Showing the progress dialog
        showProgressDialogWithTitle("Uploading...");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ROOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        hideProgressDialogWithTitle();
                        //Showing toast message of the response
                        Toast.makeText(ProfileActivity.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        hideProgressDialogWithTitle();
                        //Showing toast
                        Toast.makeText(ProfileActivity.this, ""+volleyError, Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap1);
                Log.d(TAG, "getParams: Image : "+path);
                //Getting Image Name
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();
                params.put("sendimage", image);
                params.put("login", "3");

                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    public String getStringImage(Bitmap bitmap1){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage1 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage1;
    }

        //TODO Method to show Progress bar
    private void showProgressDialogWithTitle(String substring) {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //TODO Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage(substring);
        progressDialog.show();
    }
    // TODO Method to hide/ dismiss Progress bar
    private void hideProgressDialogWithTitle() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.dismiss();
    }
    //TODO Internet Connection
    //TODO check network connection
    public boolean isConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info != null && info.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;
        } else
            return false;
    }
    //TODO Display No Internet custom dialog
    private void showCustomDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("No Internet Connection");
        alert.setMessage("Please check your internet connection and try again...!!!");
        alert.setCancelable(false);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }
}