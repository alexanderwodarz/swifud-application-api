package de.alexanderwodarz.code.swifud.application;

import de.alexanderwodarz.code.swifud.application.model.Item;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Invoice {

    private final JSONObject invoice;

    public PaymentMethodType[] getPaymentMethods() {
        List<PaymentMethodType> methods = new ArrayList<>();
        JSONArray array = invoice.getJSONArray("paymentMethods");
        for (int i = 0; i < array.length(); i++) {
            try {
                methods.add(PaymentMethodType.valueOf(array.getString(i)));
            } catch (Exception e) {
            }
        }
        return methods.toArray(new PaymentMethodType[0]);
    }

    public String getStatus() {
        return invoice.getString("status");
    }

    public String getReference() {
        return invoice.getString("reference");
    }

    public String getCondition() {
        return invoice.getString("condition");
    }

    public String getInvoiceNumber() {
        return invoice.getString("invoiceNumber");
    }

    public String getInvoiceDate() {
        return invoice.getString("invoiceDate");
    }

    public String getDeliveryDate() {
        return invoice.getString("deliveryDate");
    }

    public JSONArray getItems() {
        return invoice.getJSONArray("items");
    }

    public List<Item> getItemObjects() {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < getItems().length(); i++) {
            JSONObject item = getItems().getJSONObject(i);
            items.add(new Item(item.getString("description"), item.getInt("amount"), item.getDouble("price")));
        }
        return items;
    }

    public boolean isPayed() {
        return invoice.getBoolean("payed");
    }

    public JSONObject getInvoice() {
        return invoice;
    }

    public String getPayLink() {
        return (Application.isDebug() ? "http://localhost:8080/" : "https://swifud.alexanderwodarz.de/") + "invoice/" + getInvoiceNumber() + "?password=" + getPassword();
    }

    public String getPassword() {
        return invoice.getString("password");
    }

    public double getBrutto() {
        return invoice.getDouble("brutto");
    }

    public double getNetto() {
        return invoice.getDouble("netto");
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public String getStripeSecret() {
        return invoice.getString("stripe_secret");
    }

    public JSONObject getAddress() {
        return invoice.getJSONObject("address");
    }

    public String getLayout() {
        return invoice.getString("layout");
    }

    public JSONObject build() {
        JSONObject build = new JSONObject();
        build.put("stripe_secret", getStripeSecret());
        build.put("reference", getReference());
        build.put("condition", getCondition());
        build.put("address", getAddress());
        build.put("invoiceNumber", getInvoiceNumber());
        build.put("items", getItems());
        build.put("payed", isPayed());
        build.put("invoiceDate", getInvoiceDate());
        build.put("deliveryDate", getDeliveryDate());
        build.put("brutto", getBrutto());
        build.put("netto", getNetto());
        build.put("status", getStatus());
        return build;
    }
}
