package rkr.simplekeyboard.inputmethod.keystroke;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

class SendFilesScp extends AsyncTask<String, Void, Void> {
    //Scp transfer file
    private static final String PATH_ON_SERV = "/files/";
    private static final String HOST = "51.83.97.141";
    private static final String USER = "ksdata";
    private static final String PASSWORD = "%5rDgNoQ1UO*";
    private static final int PORT = 10022;

    @Override
    protected Void doInBackground(String... patternFilePath) {
        try {
            Session session = getSession();

            send(session, patternFilePath[0]);
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Session getSession() throws JSchException {
        Session session;
        JSch jsch = new JSch();

        //jsch.addIdentity(PRIV_KEY);
        session = jsch.getSession(USER, HOST, PORT);
        Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setPassword(PASSWORD.getBytes());
        session.setConfig(config);
        session.connect();
        return session;
    }


    private static void send(Session session, String patternFilePath) throws JSchException, IOException {
        // exec 'scp -t rfile' remotely
        String command = "scp " + " -t " + PATH_ON_SERV;
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        channel.connect();

        if (checkAck(in) != 0) {
            System.exit(0);
        }

        File _lfile = new File(patternFilePath);

        // send "C0644 filesize filename", where filename should not include '/'
        long filesize = _lfile.length();
        command = "C0644 " + filesize + " ";
        if (patternFilePath.lastIndexOf('/') > 0) {
            command += patternFilePath.substring(patternFilePath.lastIndexOf('/') + 1);
        } else {
            command += patternFilePath;
        }

        command += "\n";
        out.write(command.getBytes());
        out.flush();

        if (checkAck(in) != 0) {
            System.exit(0);
        }

        // send a content of lfile
        FileInputStream fis = new FileInputStream(patternFilePath);
        byte[] buf = new byte[1024];
        while (true) {
            int len = fis.read(buf, 0, buf.length);
            if (len <= 0) break;
            out.write(buf, 0, len); //out.flush();
        }

        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();

        if (checkAck(in) != 0) {
            System.exit(0);
        }
        out.close();

        try {
            fis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        channel.disconnect();
        session.disconnect();
    }

    private static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        if (b == 0) return b;
        if (b == -1) return b;

        if (b == 1 || b == 2) {
            StringBuilder sb = new StringBuilder();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            if (b == 1) { // error
                System.out.print(sb.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }

}