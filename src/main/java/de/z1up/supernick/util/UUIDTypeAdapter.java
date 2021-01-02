// package
package de.z1up.supernick.util;

// imports
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.UUID;

/**
 * TypeAdapter to read UUID
 */
public class UUIDTypeAdapter extends TypeAdapter<UUID> {

    // override
    @Override
    public void write(JsonWriter out, UUID value) throws IOException {
        out.value(fromUUID(value));
    }

    @Override
    public UUID read(JsonReader in) throws IOException {
        return fromString(in.nextString());
    }

    // methods

    /**
     * Shortens the long UUID and returns it as a String.
     * @param value The long UUID.
     * @return The shorted UUID as String.
     */
    public static String fromUUID(UUID value) {
        return value.toString().replace("-", "");
    }

    /**
     * Replaces the shorted UUID with a
     * @param input
     * @return
     */
    public static UUID fromString(String input) {
        return UUID.fromString(input.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }

}