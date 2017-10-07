package br.com.i9algo.autaz.pdv.data.remote.repositoryes;


import br.com.i9algo.autaz.pdv.data.local.PreferencesRepository;
import br.com.i9algo.autaz.pdv.data.local.UserRealmRepository;
import br.com.i9algo.autaz.pdv.data.remote.subscribers.AuthValidateSubscriber;
import br.com.i9algo.autaz.pdv.data.remote.subscribers.LoginSubscriber;
import br.com.i9algo.autaz.pdv.data.remote.subscribers.SubscriberInterface;
import br.com.i9algo.autaz.pdv.domain.constants.AuthAttr;
import br.com.i9algo.autaz.pdv.domain.models.User;
import rx.schedulers.Schedulers;

/**
 * Created by aStraube on 12/07/2017.
 */
public class AuthRepository {

    private final SubscriberInterface _owner;

    public AuthRepository(final SubscriberInterface owner) {
        this._owner = owner;
    }

    public void onLogin(String username, String password) throws Exception {
        //Log.v("AuthRepository", "API WEB - onLogin");

        // Armazenar username em cache para exibir na tela de login posteriormente
        PreferencesRepository.setValue(AuthAttr.USERNAME, username);

        _owner.getApiService().authorization(username, password)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new LoginSubscriber(_owner));
    }

    public void onValidateCredentialsUser(final SubscriberInterface owner) {
        //Log.v("AuthRepository", "API WEB - onValidateCredentialsUser");

        User user = UserRealmRepository.getFirst();
        if (user == null || user.getPublicToken().isEmpty() || user.getApiToken().isEmpty()) {
            String msg = "Usuario nao foi identificado corretamente";
            _owner.onSubscriberError(new Throwable(msg), "Erro de credenciais", msg);
            return;
        }
        _owner.getApiService().authorizationValidate(user.getApiToken(), user.getPublicToken())
                .subscribeOn(Schedulers.io())
                .subscribe(new AuthValidateSubscriber(_owner));
    }
}
