package com.kangsk.detox.lockdowneditfragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.R;
import com.kangsk.detox.lockdownfragment.utility.LockdownManager;

public class LockdownEditFragment extends DialogFragment implements View.OnClickListener {

    private LockdownManager mLockdownManager;
    private Context mApplicationContext;

    private RecyclerView mLockdownEditOptionsRecyclerView;
    private LockdownEditOptionsAdapter mLockdownEditOptionsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApplicationContext = getActivity().getApplicationContext();
        mLockdownManager = LockdownManager.getInstance(mApplicationContext);
        mLockdownEditOptionsAdapter = new LockdownEditOptionsAdapter(mLockdownManager, mApplicationContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lockdown_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLockdownEditOptionsRecyclerView = view.findViewById(R.id.recycler_view_lockdown_edit_options);
        mLockdownEditOptionsRecyclerView.setLayoutManager(new LinearLayoutManager(mApplicationContext));
        mLockdownEditOptionsRecyclerView.setAdapter(mLockdownEditOptionsAdapter);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    @Override
    public void dismiss() {
        getParentFragmentManager().popBackStack();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }



}
