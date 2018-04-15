package com.example.jayshil.tdrinc.Profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jayshil.tdrinc.LoginRegister.LoginActivity;
import com.example.jayshil.tdrinc.LoginRegister.User_info;
import com.example.jayshil.tdrinc.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    Button signOut, editProfile;
    TextView text;
    View v;
    ImageView profileImage, mainProfileImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imagePath;
    public Uri url;
    public Uri onUrl;
    String childString = "userProfileImages";


    FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    DatabaseReference ref1;
    private StorageReference storageReference;
    private String userID, userName;
    String uriii;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        signOut = (Button)v.findViewById(R.id.signout);
        text = (TextView)v.findViewById(R.id.welcometext);
        editProfile = (Button)v.findViewById(R.id.editProfileB);
        mainProfileImage = (ImageView)v.findViewById(R.id.imageMain);

//---------------------------Firebase Variable Declaration----------------------
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        ref1 = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference("profileImages");
        userID = user.getUid();


        if(firebaseAuth.getCurrentUser()==null){
            getActivity().finish();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
ref1.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Image_Upload image = new Image_Upload();
        image.setImageUri(dataSnapshot.child(childString).child(userID).getValue(Image_Upload.class).getImageUri());
        uriii = image.getImageUri();
        Log.d("URI: ",uriii);
        onUrl = Uri.parse(uriii);
        if(onUrl != null){
            Glide.with(getContext()).load(onUrl).centerCrop().into(mainProfileImage);
        }
        else {
            mainProfileImage.setImageResource(R.drawable.cam);
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});

//----------------------Get Name from Database-----------------------------
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren()){
                    User_info userInfo = new User_info();
                    userInfo.setName(snap.child(userID).getValue(User_info.class).getName());
                    userName = userInfo.getName();
                    text.setText(userInfo.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//----------------------Get Name From Database Ends-----------------------------

        //getProfileImageFromDatabase(uriii);

        signOut.setOnClickListener(this);
        editProfile.setOnClickListener(this);
        return v;

    }
//------------------------Update Profile Name Code-------------------------------------------
    public void updateProfile(final String uId, String uName){
        AlertDialog.Builder dialog =  new AlertDialog.Builder(v.getContext());
        final View vee = LayoutInflater.from(v.getContext()).inflate(R.layout.update_profile, null, false);
        dialog.setView(vee);
        profileImage = (ImageView)vee.findViewById(R.id.circleImageView);
        final EditText name11 = (EditText)vee.findViewById(R.id.newname);
        Button upButton = (Button)vee.findViewById(R.id.upbut);

        name11.setText(uName);
        Glide.with(getContext()).load(onUrl).centerCrop().into(profileImage);

        dialog.setTitle("Update Profile");
        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = name11.getText().toString();
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(getActivity(), "Name is required", Toast.LENGTH_LONG).show();
                }else {
                    updateName(uId, name);
                    alertDialog.dismiss();
                }


            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfilePicture();
            }
        });
    }
//------------------------Update Profile Name Code Ends------------------------------------------

    public void changeProfilePicture(){
        openFilechooser();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //Toast.makeText(context, "Profile Activity", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        if(v ==signOut){
            firebaseAuth.signOut();
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));

        }
        if(v ==editProfile){
            updateProfile(userID, userName);
        }


    }
//----------------------------Insert Updated Name into Database---------------------------------------------
    public void updateName(String useId, String useName){

        DatabaseReference databaseref = FirebaseDatabase.getInstance().getReference("userinfo").child(useId);
        User_info info = new User_info(useName);
        databaseref.setValue(info);
    }

    public void openFilechooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            imagePath = data.getData();
            profileImage.setImageURI(imagePath);
            uploadImage();
        }
    }

    private String getImageExtension(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    public void uploadImage(){
        StorageReference ref = storageReference.child(userName+"."+getImageExtension(imagePath));

        ref.putFile(imagePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //url = taskSnapshot.getDownloadUrl();
                        uriii = taskSnapshot.getDownloadUrl().toString();

                        Image_Upload image_upload = new Image_Upload(taskSnapshot.getDownloadUrl().toString());
                        databaseReference.child("userProfileImages").child(userID).setValue(image_upload);

                        getProfileImageFromDatabase(uriii);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Image not Uploaded", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void getProfileImageFromDatabase(final String u){

        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snap: dataSnapshot.getChildren()){
                    //Image_Upload image = new Image_Upload(url.toString());
                    //image.setImageUri(snap.child(userID).getValue(Image_Upload.class).getImageUri());
                    Glide.with(getContext()).load(u).centerCrop().into(mainProfileImage);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
