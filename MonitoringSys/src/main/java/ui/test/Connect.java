package ui.test; /**
 * Created by ANTON on 08.11.2015.
 */

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import javax.swing.*;

public class Connect {
    String user = "ANTON";
    String host1 = "localhost";
    String password = "1234";
    //String host1 = "127.0.0.1";
    int port=22;
        public void connect(DefaultListModel dlist) {
            JSch jsch = new JSch();
            try {
               Session session = jsch.getSession(user, host1, port);
               // Session session = jsch.getSession(host1);

                session.setPassword(password);
                
                session.setConfig("StrictHostKeyChecking", "no");
                dlist.addElement("Establishing Connection...");
                session.connect();
                System.out.println("Connection established.");
                System.out.println("Crating SFTP Channel.");
                ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
                sftpChannel.connect();
                System.out.println("SFTP Channel created.");
            } catch (JSchException e) {
                e.printStackTrace();
            }
        }
}
