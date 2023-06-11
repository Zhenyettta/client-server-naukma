import org.junit.jupiter.api.Test;
import org.zhenyok.logic.Decryptor;
import org.zhenyok.logic.Encryptor;
import org.zhenyok.logic.Processor;
import org.zhenyok.pojo.Message;
import org.zhenyok.pojo.Package;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProcessorTest {

    @Test
    public void testProcessMessages() {
        List<Message> messages = new ArrayList<>();
        List<Message> messages1 = new ArrayList<>();
        messages1.add(new Message(1, 1, null, 0, 10, 0.0, "Product1".getBytes()));

        messages.add(new Message(1, 1, null, 1, 0, 0.0, "Product1".getBytes()));
        messages.add(new Message(1, 1, null, 2, 3, 0.0, "Product1".getBytes()));
        messages.add(new Message(1, 1, null, 0, 3, 0.0, "Product3".getBytes()));
        messages.add(new Message(1, 1, null, 4, 0, 0.0, "Group2".getBytes()));
        messages.add(new Message(1, 1, null, 5, 0, 0.0, "Product3 Group2".getBytes()));
        messages.add(new Message(1, 1, null, 3, 50, 0, "Product1".getBytes()));
        messages.add(new Message(1, 1, null, 1, 0, 57.0, "Product1".getBytes()));

        Processor.processMessagesInParallel(messages1);
        Processor.processMessagesInParallel(messages);



        assertEquals("Count of your product = 10", new String(messages.get(0).getMessageBytes()));
        assertEquals("Removed 3 products, actual count = 7", new String(messages.get(1).getMessageBytes()));
        assertEquals("Group Group2 was created", new String(messages.get(3).getMessageBytes()));
        assertEquals("Product Product3 was successfully added to Group2", new String(messages.get(4).getMessageBytes()));
        assertEquals("Added 50 products, actual count = 57", new String(messages.get(5).getMessageBytes()));
        assertEquals("Count of your product = 57", new String(messages.get(6).getMessageBytes()));
    }

    @Test
    public void testEncryptionAndDecryption() {
        List<Message> messages = prepareTestData();

        List<byte[]> encodedPackages = Encryptor.encode(messages);

        List<Package> packages = preparePackages(encodedPackages);
        List<Message> decodedMessages = Decryptor.decode(packages);

        assertNotNull(encodedPackages);
        assertNotNull(packages);
        assertNotNull(decodedMessages);
        assertEquals(messages.size(), decodedMessages.size());

        for (int i = 0; i < messages.size(); i++) {
            Message originalMessage = messages.get(i);
            Message decodedMessage = decodedMessages.get(i);

            assertEquals(originalMessage.getCType(), decodedMessage.getCType());
            assertEquals(originalMessage.getBUserId(), decodedMessage.getBUserId());
            assertEquals(originalMessage.getCommand(), decodedMessage.getCommand());
            assertEquals(originalMessage.getCount(), decodedMessage.getCount());
            assertEquals(originalMessage.getPrice(), decodedMessage.getPrice());
            assertEquals(new String(originalMessage.getMessageBytes()), new String(decodedMessage.getMessageBytes()));
            assertEquals(new String(originalMessage.getDataBytes()), new String(decodedMessage.getDataBytes()));
        }
    }

    private List<Message> prepareTestData() {
        List<Message> messages = new ArrayList<>();

        Message message1 = new Message(1, 1, "Hello".getBytes(), 1, 10, 19.99, "Data1".getBytes());
        Message message2 = new Message(2, 2, "World".getBytes(), 2, 5, 9.99, "Data2".getBytes());

        messages.add(message1);
        messages.add(message2);

        return messages;
    }

    private List<Package> preparePackages(List<byte[]> encodedPackages) {
        List<Package> packages = new ArrayList<>();

        for (byte[] encodedPackage : encodedPackages) {
            try {
                Package pack = Package.createPackage(ByteBuffer.wrap(encodedPackage));
                packages.add(pack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return packages;
    }
}
