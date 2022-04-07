package liftup.tech.mixtureofstyles.Utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    private ProgressDialog loadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setCancelable(false);
    }

    protected void showProgressDialog(String displayMessage) {
        if (loadingDialog != null) {
            loadingDialog.setMessage(displayMessage);
            loadingDialog.show();
        }
    }

    protected void dismissProgressDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    protected void addDevLog(String tag, String log) {
        Log.d(tag,log);
    }
}
