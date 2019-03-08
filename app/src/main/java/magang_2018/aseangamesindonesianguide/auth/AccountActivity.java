package magang_2018.aseangamesindonesianguide.auth;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import magang_2018.aseangamesindonesianguide.R;
import magang_2018.aseangamesindonesianguide.content.MainActivity;
import magang_2018.aseangamesindonesianguide.customfonts.EditText_Roboto_Regular;

public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private StorageReference sr_image;

    private EditText_Roboto_Regular text_name;
    private Button action_save_account;
    private CircleImageView image_user;

    private Uri uriImage;

    private String currentUserId, downloadUri, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        sr_image = FirebaseStorage.getInstance().getReference();

        text_name = findViewById(R.id.edit_text_name);
        action_save_account = findViewById(R.id.action_save_account);
        image_user = findViewById(R.id.civ_user_image);

        action_save_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationRequest();
            }
        });

        image_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRequestImage();
            }
        });
    }

    private void setRequestImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    private void validationRequest() {
        name = text_name.getText().toString();
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please write your name....", Toast.LENGTH_SHORT).show();
        }else if (uriImage == null){
            Toast.makeText(this, "Please Choose your images...", Toast.LENGTH_SHORT).show();
        }
        else{
            saveImageToStorage();
        }
    }

    private void saveImageToStorage() {
        StorageReference filePath = sr_image.child("profile_images").child(currentUserId+".jpg");
        filePath.putFile(uriImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    downloadUri = task.getResult().getDownloadUrl().toString();
                    saveAccountInformation();

                }else {
                    String message = task.getException().getMessage();
                    Toast.makeText(AccountActivity.this, "Error " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveAccountInformation() {
        HashMap userMap = new HashMap();
        userMap.put("image", downloadUri);
        userMap.put("name", name);

        userRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    sendUserToMainActivity();
                }else{
                    String message = task.getException().getMessage();
                    Log.i("Error: ", message);
                }
            }
        });
    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(AccountActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uriImage = result.getUri();
                image_user.setImageURI(uriImage);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                String message = error.getMessage();
                Log.i("Error: ", message);
            }
        }
    }
}
