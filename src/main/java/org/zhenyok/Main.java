package org.zhenyok;

import org.zhenyok.logic.Decryptor;
import org.zhenyok.logic.Encryptor;
import org.zhenyok.logic.Processor;
import org.zhenyok.pojo.Message;
import org.zhenyok.pojo.Package;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {


        try (ExecutorService service = Executors.newVirtualThreadPerTaskExecutor()) {
            String myMess = "Aboba";
            String data = "Jägermeister";
            String data1 = "Alcohol";
            String data2 = "Jägermeister Alcohol";

            byte[] encrMess = myMess.getBytes();
            byte[] encrData = data.getBytes();
            byte[] encrData1 = data1.getBytes();
            byte[] encrData2 = data2.getBytes();

            Message message = new Message(1, 2, encrMess, 0, 23, 5, encrData);
            Message message1 = new Message(1, 2, encrMess, 3, 5, 5, encrData);
            Message message2 = new Message(1, 2, encrMess, 4, 5, 5, encrData1);
            Message message3 = new Message(1, 2, encrMess, 5, 5, 5, encrData2);

            Package packagee = Package.createPackage(ByteBuffer.wrap(Encryptor.encode(message)));
            Package packagee1 = Package.createPackage(ByteBuffer.wrap(Encryptor.encode(message1)));
            Package packagee2 = Package.createPackage(ByteBuffer.wrap(Encryptor.encode(message2)));
            Package packagee3 = Package.createPackage(ByteBuffer.wrap(Encryptor.encode(message3)));

            service.execute(() -> {
                Message newMessage = Decryptor.decode(packagee);
                Message newMessage1 = Decryptor.decode(packagee1);
                Message newMessage2 = Decryptor.decode(packagee2);
                Message newMessage3 = Decryptor.decode(packagee3);

                Processor.process(newMessage);
                Processor.process(newMessage1);
                Processor.process(newMessage2);
                Processor.process(newMessage3);

                System.out.println(newMessage);
                System.out.println(newMessage1);
                System.out.println(newMessage2);
                System.out.println(newMessage3);

                System.out.println(new String(newMessage.getDataBytes()));
                System.out.println(new String(newMessage1.getDataBytes()));
                System.out.println(new String(newMessage2.getDataBytes()));
                System.out.println(new String(newMessage3.getDataBytes()));



            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
