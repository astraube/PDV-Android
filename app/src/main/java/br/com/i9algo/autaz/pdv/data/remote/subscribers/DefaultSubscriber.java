package br.com.i9algo.autaz.pdv.data.remote.subscribers;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import br.com.i9algo.autaz.pdv.domain.models.inbound.ResultStatusDefault;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import rx.Subscriber;

public class DefaultSubscriber<T> extends Subscriber<T> {

    public ResultStatusDefault resultStatus = null;

    @Override
    public void onCompleted() {
        // no-op by default
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();

        if (e instanceof HttpException) {
            // As excecoes com a classe 'retrofit2.HttpException' sao de tipo 'RetrofitError.Kind.HTTP'.
            // Isso significa que sua chamada retornou um código de status não 2XX.
            HttpException ex = (HttpException) e;
            ResponseBody responseBody = ex.response().errorBody();

            resultStatus = getErrorResult(responseBody);
            resultStatus.error = e;

        } else if (e instanceof SocketTimeoutException) {
            resultStatus = new ResultStatusDefault();
            resultStatus.title = "Timeout Error";
            resultStatus.error = e;

        } else if (e instanceof IOException) {
            // Timeout
            // As excecoes com classe 'java.io.IOException' sao de tipo 'RetrofitError.Kind.NETWORK' e 'RetrofitError.Kind.CONVERSION'.
            // Isso significa que algo correu mal com sua chamada ou (de) serializacao.
            resultStatus = new ResultStatusDefault();
            resultStatus.title = "Timeout Error";
            resultStatus.error = e;

        } else if (e instanceof IllegalStateException) {
            // ConversionError
            resultStatus = new ResultStatusDefault();
            resultStatus.title = "Timeout Error";
            resultStatus.error = e;

        } else {
            // Other Error
            resultStatus = new ResultStatusDefault();
            resultStatus.error = e;
        }
    }

    @Override
    public void onNext(T t) {
        // no-op by default
    }

    public ResultStatusDefault getSuccessResult(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            JSONObject errJson = jsonObject.getJSONObject("success");
            return ResultStatusDefault.fromJson(errJson.toString());

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converter erro de retorno em JSONObject
     * @param responseBody
     * @return
     */
    public ResultStatusDefault getErrorResult(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            JSONObject errJson = jsonObject.getJSONObject("error");
            return ResultStatusDefault.fromJson(errJson.toString());

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Recuperar "message" do erro de retorno
     * @param responseBody
     * @return
     */
    public String getErrorMessage(ResponseBody responseBody) {
        try {
            ResultStatusDefault errJson = this.getErrorResult(responseBody);
            return (errJson != null) ? errJson.message : null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
