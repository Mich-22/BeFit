package com.example.befit.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public Intent GoToFacebook(Context ctx){
        String url = "fb://page/100761175968929";
        Uri uri = Uri.parse(url);

        try {
            ApplicationInfo applicationInfo = ctx.getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

/*
    private void openWhatsApp(String numero,String mensaje){

        try{
            PackageManager packageManager = getActivity().getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone="+ numero +"&text=" + URLEncoder.encode(mensaje, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }else {
                KToast.errorToast(getActivity(), getString(R.string.no_whatsapp), Gravity.BOTTOM, KToast.LENGTH_SHORT);
            }
        } catch(Exception e) {
            Log.e("ERROR WHATSAPP",e.toString());
            KToast.errorToast(getActivity(), getString(R.string.no_whatsapp), Gravity.BOTTOM, KToast.LENGTH_SHORT);
        }

    }*/
}