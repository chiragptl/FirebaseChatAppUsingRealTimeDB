package com.example.chatappfirebase.setProfile;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.chatappfirebase.R;
import com.example.chatappfirebase.dashboard.DashboardFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SetProfileFragment extends Fragment {

    private ImageView mgetuserimageinimageview;
    private static final int PICK_IMAGE = 123;
    private Uri imagepath;
    private EditText mgetusername;
    private FirebaseAuth firebaseAuth;
    private String name;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String ImageUriAccessToken;
    ProgressBar mprogressbarofsetprofile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_profile, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mgetusername = view.findViewById(R.id.getusername);
        CardView mgetuserimage = view.findViewById(R.id.getuserimage);
        mgetuserimageinimageview = view.findViewById(R.id.getuserimageinimageview);
        android.widget.Button msaveprofile = view.findViewById(R.id.saveProfile);
        mprogressbarofsetprofile = view.findViewById(R.id.progressbarofsetProfile);

        mgetuserimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });


        msaveprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = mgetusername.getText().toString();
                if (name.isEmpty()) {
                    Log.d("empty", "Name is Empty");
                } else if (imagepath == null) {
                    Log.d("empty", "Image is Empty");
                } else {
                    mprogressbarofsetprofile.setVisibility(View.VISIBLE);
                    sendDataToRealTimeDatabase();
                    mprogressbarofsetprofile.setVisibility(View.INVISIBLE);
                    Log.d("working","now work on chatActivity");
                    Fragment fragment = new DashboardFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.replace, fragment);
                    fragmentTransaction.commit();
                }
            }
        });
        return view;
    }

    private void sendDataToRealTimeDatabase() {
        name = mgetusername.getText().toString().trim();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile muserprofile = new UserProfile(name, firebaseAuth.getUid());
        databaseReference.setValue(muserprofile);
        Log.d("profileadded", "User Profile Added Sucessfully");
        sendImagetoStorage();
    }

    private void sendImagetoStorage() {
        StorageReference imageref = storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile Pic");
        //Image compresesion
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imagepath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        ///putting image to storage
//
//        UploadTask uploadTask = imageref.putBytes(data);
//
//        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        ImageUriAcessToken = uri.toString();
//                        Log.d("success", "URI get success");
//                        sendDataTocloudFirestore();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("fail","URI get Failed");
//                    }
//                });
//                sendDataTocloudFirestore();
//                Log.d("upload","Image is uploaded");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("fail","Image Not Uploaded");
//            }
//        });
        sendDataTocloudFirestore();
    }
    private void sendDataTocloudFirestore() {

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("name", name);
        userdata.put("image", ImageUriAccessToken);
        userdata.put("uid", firebaseAuth.getUid());
        userdata.put("status", "Online");

        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("success","Data on Cloud Firestore send success");
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imagepath = data.getData();
            mgetuserimageinimageview.setImageURI(imagepath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
