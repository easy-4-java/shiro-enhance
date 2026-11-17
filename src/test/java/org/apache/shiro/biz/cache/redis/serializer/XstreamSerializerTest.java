package org.apache.shiro.biz.cache.redis.serializer;

import com.thoughtworks.xstream.security.ForbiddenClassException;
import org.crazycake.shiro.exception.SerializationException;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class XstreamSerializerTest {

    @Test
    public void rejectsUnlistedPayloadType() throws SerializationException {
        XstreamSerializer<Object> serializer = new XstreamSerializer<>();
        String xml = "<org.apache.shiro.biz.cache.redis.serializer.XstreamSerializerTest_-Payload>"
                + "<value>unsafe</value></org.apache.shiro.biz.cache.redis.serializer.XstreamSerializerTest_-Payload>";
        try {
            serializer.deserialize(xml.getBytes(StandardCharsets.UTF_8));
            fail("Unlisted types must be rejected");
        } catch (ForbiddenClassException expected) {
            // XStream's default allowlist rejected the XML type.
        }
    }

    @Test
    public void roundTripsExplicitlyAllowedPayloadType() throws SerializationException {
        XstreamSerializer<Payload> serializer = new XstreamSerializer<>(Payload.class);
        Payload source = new Payload();
        source.value = "safe";
        assertEquals("safe", serializer.deserialize(serializer.serialize(source)).value);
    }

    public static class Payload {
        public String value;
    }
}
