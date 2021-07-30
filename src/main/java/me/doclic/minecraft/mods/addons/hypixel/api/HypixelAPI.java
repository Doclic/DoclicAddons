package me.doclic.minecraft.mods.addons.hypixel.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import me.doclic.minecraft.mods.addons.ConfigurationManager;
import me.doclic.minecraft.mods.addons.hypixel.api.skyblock.auctions.AuctionHouse;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Map;

public class HypixelAPI {

    public static AuctionHouse getAuctionHouse(int page) {
        return (AuctionHouse) getNoException("skyblock/auctions", AuctionHouse.class, "page=" + page); }
    public static APIKeyInfo getAPIKeyInfo() { return (APIKeyInfo) getNoException("key",  APIKeyInfo.class); }
    public static boolean isCurrentAPIKeyValid() {

        try { return get("key", APIKeyInfo.class).isSuccessful(); }
        catch (Exception e) { return false; }

    }


    private static Request getNoException(String apiUrl, Class<? extends Request> clazz, String... params) {

        try { return get(apiUrl, clazz, params); }
        catch (Exception e) { e.printStackTrace(); }

        return null;

    }

    private static Request get(String apiUrl, Class<? extends Request> clazz, String... params) throws IOException, InstantiationException, IllegalAccessException {

        final Request instance = clazz.newInstance();

        final StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("https://api.hypixel.net/").append(apiUrl).append("?key=").append(ConfigurationManager.getAPIKey());
        for (String param : params)
            urlBuilder.append('&').append(param);

        final String url = IOUtils.toString(new InputStreamReader(new URL(urlBuilder.toString()).openStream()));
        if (url == null || url.isEmpty()) return instance;
        final JsonElement jsonElement = new JsonParser().parse(url);

        if (jsonElement == null || !jsonElement.isJsonObject()) return null;

        final JsonObject json = jsonElement.getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {

            try {

                final Field field = clazz.getDeclaredField(entry.getKey());
                if (!field.isAccessible()) field.setAccessible(true);

                final JsonElement value = entry.getValue();

                if (value.isJsonPrimitive()) {

                    final JsonPrimitive primitive = value.getAsJsonPrimitive();
                    if (primitive.isBoolean())
                        field.setBoolean(instance, primitive.getAsBoolean());
                    else if (primitive.isString())
                        field.set(instance, primitive.getAsString());
                    else if (primitive.isNumber()) {

                        final Number number = primitive.getAsNumber();
                        final Type type = field.getGenericType();
                        final Class<?> classType = (Class<?>) type;
                        if (!classType.isPrimitive()) field.set(instance, primitive.getAsNumber());
                        else {

                            if (type == Long.TYPE)
                                field.setLong(instance, number.longValue());
                            else if (type == Integer.TYPE)
                                field.setInt(instance, number.intValue());
                            else if (type == Short.TYPE)
                                field.setShort(instance, number.shortValue());
                            else if (type == Byte.TYPE)
                                field.setShort(instance, number.byteValue());
                            else if (type == Float.TYPE)
                                field.setFloat(instance, number.floatValue());
                            else if (type == Double.TYPE)
                                field.setDouble(instance, number.doubleValue());

                        }

                    }

                } else if (value.isJsonObject()) field.set(instance, value.getAsJsonObject());
                else if (value.isJsonArray()) field.set(instance, value.getAsJsonArray());

            } catch (NoSuchFieldException e) {
                System.out.println("Field " + entry.getKey() + " not found in " + clazz);
                continue;
            }

            System.out.println("Field " + entry.getKey() + " found in " + clazz);

        }

        return instance;

    }

    public interface Request {

        boolean isSuccessful();

    }

}
