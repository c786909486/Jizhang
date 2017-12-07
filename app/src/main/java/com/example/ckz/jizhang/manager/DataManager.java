package com.example.ckz.jizhang.manager;

import android.content.Context;

import com.example.ckz.jizhang.bean.BillNetBean;
import com.example.ckz.jizhang.db.BillLocalBean;
import com.example.ckz.jizhang.user.MyUser;
import com.example.ckz.jizhang.util.LogUtils;
import com.example.ckz.jizhang.util.SPUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by CKZ on 2017/12/3.
 */

public class DataManager {
    public static final int DOWNLOAD_TYPE = 1;
    public static final int UPLOAD_TYPE = 2;

    private static DataManager manager;
    private Context context;

    private DataManager(Context context) {
        this.context = context;
    }

    public static DataManager getInstance(Context context){
        if (manager == null){
            synchronized (DataManager.class){
                if (manager == null){
                    manager = new DataManager(context);
                }
            }
        }
        return manager;
    }

    /**
     * 网络数据转本地数据
     * 保存billId不存在的
     * @param netBean
     */
    private void netToLocal(BillNetBean netBean){

               BillLocalBean localBean = new BillLocalBean();
               localBean.setUserId(netBean.getUser().getObjectId());
               localBean.setDate(netBean.getDate());
               localBean.setMoney(netBean.getMoney());
               localBean.setText(netBean.getText());
               localBean.setType(netBean.getType());
               localBean.setBillId(netBean.getBillId());
               localBean.setIsUpload(true);
               localBean.save();
               LogUtils.d(getClass().getSimpleName(),"保存本地成功");


    }

