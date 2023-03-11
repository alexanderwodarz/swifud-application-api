package de.alexanderwodarz.code.swifud.application;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import de.alexanderwodarz.code.model.varible.VaribleMap;
import de.alexanderwodarz.code.rest.ClientThread;
import de.alexanderwodarz.code.swifud.application.model.InvoiceAddress;
import de.alexanderwodarz.code.swifud.application.model.Item;
import de.alexanderwodarz.code.web.WebCore;
import de.alexanderwodarz.code.web.rest.annotation.RestApplication;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor

@RestApplication
public class Application {

    public static Function<JSONObject, JSONObject> consumer;
    @Setter
    protected static String secret, name;
    @Setter
    @Getter
    private static boolean debug = false;

    public static void startHook(Function<JSONObject, JSONObject> consumer) throws Exception {
        VaribleMap map = new VaribleMap();
        map.put("port", "9382");
        Application.consumer = consumer;
        WebCore.start(Application.class, map);
    }

    public static List<Invoice> listInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        Request request = new Request("/application/invoices", ClientThread.RequestMethod.GET, null);
        for (int i = 0; i < request.getResultArray().length(); i++)
            invoices.add(new Invoice(request.getResultArray().getJSONObject(i)));
        return invoices;
    }

    public static Invoice getInvoice(String invoiceNumber) {
        Request request = new Request("/application/invoices/" + invoiceNumber, ClientThread.RequestMethod.GET, null);
        return request.getStatus() == 200 ? new Invoice(request.getResultObject()) : null;
    }

    public static Invoice createInvoice(String condition, String invoiceDate, String deliveryDate, String layout, InvoiceAddress address, List<PaymentMethodType> types, List<Item> items) {
        JSONObject create = new JSONObject();
        create.put("condition", condition);
        create.put("invoiceDate", invoiceDate);
        create.put("deliveryDate", deliveryDate);
        create.put("layout", layout);
        create.put("address", address.build());
        create.put("paymentMethods", new JSONArray(types));
        create.put("items", new JSONArray(items));
        Request request = new Request("/invoice", ClientThread.RequestMethod.PUT, create);
        return new Invoice(request.getResultObject());
    }

    @Getter
    private static class Request {

        private final String result;
        private final int status;
        private JSONObject resultObject;
        private JSONArray resultArray;

        public Request(String path, ClientThread.RequestMethod method, JSONObject body) {
            ClientThread thread = new ClientThread((Application.isDebug() ? "http://localhost:13413/api/" : "https://swifud.alexanderwodarz.de/api/") + (path.startsWith("/") ? path.replaceFirst("/", "") : path), method);
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + JWT.create().withIssuer(Application.name).sign(Algorithm.HMAC256(Application.secret)));
            thread.setHeaders(headers);
            if (body != null)
                thread.setBody(body);
            thread.run();
            while (thread.isAlive()) {
            }
            status = thread.getStatus();
            result = thread.getResponse();
            if (result.startsWith("{"))
                resultObject = new JSONObject(result);
            if (result.startsWith("["))
                resultArray = new JSONArray(result);
        }

    }
}
