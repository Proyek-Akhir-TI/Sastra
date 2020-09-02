package sastra.panji.dhimas.proggeres.sarang.Sarang_dua;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Sarang1 fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}