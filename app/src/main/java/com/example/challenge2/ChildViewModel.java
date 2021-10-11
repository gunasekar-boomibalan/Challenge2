package com.example.challenge2;

import androidx.lifecycle.ViewModel;

import io.objectbox.Box;
import io.objectbox.android.ObjectBoxLiveData;

public class ChildViewModel extends ViewModel {

    private ObjectBoxLiveData<Child> childLiveData;

    public ObjectBoxLiveData<Child> getChildLiveData(Box<Child> notesBox) {
        if (childLiveData == null) {
            childLiveData = new ObjectBoxLiveData<>(notesBox.query().order(Child_.timeStamp).build());
        }
        return childLiveData;
    }
}