    /**
     * 本地数据上传
     * 上传未上传的
     * @param localBean
     * @param listener
     */
    public void localToNet(final BillLocalBean localBean, final OnUpdateListener listener){
        if (!localBean.isUpload()){
            BillNetBean netBean = new BillNetBean();
            netBean.setUser(MyUser.getCurrentUser(MyUser.class));
            netBean.setDate(localBean.getDate());
            String[] strings = localBean.getDate().split("-");
            netBean.setMonth(strings[0]+"-"+strings[1]);
            netBean.setBillId(localBean.getBillId());
            netBean.setMoney(localBean.getMoney());
            netBean.setText(localBean.getText());
            netBean.setType(localBean.getType());
            netBean.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null){
                        BillLocalBean update = new BillLocalBean();
                        update.setIsUpload(true);
                        update.setUserId(MyUser.getCurrentUser().getObjectId());
                        update.updateAll("billId = ?",localBean.getBillId());
                        listener.onSuccess();
                        SPUtils.putBooleanSp(context,"need_sync",false);
                        LogUtils.d(getClass().getSimpleName(),"上传成功");
                    }else {
                        listener.onFaild(e);
                    }
                }
            });
        }
    }

    /**
     * 登陆情况下
     * 获取所有本地数据
     * @return
     */
    private List<BillLocalBean> getLoginedAllLocal(){

        return BillLocalBean.where("userId = ?",MyUser.getCurrentUser().getObjectId())
//                .order("publishdate desc")
//                .limit(10)
//                .offset(0)
                .find(BillLocalBean.class);
    }

    /**
     * 未登录情况下
     * 获取所有本地数据
     * @return
     */
    private List<BillLocalBean> getAllLocal(){
        return BillLocalBean.where("userId = ?","none")
//                .order("publishdate desc")
//                .limit(10)
//                .offset(0)
                .find(BillLocalBean.class);
    }

    /**
     * 获取本地数据
     * @param listener
     */
    public void getLocalData(OnLocalGetListener listener){
        if (MyUser.getCurrentUser()!=null){
            listener.onSuccess(getLoginedAllLocal());
        }else {
            listener.onSuccess(getAllLocal());
        }
    }

    /**
     * 登陆后，上传本地数据
     * @return
     */
    public DataManager updateAfterLogin(final OnUpdateListener listener, final OnGetCurrenListener listener1){
       getAllNetData(new FindListener<BillNetBean>() {
           @Override
           public void done(List<BillNetBean> list, BmobException e) {
               if (e==null){
                  List<BillLocalBean> data = getAllLocal();
                   for (int i=0;i<data.size();i++){
                       for (BillNetBean netBean:list){
                           if (!netBean.getBillId().equals(data.get(i).getBillId())){
                               localToNet(data.get(i),listener);
                               listener1.onUploadSuccess(UPLOAD_TYPE,i,data.size());
                           }
                       }
                   }
               }else {
                   listener1.onFaild(UPLOAD_TYPE,e);
               }
           }
       });
       return this;
    }

    /**
     * 登陆后，同步数据
     * t
     */
    public DataManager synchronizeData(final OnGetCurrenListener listener){
        getAllNetData(new FindListener<BillNetBean>() {
            @Override
            public void done(List<BillNetBean> list, BmobException e) {
                if (e == null){
                    for (int i=0;i<list.size();i++){
                        netToLocal(list.get(i));
                        listener.onDownloadSuccess(DOWNLOAD_TYPE,i,list.size());
                    }

                }else {
                    listener.onFaild(DOWNLOAD_TYPE,e);
                }
            }
        });
        return this;
    }

    /**
     * 获取该账户的所有数据
     * @param listener
     */
    public void getAllNetData(FindListener<BillNetBean> listener){

        BmobQuery<BillNetBean> query = new BmobQuery<BillNetBean>();
        query.addWhereEqualTo("user",MyUser.getCurrentUser())
//                .setLimit(500)
//                .setSkip(500)
                .order("-createdAt")
             .findObjects(listener);
    }

    /**
     * 未登录，保存到本地
     * @param billId
     * @param type
     * @param text
     * @param date
     * @param money
     * @return
     */
    private BillLocalBean addLocalNoLogin(String billId, String type, String text, String date, float money){
        BillLocalBean localBean = new BillLocalBean();
        localBean.setUserId("none");
        localBean.setIsUpload(false);
        localBean.setBillId(billId);
        localBean.setType(type);
        localBean.setText(text);
        localBean.setDate(date);
        localBean.setMoney(money);
        localBean.save();
        SPUtils.putBooleanSp(context,"need_sync",true);
        return localBean;
    }

    /**
     * 已登陆，保存本地
     * @param billId
     * @param type
     * @param text
     * @param date
     * @param money
     * @return
     */
    private BillLocalBean addLocalWithLogin(String billId, String type, String text, String date, float money){
        BillLocalBean localBean = new BillLocalBean();
        localBean.setUserId(BmobUser.getCurrentUser().getObjectId());
        localBean.setIsUpload(false);
        localBean.setBillId(billId);
        localBean.setType(type);
        localBean.setText(text);
        localBean.setDate(date);
        localBean.setMoney(money);
        localBean.save();
        SPUtils.putBooleanSp(context,"need_sync",true);
        return localBean;
    }

    /**
     * 保存到本地
     * @param billId
     * @param type
     * @param text
     * @param date
     * @param money
     * @return
     */
    public BillLocalBean addLocalData(String billId, String type, String text, String date, float money){
        if (BmobUser.getCurrentUser()!=null){
           return addLocalWithLogin(billId,type,text,date,money);
        }else {
            return addLocalNoLogin(billId,type,text,date,money);
        }
    }

    public void upDateNoLoginData(){
        BillLocalBean localBean = new BillLocalBean();
        localBean.setUserId(MyUser.getCurrentUser().getObjectId());
        localBean.updateAll("userId = ?","none");
    }




    public interface OnUpdateListener{
        void onSuccess();
        void onFaild(BmobException e);
    }
    public interface OnGetCurrenListener{

        void onDownloadSuccess(int type,int position,int total);
        void onUploadSuccess(int type,int position,int total);
        void onFaild(int type,BmobException e);
    }

    public interface OnLocalGetListener{
        void onSuccess(List<BillLocalBean> data);
    }

    public interface OnDataCallback{
        void onCallback(BillLocalBean localBean);
    }


}
