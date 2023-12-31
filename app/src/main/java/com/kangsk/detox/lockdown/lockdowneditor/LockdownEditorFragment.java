package com.kangsk.detox.lockdown.lockdowneditor;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.kangsk.detox.R;
import com.kangsk.detox.utility.LockdownManager;

public class LockdownEditorFragment extends DialogFragment {

    /*
     * PRIVATE FIELDS
     */
    private Context mApplicationContext;
    private LockdownManager mLockdownManager;

    private RecyclerView mLockdownEditorRecyclerView;
    private LockdownEditorAdapter mLockdownEditorAdapter;
    private MaterialButton mSave;
    private MaterialButton mCancel;

    /*
     * onCreate: called when the Fragment is created, responsible for instantiating fields.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApplicationContext = getActivity().getApplicationContext();
        mLockdownManager = LockdownManager.getInstance(mApplicationContext);
        mLockdownEditorAdapter = new LockdownEditorAdapter(mApplicationContext, mLockdownManager);
    }

    /*
     * onCreateView: called when the fragment's view is created. Responsible for inflating the newly
     * created view with the appropriate layout resource.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lockdown_editor, container, false);
    }


    /*
     * onViewCreated: called once the fragment's view is inflated. Responsible for creating the LockdownEditorFragment's
     * RecyclerView, setting its LayoutManager, and setting its adapter.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLockdownEditorRecyclerView = view.findViewById(R.id.recycler_lockdown_editor_fragment);
        mLockdownEditorRecyclerView.setLayoutManager(new LinearLayoutManager(mApplicationContext));
        mLockdownEditorRecyclerView.setAdapter(mLockdownEditorAdapter);

        mSave = view.findViewById(R.id.button_lockdown_editor_fragment_save);
        mCancel = view.findViewById(R.id.button_lockdown_editor_fragment_cancel);
        mSave.setOnClickListener(new View.OnClickListener() {
            /*
             * onClick: listener called when the 'Save' button is clicked.
             */
            @Override
            public void onClick(View button) {
                dismiss();
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            /*
             * onClick: listener called when the 'Cancel' button is clicked.
             */
            @Override
            public void onClick(View button) {
                dismiss();
            }
        });
    }

    /*
     * dismiss: responsible for dismissing the current dialog fragment and popping the fragment
     * transaction back stack.
     */
    @Override
    public void dismiss() {
        getParentFragmentManager().popBackStack();
    }

}
