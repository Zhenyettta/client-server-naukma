import org.junit.jupiter.api.Test;
import org.zhenyok.pojo.Message;
import org.zhenyok.pojo.Package;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.zhenyok.Receiver.decode;
import static org.zhenyok.Sender.encode;

class PackageTest {
    @Test
    public void globalTest() throws Exception {
        String testMessage = "Katya bacha best";
        byte[] encryptedMessageBytes = testMessage.getBytes();

        Message message = new Message(1, 1, encryptedMessageBytes);

        Package myPackage = Package.createPackage(ByteBuffer.wrap(encode(message)));

        Message decodedMessage = decode(myPackage);

        assertEquals(decodedMessage.cType(), message.cType());
        assertEquals(decodedMessage.bUserId(), message.bUserId());
        assertEquals(new String(decodedMessage.messageBytes()), testMessage);
    }
}
