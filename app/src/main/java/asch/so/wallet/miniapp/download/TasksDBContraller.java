package asch.so.wallet.miniapp.download;

import android.text.TextUtils;

import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.util.List;

import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Dapp;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by kimziv on 2018/1/22.
 */

public class TasksDBContraller {


    public List<TaskModel> getAllTasks() {

        RealmResults<TaskModel> results = getRealm().where(TaskModel.class).findAll();

        return results;
    }

    public TaskModel addTask(final Dapp dapp, final String path) {
        if ( dapp==null || TextUtils.isEmpty(dapp.getLink()) || TextUtils.isEmpty(path)) {
            return null;
        }
        final int id = FileDownloadUtils.generateId(dapp.getLink(), path);

        TaskModel model = new TaskModel();
        model.setId(id);
        model.setPath(path);
        model.setDapp(dapp);
        model.setUrl(dapp.getLink());
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(model);
            }
        });
        return model;
    }

    public void deleteTask(){

    }

    private Realm getRealm(){
        return Realm.getDefaultInstance();
    }
}
