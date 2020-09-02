package sastra.panji.dhimas.proggeres.sarang.Sarang_tiga;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Sarang_tiga fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}