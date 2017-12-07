package com.example.ckz.jizhang.view.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ckz.jizhang.R;
import com.example.ckz.jizhang.adapter.AddBillAdapter;
import com.example.ckz.jizhang.bean.BillNetBean;
import com.example.ckz.jizhang.callback.OnDataCallback;
import com.example.ckz.jizhang.db.BillLocalBean;
import com.example.ckz.jizhang.manager.ShowAddPop;
import com.example.ckz.jizhang.presenter.MRecordPresenter;
import com.example.ckz.jizhang.util.LogUtils;
import com.example.ckz.jizhang.util.SPUtils;
import com.example.ckz.jizhang.view.mvpview.MRecordView;
import com.example.vuandroidadsdk.showpop.ShowPopup;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by CKZ on 2017/11/27.
 */

public class RecordFragment extends BaseFragment implements View.OnClickListener,MRecordView{
    private String TAG = getClass().getSimpleName();
    private static final int ANIM_CLOSE = 1;
    private static final int ANIM_START = 2;
    private int current = ANIM_CLOSE;
    private ListView recordlist;
    private FloatingActionButton mAddBtn;
    private LinearLayout mAddLayout;
    private TextView mAddBill;
    private List<Object> mData;
    private AddBillAdapter mAdapter;
    private TwinklingRefreshLayout mRefresh;
    private MRecordPresenter mPresenter;
    private ShowAddPop pop;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_record,container,false);
       initView(view);
       initData();
        mPresenter = new MRecordPresenter(getContext(),this);
        pop = new ShowAddPop(getContext());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
       if (SPUtils.getBooleanSp(getContext(),"need_sync")){
           sync();
       }else {
           mPresenter.getLocalData();
       }
    }

    private void sync() {

       Observable.interval(0,2, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Observer<Long>() {
                   @Override
                   public void onCompleted() {

                   }
                   @Override
                   public void onError(Throwable throwable) {

                   }
                   @Override
                   public void onNext(Long aLong) {
                       if (aLong == 0){
                           mPresenter.synchronizeData();
                       }else if (aLong == 2){
                           mPresenter.getLocalData();
                       }
                   }
               });

    }

    private void initData() {
        mData = new ArrayList<>();
        mAdapter = new AddBillAdapter(getContext(),mData);
        recordlist.setAdapter(mAdapter);
    }

    private void initView(View view) {
        recordlist = (ListView) view.findViewById(R.id.record_list);
        mAddBtn = ((FloatingActionButton) view.findViewById(R.id.add_btn));
        mAddLayout = ((LinearLayout) view.findViewById(R.id.menu_layout));
        mAddBill = ((TextView) view.findViewById(R.id.add_bill));
        mRefresh = ((TwinklingRefreshLayout) view.findViewById(R.id.refresh_layout));
        mRefresh.setHeaderView(new ProgressLayout(getContext()));
        mAddBill.setOnClickListener(this);
        mAddBtn.setOnClickListener(this);
        mRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mPresenter.getLocalData();
                refreshLayout.finishRefreshing();
            }
        });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_btn:
                if (current == ANIM_CLOSE){
                    startAmin();
                    showAdd();
                    current = ANIM_START;
                }else {
                    closeAnim();
                    current = ANIM_CLOSE;
                    closeAdd();
                }
                break;
            case R.id.add_bill:
                pop.createView(view).setOnApplyClickListener(new ShowAddPop.OnApplyClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getContext(),"type:"+pop.getType()+"\ntext:"+pop.getText()+"\ndata:"+pop.getDate()+"\nmoney"+pop.getMoney(),Toast.LENGTH_SHORT).show();
                    mPresenter.addBill(System.currentTimeMillis()+"",pop.getType(),pop.getText(),pop.getDate(),pop.getMoney());
                    }
                });
                break;

        }
    }

    private void closeAdd() {
        playAni(1.0f, 0, 100f, 0, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAddLayout.setVisibility(View.GONE);
            }
        });

    }
    private void playAni(float scaleFrom, float scaleTo, float alphaFrom, float alphaTo, AnimatorListenerAdapter listener){
        ValueAnimator scaleAni = ValueAnimator.ofFloat(scaleFrom,scaleTo);
        scaleAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float scale = (float) valueAnimator.getAnimatedValue();
                mAddBill.setScaleX(scale);
                mAddBill.setScaleX(scale);
            }
        });
        ValueAnimator alphaAni = ValueAnimator.ofFloat(alphaFrom,alphaTo);
        alphaAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float alpha = (float) valueAnimator.getAnimatedValue();
                mAddBill.setAlpha(alpha);
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.setDuration(500L);
        set.playTogether(scaleAni,alphaAni);
        set.setInterpolator(new DecelerateInterpolator());
        set.start();
        set.addListener(listener);
    }

    private void showAdd() {
        mAddLayout.setVisibility(View.VISIBLE);
        playAni(0f, 1.0f, 0f, 100f, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });

    }

    private void closeAnim() {
        ValueAnimator animator = ValueAnimator.ofFloat(-45,-65,0);
        animator.setDuration(500L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float rotate = (float) valueAnimator.getAnimatedValue();
                mAddBtn.setRotation(rotate);
            }
        });
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    private void startAmin() {
        ValueAnimator animator = ValueAnimator.ofFloat(0,20,-45);
        animator.setDuration(500L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float rotate = (float) valueAnimator.getAnimatedValue();
                mAddBtn.setRotation(rotate);
            }
        });
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    @Override
    public void showLocalData(List<BillLocalBean> localBean) {
        mData.clear();
        mData.addAll(localBean);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void showNetData(List<BillNetBean> netBean) {

    }

    @Override
    public void showDialog(int type, int position, int total) {
        Toast.makeText(getContext(),type+"第"+position+"个",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideDialog() {

    }

    @Override
    public void addBillData(final BillLocalBean data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mData.add(0,data);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
