package br.com.micronaut;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.MicronautRequestHandler;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.netty.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static io.micronaut.http.HttpHeaders.ACCEPT;
import static io.micronaut.http.HttpHeaders.USER_AGENT;

@Introspected
public class TestHandler extends MicronautRequestHandler<Map<String, Object>, Void>{

    private static final Logger LOG = LoggerFactory.getLogger(TestHandler.class);

    private static final String URI_CEP = "https://viacep.com.br/ws/";
    private static final String URI_CEP_BOTTON = "/json/";
    private static final String NUM_CEP = "13012000";

    @Override
    public Void execute(Map<String, Object> input) {

        if (LOG.isTraceEnabled() || LOG.isInfoEnabled() || LOG.isErrorEnabled()) {
            try {
                LOG.trace("Input {}", input);
                LOG.info("Chamar API CEP: {}", NUM_CEP);
                String retornoCep = recuperaEndereco(NUM_CEP);
                LOG.info("Retorno API CEP: {}", retornoCep);
            }catch (Exception e){
                LOG.error("Erro ao buscar CEP: {}", e.getLocalizedMessage());
            }
        }
        return null;
    }

    public String recuperaEndereco(final String numCep){
        HttpRequest<?> req = HttpRequest.GET(URI_CEP + numCep + URI_CEP_BOTTON)
                .header(USER_AGENT, "Micronaut HTTP Client")
                .header(ACCEPT, "application/json");

        RxHttpClient rxHttpClient = new DefaultHttpClient();
        String resultado = rxHttpClient.toBlocking().retrieve(req);
        rxHttpClient.close();

        return resultado;
    }
}
