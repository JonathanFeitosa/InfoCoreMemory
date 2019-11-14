package br.com.itriadsystem.androidhardware;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;

public class CPUCoreDiffCallback extends DiffUtil.Callback {

    private final ArrayList<CPUCoreInfo> mOldCPUCoreList;
    private final ArrayList<CPUCoreInfo> mNewCPUCoreList;

    public CPUCoreDiffCallback(ArrayList<CPUCoreInfo> OldCPUCoreList, ArrayList<CPUCoreInfo> NewCPUCoreList) {
        this.mOldCPUCoreList = OldCPUCoreList;
        this.mNewCPUCoreList = NewCPUCoreList;
    }

    @Override
    public int getOldListSize() {
        return mOldCPUCoreList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewCPUCoreList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldCPUCoreList.get(oldItemPosition).getCoreName().equals(mNewCPUCoreList.get(
                newItemPosition).getCoreName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final CPUCoreInfo oldThermal = mOldCPUCoreList.get(oldItemPosition);
        final CPUCoreInfo newThermal = mNewCPUCoreList.get(newItemPosition);

        return oldThermal.getCoreValue().equals(newThermal.getCoreValue());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}