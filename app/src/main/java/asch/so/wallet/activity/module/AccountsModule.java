package asch.so.wallet.activity.module;

import asch.so.wallet.contract.AccountsContract;
import dagger.Module;
import dagger.Provides;

/**
 * Created by kimziv on 2017/9/21.
 */

@Module
public class AccountsModule {
    private AccountsContract.View view;

    public AccountsModule(AccountsContract.View view){
        this.view=view;
    }

    @Provides
    AccountsContract.View provideAccountsManagerContractView(){
        return view;
    }
}
